package xm.space.ultimatememspace.presentation.screens.connectedplayers

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.unit.dp
import xm.space.ultimatememspace.core.uikit.components.image.ImageComponent
import xm.space.ultimatememspace.core.uikit.components.loaders.OverlayLoader
import xm.space.ultimatememspace.core.uikit.components.text.StyleBody
import xm.space.ultimatememspace.core.uikit.components.toolbar.RightNavigationToolbar
import xm.space.ultimatememspace.core.uikit.fullBottomLinks
import xm.space.ultimatememspace.core.uikit.fullHorizontalLinks
import xm.space.ultimatememspace.core.uikit.justCenterLinks
import xm.space.ultimatememspace.core.uikit.leftCenterLinks
import xm.space.ultimatememspace.presentation.models.player.PlayerUi
import xm.space.ultimatememspace.ui.theme.GrayMain
import xm.space.ultimatememspace.ui.theme.White

@Composable
fun ConnectedPlayersContent(
    navController: NavController,
    vm: ConnectedPlayersViewModel = koinViewModel()
) {
    vm.navController = navController
    val barState = vm.barState.collectAsState()
    val state = vm.state.collectAsState()

    BackHandler { vm.onBackFinishClick() }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (toolbar, players) = createRefs()
        RightNavigationToolbar(
            data = barState.value,
            modifier = Modifier.constrainAs(toolbar) { fullHorizontalLinks() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(players) {
                    top.linkTo(toolbar.bottom)
                    fullBottomLinks()
                    height = Dimension.fillToConstraints
                }
        ) {
            items(
                items = state.value.players,
                itemContent = {
                    Spacer(modifier = Modifier.height(8.dp))
                    PlayerItemCard(data = it)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            )
        }
    }

    if (state.value.isOverlayLoading) {
        OverlayLoader(overlayTitle = vm.getOverlayTitle(state.value.overlayTitleResId))
    }
}

@Composable
private fun PlayerItemCard(data: PlayerUi) = with(data) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
        ConstraintLayout(
            modifier = Modifier
                .background(color = GrayMain, shape = RoundedCornerShape(30))
                .border(
                    width = if (playerIsMe) 2.dp else 1.dp,
                    color = if (playerIsMe) Green else White,
                    shape = RoundedCornerShape(30)
                )
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
        ) {
            val (userIcon, userName) = createRefs()
            ImageComponent(
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp)
                    .constrainAs(userIcon) { leftCenterLinks() },
                model = playerIconId,
                contentDescription = ""
            )
            Text(
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.Start)
                    .padding(start = 16.dp)
                    .constrainAs(userName) {
                        start.linkTo(userIcon.end)
                        justCenterLinks()
                    },
                text = playerName,
                style = StyleBody
            )
        }
    }
}