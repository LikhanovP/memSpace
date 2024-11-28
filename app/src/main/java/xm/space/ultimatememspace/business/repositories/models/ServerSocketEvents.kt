package xm.space.ultimatememspace.business.repositories.models

import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents

sealed class ServerSocketEvents {

    data object ServerOpenEvent : ServerSocketEvents()

    data object ServerMessageEvent : ServerSocketEvents()

    data class ServerNetworkEvent(val event: NetworkEvents) : ServerSocketEvents()

    data object ServerCloseEvent : ServerSocketEvents()

    data object ServerErrorEvent : ServerSocketEvents()

    data object ServerStartEvent : ServerSocketEvents()
}