package xm.space.ultimatememspace.core.uikit.components.gridavatars

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.Icon
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.core.uikit.components.image.ImageComponent
import xm.space.ultimatememspace.presentation.models.avatar.AvatarUi
import xm.space.ultimatememspace.ui.theme.TransPurpleMain
import xm.space.ultimatememspace.ui.theme.White

/**
 * Компонент горизонтального списка аватаров
 * @param modifier Опции отображения
 * @param playerAvatars Список аватаров
 * @param avatarSelectCallback Коллбэк выбора аватара
 */
@Composable
fun GridAvatarsComponent(
    modifier: Modifier = Modifier,
    playerAvatars: List<AvatarUi>,
    avatarSelectCallback: (Int) -> Unit,
    focusManager: FocusManager
) = LazyVerticalGrid(
    modifier = modifier,
    columns = GridCells.Fixed(5),
    content = {
        items(playerAvatars.size) { index ->
            with(playerAvatars[index]) {
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            focusManager.clearFocus()
                            avatarSelectCallback.invoke(id)
                        }
                ) {
                    ImageComponent(
                        modifier = Modifier
                            .width(48.dp)
                            .height(48.dp),
                        model = resId
                    )
                    if (isSelect) {
                        Box(
                            modifier = Modifier
                                .width(48.dp)
                                .height(48.dp)
                                .background(color = TransPurpleMain, shape = CircleShape)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(24.dp)
                                    .align(alignment = Alignment.BottomCenter),
                                painter = painterResource(id = R.drawable.ic_check),
                                contentDescription = String(),
                                tint = White
                            )
                        }
                    }
                }
            }
        }
    }
)