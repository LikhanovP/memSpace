package xm.space.ultimatememspace.core.di

import org.koin.dsl.module
import xm.space.ultimatememspace.business.domain.interactors.EventsInteractor
import xm.space.ultimatememspace.business.domain.interactors.EventsInteractorImpl

val interactorsModule = module {
    factory<EventsInteractor> { EventsInteractorImpl(get(), get(), get(), get()) }
}