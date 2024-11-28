package xm.space.ultimatememspace.core.di

import org.koin.dsl.module
import xm.space.ultimatememspace.business.repositories.avatars.AvatarRepository
import xm.space.ultimatememspace.business.repositories.avatars.AvatarRepositoryImpl
import xm.space.ultimatememspace.business.repositories.memes.MemesRepository
import xm.space.ultimatememspace.business.repositories.memes.MemesRepositoryImpl
import xm.space.ultimatememspace.business.repositories.memes.freememes.FreeMemesRepository
import xm.space.ultimatememspace.business.repositories.memes.freememes.FreeMemesRepositoryImpl
import xm.space.ultimatememspace.business.repositories.memes.paidmemes.PaidMemesRepository
import xm.space.ultimatememspace.business.repositories.memes.paidmemes.PaidMemesRepositoryImpl
import xm.space.ultimatememspace.business.repositories.network.NetworkRepository
import xm.space.ultimatememspace.business.repositories.network.NetworkRepositoryImpl
import xm.space.ultimatememspace.business.repositories.networkplayer.NetworkPlayerRepository
import xm.space.ultimatememspace.business.repositories.networkplayer.NetworkPlayerRepositoryImpl
import xm.space.ultimatememspace.business.repositories.onboarding.OnBoardingRepository
import xm.space.ultimatememspace.business.repositories.onboarding.OnBoardingRepositoryImpl
import xm.space.ultimatememspace.business.repositories.practicemanager.PracticeManagerRepository
import xm.space.ultimatememspace.business.repositories.practicemanager.PracticeManagerRepositoryImpl
import xm.space.ultimatememspace.business.repositories.practicemanager.UserPracticeManagerRepository
import xm.space.ultimatememspace.business.repositories.practicemanager.UserPracticeManagerRepositoryImpl
import xm.space.ultimatememspace.business.repositories.profile.ProfileRepository
import xm.space.ultimatememspace.business.repositories.profile.ProfileRepositoryImpl
import xm.space.ultimatememspace.business.repositories.reconnectmanager.ReconnectedManagerRepository
import xm.space.ultimatememspace.business.repositories.reconnectmanager.ReconnectedManagerRepositoryImpl
import xm.space.ultimatememspace.business.repositories.settings.SettingsRepository
import xm.space.ultimatememspace.business.repositories.settings.SettingsRepositoryImpl
import xm.space.ultimatememspace.business.repositories.shop.ShopRepository
import xm.space.ultimatememspace.business.repositories.shop.ShopRepositoryImpl
import xm.space.ultimatememspace.business.repositories.situations.SituationsRepository
import xm.space.ultimatememspace.business.repositories.situations.SituationsRepositoryImpl

val repositoriesModule = module {
    single<PracticeManagerRepository> { PracticeManagerRepositoryImpl(get(), get(), get(), get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    single<OnBoardingRepository> { OnBoardingRepositoryImpl(get()) }
    single<NetworkRepository> { NetworkRepositoryImpl(get(), get(), get(), get(), get()) }
    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
    single<AvatarRepository> { AvatarRepositoryImpl() }
    single<NetworkPlayerRepository> { NetworkPlayerRepositoryImpl(get(), get()) }
    single<MemesRepository> { MemesRepositoryImpl(get(), get(), get()) }
    single<SituationsRepository> { SituationsRepositoryImpl(get()) }
    single<UserPracticeManagerRepository> { UserPracticeManagerRepositoryImpl(get(), get(), get()) }
    single<ShopRepository> { ShopRepositoryImpl(get(), get(), get(), get()) }
    single<ReconnectedManagerRepository> { ReconnectedManagerRepositoryImpl() }
    single<FreeMemesRepository> { FreeMemesRepositoryImpl() }
    single<PaidMemesRepository> { PaidMemesRepositoryImpl() }
}