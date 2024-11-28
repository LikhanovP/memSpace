@file:OptIn(ExperimentalGlideComposeApi::class)

package xm.space.ultimatememspace.core.uikit.components.image

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun ImageComponent(
    modifier: Modifier,
    model: Int? = null,
    modelBitmap: Bitmap? = null,
    contentDescription: String = "",
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit
) = GlideImage(
    modifier = modifier,
    model = model ?: modelBitmap,
    contentDescription = contentDescription,
    alignment = alignment,
    contentScale = contentScale
)