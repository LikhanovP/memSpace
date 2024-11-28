package xm.space.ultimatememspace.business.engine.callbacks

import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents

/**
 * Network client event callbacks
 */
interface ClientCallback {

    /*** Successful connect*/
    suspend fun onClientOpenEvent()

    /*** Simple message from server */
    suspend fun onClientMessageEvent(message: String?)

    /**
     * Event with data from server callback
     * @param event Network event
     */
    suspend fun onClientMessageEvent(event: NetworkEvents)

    /*** Close event callback */
    suspend fun onClientCloseEvent()

    /*** Server error callback */
    suspend fun onClientErrorEvent()
}