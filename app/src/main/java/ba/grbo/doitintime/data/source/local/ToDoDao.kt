package ba.grbo.doitintime.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.grbo.doitintime.data.ToDo
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Insert
    suspend fun insert(toDo: ToDo)

    @Query("SELECT COUNT(*) FROM to_dos_table")
    fun observeCountAll(): Flow<Int>

    @Query("SELECT * FROM to_dos_table WHERE id = :id")
    fun observe(id: Int): Flow<ToDo>

    @Query("SELECT * FROM to_dos_table ORDER BY id ASC")
    fun observeAll(): Flow<List<ToDo>>
}