package xm.space.ultimatememspace.business.repositories.networkplayer

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.json.JSONObject
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents
import xm.space.ultimatememspace.business.engine.GameWebSocketClient
import xm.space.ultimatememspace.business.engine.callbacks.BEST_MEM_CHOOSE_APPROVE
import xm.space.ultimatememspace.business.engine.callbacks.CONN_PROFILE_DATA
import xm.space.ultimatememspace.business.engine.callbacks.ClientCallback
import xm.space.ultimatememspace.business.engine.callbacks.EXPRESS_YOURSELF
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_AVATAR
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_ID
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_MEM_ID
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_NAME
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_PLAYER_OWNER_CHOICE
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_REQUEST_KEY
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_USES_MEM_ID
import xm.space.ultimatememspace.business.engine.callbacks.MEM_WAS_CHOOSE
import xm.space.ultimatememspace.business.engine.callbacks.QUESTION_CHOOSE
import xm.space.ultimatememspace.business.repositories.models.ClientSocketEvents
import xm.space.ultimatememspace.business.repositories.profile.ProfileRepository
import xm.space.ultimatememspace.business.repositories.settings.SettingsRepository
import java.nio.charset.Charset

class NetworkPlayerRepositoryImpl(
    private val settingsRepository: SettingsRepository,
    private val profileRepository: ProfileRepository
): NetworkPlayerRepository, ClientCallback {

    private val events = MutableSharedFlow<ClientSocketEvents>()

    private var clientSocket: GameWebSocketClient? = null

    override suspend fun observeSocketEvents() = events.asSharedFlow()

    override suspend fun createSocket() {
        clientSocket = GameWebSocketClient(
            address = "$PREFIX_LINK${settingsRepository.getNetworkData().first}:${settingsRepository.getNetworkData().second}",
            clientCallback = this
        )
        clientSocket?.connect()
    }

    override suspend fun closeSocket() {
        clientSocket?.close()
        clientSocket = null
    }

    override suspend fun onClientOpenEvent() {
        events.emit(ClientSocketEvents.ClientOpenEvent)
    }

    override suspend fun onClientMessageEvent(message: String?) {
        events.emit(ClientSocketEvents.ClientMessageEvent(message = message))
    }

    override suspend fun onClientMessageEvent(event: NetworkEvents) {
        events.emit(ClientSocketEvents.ServerEvent(event = event))
    }

    override suspend fun onClientCloseEvent() {
        events.emit(ClientSocketEvents.ClientCloseEvent)
    }

    override suspend fun onClientErrorEvent() {
        events.emit(ClientSocketEvents.ClientErrorEvent)
    }

    override suspend fun userProfileEvent() {
        val profile = profileRepository.getFullProfile()
        JSONObject().apply {
            put(FIELD_REQUEST_KEY, CONN_PROFILE_DATA)
            put(FIELD_ID, profile.id)
            put(FIELD_NAME, profile.name)
            put(FIELD_AVATAR, profile.avatar)
        }.send()
    }

    override suspend fun sendSituationEvent(situationId: Int) {
        JSONObject().apply {
            put(FIELD_REQUEST_KEY, QUESTION_CHOOSE)
            put(FIELD_ID, situationId)
        }.send()
    }

    override suspend fun sendMemeEvent(memeId: Int) {
        JSONObject().apply {
            put(FIELD_REQUEST_KEY, MEM_WAS_CHOOSE)
            put(FIELD_ID, profileRepository.getUserId())
            put(FIELD_USES_MEM_ID, memeId)
        }.send()
    }

    override suspend fun playerChooseBestMemEvent(playerWhoChooseId: Int, memeId: Int) {
        JSONObject().apply {
            put(FIELD_REQUEST_KEY, BEST_MEM_CHOOSE_APPROVE)
            put(FIELD_PLAYER_OWNER_CHOICE, playerWhoChooseId)
            put(FIELD_MEM_ID, memeId)
        }.send()
    }

    override suspend fun expressYourself() {
        JSONObject().apply {
            put(FIELD_REQUEST_KEY, EXPRESS_YOURSELF)
            put(FIELD_ID, profileRepository.getUserId())
        }.send()
    }

    private fun JSONObject.send() = clientSocket?.send(this.toString().toByteArray(Charset.defaultCharset()))

    companion object {
        private const val PREFIX_LINK = "ws://"
    }
}