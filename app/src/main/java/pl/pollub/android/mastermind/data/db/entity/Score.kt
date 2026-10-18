package pl.pollub.android.mastermind.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "scores",
    foreignKeys = [
        ForeignKey(
            entity = Player::class,
            parentColumns = ["playerId"],
            childColumns = ["playerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("playerId")]
)
data class Score(
    @PrimaryKey(autoGenerate = true)
    val scoreId: Long = 0,
    val playerId: Long,
    val attempts: Int,
    val colors: Int,
    val timestamp: Long = System.currentTimeMillis()
)
