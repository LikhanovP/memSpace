package xm.space.ultimatememspace.business.repositories.network

import android.content.Context
import android.net.wifi.WifiManager
import android.os.StrictMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import xm.space.ultimatememspace.business.domain.models.connecteduser.ConnectedUser
import xm.space.ultimatememspace.business.domain.models.memes.PlayerMemes
import xm.space.ultimatememspace.business.domain.models.network.InvitationEvent
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents
import xm.space.ultimatememspace.business.domain.models.results.PlayerResult
import xm.space.ultimatememspace.business.domain.models.situation.SituationOption
import xm.space.ultimatememspace.business.engine.GameWebSocketServer
import xm.space.ultimatememspace.business.engine.callbacks.ALL_MEMS_FOR_CHOOSE_BEST
import xm.space.ultimatememspace.business.engine.callbacks.BROADCAST_LINE_UP
import xm.space.ultimatememspace.business.engine.callbacks.BROADCAST_START_GAME
import xm.space.ultimatememspace.business.engine.callbacks.CONTINUE_GAME
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_AVATAR
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_COUNT
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_CURRENT_USERS
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_ID
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_MEMES_COUNTS
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_MEME_ID
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_NAME
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_PLAYER_AVATAR
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_PLAYER_ICON
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_PLAYER_ID
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_PLAYER_IS_WINNER
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_PLAYER_MEMES
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_PLAYER_NAME
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_PLAYER_WINS_COUNT
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_QUESTION
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_QUESTIONS_LIST
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_REQUEST_KEY
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_ROUND_ORDER
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_SITUATION_ID
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_USERS_MEMS
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_USER_WHO_ASK
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_USES_MEM_ID
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_VIEW_RESULTS
import xm.space.ultimatememspace.business.engine.callbacks.FILED_PLAYERS_MEMES
import xm.space.ultimatememspace.business.engine.callbacks.MEME_COUNTS
import xm.space.ultimatememspace.business.engine.callbacks.PLAYERS_MEMES
import xm.space.ultimatememspace.business.engine.callbacks.PLAYERS_NEW_MEMES
import xm.space.ultimatememspace.business.engine.callbacks.POLLING_USERS
import xm.space.ultimatememspace.business.engine.callbacks.QUESTION_CHOOSE
import xm.space.ultimatememspace.business.engine.callbacks.ServerCallback
import xm.space.ultimatememspace.business.engine.callbacks.VIEW_RESULT
import xm.space.ultimatememspace.business.repositories.models.ServerSocketEvents
import xm.space.ultimatememspace.business.repositories.networkplayer.NetworkPlayerRepository
import xm.space.ultimatememspace.business.repositories.practicemanager.PracticeManagerRepository
import xm.space.ultimatememspace.business.repositories.profile.ProfileRepository
import xm.space.ultimatememspace.business.repositories.situations.SituationsRepository
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.charset.Charset


/**
 * Implementation [NetworkRepository]
 */
