package xm.space.ultimatememspace.business.repositories.practicemanager

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import xm.space.ultimatememspace.business.domain.models.connecteduser.ConnectedUser
import xm.space.ultimatememspace.business.domain.models.connecteduser.UserPoints
import xm.space.ultimatememspace.business.domain.models.memes.MemeGameState
import xm.space.ultimatememspace.business.domain.models.memes.MemeOption
import xm.space.ultimatememspace.business.domain.models.memes.MemeRoundVotes
import xm.space.ultimatememspace.business.domain.models.memes.PlayerMemes
import xm.space.ultimatememspace.business.domain.models.memes.PlayerPickMeme
import xm.space.ultimatememspace.business.domain.models.results.PlayerResult
import xm.space.ultimatememspace.business.domain.models.situation.SituationOption
import xm.space.ultimatememspace.business.repositories.avatars.AvatarRepository
import xm.space.ultimatememspace.business.repositories.memes.MemesRepository
import xm.space.ultimatememspace.business.repositories.situations.SituationsRepository
import xm.space.ultimatememspace.presentation.models.result.ResultUi
import java.util.LinkedList
import java.util.Queue
import xm.space.ultimatememspace.business.repositories.getValue
import xm.space.ultimatememspace.business.repositories.setValue

class PracticeManagerRepositoryImpl(
    private val memesRepository: MemesRepository,
    private val situationsRepository: SituationsRepository,
    private val avatarRepository: AvatarRepository,
    private val sharedPreferences: SharedPreferences
) : PracticeManagerRepository {

    private val connectedUsers = MutableStateFlow<List<ConnectedUser>>(emptyList())

    private val usersPointsAtGame = MutableStateFlow<List<UserPoints>>(emptyList())

    private val memesAtGame = MutableStateFlow<List<MemeOption>>(emptyList())

    private val situationsAtGame = MutableStateFlow<List<SituationOption>>(emptyList())

    /*** Player who choose situation */
    private var currentActivePlayerAtRound: Int by sharedPreferences

    /*** Current voted of meme at round */
    private val userRoundChooses = MutableStateFlow<List<MemeGameState>>(emptyList())

    /*** Queue of players */
    private var queueUsers: Queue<ConnectedUser> = LinkedList()

    /*** Identifier round number */
    private var roundOrder = 0

    /*** Current list of votes by best meme */
    private val playersChooseBestMemes = MutableStateFlow<List<PlayerPickMeme>>(emptyList())

    /*** Number of rounds */
    private var roundCount = 3

    override suspend fun updateRoundCount(value: Int) {
        roundCount = value
    }

    override suspend fun observeConnectedUsers(): SharedFlow<List<ConnectedUser>> =
        connectedUsers.asSharedFlow()

    override suspend fun addNewUser(user: ConnectedUser) {
        val newData = connectedUsers.value.toMutableList()
        if (!newData.any { it.id == user.id }) newData.add(user)
        connectedUsers.value = newData

        val newDataPoints = usersPointsAtGame.value.toMutableList()
        if (!newDataPoints.any { it.userId == user.id })
            newDataPoints.add(UserPoints(userId = user.id))

        usersPointsAtGame.value = newDataPoints
    }

    override suspend fun initializeGameState(): List<PlayerMemes> {
        currentActivePlayerAtRound = 0
        memesAtGame.value = memesRepository.getMemes().shuffled()
        situationsAtGame.value = situationsRepository.getQuestions().shuffled()
        return connectedUsers.value.map { player ->
            PlayerMemes(
                playerId = player.id,
                playerMemes = memesAtGame.value.getStartPackMemes().joinToString(separator = "#")
            ).also {
                memesAtGame.value = memesAtGame.value.toMutableList().removeAlreadyUsedMemes()
            }
        }
    }

    override suspend fun setQueueUsers(): ConnectedUser? {
        connectedUsers.value.sortedBy { it.id }.let { sortedList ->
            queueUsers.addAll(sortedList)
        }.also {
            return queueUsers.poll()
        }
    }

    override suspend fun getSituationsAtGame(): List<SituationOption> =
        if (situationsAtGame.value.size > 10)
            situationsAtGame.value.shuffled().subList(0, 10)
        else situationsAtGame.value.shuffled()

    override suspend fun updateRoundOrder() {
        roundOrder += 1
    }

    override suspend fun getRoundOrder() = roundOrder

    override suspend fun addUserMemeChoose(userId: Int, memeId: Int): Boolean {
        val newData = userRoundChooses.value.toMutableList()
        newData.add(MemeGameState(userId = userId, userMemId = memeId))
        userRoundChooses.value = newData
        return newData.size == connectedUsers.value.size
    }

    override suspend fun allMemePicksDone() = userRoundChooses.value.size == connectedUsers.value.size

    override suspend fun getUsersMemeChoose() = userRoundChooses.value

    override suspend fun addPickPlayer(playerWhoPickId: Int, memeId: Int): Boolean {
        val newPicks = playersChooseBestMemes.value.toMutableList()
        newPicks.add(
            PlayerPickMeme(
                playerWhoPickId = playerWhoPickId,
                memePickId = memeId
            )
        )
        playersChooseBestMemes.value = newPicks
        return newPicks.size == connectedUsers.value.size
    }

    override suspend fun getMemeCountVotes() = getMemesVotes()

    override suspend fun getResultRound(): Pair<List<ResultUi>, List<ResultUi>> {
        val avatars = avatarRepository.getAvatars()
        val sortedResults = getMemesVotes().map { memInfo ->
            memInfo.toUi(
                memeResId = memesRepository.getMemes()
                    .first { it.memeId == memInfo.memeId }.memeResId,
                isWinner = getMemesVotes().maxOf { it.count } == memInfo.count,
                avatarResId = avatars.first { it.iconId == memInfo.playerIcon }.iconResId
            )
        }
        return sortedResults.filter { it.isWinner } to sortedResults.filter { !it.isWinner }
    }

    override suspend fun isTheEnd() = roundCount == 0

    override suspend fun calculateResult(): List<PlayerResult> {
        addResultsAtRound().run {
            val maxCountVote = usersPointsAtGame.value.maxOfOrNull { it.userPoints.sum() }
            return usersPointsAtGame.value.map { playerPoints ->
                val player = connectedUsers.value.first { it.id == playerPoints.userId }
                PlayerResult(
                    playerName = player.name,
                    playerAvatar = player.avatar,
                    playerWinsCount = playerPoints.userPoints.sum(),
                    playerIsWinner = playerPoints.userPoints.sum() == maxCountVote
                )
            }
        }
    }

    override suspend fun goNextRound(): ConnectedUser? {
        addResultsAtRound().run {
            situationsAtGame.value = situationsAtGame.value.toMutableList().apply {
                removeAt(
                    situationsAtGame.value.indexOfFirst {
                        it.id == situationsRepository.getSituationAtRound()?.id
                    }
                )
            }
            return getNextUser()
        }
    }

    override suspend fun getNewPlayersMemes(): List<PlayerMemes> {
        return connectedUsers.value.map { player ->
            PlayerMemes(
                playerId = player.id,
                playerMemes = memesAtGame.value.getNewMeme().memeId.toString()
            ).also {
                memesAtGame.value = memesAtGame.value.toMutableList().removeNewMeme()
            }
        }
    }

    override suspend fun recheckOnlineUsers(ids: List<Int>) {
        val disconnectedUsers = connectedUsers.value.filter { !ids.contains(it.id) }
        connectedUsers.value = connectedUsers.value.toMutableList().apply {
            removeAll(disconnectedUsers)
        }
        usersPointsAtGame.value = usersPointsAtGame.value.toMutableList().apply {
            removeAll { !ids.contains(it.userId) }
        }
        queueUsers.removeAll(disconnectedUsers.toSet())
    }

    override suspend fun saveActivePlayerAtRound(id: Int) {
        currentActivePlayerAtRound = id
    }

    override suspend fun getActivePlayerAtRound() = currentActivePlayerAtRound

    override suspend fun isActiveUserInParty(id: Int): Boolean {
        return connectedUsers.value.find { it.id == id } != null
    }

    override suspend fun getNextUserAfterRecheck(): ConnectedUser? {
        return if (queueUsers.isNotEmpty()) {
            queueUsers.poll()
        } else null
    }

    override suspend fun allBestMemePicksDone() = playersChooseBestMemes.value.size >= connectedUsers.value.size

    override suspend fun clearAllDataBeforeNewGame() {
        usersPointsAtGame.value = usersPointsAtGame.value.map { it.copy(userPoints = emptyList()) }
        roundOrder = 0
        roundCount = 3
    }

    override suspend fun isPlayersEmpty(): Boolean {
        return connectedUsers.value.size < 2
    }

    /*** Getting next player */
    private suspend fun getNextUser(): ConnectedUser? {
        return if (queueUsers.isNotEmpty()) {
            queueUsers.poll()
        } else {
            roundCount--
            setQueueUsers()
        }
    }

    private fun addResultsAtRound() {
        val memesAtRound = userRoundChooses.value.map { it.userMemId }
        val memesVotes = memesAtRound.map { memId ->
            MemeRoundVotes(
                memeId = memId,
                count = playersChooseBestMemes.value.count { playerPick ->
                    playerPick.memePickId == memId
                },
                playerName = String(),
                playerIcon = 0
            )
        }
        val maxVoteCount = memesVotes.maxOf { it.count }
        memesVotes.forEach {
            if (it.count == maxVoteCount) {
                giveWinnerPoints(it.memeId)
            } else {
                giveLoserZeroPoints(it.memeId)
            }
        }
        userRoundChooses.value = emptyList()
        playersChooseBestMemes.value = emptyList()
    }

    private fun getMemesVotes(): List<MemeRoundVotes> {
        val memesAtRound = userRoundChooses.value.map { it.userMemId }
        return memesAtRound.map { memId ->
            val player = connectedUsers.value.find {
                it.id == userRoundChooses.value.find { value ->
                    value.userMemId == memId
                }?.userId
            }
            MemeRoundVotes(
                memeId = memId,
                playerName = player?.name.orEmpty(),
                playerIcon = player?.avatar ?: 0,
                count = playersChooseBestMemes.value.count { playerPick ->
                    playerPick.memePickId == memId
                }
            )
        }
    }

    /**
     * Заполнить победителю баллы за раунд
     * @param memeId Идентификатор мема
     */
    private fun giveWinnerPoints(memeId: Int) {
        userRoundChooses.value.first { it.userMemId == memeId }.let { playerPick ->
            val newData = usersPointsAtGame.value.toMutableList()
            usersPointsAtGame.value = newData.map { playerPoints ->
                if (playerPick.userId == playerPoints.userId) {
                    val newScore = playerPoints.userPoints.toMutableList()
                    newScore.add(SCORE)
                    playerPoints.copy(userPoints = newScore)
                } else playerPoints
            }
        }
    }

    /**
     * Заполнить лузеру очки за раунд
     * @param memeId Идентификатор мема
     */
    private fun giveLoserZeroPoints(memeId: Int) {
        userRoundChooses.value.first { it.userMemId == memeId }.let { playerPick ->
            val newData = usersPointsAtGame.value.toMutableList()
            usersPointsAtGame.value = newData.map { playerPoints ->
                if (playerPick.userId == playerPoints.userId) {
                    val newScore = playerPoints.userPoints.toMutableList()
                    newScore.add(ZERO_SCORE)
                    playerPoints.copy(userPoints = newScore)
                } else playerPoints
            }
        }
    }

    companion object {

        private const val SCORE = 1

        private const val ZERO_SCORE = 0
    }
}