package xm.space.ultimatememspace.presentation.screens.relatedchoosememe.waitmeme

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import xm.space.ultimatememspace.core.uikit.components.loaders.CircularWaitProgress
import xm.space.ultimatememspace.core.uikit.components.loaders.OverlayLoader

@Composable
fun WaitMemeContent(
    vm: WaitMemeViewModel = koinViewModel(),
    navController: NavController
) {
    vm.navController = navController
    val state = vm.state.collectAsState()

    BackHandler { vm.onBackFinishClick() }

    CircularWaitProgress(
        waitTitle = state.value.waitTitle,
        animType = state.value.animType
    )

    if (state.value.isOverlayLoading) {
        OverlayLoader(overlayTitle = vm.getOverlayTitle(state.value.overlayTitleResId))
    }
}