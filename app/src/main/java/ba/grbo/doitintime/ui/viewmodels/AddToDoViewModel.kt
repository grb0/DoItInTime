package ba.grbo.doitintime.ui.viewmodels

import androidx.annotation.StringRes
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ba.grbo.doitintime.R
import ba.grbo.doitintime.data.Info
import ba.grbo.doitintime.data.Priority
import ba.grbo.doitintime.data.Result.Error
import ba.grbo.doitintime.data.Result.Success
import ba.grbo.doitintime.data.Status
import ba.grbo.doitintime.data.ToDo
import ba.grbo.doitintime.data.source.ToDosRepository
import ba.grbo.doitintime.utilities.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddToDoViewModel @ViewModelInject constructor(
    private val repository: ToDosRepository
) : ViewModel() {
    private var wasTitleWarningCleaned = false

    val title: MutableLiveData<String> = MutableLiveData()
    val priority: MutableLiveData<Priority> = MutableLiveData()
    val status: MutableLiveData<Status> = MutableLiveData()

    val info = Info(title, priority, status)

    val toDo = ToDo(info, emptyList())

//    val toDo = repository.observeToDo(1)
//        .asLiveData()
//        .map {
//            when (it) {
//                is Success -> it.data
//                is Error -> null // inform the user of error
//            }
//        }

    private val _titleEvent = MutableLiveData<Event<@StringRes Int>>()
    val titleEvent: LiveData<Event<Int>>
        get() = _titleEvent

    private val _titleStillTooLongEvent = MutableLiveData<Event<@StringRes Int>>()
    val titleStillTooLongEvent: LiveData<Event<Int>>
        get() = _titleStillTooLongEvent

    private val _proceedToToDosFragmentEvent = MutableLiveData<Event<Unit>>()
    val proceedToToDosFragmentEvent: LiveData<Event<Unit>>
        get() = _proceedToToDosFragmentEvent

    private val _invalidFieldsErrorEvent =
        MutableLiveData<Event<Triple<@StringRes Int, @StringRes Int, @StringRes Int>>>()
    val invalidFieldsErrorEvent: LiveData<Event<Triple<Int, Int, Int>>>
        get() = _invalidFieldsErrorEvent

    private val _databaseErrorEvent =
        MutableLiveData<Event<Triple<@StringRes Int, @StringRes Int, @StringRes Int>>>()
    val databaseErrorEvent: LiveData<Event<Triple<Int, Int, Int>>>
        get() = _databaseErrorEvent

    private val _unknownErrorEvent =
        MutableLiveData<Event<Triple<@StringRes Int, @StringRes Int, @StringRes Int>>>()
    val unknownErrorEvent: LiveData<Event<Triple<Int, Int, Int>>>
        get() = _unknownErrorEvent

    fun onConfirmAddClicked() {
        if (areFieldsValid()) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    repository.insertInfo(
                        Info(
                            title,
                            priority,
                            status
                        )
                    ).apply {
                        when (this) {
                            is Success -> {
                                withContext(Dispatchers.Main) {
                                    proceedToToDosFragment()
                                }
                            }
                            is Error -> {
                                withContext(Dispatchers.Main) { informUserOfDatabaseError() }
                            }
                        }
                    }
                } catch (e: NullPointerException) {
                    withContext(Dispatchers.Main) { informUserOfInvalidFieldsError() }
                } catch (e: Exception) {
                    informUserOfUnknownError()
                }
            }
        }
    }

    fun onTitleLengthChanged(length: Int) {
        if (length > 30) informUserOfTitleBeingTooLong()
        else if (!wasTitleWarningCleaned) cleanTitleWarning()
    }

    private fun proceedToToDosFragment() {
        _proceedToToDosFragmentEvent.value = Event(Unit)
    }

    private fun informUserOfTitleBeingTooLong() {
        _titleEvent.value = Event(R.string.title_too_long)
        wasTitleWarningCleaned = false
    }


    private fun cleanTitleWarning() {
        _titleEvent.value = null
        wasTitleWarningCleaned = true
    }

    private fun informUserOfDatabaseError() {
        _databaseErrorEvent.value = Event(
            Triple(
                R.string.database_error_title,
                R.string.database_error_message,
                R.string.database_error_button_text
            )
        )
    }

    private fun informUserOfInvalidFieldsError() {
        _invalidFieldsErrorEvent.value = Event(
            Triple(
                R.string.invalid_fields_error_title,
                R.string.invalid_fields_error_message,
                R.string.invalid_fields_error_button_text
            )
        )
    }

    private fun informUserOfUnknownError() {
        _unknownErrorEvent.value = Event(
            Triple(
                R.string.unknown_error_title,
                R.string.unknown_error_message,
                R.string.unknown_error_button_text
            )
        )
    }

    private fun areFieldsValid(): Boolean {
        return isTitleValid()
    }

    private fun informUserOfTitleBeingEmpty() {
        _titleEvent.value = Event(R.string.title_empty)
        wasTitleWarningCleaned = false
    }

    private fun informUserOfTitleStillBeingTooLong() {
        _titleStillTooLongEvent.value = Event(R.string.title_too_long_message)
    }

    private fun isTitleValid() = title.isValid(
        ::informUserOfTitleBeingEmpty,
        ::informUserOfTitleStillBeingTooLong,
        true
    )

    private fun LiveData<String>.isValid(
        informUserOfBeingEmpty: () -> Unit,
        informUserOfBeingTooLong: () -> Unit,
        checkLength: Boolean = false
    ): Boolean {
        if (value == null || value == "") {
            informUserOfBeingEmpty()
            return false
        }
        if (checkLength) {
            try {
                if (value!!.length > 30) {
                    informUserOfBeingTooLong()
                    return false
                }
            } catch (e: NullPointerException) {
                informUserOfBeingEmpty()
                return false
            }
        }
        return true
    }
}