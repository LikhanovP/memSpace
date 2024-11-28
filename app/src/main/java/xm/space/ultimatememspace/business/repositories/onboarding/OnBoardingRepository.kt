package xm.space.ultimatememspace.business.repositories.onboarding

import xm.space.ultimatememspace.business.domain.models.onboarding.OnBoardingOption

/**
 * Settings for onboarding
 */
interface OnBoardingRepository {

    /**
     * Getting all option for onboarding
     */
    suspend fun getOnBoardingOptions(): List<OnBoardingOption>

    /**
     * Setting flag to true for onboarding view
     */
    suspend fun setOnboardingViewed()

    /**
     * Getting viewed status for onboarding
     */
    suspend fun isOnBoardingViewed(): Boolean
}