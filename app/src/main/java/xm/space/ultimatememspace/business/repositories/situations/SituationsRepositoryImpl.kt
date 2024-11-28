package xm.space.ultimatememspace.business.repositories.situations

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import xm.space.ultimatememspace.business.domain.models.situation.SituationOption
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Implementations [SituationsRepository]
 */
class SituationsRepositoryImpl(private val context: Context) : SituationsRepository {

    /*** Situation id at round */
    private val situationId = MutableStateFlow<Int?>(null)

    override suspend fun getQuestions(): List<SituationOption> {
        return mutableListOf<SituationOption>().apply {
            val reader = BufferedReader(InputStreamReader(context.assets.open("situations.txt")))
            var line = reader.readLine()
            while (line != null) {
                val data = line.split("#")
                add(
                    SituationOption(
                        id = data.first().toInt(),
                        question = data.last().replace(";", "")
                    )
                )
                line = reader.readLine()
            }
        }
    }

    override suspend fun getSituationAtRound() = getQuestions().find { it.id == situationId.value }

    override suspend fun saveSituationAtRound(id: Int) {
        situationId.value = id
    }
}