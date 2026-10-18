package pl.pollub.android.mastermind

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import pl.pollub.android.mastermind.data.db.repository.ScoresRepository

class ResultsViewModel(
    private val scoresRepository: ScoresRepository
) : ViewModel() {

    val scores = scoresRepository
        .getScores()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
}
