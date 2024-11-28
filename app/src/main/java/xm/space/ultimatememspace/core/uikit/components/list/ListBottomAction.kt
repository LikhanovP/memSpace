package xm.space.ultimatememspace.core.uikit.components.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayoutScope
import xm.space.ultimatememspace.core.uikit.components.button.OutlineMainButton
import xm.space.ultimatememspace.core.uikit.fullBottomLinks
import xm.space.ultimatememspace.ui.theme.First
import xm.space.ultimatememspace.ui.theme.Fives
import xm.space.ultimatememspace.ui.theme.Fourth
import xm.space.ultimatememspace.ui.theme.Second
import xm.space.ultimatememspace.ui.theme.Third

@Composable
fun ConstraintLayoutScope.ListBottomAction(
    proceedTitle: String,
    proceedCallback: () -> Unit,
    proceedRef: ConstrainedLayoutReference,
    isProceedEnable: Boolean = true
) {
    Box(
        modifier = Modifier.constrainAs(proceedRef) {
            fullBottomLinks()
        }.fillMaxWidth().height(80.dp).background(
            brush = Brush.verticalGradient(colors = listOf(First, Second, Third, Fourth, Fives))
        )
    ) {
        OutlineMainButton(
            modifier = Modifier.align(Alignment.Center),
            actionTitle = proceedTitle,
            callback = { proceedCallback.invoke() },
            isEnabled = isProceedEnable
        )
    }
}