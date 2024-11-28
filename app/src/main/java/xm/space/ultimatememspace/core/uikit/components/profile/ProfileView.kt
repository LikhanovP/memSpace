package xm.space.ultimatememspace.core.uikit.components.profile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.core.uikit.components.image.ImageComponent
import xm.space.ultimatememspace.core.uikit.components.text.StyleBody
import xm.space.ultimatememspace.core.uikit.components.text.StyleBodyHint
import xm.space.ultimatememspace.core.uikit.components.text.StyleBodyLarge
import xm.space.ultimatememspace.core.uikit.components.text.StyleBtnDisable
import xm.space.ultimatememspace.core.uikit.components.text.StyleSubtitleRegular
import xm.space.ultimatememspace.core.uikit.leftCenterLinks
import xm.space.ultimatememspace.presentation.models.profile.ProfileCardUi

@Composable
fun ProfileView(
    modifier: Modifier,
    profile: ProfileCardUi
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp, start = 4.dp)
        ) {
            val (icon, text) = createRefs()
            ImageComponent(
                modifier = Modifier
                    .width(52.dp)
                    .height(52.dp)
                    .padding(end = 12.dp)
                    .constrainAs(icon) { leftCenterLinks() },
                model = profile.profileAvatar
            )
            Text(
                modifier = Modifier.constrainAs(text) {
                    start.linkTo(icon.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                text = profile.playerName,
                style = StyleBodyHint
            )
        }
    }
}