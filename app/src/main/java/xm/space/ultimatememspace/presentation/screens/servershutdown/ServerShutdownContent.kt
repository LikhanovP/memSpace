package xm.space.ultimatememspace.presentation.screens.servershutdown

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.airbnb.lottie.LottieAnimationView
import org.koin.androidx.compose.koinViewModel
import xm.space.ultimatememspace.core.uikit.components.button.OutlineMainButton
import xm.space.ultimatememspace.core.uikit.components.text.StyleSubtitle
import xm.space.ultimatememspace.core.uikit.fullHorizontalLinks
import xm.space.ultimatememspace.core.uikit.fullSizeLinks

@Composable
fun ServerShutdownContent(
    vm: ServerShutdownViewModel = koinViewModel(),
    navController: NavController
) {
    vm.navController = navController
    val state = vm.state.collectAsState()

    BackHandler { vm.onBackFinishClick() }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (animation, proceedRef, title, divider) = createRefs()

        AndroidView(
            modifier = Modifier.constrainAs(animation) {
                fullSizeLinks()
            }.padding(32.dp),
            factory = { context ->
                LottieAnimationView(context).apply {
                    setAnimation(state.value.animType)
                    playAnimation()
                    repeatCount = Int.MAX_VALUE
                }
            }
        )

        Spacer(modifier = Modifier.constrainAs(divider) {
            fullSizeLinks()
        })

        Text(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .constrainAs(title) {
                    fullHorizontalLinks()
                    top.linkTo(divider.bottom)
                    bottom.linkTo(proceedRef.top)
                },
            text = state.value.waitTitle,
            style = StyleSubtitle,
            textAlign = TextAlign.Center
        )

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