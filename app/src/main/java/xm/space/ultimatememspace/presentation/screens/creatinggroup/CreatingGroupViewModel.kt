package xm.space.ultimatememspace.presentation.screens.creatinggroup

import android.graphics.Bitmap
import android.graphics.Color
import android.os.CountDownTimer
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.business.domain.models.connecteduser.ConnectedUser
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.ConnProfile
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents.RemindUser
import xm.space.ultimatememspace.business.repositories.avatars.AvatarRepository
import xm.space.ultimatememspace.business.repositories.models.ServerSocketEvents
import xm.space.ultimatememspace.business.repositories.shop.ShopRepository
import xm.space.ultimatememspace.core.models.toolbar.ToolbarState
import xm.space.ultimatememspace.core.viewmodel.BaseViewModel
import xm.space.ultimatememspace.core.viewmodel.GameProcessState
import xm.space.ultimatememspace.navigation.ALL_MEMES_PACKS
import xm.space.ultimatememspace.navigation.CHOOSE_ROLE_CONTENT
import xm.space.ultimatememspace.navigation.CHOOSE_SITUATION
import xm.space.ultimatememspace.navigation.CREATING_GROUP
import xm.space.ultimatememspace.navigation.WAIT_SITUATION
import xm.space.ultimatememspace.presentation.screens.chooserole.getFormattedAddress
import xm.space.ultimatememspace.presentation.screens.creatinggroup.models.CreatingGroupState
import xm.space.ultimatememspace.presentation.screens.shop.models.ShopState

/**
 * Screen logic group creating
 */
