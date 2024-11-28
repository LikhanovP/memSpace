package xm.space.ultimatememspace.presentation.screens.creatinggroup

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.with
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.core.uikit.components.image.ImageComponent
import xm.space.ultimatememspace.core.uikit.components.list.ListBottomAction
import xm.space.ultimatememspace.core.uikit.components.loaders.OverlayLoader
import xm.space.ultimatememspace.core.uikit.components.spacers.SpacerItem
import xm.space.ultimatememspace.core.uikit.components.text.StyleBody
import xm.space.ultimatememspace.core.uikit.components.text.StyleSubhead
import xm.space.ultimatememspace.core.uikit.components.text.StyleTitle
import xm.space.ultimatememspace.core.uikit.components.text.TitleItem
import xm.space.ultimatememspace.core.uikit.components.toolbar.RightNavigationToolbar
import xm.space.ultimatememspace.core.uikit.fullHorizontalLinks
import xm.space.ultimatememspace.core.uikit.fullSizeLinks
import xm.space.ultimatememspace.core.uikit.fullTopLinks
import xm.space.ultimatememspace.core.uikit.gridItems
import xm.space.ultimatememspace.core.uikit.verticalFullSizeLinks
import xm.space.ultimatememspace.presentation.models.avatar.AvatarUi
import xm.space.ultimatememspace.presentation.screens.shop.ShopContent
import xm.space.ultimatememspace.ui.theme.Charizma
import xm.space.ultimatememspace.ui.theme.TransPurpleMain
import xm.space.ultimatememspace.ui.theme.White

/**
 * Screen layout for creating group
 * @param vm Screen logic
 * @param navController Navigation
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CreatingGroupContent(
    vm: CreatingGroupViewModel = koinViewModel(),
    navController: NavController
) {
    vm.navController = navController
    val state = vm.state.collectAsState()
    val barState = vm.barState.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    BackHandler { vm.onBackClick() }

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

    if (state.value.isCurtainShow) {
        showBottomSheet(
            modalSheetState = modalSheetState,
            coroutineScope = coroutineScope
        )
    }


    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        modifier = Modifier.fillMaxSize(),
        sheetState = modalSheetState,
        sheetContent = {
            ShopContent(
                data = state.value.shopData,
                allMemesAtPackCallback = { memesPack ->
                    vm.onAllMemesAtPackClick(memesPack = memesPack)
                }
            )
        }
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (toolbarRef, proceedRef, contentRef) = createRefs()
            Row(modifier = Modifier.constrainAs(toolbarRef) { fullTopLinks() }) {
                RightNavigationToolbar(
                    data = barState.value,
                    rightIconCallback = { vm.onMemesShopClick() }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(contentRef) {
                        fullHorizontalLinks()
                        top.linkTo(toolbarRef.bottom)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }
            ) {
                item { SpacerItem(value = 16F) }
                item { AttentionItem(makeSureTitle = state.value.makeSureTitle) }
                item { SpacerItem(value = 32F) }
                item { QrCodeItem(qrWifi = state.value.qrWifi) }
                item {
                    RoundCountPicker(state.value.roundCountTitle) { vm.onRoundCountChange(it) }
                }
                item { SpacerItem(value = 32F) }
                if (state.value.players.isNotEmpty()) {
                    item { TitleItem(value = state.value.connectedTitle, style = StyleBody) }
                    item { SpacerItem(value = 16F) }
                    gridItems(
                        data = state.value.players,
                        columnCount = 5,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    ) { itemData ->
                        PlayerItem(
                            playerName = itemData.playerName,
                            playerAvatar = itemData.getAvatarUi()
                        )
                    }
                    item { SpacerItem(value = 80F) }
                }
            }
            ListBottomAction(
                proceedTitle = state.value.gameStartTitle,
                proceedCallback = { vm.startGameClick() },
                proceedRef = proceedRef,
                isProceedEnable = true//state.value.players.size > 1
            )
        }
    }

    if (state.value.isOverlayLoading) {
        OverlayLoader(overlayTitle = vm.getOverlayTitle(state.value.overlayTitleResId))
    }
}

/**
 * Warning component
 * @param makeSureTitle Description warning
 */
