package xm.space.ultimatememspace.presentation.screens.shop

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack
import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.core.uikit.components.spacers.SpacerItem
import xm.space.ultimatememspace.core.uikit.components.text.StyleBody
import xm.space.ultimatememspace.core.uikit.components.text.StyleTitle
import xm.space.ultimatememspace.core.uikit.components.text.TitleItem
import xm.space.ultimatememspace.core.uikit.fullBottomLinks
import xm.space.ultimatememspace.core.uikit.fullHorizontalLinks
import xm.space.ultimatememspace.core.uikit.fullSizeLinks
import xm.space.ultimatememspace.core.uikit.fullTopLinks
import xm.space.ultimatememspace.presentation.models.meme.MemesOptionUi
import xm.space.ultimatememspace.presentation.screens.shop.models.ShopState
import xm.space.ultimatememspace.ui.theme.Black
import xm.space.ultimatememspace.ui.theme.Error
import xm.space.ultimatememspace.ui.theme.Green
import xm.space.ultimatememspace.ui.theme.ShopFive
import xm.space.ultimatememspace.ui.theme.ShopFour
import xm.space.ultimatememspace.ui.theme.ShopOne
import xm.space.ultimatememspace.ui.theme.ShopSix
import xm.space.ultimatememspace.ui.theme.ShopThree
import xm.space.ultimatememspace.ui.theme.ShopTwo
import xm.space.ultimatememspace.ui.theme.White

@Composable
fun ShopContent(
    data: ShopState,
    allMemesAtPackCallback: (MemesPack) -> Unit
) = with(data) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .height(16.dp)
            .background(
                brush = Brush.verticalGradient(
                    listOf(ShopOne, ShopTwo, ShopThree, ShopFour, ShopFive, ShopSix)
                )
            )
    ) {
        val (button, list, bang) = createRefs()

        Spacer(
            modifier = Modifier
                .height(8.dp)
                .constrainAs(bang) {
                    fullTopLinks()
                }
        )

        Column(
            modifier = Modifier
                .width(64.dp)
                .height(6.dp)
                .background(color = White, shape = RoundedCornerShape(12.dp))
                .constrainAs(button) {
                    top.linkTo(bang.bottom)
                    fullHorizontalLinks()
                }
        ) { Unit }

        LazyColumn(
            modifier = Modifier
                .padding(top = 16.dp)
                .constrainAs(list) {
                    top.linkTo(button.bottom)
                    fullBottomLinks()
                }
        ) {
            item { SpacerItem(value = 12F) }
            item {
                TitleItem(
                    value = data.barTitle,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = StyleTitle
                )
            }
            item { SpacerItem(value = 16F) }
            items(
                items = memesOptions,
                itemContent = {
                    ShopItemCard(it, allMemesAtPackCallback)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            )
        }
    }
}

@Composable
fun ShopItemCard(
    data: MemesOptionUi,
    allMemesAtPackCallback: (MemesPack) -> Unit
) = with(data) {

    var visibleHavePack by remember { mutableStateOf(true) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
            .clickable { allMemesAtPackCallback.invoke(memesPack) }
    ) {
        val (mainCard, iconLable, bottomCard, subBottomCard) = createRefs()

        Card(
            modifier = Modifier
                .constrainAs(bottomCard) {
                    fullSizeLinks()
                    height = Dimension.fillToConstraints
                }
                .fillMaxSize()
                .height(16.dp)
                .padding(start = 32.dp, end = 32.dp, top = 32.dp, bottom = 8.dp)
                .alpha(0.5F),
            shape = RoundedCornerShape(
                topStart = 40.dp,
                topEnd = 40.dp,
                bottomStart = 50.dp,
                bottomEnd = 50.dp
            ),
        ) { Unit }
        Card(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(subBottomCard) {
                    fullSizeLinks()
                    height = Dimension.fillToConstraints
                }
                .height(8.dp)
                .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp)
                .alpha(0.8F),
            shape = RoundedCornerShape(
                topStart = 40.dp,
                topEnd = 40.dp,
                bottomStart = 50.dp,
                bottomEnd = 50.dp
            ),
        ) { Unit }
        Card(
            modifier = Modifier
                .constrainAs(mainCard) {
                    fullSizeLinks()
                }
                .animateContentSize()
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 24.dp),
            shape = RoundedCornerShape(40.dp),
        ) {
            Column(
                modifier = Modifier
                    .background(color = White)
                    .padding(start = 32.dp, top = 48.dp, end = 32.dp, bottom = 8.dp),
            ) {
                Column {
                    Text(
                        text = optionTitle,
                        fontSize = 20.sp,
                        color = Black,
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = optionDesc,
                        fontSize = 20.sp,
                        color = Black,
                        textAlign = TextAlign.Left
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                ConstraintLayout(
                    modifier = Modifier
                        .animateContentSize()
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                ) {
                    val (count, price) = createRefs()
                    Card(
                        modifier = Modifier
                            .constrainAs(count) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                            },
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomStart = 8.dp,
                            bottomEnd = 8.dp
                        ),
                        colors = CardDefaults.cardColors(containerColor = Green)
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            style = StyleBody,
                            text = freeAtPackCount
                        )
                    }
                    Card(
                        modifier = Modifier
                            .constrainAs(price) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            },
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomStart = 8.dp,
                            bottomEnd = 8.dp
                        ),
                        colors = CardDefaults.cardColors(containerColor = Error)
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            style = StyleBody,
                            text = paidAtPackCount
                        )
                    }
                }
            }
        }
        Card(
            modifier = Modifier
                .constrainAs(iconLable) {
                    fullHorizontalLinks()
                    top.linkTo(parent.top)
                }
                .width(64.dp)
                .height(64.dp),
            shape = CircleShape,
        ) {
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .align(Alignment.CenterHorizontally),
                shape = CircleShape
            ) {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Black)
                ) {
                    val icon = createRef()
                    Image(
                        modifier = Modifier.constrainAs(icon) {
                            fullSizeLinks()
                        },
                        painter = painterResource(id = optionIcon),
                        contentDescription = String.empty()
                    )
                }
            }
        }
    }
}