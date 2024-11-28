package xm.space.ultimatememspace.business.domain.models.onboarding

import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.core.providers.ResourceProvider
import xm.space.ultimatememspace.presentation.screens.onboarding.models.OnBoardingItem

/**
 * States for instruction items
 */
sealed class OnBoardingOption {

    /*** Item title*/
    abstract val title: Int?

    /*** Item image*/
    abstract val image: Int

    /*** Item description*/
    abstract val description: Int

    /*** Game description option */
    data object GroupOption : OnBoardingOption() {
        override val title = R.string.app_name
        override val image = R.drawable.fashion_group
        override val description = R.string.onboarding_group
    }

    /*** Game timing option */
    data object TimeOption : OnBoardingOption() {
        override val title: Int? = null
        override val image = R.drawable.fashion_time
        override val description = R.string.onboarding_time
    }

    /*** Memes other users option */
    data object AnotherUsersOption : OnBoardingOption() {
        override val title: Int? = null
        override val image = R.drawable.fashion_another_user
        override val description = R.string.onboarding_another_users
    }

    /*** Choose best meme option */
    data object CupOption : OnBoardingOption() {
        override val title: Int? = null
        override val image = R.drawable.fashion_cup
        override val description = R.string.onboarding_cup
    }

    /*** Create group and lets play option */
    data object StartOption : OnBoardingOption() {
        override val title: Int? = null
        override val image: Int = R.drawable.fashion_rocket
        override val description: Int = R.string.onboarding_rocket
    }

    companion object {

        /**
         * Getting all option for onboarding
         */
        fun getOnBoardingItems() = listOf(GroupOption, TimeOption, AnotherUsersOption, CupOption, StartOption)
    }
}

/**
 * Transformation [OnBoardingOption] to [OnBoardingItem]
 * @param resourceProvider resource provider
 */
fun OnBoardingOption.toUi(resourceProvider: ResourceProvider) = OnBoardingItem(
    title = title?.let { resourceProvider.getString(it) }.orEmpty(),
    image = image,
    description = resourceProvider.getString(description)
)