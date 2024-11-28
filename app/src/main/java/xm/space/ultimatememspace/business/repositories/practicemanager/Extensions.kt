package xm.space.ultimatememspace.business.repositories.practicemanager

import xm.space.ultimatememspace.business.domain.models.memes.MemeOption

/**
 * Returns list of six memes
 */
fun List<MemeOption>.getStartPackMemes() = slice(0..5).map { it.memeId }

fun List<MemeOption>.getNewMeme() = last()

fun MutableList<MemeOption>.removeNewMeme() = this.apply {
    removeAt(this.lastIndex)
}

/**
 * Удаляет первые 6 мемов из списка
 */
fun MutableList<MemeOption>.removeAlreadyUsedMemes() = this.apply {
    removeAt(5)
    removeAt(4)
    removeAt(3)
    removeAt(2)
    removeAt(1)
    removeAt(0)
}