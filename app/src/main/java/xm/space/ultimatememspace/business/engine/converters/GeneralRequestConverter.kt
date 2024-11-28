package xm.space.ultimatememspace.business.engine.converters

import com.google.gson.GsonBuilder
import org.json.JSONObject
import xm.space.ultimatememspace.business.domain.models.results.PlayerResult
import xm.space.ultimatememspace.business.domain.models.connecteduser.ConnectedUser
import xm.space.ultimatememspace.business.domain.models.memes.MemeRoundVotes
import xm.space.ultimatememspace.business.domain.models.memes.PlayerMemes
import xm.space.ultimatememspace.business.domain.models.memevote.MemeVote
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents
import xm.space.ultimatememspace.business.domain.models.situation.SituationOption
import xm.space.ultimatememspace.business.engine.callbacks.ALL_MEMS_FOR_CHOOSE_BEST
import xm.space.ultimatememspace.business.engine.callbacks.BEST_MEM_CHOOSE_APPROVE
import xm.space.ultimatememspace.business.engine.callbacks.BROADCAST_LINE_UP
import xm.space.ultimatememspace.business.engine.callbacks.BROADCAST_START_GAME
import xm.space.ultimatememspace.business.engine.callbacks.CALCULATE_RESULT
import xm.space.ultimatememspace.business.engine.callbacks.CONN_PROFILE_DATA
import xm.space.ultimatememspace.business.engine.callbacks.CONTINUE_GAME
import xm.space.ultimatememspace.business.engine.callbacks.EXPRESS_YOURSELF
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_AVATAR
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_CURRENT_USERS
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_ID
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_MEMES_COUNTS
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_MEM_ID
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_NAME
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_PLAYER_OWNER_CHOICE
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_QUESTIONS_LIST
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_REQUEST_KEY
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_ROUND_ORDER
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_SITUATION_ID
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_USERS_MEMS
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_USER_WHO_ASK
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_USES_MEM_ID
import xm.space.ultimatememspace.business.engine.callbacks.FIELD_VIEW_RESULTS
import xm.space.ultimatememspace.business.engine.callbacks.FILED_PLAYERS_MEMES
import xm.space.ultimatememspace.business.engine.callbacks.MEME_COUNTS
import xm.space.ultimatememspace.business.engine.callbacks.MEM_WAS_CHOOSE
import xm.space.ultimatememspace.business.engine.callbacks.PLAYERS_MEMES
import xm.space.ultimatememspace.business.engine.callbacks.PLAYERS_NEW_MEMES
import xm.space.ultimatememspace.business.engine.callbacks.POLLING_USERS
import xm.space.ultimatememspace.business.engine.callbacks.QUESTION_CHOOSE
import xm.space.ultimatememspace.business.engine.callbacks.VIEW_RESULT
import java.nio.ByteBuffer
import java.nio.charset.Charset

fun convertToSuitableModel(message: ByteBuffer): NetworkEvents {
    val buffer = ByteArray(message.remaining())
    message.get(buffer)
    val source = JSONObject(String(buffer, Charset.defaultCharset()))
    return when (source.get(FIELD_REQUEST_KEY)) {
        EXPRESS_YOURSELF -> NetworkEvents.RemindUser(id = source.getInt(FIELD_ID))
        CONN_PROFILE_DATA -> NetworkEvents.ConnProfile(
            id = source.getInt(FIELD_ID),
            name = source.getString(FIELD_NAME),
            avatar = source.getInt(FIELD_AVATAR)
        )
        BROADCAST_LINE_UP -> {
            val gson = GsonBuilder().create()
            NetworkEvents.LineUp(
                connUsers = gson.fromJson(
                    source.get(FIELD_CURRENT_USERS).toString(),
                    Array<ConnectedUser>::class.java
                ).toList()
            )
        }
        BROADCAST_START_GAME -> {
            val gson = GsonBuilder().create()
            NetworkEvents.StartGame(
                mainUserId = source.getInt(FIELD_USER_WHO_ASK),
                mainUserName = source.getString(FIELD_NAME),
                questions = gson.fromJson(
                    source.get(FIELD_QUESTIONS_LIST).toString(),
                    Array<SituationOption>::class.java
                ).toList()
            )
        }
        QUESTION_CHOOSE -> {
            NetworkEvents.QuestionWasChoose(
                id = source.getInt(FIELD_ID)
            )
        }
        MEM_WAS_CHOOSE -> {
            NetworkEvents.MemWasChoose(
                userId = source.getInt(FIELD_ID),
                memId = source.getInt(FIELD_USES_MEM_ID)
            )
        }
        ALL_MEMS_FOR_CHOOSE_BEST -> {
            val gson = GsonBuilder().create()
            NetworkEvents.VoteBestMem(
                memes = gson.fromJson(
                    source.get(FIELD_USERS_MEMS).toString(),
                    Array<MemeVote>::class.java
                ).toList(),
                roundOrder = source.getInt(FIELD_ROUND_ORDER),
                situationId = source.getInt(FIELD_SITUATION_ID)
            )
        }
        BEST_MEM_CHOOSE_APPROVE -> {
            NetworkEvents.BestMemChoosing(
                playerWhoChooseId = source.getInt(FIELD_PLAYER_OWNER_CHOICE),
                memeId = source.getInt(FIELD_MEM_ID)
            )
        }
        CALCULATE_RESULT -> NetworkEvents.CalculateResult
        MEME_COUNTS -> {
            val gson = GsonBuilder().create()
            NetworkEvents.ResultVotesMeme(
                votes = gson.fromJson(
                    source.get(FIELD_MEMES_COUNTS).toString(),
                    Array<MemeRoundVotes>::class.java
                ).toList()
            )
        }
        VIEW_RESULT -> {
            val gson = GsonBuilder().create()
            NetworkEvents.ViewGameResult(
                playersResults = gson.fromJson(
                    source.get(FIELD_VIEW_RESULTS).toString(),
                    Array<PlayerResult>::class.java
                ).toList()
            )
        }
        PLAYERS_MEMES -> {
            val gson = GsonBuilder().create()
            NetworkEvents.PlayersMemes(
                playersMemes = gson.fromJson(
                    source.get(FILED_PLAYERS_MEMES).toString(),
                    Array<PlayerMemes>::class.java
                ).toList()
            )
        }
        PLAYERS_NEW_MEMES -> {
            val gson = GsonBuilder().create()
            NetworkEvents.PlayersNewMemes(
                playersMemes = gson.fromJson(
                    source.get(FILED_PLAYERS_MEMES).toString(),
                    Array<PlayerMemes>::class.java
                ).toList()
            )
        }
        POLLING_USERS -> NetworkEvents.PollingUsers
        CONTINUE_GAME -> NetworkEvents.ContinueGame
        else -> NetworkEvents.Unknown
    }
}