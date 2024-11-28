package xm.space.ultimatememspace.core.uikit.components.toolbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import xm.space.ultimatememspace.core.models.toolbar.ToolbarState
import xm.space.ultimatememspace.core.uikit.components.image.ImageComponent
import xm.space.ultimatememspace.core.uikit.components.text.StyleLogoNew
import xm.space.ultimatememspace.core.uikit.fullSizeLinks
import xm.space.ultimatememspace.core.uikit.leftMiddleLinks
import xm.space.ultimatememspace.core.uikit.rightMiddleLinks

@Composable
fun RightNavigationToolbar(
    data: ToolbarState,
    leftIconCallback: (() -> Unit)? = null,
    rightIconCallback: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
    ) {
        val (leftNavIcon, rightNavIcon, title) = createRefs()
        data.leftIcon?.let { icon ->
            ImageComponent(
                modifier = Modifier
                    .width(22.dp)
                    .height(22.dp)
                    .constrainAs(leftNavIcon) { leftMiddleLinks() }
                    .clickable {
                        leftIconCallback?.invoke()
                    },
                model = icon,
                contentDescription = String()
            )
        }
        data.rightIcon?.let { icon ->
            ImageComponent(
                modifier = Modifier
                    .width(22.dp)
                    .height(22.dp)
                    .constrainAs(rightNavIcon) { rightMiddleLinks() }
                    .clickable {
                        rightIconCallback?.invoke()
                    },
                model = icon,
                contentDescription = String()
            )
        }
        Text(
            modifier = Modifier.constrainAs(title) { fullSizeLinks() },
            text = data.title,
            style = StyleLogoNew
        )
    }
}

