package pl.pollub.android.mastermind

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.pollub.android.mastermind.data.db.dao.PlayerDao
import pl.pollub.android.mastermind.data.db.dao.ScoreDao
import pl.pollub.android.mastermind.data.db.entity.Player
import pl.pollub.android.mastermind.data.db.entity.Score

@Database(
    entities = [Player::class, Score::class],
    version = 1,
    exportSchema = false
)
abstract class MasterAndDatabase : RoomDatabase() {

    abstract fun playerDao(): PlayerDao
    abstract fun scoreDao(): ScoreDao

    companion object {
        @Volatile private var INSTANCE: MasterAndDatabase? = null

        fun getDatabase(context: Context): MasterAndDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    MasterAndDatabase::class.java,
                    "masterand_db"
                ).build().also { INSTANCE = it }
            }
    }
}