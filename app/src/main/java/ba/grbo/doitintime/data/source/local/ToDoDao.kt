package ba.grbo.doitintime.data.source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ba.grbo.doitintime.data.ToDo
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Transaction
    @Query("SELECT * FROM infos_table WHERE id = :infoId")
    fun observe(infoId: Int): Flow<ToDo>

    @Transaction
    @Query("SELECT * FROM infos_table ORDER BY id DESC")
    fun observeAll(): Flow<List<ToDo>>
}