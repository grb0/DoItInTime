package ba.grbo.doitintime.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import ba.grbo.doitintime.data.source.ToDosRepository

class ToDosViewModel @ViewModelInject constructor(
    private val repository: ToDosRepository
) : ViewModel() {
//    val toDosCount = Transformations.map(repository.observeToDosCount().asLiveData()) {
//        when (it) {
//            is Success -> it.data
//            is Error -> {
////                informUserOfDatabaseError()
//                null
//            }
//        }
//    }
}