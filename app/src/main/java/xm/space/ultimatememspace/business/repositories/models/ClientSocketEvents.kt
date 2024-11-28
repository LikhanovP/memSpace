package xm.space.ultimatememspace.business.repositories.models

import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents

sealed class ClientSocketEvents {

    data class ServerEvent(val event: NetworkEvents) : ClientSocketEvents()

    data object ClientCloseEvent : ClientSocketEvents()

    data object ClientErrorEvent : ClientSocketEvents()

    data class ClientMessageEvent(val message: String?) : ClientSocketEvents()

    data object ClientOpenEvent : ClientSocketEvents()
}