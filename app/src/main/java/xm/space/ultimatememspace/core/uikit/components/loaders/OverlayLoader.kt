package xm.space.ultimatememspace.core.uikit.components.loaders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import xm.space.ultimatememspace.core.uikit.components.text.StyleBodyLarge
import xm.space.ultimatememspace.core.uikit.fullHorizontalLinks
import xm.space.ultimatememspace.core.uikit.fullSizeLinks

@Composable
fun OverlayLoader(overlayTitle: String) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray.copy(alpha = 0.5F))
    ) {
        val (loader, title) = createRefs()

        CircularProgressIndicator(
            modifier = Modifier.constrainAs(loader) {
                fullSizeLinks()
            }
        )

        Text(
            modifier = Modifier.constrainAs(title) {
                fullHorizontalLinks()
                top.linkTo(loader.bottom)
            }.padding(top = 32.dp),
            text = overlayTitle,
            style = StyleBodyLarge,
            textAlign = TextAlign.Center
        )
    }
}