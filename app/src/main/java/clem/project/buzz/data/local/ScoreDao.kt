package clem.project.buzz.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {

    @Query("""
        SELECT *
        FROM scores
        ORDER BY value DESC, timestamp ASC
        LIMIT :limit
    """)
    fun topScores(limit: Int): Flow<List<Score>>

    @Insert
    suspend fun insert(score: Score)
}
