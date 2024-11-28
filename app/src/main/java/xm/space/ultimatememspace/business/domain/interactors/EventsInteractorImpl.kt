package xm.space.ultimatememspace.business.domain.interactors

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ClientDisconnectEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ForServerEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ServerShutdownEvent
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess.ForClientEvent
import xm.space.ultimatememspace.business.repositories.models.ClientSocketEvents.ServerEvent
import xm.space.ultimatememspace.business.repositories.models.ClientSocketEvents.ClientCloseEvent
import xm.space.ultimatememspace.business.repositories.models.ClientSocketEvents.ClientOpenEvent
import xm.space.ultimatememspace.business.repositories.models.ServerSocketEvents.ServerCloseEvent
import xm.space.ultimatememspace.business.repositories.models.ServerSocketEvents.ServerNetworkEvent
import xm.space.ultimatememspace.business.repositories.network.NetworkRepository
import xm.space.ultimatememspace.business.repositories.networkplayer.NetworkPlayerRepository
import xm.space.ultimatememspace.business.repositories.profile.ProfileRepository
import xm.space.ultimatememspace.business.repositories.reconnectmanager.ReconnectedManagerRepository

/**
 * Implementation [EventsInteractor]
 * @property profileRepository Source data of profile
 * @property networkRepository Admin network source data
 * @property networkPlayerRepository Player network source data
 */
class EventsInteractorImpl(
    private val profileRepository: ProfileRepository,
    private val networkRepository: NetworkRepository,
    private val networkPlayerRepository: NetworkPlayerRepository,
    private val reconnectedManagerRepository: ReconnectedManagerRepository
) : EventsInteractor {

    private val event = MutableSharedFlow<EventsGameProcess>()

    init {
        CoroutineScope(Dispatchers.Unconfined).launch {
            if (profileRepository.isProfileBelongAdmin()) {
                networkRepository.observeSocketEvents().collect { serverSocketEvent ->
                    when (serverSocketEvent) {
                        is ServerCloseEvent -> {
                            reconnectedManagerRepository.clearUsersList()
                            event.emit(ClientDisconnectEvent)
                        }
                        is ServerNetworkEvent -> {
                            event.emit(ForServerEvent(event = serverSocketEvent.event))
                        }
                        else -> Unit
                    }
                }
            } else {
                networkPlayerRepository.observeSocketEvents().collect { clientSocketEvent ->
                    when (clientSocketEvent) {
                        is ClientCloseEvent -> event.emit(ServerShutdownEvent)
                        is ServerEvent ->
                            event.emit(ForClientEvent(event = clientSocketEvent.event))
                        else -> Unit
                    }
                }
            }
        }
    }

    override suspend fun observeEvents() = event.asSharedFlow()
}