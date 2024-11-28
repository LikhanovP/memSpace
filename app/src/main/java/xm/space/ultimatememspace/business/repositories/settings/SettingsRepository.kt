package xm.space.ultimatememspace.business.repositories.settings

import kotlinx.coroutines.flow.SharedFlow

/**
 * Settings data source
 */
interface SettingsRepository {

    /**
     * Set value contains flag of view onboarding to true
     */
    suspend fun setOnboardingViewed()

    /**
     * Getting the value whether the onboarding was viewed
     */
    suspend fun isOnBoardingViewed(): Boolean

    suspend fun setNetworkData(ip: String, port: String)

    suspend fun getNetworkData(): Pair<String, String>

    suspend fun observeFinishState(): SharedFlow<Boolean>

    suspend fun letFinishApp()
}