package ba.grbo.doitintime.ui.viewmodels

import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ba.grbo.doitintime.data.source.ToDosRepository

class UpdateToDoViewModel(
    private val repository: ToDosRepository,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val toDoIdd = try {
        requireNotNull(savedStateHandle.get<Int>("toDoId"))
    } catch (e: IllegalArgumentException) {
        //        informUserOfError()
        -1
    }

    private val toDoIda = try {
        savedStateHandle.get<Int>("toDoId")!!
    } catch (e: NullPointerException) {
//        informUserOfError()
        -1
    }

    private val toDoId = savedStateHandle.get<Int>("toDoId") ?: -1
    val toDo = repository.observeToDo(toDoId)
}