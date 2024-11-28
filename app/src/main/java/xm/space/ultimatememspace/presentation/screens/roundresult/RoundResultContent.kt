package xm.space.ultimatememspace.presentation.screens.roundresult

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import xm.space.ultimatememspace.core.uikit.components.image.ImageComponent
import xm.space.ultimatememspace.core.uikit.components.image.ImageSwitcher
import xm.space.ultimatememspace.core.uikit.components.list.ListBottomAction
import xm.space.ultimatememspace.core.uikit.components.loaders.OverlayLoader
import xm.space.ultimatememspace.core.uikit.components.spacers.SpacerCustom
import xm.space.ultimatememspace.core.uikit.components.spacers.SpacerItem
import xm.space.ultimatememspace.core.uikit.components.text.StyleSubhead
import xm.space.ultimatememspace.core.uikit.components.text.StyleSubtitle
import xm.space.ultimatememspace.core.uikit.components.text.TitleItem
import xm.space.ultimatememspace.core.uikit.components.toolbar.RightNavigationToolbar
import xm.space.ultimatememspace.core.uikit.fullHorizontalLinks
import xm.space.ultimatememspace.core.uikit.fullSizeLinks
import xm.space.ultimatememspace.core.uikit.fullTopLinks
import xm.space.ultimatememspace.core.uikit.gridItems
import xm.space.ultimatememspace.core.uikit.justCenterLinks
import xm.space.ultimatememspace.presentation.models.result.ResultUi
import xm.space.ultimatememspace.ui.theme.Black
import xm.space.ultimatememspace.ui.theme.Charizma
import xm.space.ultimatememspace.ui.theme.FullAlpha
import xm.space.ultimatememspace.ui.theme.HalfBlackAlpha
import xm.space.ultimatememspace.ui.theme.MentholAlpha
import xm.space.ultimatememspace.ui.theme.SemiBlackAlpha
import xm.space.ultimatememspace.ui.theme.White

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RoundResultContent(
    vm: RoundResultViewModel = koinViewModel(),
    navController: NavController
) {
    vm.navController = navController
    val barState = vm.barState.collectAsState()
    val state = vm.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmValueChange = {
            if (it == ModalBottomSheetValue.Hidden) {
                vm.onFullViewClose()
            }
            true
        }
    )

    BackHandler { vm.onGeneralBackClick() }

    state.value.curtainState.fullViewResId.first?.let {
        coroutineScope.launch {
            modalSheetState.show()
        }
    }

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        modifier = Modifier.fillMaxSize(),
        sheetState = modalSheetState,
        sheetContent = {
            ImageSwitcher(
                allMemes = state.value.winnersMemes.toMutableList().apply {
                    addAll(state.value.otherMemes)
                }.map { it.memeResId },
                coroutineScope = coroutineScope,
                curtainState = state.value.curtainState,
                closeCallback = { vm.onGeneralBackClick() }
            )
        }
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (toolbar, content, proceed) = createRefs()
            RightNavigationToolbar(
                modifier = Modifier.constrainAs(toolbar) { fullTopLinks() },
                data = barState.value
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
                state.value.winnersMemes.forEach {
                    item {
                        MemeResultItem(data = it) { memeResId ->
                            vm.onFullViewImageClick(memeResId)
                        }
                    }
                    item { SpacerItem(value = 16F) }
                }
                if (state.value.otherMemes.isNotEmpty()) {
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
                    data = state.value.otherMemes,
                    columnCount = 2,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                ) { itemData ->
                    MemeResultItem(
                        modifier = Modifier,
                        data = itemData,
                        longClick = { memeResId ->
                            vm.onFullViewImageClick(memeResId)
                        }
                    )
                }
                item { SpacerItem(value = 80F) }
            }

            if (state.value.isOwner) {
                ListBottomAction(
                    proceedTitle = state.value.nextRoundTitle,
                    proceedCallback = { vm.onNextRoundClick() },
                    proceedRef = proceed
                )
            }

            if (state.value.isOverlayLoading) {
                OverlayLoader(overlayTitle = vm.getOverlayTitle(state.value.overlayTitleResId))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MemeResultItem(
    modifier: Modifier = Modifier.padding(start = 16.dp, end = 16.dp),
    data: ResultUi,
    longClick: (Int) -> Unit
) {
    Card(
        modifier = modifier
            .width(184.dp)
            .height(140.dp),
        shape = RoundedCornerShape(20),
        border = BorderStroke(2.dp, White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onClick = { Unit },
                    onLongClick = {
                        longClick.invoke(data.memeResId)
                    }
                )
        ) {
            ImageComponent(
                modifier = Modifier.fillMaxSize(),
                model = data.memeResId,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
            ConstraintLayout(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                FullAlpha,
                                SemiBlackAlpha,
                                HalfBlackAlpha,
                                Black
                            )
                        )
                    )
                    .padding(start = 8.dp, bottom = 8.dp, end = 8.dp),
            ) {
                val (avatar, name, votes) = createRefs()
                ImageComponent(
                    modifier = Modifier
                        .constrainAs(avatar) {
                            start.linkTo(parent.start)
                            justCenterLinks()
                        }
                        .width(30.dp)
                        .height(30.dp),
                    model = data.playerIcon,
                    contentDescription = String()
                )
                Text(
                    modifier = Modifier
                        .constrainAs(name) {
                            start.linkTo(avatar.end)
                            justCenterLinks()
                        }
                        .padding(start = 4.dp),
                    text = data.playerName,
                    style = StyleSubhead
                )
                ConstraintLayout(
                    modifier = Modifier
                        .constrainAs(votes) {
                            end.linkTo(parent.end)
                            justCenterLinks()
                        }
                        .width(24.dp)
                        .height(24.dp)
                        .background(color = MentholAlpha, shape = CircleShape)
                ) {
                    val text = createRef()
                    Text(
                        modifier = Modifier.constrainAs(text) {
                            fullSizeLinks()
                        },
                        text = data.votes.toString(),
                        color = White,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}