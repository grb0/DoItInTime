package ba.grbo.doitintime.data.source

import ba.grbo.doitintime.data.Info
import ba.grbo.doitintime.data.Result
import ba.grbo.doitintime.data.Task
import ba.grbo.doitintime.data.ToDo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultToDosRepository @Inject constructor(
    private val toDosSource: ToDosSource
) : ToDosRepository {
    override suspend fun insertInfo(info: Info): Result<Unit> = toDosSource.insertInfo(info)

    override suspend fun insertTask(task: Task): Result<Unit> = toDosSource.insertTask(task)

    override suspend fun insertTasks(tasks: List<Task>): Result<Unit> =
        toDosSource.insertTasks(tasks)

    override fun observeInfosCount(): Flow<Result<Int>> = toDosSource.observeInfosCount()

    override fun observeInfo(id: Int): Flow<Result<Info>> = toDosSource.observeInfo(id)

    override fun observeTask(id: Int): Flow<Result<Task>> = toDosSource.observeTask(id)

    override fun observeInfos(): Flow<Result<List<Info>>> = toDosSource.observeInfos()

    override fun observeTasks(infoId: Int): Flow<Result<List<Task>>> =
        toDosSource.observeTasks(infoId)

    override fun observeToDo(infoId: Int): Flow<Result<ToDo>> = toDosSource.observeToDo(infoId)

    override fun observeToDos(): Flow<Result<List<ToDo>>> = toDosSource.observeToDos()
}