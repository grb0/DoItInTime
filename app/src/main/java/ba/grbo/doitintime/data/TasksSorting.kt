package ba.grbo.doitintime.data

import androidx.lifecycle.MutableLiveData

data class TasksSorting(
    val type: MutableLiveData<TasksSortingType> = MutableLiveData(TasksSortingType.CUSTOM),
    val order: MutableLiveData<TasksSortingOrder> = MutableLiveData(TasksSortingOrder.DESCENDING)
)