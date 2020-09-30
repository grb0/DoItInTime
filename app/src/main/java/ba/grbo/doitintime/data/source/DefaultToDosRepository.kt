package ba.grbo.doitintime.data.source

import ba.grbo.doitintime.data.Result
import ba.grbo.doitintime.data.ToDo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultToDosRepository @Inject constructor(
    private val localToDosSource: ToDosSource
) : ToDosRepository {
    override suspend fun insertTodo(toDo: ToDo): Result<Unit> = localToDosSource.insert(toDo)

    override fun observeToDosCount(): Flow<Result<Int>> = localToDosSource.observeCountAll()

    override fun observeToDo(id: Int): Flow<Result<ToDo>> = localToDosSource.observe(id)

    override fun observeToDos(): Flow<Result<List<ToDo>>> = localToDosSource.observeAll()
}