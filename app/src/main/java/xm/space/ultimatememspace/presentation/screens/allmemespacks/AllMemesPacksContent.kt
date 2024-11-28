package xm.space.ultimatememspace.presentation.screens.allmemespacks

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import xm.space.ultimatememspace.core.uikit.components.image.ImageComponent
import xm.space.ultimatememspace.core.uikit.components.image.ImageSwitcher
import xm.space.ultimatememspace.core.uikit.components.spacers.SpacerItem
import xm.space.ultimatememspace.core.uikit.components.toolbar.RightNavigationToolbar
import xm.space.ultimatememspace.core.uikit.fullBottomLinks
import xm.space.ultimatememspace.core.uikit.fullHorizontalLinks
import xm.space.ultimatememspace.core.uikit.gridItems

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AllMemesPacksContent(
    vm: AllMemesPacksViewModel = koinViewModel(),
    navController: NavController
) {
    vm.navController = navController
    val state = vm.state.collectAsState()
    val barState = vm.barState.collectAsState()
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

    BackHandler {
        vm.onGeneralBackClick()
    }

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
                allMemes = state.value.memesOptionsResIds,
                coroutineScope = coroutineScope,
                curtainState = state.value.curtainState,
                closeCallback = { vm.onGeneralBackClick() }
            )
        }
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (toolbar, memes) = createRefs()

            RightNavigationToolbar(
                data = barState.value,
                modifier = Modifier.constrainAs(toolbar) { fullHorizontalLinks() },
                leftIconCallback = {
                    vm.onCloseClick()
                }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(memes) {
                        top.linkTo(toolbar.bottom)
                        fullBottomLinks()
                        height = Dimension.fillToConstraints
                    }
            ) {
                item { SpacerItem(value = 16F) }
                gridItems(
                    data = state.value.memesOptionsResIds,
                    columnCount = 2,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                ) {
                    MemeResultItem(
                        modifier = Modifier.align(Alignment.Center),
                        memeResId = it
                    ) { memeResId ->
                        vm.onMemeViewerClick(memeResId = memeResId)
                    }
                }
                item { SpacerItem(value = 16F) }
            }
        }
    }
}

@Composable
private fun MemeResultItem(
    modifier: Modifier = Modifier.padding(start = 16.dp, end = 16.dp),
    memeResId: Int,
    memeViewerCallback: (Int) -> Unit
) {
    Card(
        modifier = modifier
            .width(184.dp)
            .height(140.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ImageComponent(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        memeViewerCallback.invoke(memeResId)
                    },
                model = memeResId,
                contentDescription = "",
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
        }
    }
}