package xm.space.ultimatememspace.business.domain.models.events

import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents

sealed class EventsGameProcess {

    data class ForClientEvent(val event: NetworkEvents) : EventsGameProcess()

    data class ForServerEvent(val event: NetworkEvents) : EventsGameProcess()

    data object ClientDisconnectEvent : EventsGameProcess()

    data object ServerShutdownEvent : EventsGameProcess()
}