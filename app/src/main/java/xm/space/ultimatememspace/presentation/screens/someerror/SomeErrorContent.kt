package xm.space.ultimatememspace.presentation.screens.someerror

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.core.uikit.components.button.OutlineMainButton
import xm.space.ultimatememspace.core.uikit.components.text.StyleBodyHint
import xm.space.ultimatememspace.core.uikit.components.text.StyleSubtitle
import xm.space.ultimatememspace.core.uikit.fullHorizontalLinks
import xm.space.ultimatememspace.core.uikit.fullSizeLinks
import xm.space.ultimatememspace.ui.theme.Error

@Composable
fun SomeErrorContent(
    vm: SomeErrorViewModel = koinViewModel(),
    navController: NavController
) {
    vm.navController = navController
    val state = vm.state.collectAsState()

    BackHandler { vm.onBackFinishClick() }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (proceedRef, content) = createRefs()

        ConstraintLayout(modifier = Modifier.constrainAs(content) {
            fullSizeLinks()
        }) {
            val (icon, main, description) = createRefs()
            Icon(
                modifier = Modifier.constrainAs(icon) {
                    fullHorizontalLinks()
                },
                painter = painterResource(id = R.drawable.ic_cross),
                contentDescription = "",
                tint = Error
            )
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .constrainAs(main) {
                        fullHorizontalLinks()
                        top.linkTo(icon.bottom)
                    },
                text = state.value.errorTitle,
                style = StyleSubtitle,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                    .constrainAs(description) {
                        fullHorizontalLinks()
                        top.linkTo(main.bottom)
                    },
                text = state.value.errorDescription,
                style = StyleBodyHint,
                textAlign = TextAlign.Center
            )
        }

        OutlineMainButton(
            modifier = Modifier
                .constrainAs(proceedRef) {
                    fullHorizontalLinks()
                    bottom.linkTo(parent.bottom)
                }
                .padding(bottom = 27.dp),
            actionTitle = state.value.proceedTitle,
            callback = { vm.onProceedClick() }
        )
    }
}