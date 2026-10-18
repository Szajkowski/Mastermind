package pl.pollub.android.mastermind.data.db.repository

import kotlinx.coroutines.flow.Flow
import pl.pollub.android.mastermind.data.db.dao.ScoreDao
import pl.pollub.android.mastermind.data.db.entity.PlayerScore
import pl.pollub.android.mastermind.data.db.entity.Score

class ScoresRepositoryImpl(
    private val dao: ScoreDao
) : ScoresRepository {

    override suspend fun addScore(playerId: Long, attempts: Int, colors: Int) {
        dao.insert(
            Score(
                playerId = playerId,
                attempts = attempts,
                colors = colors
            )
        )
    }

    override fun getScores(): Flow<List<PlayerScore>> = dao.getScores()
}
