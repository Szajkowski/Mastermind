package pl.pollub.android.mastermind.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.pollub.android.mastermind.data.db.entity.PlayerScore
import pl.pollub.android.mastermind.data.db.entity.Score

@Dao
interface ScoreDao {

    @Insert
    suspend fun insert(score: Score)

    @Query("""
        SELECT players.name AS name, scores.attempts AS attempts, scores.colors AS colors
        FROM scores
        INNER JOIN players ON players.playerId = scores.playerId
        ORDER BY colors DESC, attempts ASC
    """)
    fun getScores(): Flow<List<PlayerScore>>
}
