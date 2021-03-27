package ba.grbo.doitintime.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.grbo.doitintime.data.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task)

    @Insert
    suspend fun insert(tasks: List<Task>)

    @Query("SELECT * FROM tasks_table WHERE id = :id")
    fun observe(id: Int): Flow<Task>

    @Query("SELECT * FROM tasks_table WHERE id = :infoId ORDER BY id DESC")
    fun observeAll(infoId: Int): Flow<List<Task>>
}