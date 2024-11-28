package xm.space.ultimatememspace.core.uikit

import androidx.constraintlayout.compose.ConstrainScope

fun ConstrainScope.fullTopLinks() {
    top.linkTo(parent.top)
    fullHorizontalLinks()
}

fun ConstrainScope.fullHorizontalLinks() {
    start.linkTo(parent.start)
    end.linkTo(parent.end)
}

fun ConstrainScope.rightMiddleLinks() {
    top.linkTo(parent.top)
    end.linkTo(parent.end)
    bottom.linkTo(parent.bottom)
}

fun ConstrainScope.leftMiddleLinks() {
    top.linkTo(parent.top)
    start.linkTo(parent.start)
    bottom.linkTo(parent.bottom)
}

fun ConstrainScope.fullSizeLinks() {
    top.linkTo(parent.top)
    bottom.linkTo(parent.bottom)
    start.linkTo(parent.start)
    end.linkTo(parent.end)
}

fun ConstrainScope.fullBottomLinks() {
    bottom.linkTo(parent.bottom)
    start.linkTo(parent.start)
    end.linkTo(parent.end)
}

fun ConstrainScope.topLeftLinks() {
    start.linkTo(parent.start)
    top.linkTo(parent.top)
}

fun ConstrainScope.topRightLinks() {
    top.linkTo(parent.top)
    end.linkTo(parent.end)
}

fun ConstrainScope.justCenterLinks() {
    top.linkTo(parent.top)
    bottom.linkTo(parent.bottom)
}

fun ConstrainScope.leftCenterLinks() {
    start.linkTo(parent.start)
    top.linkTo(parent.top)
    bottom.linkTo(parent.bottom)
}

fun ConstrainScope.verticalFullSizeLinks() {
    top.linkTo(parent.top)
    bottom.linkTo(parent.bottom)
}