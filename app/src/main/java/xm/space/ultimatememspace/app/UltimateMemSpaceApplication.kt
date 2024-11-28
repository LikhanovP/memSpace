package xm.space.ultimatememspace.app

import android.app.Application
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import xm.space.ultimatememspace.core.di.coreModule
import xm.space.ultimatememspace.core.di.interactorsModule
import xm.space.ultimatememspace.core.di.repositoriesModule
import xm.space.ultimatememspace.core.di.viewModelsModule

/**
 * Application class for app
 */
class UltimateMemSpaceApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppCenter.start(
            this, "6236f195-cb7e-4670-b2e0-7776ed0172b4",
            Analytics::class.java, Crashes::class.java
        )

        initKoin()
    }

    /**
     * Initialization DI
     */
    private fun initKoin() {
        startKoin {
            androidContext(this@UltimateMemSpaceApplication)
            modules(
                modules = listOf(
                    coreModule,
                    repositoriesModule,
                    viewModelsModule,
                    interactorsModule
                )
            )
        }
    }
}