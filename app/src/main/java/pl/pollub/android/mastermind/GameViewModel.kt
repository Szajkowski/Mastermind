package pl.pollub.android.mastermind

import androidx.lifecycle.ViewModel
import pl.pollub.android.mastermind.data.db.repository.ScoresRepository

class GameViewModel(
    private val scoresRepository: ScoresRepository
) : ViewModel() {

    suspend fun saveScore(playerId: Long, attempts: Int, colors: Int) {
        scoresRepository.addScore(playerId, attempts, colors)
    }
}
