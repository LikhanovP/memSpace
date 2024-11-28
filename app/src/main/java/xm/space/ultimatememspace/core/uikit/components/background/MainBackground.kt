package xm.space.ultimatememspace.core.uikit.components.background

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.core.uikit.components.image.ImageComponent
import xm.space.ultimatememspace.ui.theme.Black

/**
 * App main background
 */
@Composable
fun MainBackground() {
    Box(modifier = Modifier.background(color = Black)) {
        ImageComponent(
            model = R.drawable.dark_background,
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop
        )
    }
}