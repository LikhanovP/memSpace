package xm.space.ultimatememspace.presentation.screens.gameresult

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import xm.space.ultimatememspace.business.domain.models.results.PlayerResult
import xm.space.ultimatememspace.core.uikit.components.image.ImageComponent
import xm.space.ultimatememspace.core.uikit.components.list.ListBottomAction
import xm.space.ultimatememspace.core.uikit.components.loaders.OverlayLoader
import xm.space.ultimatememspace.core.uikit.components.spacers.SpacerCustom
import xm.space.ultimatememspace.core.uikit.components.spacers.SpacerItem
import xm.space.ultimatememspace.core.uikit.components.text.StyleSubtitle
import xm.space.ultimatememspace.core.uikit.components.text.TitleItem
import xm.space.ultimatememspace.core.uikit.components.toolbar.RightNavigationToolbar
import xm.space.ultimatememspace.core.uikit.fullBottomLinks
import xm.space.ultimatememspace.core.uikit.fullHorizontalLinks
import xm.space.ultimatememspace.core.uikit.fullSizeLinks
import xm.space.ultimatememspace.core.uikit.fullTopLinks
import xm.space.ultimatememspace.core.uikit.gridItems
import xm.space.ultimatememspace.ui.theme.Black
import xm.space.ultimatememspace.ui.theme.Charizma
import xm.space.ultimatememspace.ui.theme.FullAlpha
import xm.space.ultimatememspace.ui.theme.HalfBlackAlpha
import xm.space.ultimatememspace.ui.theme.MentholAlpha
import xm.space.ultimatememspace.ui.theme.SemiBlackAlpha

@Composable
fun GameResultContent(
    vm: GameResultViewModel = koinViewModel(),
    navController: NavController
) {
    vm.navController = navController
    val barState = vm.barState.collectAsState()
    val state = vm.state.collectAsState()

    BackHandler { vm.onBackFinishClick() }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (toolbar, content, proceed) = createRefs()
        RightNavigationToolbar(
            data = barState.value,
            modifier = Modifier.constrainAs(toolbar) {
                fullTopLinks()
            }
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(content) {
                    top.linkTo(toolbar.bottom)
                    bottom.linkTo(parent.bottom)
                    fullHorizontalLinks()
                    height = Dimension.fillToConstraints
                }
        ) {
            item { SpacerItem(value = 16F) }
            item { TitleItem(value = state.value.winnerTitle, style = StyleSubtitle) }
            item { SpacerItem(value = 16F) }
            state.value.winnerGame.forEach {
                item { PlayerResultItem(data = it) }
                item { SpacerItem(value = 16F) }
            }
            if (state.value.resultsByPlayers.isNotEmpty()) {
                item {
                    SpacerCustom(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(color = Charizma, shape = RoundedCornerShape(1.dp))
                    )
                }
            }
            item { SpacerItem(value = 16F) }
            gridItems(
                data = state.value.resultsByPlayers,
                columnCount = 2,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
            ) { itemData -> PlayerResultItem(data = itemData) }
            item { SpacerItem(value = 80F) }
        }

        if (state.value.isOwner) {
            ListBottomAction(
                proceedTitle = state.value.nextRoundTitle,
                proceedCallback = { vm.onNewGameClick() },
                proceedRef = proceed
            )
        }
    }

    if (state.value.isOverlayLoading) {
        OverlayLoader(overlayTitle = vm.getOverlayTitle(state.value.overlayTitleResId))
    }
}

@Composable
private fun PlayerResultItem(
    modifier: Modifier = Modifier.padding(start = 16.dp, end = 16.dp),
    data: PlayerResult
) {
    ConstraintLayout(
        modifier = modifier
            .width(150.dp)
            .height(150.dp)
    ) {
        val (avatar, votes, name) = createRefs()
        ImageComponent(
            modifier = Modifier.constrainAs(avatar) {
                fullSizeLinks()
                height = Dimension.fillToConstraints
                width = Dimension.fillToConstraints
            },
            model = data.playerAvatar,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
        ConstraintLayout(
            modifier = Modifier
                .constrainAs(votes) { fullBottomLinks() }
                .width(32.dp)
                .height(32.dp)
                .background(color = MentholAlpha, shape = CircleShape)
        ) {
            val text = createRef()
            Text(
                modifier = Modifier.constrainAs(text) {
                    fullSizeLinks()
                },
                text = data.playerWinsCount.toString(),
                textAlign = TextAlign.Center,
                style = StyleSubtitle
            )
        }
        ConstraintLayout(
            modifier = Modifier
                .constrainAs(name) {
                    fullSizeLinks()
                }
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            FullAlpha,
                            SemiBlackAlpha,
                            HalfBlackAlpha,
                            Black
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                ),
        ) {
            val playerName = createRef()
            Text(
                modifier = Modifier
                    .constrainAs(playerName) { fullSizeLinks() }
                    .padding(start = 4.dp, end = 4.dp),
                text = data.playerName,
                style = StyleSubtitle
            )
        }
    }
}