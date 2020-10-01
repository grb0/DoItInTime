package ba.grbo.doitintime.data.source.local

import ba.grbo.doitintime.data.Info
import ba.grbo.doitintime.data.Result
import ba.grbo.doitintime.data.Result.Error
import ba.grbo.doitintime.data.Result.Success
import ba.grbo.doitintime.data.Task
import ba.grbo.doitintime.data.ToDo
import ba.grbo.doitintime.data.source.ToDosSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalToDosSource @Inject constructor(
    private val toDoDao: ToDoDao,
    private val infoDao: InfoDao,
    private val taskDao: TaskDao,
    private val coroutineDispatcher: CoroutineDispatcher,
) : ToDosSource {
    override suspend fun insertInfo(info: Info): Result<Unit> = withContext(coroutineDispatcher) {
        try {
            infoDao.insert(info)
            Success(Unit)
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun insertTask(task: Task): Result<Unit> = withContext(coroutineDispatcher) {
        try {
            taskDao.insert(task)
            Success(Unit)
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun insertTasks(tasks: List<Task>): Result<Unit> =
        withContext(coroutineDispatcher) {
            try {
                taskDao.insert(tasks)
                Success(Unit)
            } catch (e: Exception) {
                Error(e)
            }
        }

    override fun observeInfosCount(): Flow<Result<Int>> = try {
        infoDao.observeCount().map { Success(it) }
    } catch (e: Exception) {
        flow { emit(Error(e)) }
    }

    override fun observeInfo(id: Int): Flow<Result<Info>> = try {
        infoDao.observe(id).map { Success(it) }
    } catch (e: Exception) {
        flow { emit(Error(e)) }
    }

    override fun observeTask(id: Int): Flow<Result<Task>> = try {
        taskDao.observe(id).map { Success(it) }
    } catch (e: Exception) {
        flow { emit(Error(e)) }
    }

    override fun observeInfos(): Flow<Result<List<Info>>> = try {
        infoDao.observeAll().map { Success(it) }
    } catch (e: Exception) {
        flow { emit(Error(e)) }
    }

    override fun observeTasks(infoId: Int): Flow<Result<List<Task>>> = try {
        taskDao.observeAll(infoId).map { Success(it) }
    } catch (e: Exception) {
        flow { emit(Error(e)) }
    }

    override fun observeToDo(infoId: Int): Flow<Result<ToDo>> = try {
        toDoDao.observe(infoId).map { Success(it) }
    } catch (e: Exception) {
        flow { emit(Error(e)) }
    }

    override fun observeToDos(): Flow<Result<List<ToDo>>> = try {
        toDoDao.observeAll().map { Success(it) }
    } catch (e: Exception) {
        flow { emit(Error(e)) }
    }
}