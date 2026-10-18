package pl.pollub.android.mastermind.data.db

import android.content.Context
import pl.pollub.android.mastermind.MasterAndDatabase
import pl.pollub.android.mastermind.data.db.repository.PlayersRepository
import pl.pollub.android.mastermind.data.db.repository.PlayersRepositoryImpl
import pl.pollub.android.mastermind.data.db.repository.ScoresRepository
import pl.pollub.android.mastermind.data.db.repository.ScoresRepositoryImpl

interface AppContainer {
    val playersRepository: PlayersRepository
    val scoresRepository: ScoresRepository
}

class AppDataContainer(context: Context) : AppContainer {
    private val db = MasterAndDatabase.getDatabase(context)

    override val playersRepository by lazy {
        PlayersRepositoryImpl(db.playerDao())
    }

    override val scoresRepository by lazy {
        ScoresRepositoryImpl(db.scoreDao())
    }
}
