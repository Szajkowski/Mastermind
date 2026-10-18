package pl.pollub.android.mastermind

import androidx.compose.runtime.mutableLongStateOf
import androidx.lifecycle.ViewModel
import pl.pollub.android.mastermind.data.db.repository.PlayersRepository

class ProfileViewModel(
    private val playersRepository: PlayersRepository
) : ViewModel() {

    var playerId = mutableLongStateOf(0L)

    suspend fun login(name: String, email: String, imageUri: String?) {
        playerId.longValue = playersRepository.login(name, email, imageUri)
    }
}
