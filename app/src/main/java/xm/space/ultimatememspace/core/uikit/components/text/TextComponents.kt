package xm.space.ultimatememspace.core.uikit.components.text

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TitleItem(
    modifier: Modifier = Modifier.padding(start = 16.dp, end = 16.dp),
    value: String,
    textAlign: TextAlign? = null,
    style: TextStyle
) = Text(
    modifier = modifier,
    text = value,
    textAlign = textAlign,
    style = style
)