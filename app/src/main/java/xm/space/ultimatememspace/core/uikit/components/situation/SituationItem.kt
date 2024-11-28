package xm.space.ultimatememspace.core.uikit.components.situation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xm.space.ultimatememspace.ui.theme.Black
import xm.space.ultimatememspace.ui.theme.White

@Composable
fun SituationItem(description: String) = Card(
    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
    elevation = 4.dp,
    shape = RoundedCornerShape(20.dp),
    backgroundColor = White
) {
    Text(
        modifier = Modifier.padding(24.dp),
        text = description,
        textAlign = TextAlign.Left,
        fontSize = 20.sp,
        color = Black,
        fontStyle = FontStyle.Normal
    )
}