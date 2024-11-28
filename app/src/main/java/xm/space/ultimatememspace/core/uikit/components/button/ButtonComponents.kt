package xm.space.ultimatememspace.core.uikit.components.button

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.core.uikit.components.image.ImageComponent
import xm.space.ultimatememspace.core.uikit.components.text.StyleBtnActive
import xm.space.ultimatememspace.core.uikit.components.text.StyleBtnDisable
import xm.space.ultimatememspace.core.uikit.components.text.StyleSubtitle
import xm.space.ultimatememspace.core.uikit.fullSizeLinks
import xm.space.ultimatememspace.ui.theme.DisableMain
import xm.space.ultimatememspace.ui.theme.MentholMain
import xm.space.ultimatememspace.ui.theme.SameGray

@Composable
fun OutlineMainButton(
    modifier: Modifier = Modifier,
    actionTitle: String,
    callback: (() -> Unit)?,
    isEnabled: Boolean = true
) {
    OutlinedButton(
        modifier = modifier,
        onClick = { callback?.invoke() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MentholMain,
            disabledBackgroundColor = DisableMain
        ),
        shape = RoundedCornerShape(35),
        border = null,
        enabled = isEnabled,
        elevation = ButtonDefaults.elevation(
            defaultElevation = 1.dp,
            pressedElevation = 4.dp,
            disabledElevation = 1.dp
        )
    ) {
        Text(
            text = actionTitle,
            modifier = Modifier.padding(
                start = 55.dp,
                end = 55.dp,
                top = 8.dp,
                bottom = 8.dp
            ),
            style = if (isEnabled) StyleBtnActive else StyleBtnDisable
        )
    }
}

@Composable
fun RoleButton(
    modifier: Modifier = Modifier,
    icon: Int,
    description: String,
    callback: (() -> Unit)
) {
    OutlinedButton(
        shape = RoundedCornerShape(20),
        colors = ButtonDefaults.textButtonColors(backgroundColor = SameGray),
        modifier = modifier,
        onClick = { callback.invoke() }
    ) {
        ConstraintLayout {
            val (role) = createRefs()
            Column(modifier = Modifier.constrainAs(role) { fullSizeLinks() }) {
                ImageComponent(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    model = icon,
                    contentDescription = String.empty()
                )
                Spacer(modifier = Modifier.height(16.dp))
                androidx.compose.material.Text(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    text = description,
                    style = StyleSubtitle,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}