package xm.space.ultimatememspace.business.repositories.settings

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import xm.space.ultimatememspace.business.repositories.getValue
import xm.space.ultimatememspace.business.repositories.setValue

/**
 * Implementation [SettingsRepository]
 * @property preferences App sandbox
 */
class SettingsRepositoryImpl(
    private val preferences: SharedPreferences
) : SettingsRepository {

    private val finishEvent = MutableSharedFlow<Boolean>()

    /*** View flag onboarding */
    private var isOnBoardingWasView: Boolean by preferences

    private var serverIp: String by preferences

    private var serverPort: String by preferences

    override suspend fun isOnBoardingViewed() = isOnBoardingWasView

    override suspend fun setOnboardingViewed() { isOnBoardingWasView = true }

    override suspend fun setNetworkData(ip: String, port: String) {
        serverIp = ip
        serverPort = port
    }

    override suspend fun getNetworkData() = serverIp to serverPort

    override suspend fun observeFinishState(): SharedFlow<Boolean> = finishEvent.asSharedFlow()

    override suspend fun letFinishApp() {
        finishEvent.emit(true)
    }
}