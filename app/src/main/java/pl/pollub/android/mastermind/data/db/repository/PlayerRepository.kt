package pl.pollub.android.mastermind.data.db.repository

interface PlayersRepository {
    suspend fun login(name: String, email: String, imageUri: String?): Long
}
