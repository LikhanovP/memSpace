package xm.space.ultimatememspace.presentation.screens.relatedchoosememe.choosememe

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
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
import xm.space.ultimatememspace.core.uikit.gridItems
import xm.space.ultimatememspace.presentation.models.meme.MemeUi
import xm.space.ultimatememspace.ui.theme.Black
import xm.space.ultimatememspace.ui.theme.LiteBlue
import xm.space.ultimatememspace.ui.theme.White

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ChooseMemeContent(
    vm: ChooseMemeViewModel = koinViewModel(),
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

    state.value.curtainState.fullViewResId.second.let { value ->
        coroutineScope.launch {
            if (value) {
                modalSheetState.show()
            } else {
                modalSheetState.hide()
            }
        }
    }

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        modifier = Modifier.fillMaxSize(),
        sheetState = modalSheetState,
        sheetContent = {
            ImageSwitcher(
                allMemes = state.value.memes.map { it.memeResId },
                coroutineScope = coroutineScope,
                curtainState = state.value.curtainState,
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
                data = barState.value,
                modifier = Modifier.constrainAs(toolbar) { fullTopLinks() }
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
                item { SituationItem(description = state.value.situation.description) }
                item { SpacerItem(value = 24F) }
                item { TitleItem(value = state.value.memesTitle, style = StyleSubtitle) }
                item { SpacerItem(value = 16F) }
                gridItems(
                    data = state.value.memes,
                    columnCount = 2,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                ) { itemData ->
                    MemVoteItem(
                        data = itemData,
                        proceedCallback = { memeId -> vm.onMemeSelect(memeId = memeId) },
                        proceedLongCallback = { memeResId ->
                            vm.onFullViewImageClick(memeResId)
                        }
                    )
                }
                item { SpacerItem(value = 80F) }
            }
            ListBottomAction(
                proceedTitle = state.value.confirmMemeTitle,
                proceedCallback = { vm.onMemeConfirm() },
                proceedRef = proceed,
                isProceedEnable = state.value.memes.find { it.memeSelect } != null
            )
        }

        if (state.value.isOverlayLoading) {
            OverlayLoader(overlayTitle = vm.getOverlayTitle(state.value.overlayTitleResId))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MemVoteItem(
    data: MemeUi,
    proceedCallback: (Int) -> Unit,
    proceedLongCallback: (Int) -> Unit
) = Box(
    modifier = Modifier
        .width(164.dp)
        .height(140.dp)
        .padding(
            start = 2.dp,
            bottom = 16.dp,
            end = 2.dp
        )
) {
    Card(
        modifier = Modifier
            .width(400.dp)
            .height(300.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(20.dp),
        backgroundColor = LiteBlue,
        border = if (data.memeSelect) BorderStroke(2.dp, White) else null
    ) {
        ImageComponent(
            model = data.memeResId,
            contentDescription = "",
            modifier = Modifier.combinedClickable(
                onLongClick = { proceedLongCallback.invoke(data.memeResId) },
                onClick = { proceedCallback.invoke(data.memeId) }
            ),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
    }
    if (data.memeSelect) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .align(alignment = Alignment.TopEnd)
                .clip(CircleShape)
                .size(20.dp)
                .background(Black)
                .clip(CircleShape)
                .background(White),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.Check, contentDescription = "")
        }
    } else {
        Box(
            modifier = Modifier
                .size(35.dp)
                .clip(CircleShape)
                .padding(8.dp)
                .align(alignment = Alignment.TopEnd)
                .border(
                    border = BorderStroke(width = 1.dp, color = White),
                    shape = CircleShape
                )
        )
    }
}