class CreatingGroupViewModel(
    private val avatarRepository: AvatarRepository,
    private val shopRepository: ShopRepository
) : BaseViewModel() {

    val state: StateFlow<CreatingGroupState>
        get() = _state
    private val _state = MutableStateFlow(CreatingGroupState())

    init {
        viewModelScope.launch {
            _barState.value = ToolbarState(
                title = resourceProvider.getString(R.string.gen_share_friends),
                rightIcon = R.drawable.ic_card
            )

            networkRepository.observeSocketEvents().collect { socketEvent ->
                when (socketEvent) {
                    is ServerSocketEvents.ServerCloseEvent -> {
                        _state.value = state.value.copy(isOverlayLoading = true)
                        onClientDisconnected(GameProcessState.Room) {
                            _state.value = state.value.copy(isOverlayLoading = false)
                        }
                    }
                    is ServerSocketEvents.ServerOpenEvent -> Unit
                    is ServerSocketEvents.ServerMessageEvent -> Unit
                    is ServerSocketEvents.ServerErrorEvent -> Unit
                    is ServerSocketEvents.ServerStartEvent -> {
                        _state.value = CreatingGroupState(
                            makeSureTitle = resourceProvider.getString(R.string.gen_make_sure),
                            gameStartTitle = resourceProvider.getString(R.string.gen_start),
                            connectedTitle = resourceProvider.getString(R.string.gen_connected),
                            shopData = ShopState(
                                barTitle = resourceProvider.getString(R.string.gen_catalog),
                                memesOptions = if (profileRepository.isProfileWasEdit()) shopRepository.getShopPack()
                                else emptyList()
                            ),
                            players = listOf(profileRepository.getFullProfile().toUi(
                                mineId = profileRepository.getUserId(),
                                avatars = avatarRepository.getAvatars()
                            )),
                            roundCountTitle = resourceProvider.getString(R.string.gen_round_count)
                        )
                        practiceManagerRepository.addNewUser(user = profileRepository.getFullProfile())
                        initQrCode()
                        broadcastTimer.start()
                    }
                    is ServerSocketEvents.ServerNetworkEvent -> {
                        when (val event = socketEvent.event) {
                            is ConnProfile -> {
                                practiceManagerRepository.addNewUser(
                                    user = ConnectedUser(
                                        id = event.id,
                                        name = event.name,
                                        avatar = event.avatar
                                    )
                                )
                            }

                            is RemindUser -> viewModelScope.launch {
                                reconnectedManagerRepository.addOnlineUser(event.id)
                            }

                            else -> Unit
                        }
                    }
                }
            }
        }
        observePlayers()
        networkRepository.createServer()
    }

    fun onRoundCountChange(value: Int) = viewModelScope.launch {
        practiceManagerRepository.updateRoundCount(value)
    }

    fun onAllMemesAtPackClick(memesPack: MemesPack) = viewModelScope.launch {
        _state.value = state.value.copy(isCurtainShow = false)
        shopRepository.setActivePack(memesPack).run {
            navController?.navigate(ALL_MEMES_PACKS)
        }
    }

    fun onMemesShopClick() {
        _state.value = state.value.copy(isCurtainShow = true)
    }

    fun onFullViewClose() {
        _state.value = state.value.copy(isCurtainShow = false)
    }

    fun startGameClick() = viewModelScope.launch {
        practiceManagerRepository.initializeGameState().run {
            userPracticeManagerRepository.saveMemesStartPack(
                memes = this.first {
                    it.playerId == profileRepository.getUserId()
                }.playerMemes
            ).let {
                networkRepository.memesForEachPlayer(this@run).run {
                    practiceManagerRepository.setQueueUsers()?.let { activePlayer ->
                        val situations = practiceManagerRepository.getSituationsAtGame()
                        networkRepository.inActionPlayerEvent(
                            player = activePlayer,
                            questions = situations
                        ).also {
                            practiceManagerRepository.updateRoundOrder()
                            userPracticeManagerRepository.saveSituationsAtRound(
                                situations = situations
                            ).run {
                                navController?.navigate(
                                    if (activePlayer.id == profileRepository.getUserId()) {
                                        CHOOSE_SITUATION
                                    } else {
                                        WAIT_SITUATION
                                    }
                                ) {
                                    popUpTo(CREATING_GROUP) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun onBackClick() {
        if (state.value.isCurtainShow) {
            _state.value = state.value.copy(isCurtainShow = false)
        } else {
            viewModelScope.launch { networkRepository.removeCreateServerEvent() }
            broadcastTimer.cancel()
            closeAllConnections()
            navController?.navigate(CHOOSE_ROLE_CONTENT) {
                popUpTo(CREATING_GROUP) { inclusive = true }
            }
        }
    }

    private fun initQrCode() {
        CoroutineScope(Dispatchers.IO).launch {
            getQrCodeBitmap(
                fullAddress = getFormattedAddress(
                    context = context,
                    port = networkRepository.getServerPort() ?: 0
                ),
            ).let {
                _state.value = state.value.copy(qrWifi = it)
            }
        }
    }

    private fun observePlayers() {
        viewModelScope.launch {
            practiceManagerRepository.observeConnectedUsers().collect { connectedUsers ->
                _state.value = state.value.copy(
                    players = connectedUsers.map {
                        it.toUi(
                            mineId = profileRepository.getUserId(),
                            avatars = avatarRepository.getAvatars()
                        )
                    }
                )
            }
        }
    }

    private fun getQrCodeBitmap(fullAddress: String): Bitmap {
        val size = 1024
        val bits = QRCodeWriter().encode(fullAddress, BarcodeFormat.QR_CODE, size, size)
        return Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(
                        x, y, if (bits[x, y]) Color.BLACK else {
                            ContextCompat.getColor(context, R.color.transparent)
                        }
                    )
                }
            }
        }
    }

    private fun broadcastEventToConnect() = viewModelScope.launch {
        networkRepository.createServerEvent(
            message = getFormattedAddress(
                context = context,
                port = networkRepository.getServerPort() ?: 0
            )
        )
    }

    private val broadcastTimer = object : CountDownTimer(BROADCAST_TIME, EVENT_INTERVAL) {
        override fun onTick(millisUntilFinished: Long) {
            broadcastEventToConnect()
        }
        override fun onFinish() { broadcastEventToConnect() }
    }

    companion object {
        private const val BROADCAST_TIME = 500000L
        private const val EVENT_INTERVAL = 1000L
    }
}