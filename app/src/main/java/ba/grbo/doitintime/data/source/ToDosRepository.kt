package ba.grbo.doitintime.data.source

import ba.grbo.doitintime.data.Info
import ba.grbo.doitintime.data.Result
import ba.grbo.doitintime.data.Task
import ba.grbo.doitintime.data.ToDo
import kotlinx.coroutines.flow.Flow

interface ToDosRepository {
    suspend fun insertInfo(info: Info): Result<Unit>

    suspend fun insertTask(task: Task): Result<Unit>

    suspend fun insertTasks(tasks: List<Task>): Result<Unit>

    fun observeInfosCount(): Flow<Result<Int>>

    fun observeInfo(id: Int): Flow<Result<Info>>

    fun observeTask(id: Int): Flow<Result<Task>>

    fun observeInfos(): Flow<Result<List<Info>>>

    fun observeTasks(infoId: Int): Flow<Result<List<Task>>>

    fun observeToDo(infoId: Int): Flow<Result<ToDo>>

    fun observeToDos(): Flow<Result<List<ToDo>>>
}