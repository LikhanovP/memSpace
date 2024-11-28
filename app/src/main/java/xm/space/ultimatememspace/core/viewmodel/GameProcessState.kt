package xm.space.ultimatememspace.core.viewmodel

import xm.space.ultimatememspace.navigation.CHOOSE_BEST_MEME
import xm.space.ultimatememspace.navigation.CHOOSE_MEME
import xm.space.ultimatememspace.navigation.CHOOSE_SITUATION
import xm.space.ultimatememspace.navigation.CREATING_GROUP
import xm.space.ultimatememspace.navigation.GAME_RESULT
import xm.space.ultimatememspace.navigation.ROUND_RESULT
import xm.space.ultimatememspace.navigation.WAIT_BEST_MEME
import xm.space.ultimatememspace.navigation.WAIT_MEME
import xm.space.ultimatememspace.navigation.WAIT_SITUATION

sealed class GameProcessState {

    abstract fun getKey(): String

    data object Room : GameProcessState() {
        override fun getKey() = CREATING_GROUP
    }

    data class ChooseSituation(val activePlayerId: Int) : GameProcessState() {
        override fun getKey() = CHOOSE_SITUATION
    }

    data class WaitChooseSituation(val activePlayerId: Int) : GameProcessState() {
        override fun getKey() = WAIT_SITUATION
    }

    data object ChooseMeme : GameProcessState() {
        override fun getKey() = CHOOSE_MEME
    }

    data object WaitChooseMeme : GameProcessState() {
        override fun getKey() = WAIT_MEME
    }

    data object ChooseBestMeme : GameProcessState() {
        override fun getKey() = CHOOSE_BEST_MEME
    }

    data object WaitChooseBestMeme : GameProcessState() {
        override fun getKey() = WAIT_BEST_MEME
    }

    data object ViewResults : GameProcessState() {
        override fun getKey() = ROUND_RESULT
    }

    data object GameViewResults : GameProcessState() {
        override fun getKey() = GAME_RESULT
    }

    data object Empty : GameProcessState() {
        override fun getKey() = EMPTY
    }

    companion object {
        private const val EMPTY = "EMPTY"
    }
}