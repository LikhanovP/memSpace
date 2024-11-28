package xm.space.ultimatememspace.business.engine

import xm.space.ultimatememspace.business.engine.callbacks.ServerCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import xm.space.ultimatememspace.business.engine.converters.convertToSuitableModel
import java.lang.Exception
import java.net.InetSocketAddress
import java.nio.ByteBuffer

class GameWebSocketServer(
    private val serverCallback: ServerCallback
) : WebSocketServer(InetSocketAddress(PORT)) {

    override fun onOpen(
        conn: WebSocket?,
        handshake: ClientHandshake?
    ) {
        CoroutineScope(Dispatchers.Unconfined).launch { serverCallback.onServerOpenEvent() }
    }

    override fun onClose(
        conn: WebSocket?,
        code: Int,
        reason: String?,
        remote: Boolean
    ) {
        CoroutineScope(Dispatchers.Unconfined).launch { serverCallback.onServerCloseEvent() }
    }

    override fun onMessage(conn: WebSocket?, message: ByteBuffer?) {
        message?.let {
            CoroutineScope(Dispatchers.Unconfined).launch {
                serverCallback.onServerMessageEvent(event = convertToSuitableModel(it))
            }
        }
    }

    override fun onMessage(
        conn: WebSocket?,
        message: String?
    ) {
        CoroutineScope(Dispatchers.Unconfined).launch {
            serverCallback.onServerMessageEvent()
        }
    }

    override fun onError(
        conn: WebSocket?,
        ex: Exception?
    ) {
        CoroutineScope(Dispatchers.Unconfined).launch {
            serverCallback.onServerErrorEvent()
        }
    }

    override fun onStart() {
        CoroutineScope(Dispatchers.Unconfined).launch {
            serverCallback.onServerStartEvent()
        }
    }

    companion object {
        private const val PORT = 0
    }
}