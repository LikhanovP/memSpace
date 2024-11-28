package xm.space.ultimatememspace.business.engine

import xm.space.ultimatememspace.business.engine.callbacks.ClientCallback
import kotlinx.coroutines.*
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import xm.space.ultimatememspace.business.engine.converters.convertToSuitableModel
import java.lang.Exception
import java.net.URI
import java.nio.ByteBuffer

class GameWebSocketClient(
    address: String,
    private val clientCallback: ClientCallback
) : WebSocketClient(URI(address)) {

    override fun onOpen(handshakedata: ServerHandshake?) {
        CoroutineScope(Dispatchers.Unconfined).launch {
            clientCallback.onClientOpenEvent()
        }
    }

    override fun onMessage(message: String?) {
        CoroutineScope(Dispatchers.Unconfined).launch {
            clientCallback.onClientMessageEvent(message)
        }
    }

    override fun onMessage(message: ByteBuffer?) {
        message?.let {
            CoroutineScope(Dispatchers.Unconfined).launch {
                clientCallback.onClientMessageEvent(event = convertToSuitableModel(it))
            }
        }
    }

    override fun onClose(
        code: Int,
        reason: String?,
        remote: Boolean
    ) {
        CoroutineScope(Dispatchers.Unconfined).launch { clientCallback.onClientCloseEvent() }
    }

    override fun onError(ex: Exception?) {
        CoroutineScope(Dispatchers.Unconfined).launch {
            clientCallback.onClientErrorEvent()
        }
    }
}