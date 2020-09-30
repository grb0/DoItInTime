package ba.grbo.doitintime.data.source

import ba.grbo.doitintime.data.Result
import ba.grbo.doitintime.data.ToDo
import kotlinx.coroutines.flow.Flow

interface ToDosSource {
    suspend fun insert(toDo: ToDo): Result<Unit>

    fun observeCountAll(): Flow<Result<Int>>

    fun observe(id: Int): Flow<Result<ToDo>>

    fun observeAll(): Flow<Result<List<ToDo>>>
}