class NetworkRepositoryImpl(
    private val context: Context,
    private val practiceManagerRepository: PracticeManagerRepository,
    private val networkPlayerRepository: NetworkPlayerRepository,
    private val profileRepository: ProfileRepository,
    private val situationsRepository: SituationsRepository
) : NetworkRepository, ServerCallback {

    private val events = MutableSharedFlow<ServerSocketEvents>()

    private var serverSocket: GameWebSocketServer? = null

    private val receiveServerSocket = DatagramSocket(8080)

    private val senderServerSocket = DatagramSocket()

    init {
        CoroutineScope(Dispatchers.Unconfined).launch {
            practiceManagerRepository.observeConnectedUsers().collect {
                val allUsers = JSONArray()
                it.forEach { user ->
                    allUsers.put(JSONObject().apply {
                        put(FIELD_ID, user.id)
                        put(FIELD_NAME, user.name)
                        put(FIELD_AVATAR, user.avatar)
                    })
                }
                JSONObject().apply {
                    put(FIELD_REQUEST_KEY, BROADCAST_LINE_UP)
                    put(FIELD_CURRENT_USERS, allUsers)
                }.send()
            }
        }
    }

    override suspend fun observeSocketEvents(): SharedFlow<ServerSocketEvents> =
        events.asSharedFlow()

    override fun createServer() {
        serverSocket = GameWebSocketServer(serverCallback = this)
        serverSocket?.start()
    }

    override suspend fun searchServer(callback: (InvitationEvent) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            receiveBroadcastMessage(callback = callback)
        }
    }

    override suspend fun removeSearchServer() {
        receiveServerSocket.close()
    }

    override suspend fun createServerEvent(message: String) {
        sendBroadcastMessage("${message}&${profileRepository.getUserName()}|")
    }

    override suspend fun removeCreateServerEvent() {
        senderServerSocket.close()
    }

    override suspend fun closeServer() {
        serverSocket?.stop()
        serverSocket = null
    }

    override suspend fun memesForEachPlayer(memes: List<PlayerMemes>) {
        val allPlayersMemes = JSONArray()
        memes.forEach { playerMemes ->
            allPlayersMemes.put(JSONObject().apply {
                put(FIELD_PLAYER_ID, playerMemes.playerId)
                put(FIELD_PLAYER_MEMES, playerMemes.playerMemes)
            })
        }
        JSONObject().apply {
            put(FIELD_REQUEST_KEY, PLAYERS_MEMES)
            put(FILED_PLAYERS_MEMES, allPlayersMemes)
        }.send()
    }

    override suspend fun inActionPlayerEvent(
        player: ConnectedUser,
        questions: List<SituationOption>
    ) {
        practiceManagerRepository.saveActivePlayerAtRound(player.id)
        val allQuestions = JSONArray()
        questions.forEach { questionOption ->
            allQuestions.put(JSONObject().apply {
                put(FIELD_ID, questionOption.id)
                put(FIELD_QUESTION, questionOption.question)
            })
        }
        JSONObject().apply {
            put(FIELD_REQUEST_KEY, BROADCAST_START_GAME)
            put(FIELD_USER_WHO_ASK, player.id)
            put(FIELD_NAME, player.name)
            put(FIELD_QUESTIONS_LIST, allQuestions)
        }.send()
    }

    override suspend fun choosesSituationEvent(situationId: Int) {
        if (profileRepository.isProfileBelongAdmin()) {
            JSONObject().apply {
                put(FIELD_REQUEST_KEY, QUESTION_CHOOSE)
                put(FIELD_ID, situationId)
            }.send()
        } else networkPlayerRepository.sendSituationEvent(situationId = situationId)
    }

    override suspend fun choosesMemesEvent() {
        val allUsersMemes = JSONArray()
        practiceManagerRepository.getUsersMemeChoose().forEach {
            allUsersMemes.put(JSONObject().apply {
                put(FIELD_ID, it.userId)
                put(FIELD_USES_MEM_ID, it.userMemId)
            })
        }
        JSONObject().apply {
            put(FIELD_REQUEST_KEY, ALL_MEMS_FOR_CHOOSE_BEST)
            put(FIELD_USERS_MEMS, allUsersMemes)
            put(FIELD_ROUND_ORDER, practiceManagerRepository.getRoundOrder())
            put(FIELD_SITUATION_ID, situationsRepository.getSituationAtRound()?.id)
        }.send()
    }

    override suspend fun memesVoteEvent() {
        val allMemesVote = JSONArray()
        practiceManagerRepository.getMemeCountVotes().forEach { vote ->
            allMemesVote.put(JSONObject().apply {
                put(FIELD_MEME_ID, vote.memeId)
                put(FIELD_COUNT, vote.count)
                put(FIELD_PLAYER_NAME, vote.playerName)
                put(FIELD_PLAYER_ICON, vote.playerIcon)
            })
        }
        JSONObject().apply {
            put(FIELD_REQUEST_KEY, MEME_COUNTS)
            put(FIELD_MEMES_COUNTS, allMemesVote)
        }.send()
    }

    override suspend fun newMemesForEachPlayer(memes: List<PlayerMemes>) {
        val allPlayersNewMemes = JSONArray()
        memes.forEach { playerMemes ->
            allPlayersNewMemes.put(JSONObject().apply {
                put(FIELD_PLAYER_ID, playerMemes.playerId)
                put(FIELD_PLAYER_MEMES, playerMemes.playerMemes)
            })
        }
        JSONObject().apply {
            put(FIELD_REQUEST_KEY, PLAYERS_NEW_MEMES)
            put(FILED_PLAYERS_MEMES, allPlayersNewMemes)
        }.send()
    }

    override suspend fun gameResultEvent(playersResults: List<PlayerResult>) {
        val allPlayersResults = JSONArray()
        playersResults.forEach { playerResult ->
            allPlayersResults.put(JSONObject().apply {
                put(FIELD_PLAYER_NAME, playerResult.playerName)
                put(FIELD_PLAYER_AVATAR, playerResult.playerAvatar)
                put(FIELD_PLAYER_WINS_COUNT, playerResult.playerWinsCount)
                put(FIELD_PLAYER_IS_WINNER, playerResult.playerIsWinner)
            })
        }
        JSONObject().apply {
            put(FIELD_REQUEST_KEY, VIEW_RESULT)
            put(FIELD_VIEW_RESULTS, allPlayersResults)
        }.send()
    }

    override suspend fun pollingUsers() {
        JSONObject().apply { put(FIELD_REQUEST_KEY, POLLING_USERS) }.send()
    }

    override suspend fun continueGame() {
        JSONObject().apply { put(FIELD_REQUEST_KEY, CONTINUE_GAME) }.send()
    }

    override fun getServerPort() = serverSocket?.port

    override suspend fun onServerOpenEvent() {
        events.emit(ServerSocketEvents.ServerOpenEvent)
    }

    override suspend fun onServerMessageEvent() {
        events.emit(ServerSocketEvents.ServerMessageEvent)
    }

    override suspend fun onServerMessageEvent(event: NetworkEvents) {
        events.emit(ServerSocketEvents.ServerNetworkEvent(event = event))
    }

    override suspend fun onServerCloseEvent() {
        events.emit(ServerSocketEvents.ServerCloseEvent)
    }

    override suspend fun onServerErrorEvent() {
        events.emit(ServerSocketEvents.ServerErrorEvent)
    }

    override suspend fun onServerStartEvent() {
        events.emit(ServerSocketEvents.ServerStartEvent)
    }

    private fun JSONObject.send() =
        serverSocket?.broadcast(this.toString().toByteArray(Charset.defaultCharset()))

    private fun sendBroadcastMessage(networkData: String) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            val sendData = networkData.encodeToByteArray()
            val sendPacket = DatagramPacket(sendData, sendData.size, getBroadcastAddress(), 8080)
            senderServerSocket.send(sendPacket)
        } catch (e: IOException) {
            Unit
        }
    }

    private fun receiveBroadcastMessage(callback: (InvitationEvent) -> Unit) {
        val buffer = ByteArray(100)
        val packet = DatagramPacket(buffer, buffer.size)
        try {
            receiveServerSocket.receive(packet)
        } catch (e: IOException) { Unit }

        val buff = String(packet.data).substringBeforeLast("|")
        if (buff.split("&").size > 1) {
            callback.invoke(
                InvitationEvent(
                    ip = buff.split("&")[0],
                    port = buff.split("&")[1],
                    ownerName = buff.split("&")[2]
                )
            )
        }
    }

    @Throws(IOException::class)
    fun getBroadcastAddress(): InetAddress? {
        val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val dhcp = wifi.dhcpInfo
        val broadcast = dhcp.ipAddress and dhcp.netmask or dhcp.netmask.inv()
        val quads = ByteArray(4)
        for (k in 0..3) quads[k] = (broadcast shr k * 8 and 0xFF).toByte()
        return InetAddress.getByAddress(quads)
    }
}