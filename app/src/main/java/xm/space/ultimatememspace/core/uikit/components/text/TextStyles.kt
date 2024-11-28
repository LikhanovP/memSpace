package xm.space.ultimatememspace.core.uikit.components.text

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.ui.theme.ClosePlace
import xm.space.ultimatememspace.ui.theme.DarkPurpleMain
import xm.space.ultimatememspace.ui.theme.DisPlace
import xm.space.ultimatememspace.ui.theme.DisableMain
import xm.space.ultimatememspace.ui.theme.Hinting
import xm.space.ultimatememspace.ui.theme.MentholMain
import xm.space.ultimatememspace.ui.theme.White

val StyleBtnActive = TextStyle(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight(700),
    fontSize = 16.sp,
    lineHeight = 24.sp,
    color = ClosePlace,
    fontFamily = FontFamily.SansSerif
)

val StyleBtnDisable = TextStyle(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight(700),
    fontSize = 16.sp,
    lineHeight = 24.sp,
    color = DisPlace,
    fontFamily = FontFamily.SansSerif
)

val StyleTitle = TextStyle(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight(400),
    fontSize = 24.sp,
    lineHeight = 36.sp,
    color = White,
    fontFamily = FontFamily.SansSerif
)

val StyleLogoNew = TextStyle(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight(500),
    fontSize = 24.sp,
    lineHeight = 30.sp,
    color = White,
    fontFamily = FontFamily(Font(R.font.orbitron))
)

val StyleBodyDark = TextStyle(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight(400),
    fontSize = 16.sp,
    lineHeight = 24.sp,
    color = ClosePlace,
    fontFamily = FontFamily.SansSerif
)

val StyleSubtitle = TextStyle(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight(500),
    fontSize = 20.sp,
    lineHeight = 32.sp,
    color = White,
    fontFamily = FontFamily.SansSerif
)

val StyleBodyBold = TextStyle(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 24.sp,
    color = DisableMain,
    fontFamily = FontFamily.SansSerif
)

val StyleBodyBoldWhite = TextStyle(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight.Bold,
    fontSize = 18.sp,
    lineHeight = 24.sp,
    color = MentholMain,
    fontFamily = FontFamily.SansSerif
)

val StyleBodyHint = TextStyle(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight(400),
    fontSize = 16.sp,
    lineHeight = 24.sp,
    color = Hinting,
    fontFamily = FontFamily.SansSerif
)

val StyleBody = TextStyle(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight(400),
    fontSize = 16.sp,
    lineHeight = 24.sp,
    color = White,
    fontFamily = FontFamily.SansSerif
)

val StyleBodyLarge = TextStyle(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight(600),
    fontSize = 18.sp,
    lineHeight = 24.sp,
    color = White,
    fontFamily = FontFamily.SansSerif
)

val StyleSubhead = TextStyle(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight(400),
    fontSize = 14.sp,
    lineHeight = 24.sp,
    color = White,
    fontFamily = FontFamily.SansSerif
)

val StyleSubtitleRegular = TextStyle(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight(400),
    fontSize = 20.sp,
    lineHeight = 32.sp,
    color = ClosePlace,
    fontFamily = FontFamily.SansSerif
)