package xm.space.ultimatememspace.business.engine.callbacks

import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents

/**
 * Network server callbacks
 */
interface ServerCallback {

    /*** Server open event callback */
    suspend fun onServerOpenEvent()

    /*** Simple message event callback */
    suspend fun onServerMessageEvent()

    /*** Event from users callback */
    suspend fun onServerMessageEvent(event: NetworkEvents)

    /*** Server close callback */
    suspend fun onServerCloseEvent()

    /*** Server error callback */
    suspend fun onServerErrorEvent()

    /*** Server start callback */
    suspend fun onServerStartEvent()
}