@Composable
private fun AttentionItem(makeSureTitle: String) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp)
) {
    ImageComponent(
        modifier = Modifier,
        model = R.drawable.ic_attention,
        contentDescription = String()
    )
    Text(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        text = makeSureTitle,
        style = StyleBody
    )
}

/**
 * Connected user item
 * @param playerName User nickname
 * @param playerAvatar User avatar
 */
@Composable
private fun PlayerItem(
    playerName: String,
    playerAvatar: AvatarUi
) {
    Column {
        Box(modifier = Modifier.padding(6.dp)) {
            ImageComponent(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
                model = playerAvatar.resId,
                contentDescription = String()
            )
            if (playerAvatar.isSelect) {
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp)
                        .background(color = TransPurpleMain, shape = CircleShape)
                ) {
                    Icon(
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                            .align(alignment = Alignment.BottomCenter),
                        painter = painterResource(id = playerAvatar.resId),
                        contentDescription = String(),
                        tint = White
                    )
                }
            }
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = playerName,
            style = StyleSubhead,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Qr code image item
 * @param qrWifi Bitmap qr
 */
@Composable
private fun QrCodeItem(qrWifi: Bitmap?) = Box(
    modifier = Modifier
        .fillMaxSize()
        .height(300.dp)
) {
    Canvas(
        modifier = Modifier
            .width(310.dp)
            .height(300.dp)
            .align(alignment = Alignment.TopCenter),
        onDraw = {
            drawRoundRect(
                color = White,
                cornerRadius = CornerRadius(
                    x = 80F,
                    y = 80F
                )
            )
        }
    )

    ImageComponent(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .width(300.dp)
            .height(if (qrWifi != null) 300.dp else 0.dp),
        modelBitmap = qrWifi,
        contentDescription = String()
    )

    if (qrWifi == null) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = Charizma
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun showBottomSheet(
    modalSheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch {
        modalSheetState.show()
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun RoundCountPicker(title: String, roundCountCallback: (Int) -> Unit) {
    var value by remember { mutableStateOf("3") }
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (number, leftArrow, rightArrow, desc) = createRefs()
        AnimatedContent(
            modifier = Modifier
                .constrainAs(number) { fullSizeLinks() }
                .padding(start = 32.dp, end = 32.dp),
            targetState = value,
            transitionSpec = {
                scaleIn(animationSpec = tween(durationMillis = 200)) with
                        ExitTransition.None
            },
            contentAlignment = Alignment.Center, label = String.empty()
        ) { count ->
            Text(
                text = count,
                style = StyleTitle
            )
        }
        IconButton(
            modifier = Modifier.constrainAs(leftArrow) {
                verticalFullSizeLinks()
                end.linkTo(number.start)
            },
            onClick = {
                val currentValue = value.toInt()
                if (currentValue > 1) {
                    value = (currentValue - 1).toString()
                    roundCountCallback.invoke(currentValue - 1)
                }
                else Unit
            }
        ) {
            Icon(
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp),
                painter = painterResource(id = R.drawable.ic_chevron_left),
                contentDescription = String.empty(),
                tint = White
            )
        }
        IconButton(
            modifier = Modifier.constrainAs(rightArrow) {
                verticalFullSizeLinks()
                start.linkTo(number.end)
            },
            onClick = {
                val currentValue = value.toInt()
                if (currentValue < 10) {
                    value = (currentValue + 1).toString()
                    roundCountCallback.invoke(currentValue + 1)
                }
                else Unit
            }
        ) {
            Icon(
                modifier = Modifier
                    .width(36.dp)
                    .height(36.dp),
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = String.empty(),
                tint = White
            )
        }
        Text(
            modifier = Modifier
                .constrainAs(desc) {
                    fullHorizontalLinks()
                    top.linkTo(number.bottom)
                }
                .padding(top = 12.dp),
            text = title,
            style = StyleBody
        )
    }
}