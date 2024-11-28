package xm.space.ultimatememspace.business.domain.interactors

import kotlinx.coroutines.flow.SharedFlow
import xm.space.ultimatememspace.business.domain.models.events.EventsGameProcess

/**
 * Data source of network events
 */
interface EventsInteractor {

    suspend fun observeEvents(): SharedFlow<EventsGameProcess>
}