package xm.space.ultimatememspace.presentation.screens.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.google.accompanist.pager.rememberPagerState
import org.koin.androidx.compose.koinViewModel
import xm.space.ultimatememspace.core.uikit.components.button.OutlineMainButton
import xm.space.ultimatememspace.core.uikit.components.image.ImageComponent
import xm.space.ultimatememspace.core.uikit.components.text.StyleTitle
import xm.space.ultimatememspace.core.uikit.components.toolbar.RightNavigationToolbar
import xm.space.ultimatememspace.core.uikit.fullBottomLinks
import xm.space.ultimatememspace.core.uikit.fullHorizontalLinks
import xm.space.ultimatememspace.core.uikit.fullTopLinks
import xm.space.ultimatememspace.navigation.ON_BOARDING
import xm.space.ultimatememspace.navigation.PROFILE_CONTENT
import xm.space.ultimatememspace.presentation.screens.onboarding.models.OnBoardingItem
import xm.space.ultimatememspace.ui.theme.DarkPurpleMain
import xm.space.ultimatememspace.ui.theme.White

/**
 * Onboarding screen layout
 * @param vm Screen logic
 * @param navController Navigation router
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(
    vm: OnBoardingViewModel = koinViewModel(),
    navController: NavController
) {
    val barState = vm.barState.collectAsState()
    val state = vm.state.collectAsState()

    BackHandler { vm.onBackFinishClick() }
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (toolbar, pager, dots, proceed) = createRefs()
        Row(modifier = Modifier.constrainAs(toolbar) { fullTopLinks() }) {
            RightNavigationToolbar(
                data = barState.value,
                leftIconCallback = {
                    vm.onboardingAnnounced()
                    navController.navigate(PROFILE_CONTENT) {
                        popUpTo(ON_BOARDING) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .constrainAs(dots) {
                    fullHorizontalLinks()
                    bottom.linkTo(proceed.top)
                }
                .padding(bottom = 36.dp)
        ) {
            HorizontalPagerIndicator(
                pagerState = pagerState,
                activeColor = White,
                inactiveColor = DarkPurpleMain,
                indicatorWidth = 13.dp,
                spacing = 16.dp
            )
        }

        OutlineMainButton(
            modifier = Modifier
                .constrainAs(proceed) { fullBottomLinks() }
                .padding(bottom = 27.dp),
            actionTitle = vm.proceedTitle,
            callback = {
                if (pagerState.currentPage != state.value.size - 1) {
                    coroutineScope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                } else {
                    vm.onboardingAnnounced()
                    navController.navigate(PROFILE_CONTENT) {
                        popUpTo(ON_BOARDING) {
                            inclusive = true
                        }
                    }
                }
            }
        )

        Column(
            modifier = Modifier.constrainAs(pager) {
                fullHorizontalLinks()
                top.linkTo(toolbar.bottom)
                bottom.linkTo(dots.top)
            }
        ) {
            OnBoardingPager(
                data = state.value,
                state = pagerState
            )
        }
    }
}

/**
 * Screen layout
 * @param data OnBoarding pages
 * @param state Pager state
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
private fun OnBoardingPager(
    data: List<OnBoardingItem>,
    state: PagerState = rememberPagerState()
) = HorizontalPager(
    state = state,
    count = data.size
) { currentPage ->
    with(data[currentPage]) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            ImageComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp),
                model = image,
                contentDescription = String()
            )
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = description,
                modifier = Modifier.fillMaxWidth(),
                style = StyleTitle,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}