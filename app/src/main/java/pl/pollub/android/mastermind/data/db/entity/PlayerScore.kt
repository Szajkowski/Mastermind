package pl.pollub.android.mastermind.data.db.entity

data class PlayerScore(
    val name: String,
    val attempts: Int,
    val colors: Int
)