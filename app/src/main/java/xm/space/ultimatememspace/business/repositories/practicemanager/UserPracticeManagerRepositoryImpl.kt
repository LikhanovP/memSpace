package xm.space.ultimatememspace.business.repositories.practicemanager

import kotlinx.coroutines.flow.MutableStateFlow
import xm.space.ultimatememspace.business.domain.models.memes.MemeRoundVotes
import xm.space.ultimatememspace.business.domain.models.memevote.MemeVote
import xm.space.ultimatememspace.business.domain.models.results.PlayerResult
import xm.space.ultimatememspace.business.domain.models.situation.SituationOption
import xm.space.ultimatememspace.business.repositories.avatars.AvatarRepository
import xm.space.ultimatememspace.business.repositories.memes.MemesRepository
import xm.space.ultimatememspace.presentation.models.result.ResultUi

/**
 * Implementation [UserPracticeManagerRepository]
 */
class UserPracticeManagerRepositoryImpl(
    private val memesRepository: MemesRepository,
    private val practiceManagerRepository: PracticeManagerRepository,
    private val avatarRepository: AvatarRepository
) : UserPracticeManagerRepository {

    /**
     * Current list of memes
     */
    private val memesPack = MutableStateFlow<List<Int>>(emptyList())

    private val situationsPack = MutableStateFlow<List<SituationOption>>(emptyList())

    private val memesForBestMemChoosing = MutableStateFlow<List<MemeVote>>(emptyList())

    private val roundOrder = MutableStateFlow(0)

    private val roundResults = MutableStateFlow<List<MemeRoundVotes>>(emptyList())

    private val gameResults = MutableStateFlow<List<PlayerResult>>(emptyList())

    override suspend fun saveMemesStartPack(memes: String) {
        memesPack.value = memes.split("#").map { it.toInt() }
    }

    override suspend fun saveSituationsAtRound(situations: List<SituationOption>) {
        situationsPack.value = situations
    }

    override suspend fun getSituationsAtRound() = situationsPack.value

    override suspend fun getMemesPack() = memesRepository.getMemes().filter {
        memesPack.value.contains(it.memeId)
    }.map { it.toUi() }

    override suspend fun saveMemesForBest(memes: List<MemeVote>, roundNumber: Int) {
        memesForBestMemChoosing.value = memes
        roundOrder.value = roundNumber
    }

    override suspend fun getRoundNumber() = roundOrder.value

    override suspend fun getMemesForBest(): List<MemeVote> {
        return memesForBestMemChoosing.value.ifEmpty {
            practiceManagerRepository.getUsersMemeChoose().map { it.toMemeVote() }
        }
    }

    override suspend fun saveResultRound(votes: List<MemeRoundVotes>) {
        roundResults.value = votes
    }

    override suspend fun getResultRound(): Pair<List<ResultUi>, List<ResultUi>> {
        val avatars = avatarRepository.getAvatars()
        val sortedResults = roundResults.value.map { memInfo ->
            memInfo.toUi(
                memeResId = memesRepository.getMemes()
                    .first { it.memeId == memInfo.memeId }.memeResId,
                isWinner = roundResults.value.maxOf { it.count } == memInfo.count,
                avatarResId = avatars.first { it.iconId == memInfo.playerIcon }.iconResId
            )
        }
        return sortedResults.filter { it.isWinner } to sortedResults.filter { !it.isWinner }
    }

    override suspend fun addNewMemeToPack(memes: String) {
        val newMemesPack = memesPack.value.toMutableList()
        newMemesPack.add(memes.toInt())
        memesPack.value = newMemesPack
    }

    override suspend fun saveGameResult(playersResults: List<PlayerResult>) {
        gameResults.value = playersResults
    }

    override suspend fun getGameResults(): Pair<List<PlayerResult>, List<PlayerResult>> {
        val avatars = avatarRepository.getAvatars()
        val sortedResults = gameResults.value.map {
            it.copy(playerAvatar = avatars.first { avatar -> avatar.iconId == it.playerAvatar }.iconResId)
        }
        return sortedResults.filter { it.playerIsWinner } to sortedResults.filter { !it.playerIsWinner }
    }

    override suspend fun removeMeme(id: Int) {
        val newMemesPack = memesPack.value.toMutableList()
        newMemesPack.removeAll { it == id }
        memesPack.value = newMemesPack
    }
}