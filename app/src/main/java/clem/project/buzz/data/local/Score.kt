package clem.project.buzz.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class Score(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val value: Int,
    val timestamp: Long = System.currentTimeMillis()
)
