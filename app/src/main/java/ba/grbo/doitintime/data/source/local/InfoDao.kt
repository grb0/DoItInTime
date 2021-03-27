package ba.grbo.doitintime.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.grbo.doitintime.data.Info
import kotlinx.coroutines.flow.Flow

@Dao
interface InfoDao {
    @Insert
    suspend fun insert(info: Info)

    @Query("SELECT COUNT(*) FROM infos_table")
    fun observeCount(): Flow<Int>

    @Query("SELECT * FROM infos_table WHERE id = :id")
    fun observe(id: Int): Flow<Info>

    @Query("SELECT * FROM infos_table ORDER BY id DESC")
    fun observeAll(): Flow<List<Info>>
}