package pl.pollub.android.mastermind.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "players")
data class Player(
    @PrimaryKey(autoGenerate = true)
    val playerId: Long = 0,
    val name: String,
    val email: String,
    val imageUri: String?
)
