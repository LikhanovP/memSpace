package xm.space.ultimatememspace.business.domain.models.network

import xm.space.ultimatememspace.business.domain.models.connecteduser.ConnectedUser
import xm.space.ultimatememspace.business.domain.models.memes.MemeRoundVotes
import xm.space.ultimatememspace.business.domain.models.memes.PlayerMemes
import xm.space.ultimatememspace.business.domain.models.memevote.MemeVote
import xm.space.ultimatememspace.business.domain.models.results.PlayerResult
import xm.space.ultimatememspace.business.domain.models.situation.SituationOption

sealed class NetworkEvents {

    data class ConnProfile(
        val id: Int,
        val name: String,
        val avatar: Int
    ) : NetworkEvents()

    data class LineUp(
        val connUsers: List<ConnectedUser>
    ) : NetworkEvents()

    data class StartGame(
        val mainUserId: Int,
        val mainUserName: String,
        val questions: List<SituationOption>
    ) : NetworkEvents()

    data class QuestionWasChoose(
        val id: Int
    ) : NetworkEvents()

    data class MemWasChoose(
        val userId: Int,
        val memId: Int
    ) : NetworkEvents()

    data class VoteBestMem(
        val memes: List<MemeVote>,
        val roundOrder: Int,
        val situationId: Int
    ) : NetworkEvents()

    /**
     * Событие сделанного выбора лучше мема пользователем
     * @param playerWhoChooseId Игрок выбравший лучший мем
     * @param memeId Идентификатор лучшего мема
     */
    data class BestMemChoosing(
        val playerWhoChooseId: Int,
        val memeId: Int
    ) : NetworkEvents()

    data object CalculateResult : NetworkEvents()

    data class ResultVotesMeme(
        val votes: List<MemeRoundVotes>,
    ) : NetworkEvents()

    data class ViewGameResult(
        val playersResults: List<PlayerResult>
    ) : NetworkEvents()

    data class PlayersMemes(
        val playersMemes: List<PlayerMemes>
    ) : NetworkEvents()

    data class PlayersNewMemes(
        val playersMemes: List<PlayerMemes>
    ) : NetworkEvents()

    data object PollingUsers : NetworkEvents()

    data class RemindUser(val id: Int) : NetworkEvents()

    data object ContinueGame : NetworkEvents()

    data object Unknown : NetworkEvents()
}