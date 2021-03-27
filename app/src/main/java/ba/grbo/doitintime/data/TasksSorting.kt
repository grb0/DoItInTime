package ba.grbo.doitintime.data

import androidx.lifecycle.MutableLiveData

data class TasksSorting(
    val type: MutableLiveData<TasksSortingType> = MutableLiveData(TasksSortingType.CUSTOM),
    val order: MutableLiveData<TasksSortingOrder> = MutableLiveData(TasksSortingOrder.DESCENDING)
) {

    override fun equals(other: Any?) = when (other) {
        null,
        !is TasksSorting -> false
        else -> type.value == other.type.value && order.value == other.order.value
    }

    override fun hashCode() = 31 * type.hashCode() + order.hashCode()
}