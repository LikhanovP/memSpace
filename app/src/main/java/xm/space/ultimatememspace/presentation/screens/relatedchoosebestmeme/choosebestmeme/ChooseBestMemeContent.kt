package xm.space.ultimatememspace.presentation.screens.relatedchoosebestmeme.choosebestmeme

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.core.uikit.components.image.ImageComponent
import xm.space.ultimatememspace.core.uikit.components.image.ImageSwitcher
import xm.space.ultimatememspace.core.uikit.components.list.ListBottomAction
import xm.space.ultimatememspace.core.uikit.components.loaders.OverlayLoader
import xm.space.ultimatememspace.core.uikit.components.situation.SituationItem
import xm.space.ultimatememspace.core.uikit.components.spacers.SpacerItem
import xm.space.ultimatememspace.core.uikit.components.text.StyleSubtitle
import xm.space.ultimatememspace.core.uikit.components.text.TitleItem
import xm.space.ultimatememspace.core.uikit.components.toolbar.RightNavigationToolbar
import xm.space.ultimatememspace.core.uikit.fullHorizontalLinks
import xm.space.ultimatememspace.core.uikit.fullTopLinks
import xm.space.ultimatememspace.ui.theme.Black
import xm.space.ultimatememspace.ui.theme.DarkPurpleMain
import xm.space.ultimatememspace.ui.theme.LiteBlue
import xm.space.ultimatememspace.ui.theme.White

@OptIn(
    ExperimentalPagerApi::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ChooseBestMemeContent(
    vm: ChooseBestMemeViewModel = koinViewModel(),
    navController: NavController
) {
    vm.navController = navController
    val barState = vm.barState.collectAsState()
    val state = vm.state.collectAsState()
    val pagerState = rememberPagerState()
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
                allMemes = state.value.memes.map { it.masterMeme.memeResId },
                coroutineScope = coroutineScope,
                curtainState = state.value.curtainState,
                onPageChanged = { currentPageActive ->
                    coroutineScope.launch {
                        pagerState.scrollToPage(page = currentPageActive)
                    }
                },
                closeCallback = { vm.onGeneralBackClick() },
                selectCallback = { resId ->
                    vm.onMemeCurtainSelect(resId = resId)
                }
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
                item { TitleItem(value = state.value.situationTitle, style = StyleSubtitle) }
                item { SpacerItem(value = 16F) }
                item { SituationItem(description = state.value.situationDescription) }
                item { SpacerItem(value = 24F) }
                item { TitleItem(value = state.value.memesTitle, style = StyleSubtitle) }
                item { SpacerItem(value = 16F) }
                item {
                    HorizontalPager(
                        modifier = Modifier.fillMaxWidth(),
                        count = state.value.memes.size,
                        state = pagerState
                    ) { currentPage ->
                        with(state.value.memes[currentPage]) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .combinedClickable(
                                        onClick = { vm.onBestMemWasSelect(masterMeme.memeId) },
                                        onLongClick = { vm.onBestMemeLongClick(masterMeme.memeResId) }
                                    )
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(355.dp)
                                        .padding(start = 16.dp, end = 16.dp),
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(20.dp),
                                    backgroundColor = LiteBlue,
                                    border = if (masterMeme.memeSelect) BorderStroke(
                                        2.dp,
                                        White
                                    ) else null
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight()
                                    ) {
                                        ImageComponent(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .fillMaxHeight(),
                                            model = masterMeme.memeResId,
                                            contentDescription = "",
                                            contentScale = ContentScale.Crop,
                                            alignment = Alignment.Center
                                        )
                                        if (masterMeme.memeSelect) {
                                            Box(
                                                modifier = Modifier
                                                    .padding(12.dp)
                                                    .align(alignment = Alignment.TopEnd)
                                                    .clip(CircleShape)
                                                    .size(24.dp)
                                                    .background(Black)
                                                    .clip(CircleShape)
                                                    .background(White)
                                                    .clickable {
                                                        vm.onBestMemWasSelect(masterMeme.memeId)
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = String.empty()
                                                )
                                            }
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .size(46.dp)
                                                    .clip(CircleShape)
                                                    .padding(12.dp)
                                                    .align(alignment = Alignment.TopEnd)
                                                    .border(
                                                        border = BorderStroke(
                                                            width = 1.dp,
                                                            color = White
                                                        ),
                                                        shape = CircleShape
                                                    )
                                                    .clickable {
                                                        vm.onBestMemWasSelect(masterMeme.memeId)
                                                    }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                item { SpacerItem(value = 16F) }
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        HorizontalPagerIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            pagerState = pagerState,
                            activeColor = White,
                            inactiveColor = DarkPurpleMain,
                            indicatorWidth = 8.dp,
                            spacing = 8.dp
                        )
                    }
                }
                item { SpacerItem(value = 80F) }
            }

            ListBottomAction(
                proceedTitle = state.value.confirmVoteTitle,
                proceedCallback = { vm.onBestMemSelectConfirm() },
                proceedRef = proceed,
                isProceedEnable = state.value.memes.find { it.masterMeme.memeSelect } != null
            )
        }

        if (state.value.isOverlayLoading) {
            OverlayLoader(overlayTitle = vm.getOverlayTitle(state.value.overlayTitleResId))
        }
    }
}