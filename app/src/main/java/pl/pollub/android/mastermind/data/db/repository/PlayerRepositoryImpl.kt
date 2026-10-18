package pl.pollub.android.mastermind.data.db.repository

import pl.pollub.android.mastermind.data.db.dao.PlayerDao
import pl.pollub.android.mastermind.data.db.entity.Player

class PlayersRepositoryImpl(
    private val dao: PlayerDao
) : PlayersRepository {

    override suspend fun login(name: String, email: String, imageUri: String?): Long {
        val existing = dao.getByEmail(email)
        return if (existing != null) {
            if (existing.name != name) {
                dao.update(existing.copy(name = name))
            }
            existing.playerId
        } else {
            dao.insert(Player(name = name, email = email, imageUri = imageUri))
        }
    }
}
