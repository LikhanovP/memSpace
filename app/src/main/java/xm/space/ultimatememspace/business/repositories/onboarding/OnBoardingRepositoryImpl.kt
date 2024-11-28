package xm.space.ultimatememspace.business.repositories.onboarding

import xm.space.ultimatememspace.business.domain.models.onboarding.OnBoardingOption
import xm.space.ultimatememspace.business.repositories.settings.SettingsRepository

/**
 * Implementation [OnBoardingRepository]
 * @property settingsRepository Settings data source
 */
class OnBoardingRepositoryImpl(
    private val settingsRepository: SettingsRepository
) : OnBoardingRepository {

    override suspend fun getOnBoardingOptions() = OnBoardingOption.getOnBoardingItems()

    override suspend fun isOnBoardingViewed() = settingsRepository.isOnBoardingViewed()

    override suspend fun setOnboardingViewed() { settingsRepository.setOnboardingViewed() }
}