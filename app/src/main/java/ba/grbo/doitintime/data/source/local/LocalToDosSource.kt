package ba.grbo.doitintime.data.source.local

import ba.grbo.doitintime.data.Result
import ba.grbo.doitintime.data.Result.Error
import ba.grbo.doitintime.data.Result.Success
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
    private val coroutineDispatcher: CoroutineDispatcher,
) : ToDosSource {
    override suspend fun insert(toDo: ToDo): Result<Unit> = withContext(coroutineDispatcher) {
        try {
            toDoDao.insert(toDo)
            Success(Unit)
        } catch (e: Exception) {
            Error(e)
        }
    }

    override fun observeCountAll(): Flow<Result<Int>> = try {
        toDoDao.observeCountAll().map { Success(it) }
    } catch (e: Exception) {
        flow { emit(Error(e)) }
    }

    override fun observe(id: Int): Flow<Result<ToDo>> = try {
        toDoDao.observe(id).map { Success(it) }
    } catch (e: Exception) {
        flow { emit(Error(e)) }
    }

    override fun observeAll(): Flow<Result<List<ToDo>>> = try {
        toDoDao.observeAll().map { Success(it) }
    } catch (e: Exception) {
        flow { emit(Error(e)) }
    }
}