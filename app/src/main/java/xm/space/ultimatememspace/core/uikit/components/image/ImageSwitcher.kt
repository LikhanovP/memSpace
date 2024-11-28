@file:OptIn(ExperimentalPagerApi::class, ExperimentalPagerApi::class, ExperimentalPagerApi::class)

package xm.space.ultimatememspace.core.uikit.components.image

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import xm.space.ultimatememspace.core.uikit.components.image.models.CurtainState
import xm.space.ultimatememspace.core.uikit.components.text.StyleBodyBold
import xm.space.ultimatememspace.core.uikit.components.text.StyleBodyBoldWhite
import xm.space.ultimatememspace.core.uikit.fullBottomLinks
import xm.space.ultimatememspace.core.uikit.fullHorizontalLinks
import xm.space.ultimatememspace.core.uikit.topLeftLinks
import xm.space.ultimatememspace.core.uikit.topRightLinks
import xm.space.ultimatememspace.ui.theme.DarkPurpleMain
import xm.space.ultimatememspace.ui.theme.ShopFive
import xm.space.ultimatememspace.ui.theme.ShopFour
import xm.space.ultimatememspace.ui.theme.ShopOne
import xm.space.ultimatememspace.ui.theme.ShopSix
import xm.space.ultimatememspace.ui.theme.ShopThree
import xm.space.ultimatememspace.ui.theme.ShopTwo
import xm.space.ultimatememspace.ui.theme.White

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ImageSwitcher(
    allMemes: List<Int>,
    coroutineScope: CoroutineScope,
    curtainState: CurtainState,
    closeCallback: () -> Unit = {},
    selectCallback: (Int) -> Unit = {},
    onPageChanged: (Int) -> Unit = {}
) {
    val pagerState = rememberPagerState()

    coroutineScope.launch {
        curtainState.fullViewResId.first?.let {
            pagerState.scrollToPage(page = it)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onPageChanged(page)
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(ShopOne, ShopTwo, ShopThree, ShopFour, ShopFive, ShopSix)
                )
            )
            .padding(top = 8.dp)
    ) {
        val (bang, image, dots, close, accept) = createRefs()
        Column(
            modifier = Modifier
                .width(64.dp)
                .height(6.dp)
                .background(color = White, shape = RoundedCornerShape(12.dp))
                .constrainAs(bang) {
                    fullHorizontalLinks()
                }
        ) { Unit }

        HorizontalPager(
            state = pagerState,
            count = allMemes.size
        ) {
            with(allMemes[it]) {
                ImageComponent(
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(image) {
                            fullBottomLinks()
                            top.linkTo(bang.bottom)
                        },
                    model = this
                )
            }
        }

        HorizontalPagerIndicator(
            modifier = Modifier
                .constrainAs(dots) {
                    fullBottomLinks()
                }
                .padding(bottom = 16.dp),
            pagerState = pagerState,
            activeColor = White,
            inactiveColor = DarkPurpleMain,
            indicatorWidth = curtainState.dotWidth.dp,
            spacing = curtainState.dotSpace.dp
        )

        curtainState.curtainCloseTitle?.let { title ->
            Box(
                modifier = Modifier
                    .constrainAs(close) { topLeftLinks() }
                    .clickable { closeCallback.invoke() }
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = title,
                    style = StyleBodyBold
                )
            }
        }

        curtainState.curtainAcceptTitle?.let { title ->
            Box(
                modifier = Modifier
                    .constrainAs(accept) { topRightLinks() }
                    .clickable { selectCallback.invoke(allMemes[pagerState.currentPage]) },
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = title,
                    style = StyleBodyBoldWhite
                )
            }
        }
    }
}