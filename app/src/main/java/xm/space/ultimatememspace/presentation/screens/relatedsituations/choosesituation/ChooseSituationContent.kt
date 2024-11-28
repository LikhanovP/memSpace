package xm.space.ultimatememspace.presentation.screens.relatedsituations.choosesituation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import xm.space.ultimatememspace.core.uikit.components.list.ListBottomAction
import xm.space.ultimatememspace.core.uikit.components.loaders.OverlayLoader
import xm.space.ultimatememspace.core.uikit.components.spacers.SpacerItem
import xm.space.ultimatememspace.core.uikit.components.text.StyleSubtitleRegular
import xm.space.ultimatememspace.core.uikit.components.toolbar.RightNavigationToolbar
import xm.space.ultimatememspace.core.uikit.fullHorizontalLinks
import xm.space.ultimatememspace.core.uikit.fullTopLinks
import xm.space.ultimatememspace.presentation.models.situations.SituationUi
import xm.space.ultimatememspace.ui.theme.Ugly
import xm.space.ultimatememspace.ui.theme.White

/**
 * Choose situation screen layout
 * @param vm Screen logic
 * @param navController Navigation
 */
@Composable
fun ChooseSituationContent(
    vm: ChooseSituationViewModel = koinViewModel(),
    navController: NavController
) {
    vm.navController = navController
    val barState = vm.barState.collectAsState()
    val state = vm.state.collectAsState()

    BackHandler { vm.onBackFinishClick() }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (toolbar, situations, proceed) = createRefs()
        RightNavigationToolbar(
            modifier = Modifier.constrainAs(toolbar) {
                fullTopLinks()
            },
            data = barState.value
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(situations) {
                    top.linkTo(toolbar.bottom)
                    bottom.linkTo(parent.bottom)
                    fullHorizontalLinks()
                    height = Dimension.fillToConstraints
                }
        ) {
            items(
                items = state.value.situations,
                itemContent = {
                    SituationItemCard(
                        data = it,
                        onSituationConfirm = { id ->
                            vm.onSituationSelect(id = id)
                        },
                        isSomeoneUsed = state.value.situations.find { situation -> situation.isSelect } != null
                    )
                }
            )
            item { SpacerItem(value = 80F) }
        }

        ListBottomAction(
            proceedTitle = state.value.proceedTitle,
            proceedCallback = { vm.onSituationConfirm() },
            proceedRef = proceed,
            isProceedEnable = state.value.situations.find { it.isSelect } != null
        )
    }

    if (state.value.isOverlayLoading) {
        OverlayLoader(overlayTitle = vm.getOverlayTitle(state.value.overlayTitleResId))
    }
}

/**
 * Layout item with situation
 * @param data Situation data
 */
@Composable
private fun SituationItemCard(
    data: SituationUi,
    onSituationConfirm: (Int) -> Unit,
    isSomeoneUsed: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                .clickable { onSituationConfirm.invoke(data.id) },
            elevation = 4.dp,
            shape = RoundedCornerShape(20.dp),
            backgroundColor = if (isSomeoneUsed) {
                if (data.isSelect) White else Ugly
            } else {
                White
            }
        ) {
            Text(
                modifier = Modifier.padding(24.dp),
                text = data.description,
                textAlign = TextAlign.Center,
                style = StyleSubtitleRegular
            )
        }
    }
}