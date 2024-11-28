package xm.space.ultimatememspace.core.di

import android.app.Application
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import xm.space.ultimatememspace.core.providers.ResourceProvider
import xm.space.ultimatememspace.core.providers.ResourceProviderImpl

val coreModule = module {
    single<ResourceProvider> { ResourceProviderImpl(get()) }
    single { getSharedPrefs(androidApplication = androidApplication()) }
}

private fun getSharedPrefs(androidApplication: Application): SharedPreferences {
    return androidApplication.getSharedPreferences(APP_PREFERENCES, android.content.Context.MODE_PRIVATE)
}

private const val APP_PREFERENCES = "ultimateMemeSettings"