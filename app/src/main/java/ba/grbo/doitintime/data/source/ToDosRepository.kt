package ba.grbo.doitintime.data.source

import ba.grbo.doitintime.data.Result
import ba.grbo.doitintime.data.ToDo
import kotlinx.coroutines.flow.Flow

interface ToDosRepository {
    suspend fun insertTodo(toDo: ToDo): Result<Unit>

    fun observeToDosCount(): Flow<Result<Int>>

    fun observeToDo(id: Int): Flow<Result<ToDo>>

    fun observeToDos(): Flow<Result<List<ToDo>>>
}