package xm.space.ultimatememspace.core.uikit.components.loaders

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.airbnb.lottie.LottieAnimationView

/**
 * Component of waiting something
 * @param waitTitle Title of waiting
 * @param animType Animation source
 */
@Composable
fun CircularWaitProgress(waitTitle: String, animType: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            factory = { context ->
                LottieAnimationView(context).apply {
                    setAnimation(animType)
                    playAnimation()
                    repeatCount = Int.MAX_VALUE
                }
            }
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = 16.dp, start = 32.dp, end = 32.dp, bottom = 32.dp),
            text = waitTitle,
            fontSize = 14.sp,
            color = Gray,
            textAlign = TextAlign.Center
        )
    }
}