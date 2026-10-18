package pl.pollub.android.mastermind.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import pl.pollub.android.mastermind.data.db.entity.Player

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(player: Player): Long

    @Update
    suspend fun update(player: Player)

    @Query("SELECT * FROM players WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): Player?
}
