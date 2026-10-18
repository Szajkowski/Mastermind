package pl.pollub.android.mastermind.data.db.repository

import kotlinx.coroutines.flow.Flow
import pl.pollub.android.mastermind.data.db.entity.PlayerScore

interface ScoresRepository {
    suspend fun addScore(playerId: Long, attempts: Int, colors: Int)
    fun getScores(): Flow<List<PlayerScore>>
}