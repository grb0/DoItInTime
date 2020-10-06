package ba.grbo.doitintime.data.source.local

import androidx.lifecycle.MutableLiveData
import androidx.room.TypeConverter
import ba.grbo.doitintime.data.*

class Converter {
    @TypeConverter
    fun mldToString(title: MutableLiveData<String>) = title.value

    @TypeConverter
    fun stringToMld(title: String) = MutableLiveData(title)

    @TypeConverter
    fun mldToPriority(priority: MutableLiveData<Priority>) = priority.value

    @TypeConverter
    fun priorityToMld(priority: Priority) = MutableLiveData(priority)

    @TypeConverter
    fun priorityToString(priority: Priority) = priority.name

    @TypeConverter
    fun stringToPriority(priority: String) = Priority.valueOf(priority)

    @TypeConverter
    fun mldToStatus(status: MutableLiveData<Status>) = status.value

    @TypeConverter
    fun statusToMld(status: Status) = MutableLiveData(status)

    @TypeConverter
    fun statusToString(status: Status) = status.name

    @TypeConverter
    fun stringToStatus(status: String) = Status.valueOf(status)

    @TypeConverter
    fun mldToTasksSorting(tasksSorting: MutableLiveData<TasksSorting>) =
        tasksSorting.value

    @TypeConverter
    fun tasksSortingToMld(tasksSorting: TasksSorting) = MutableLiveData(tasksSorting)

    @TypeConverter
    fun tasksSortingToString(tasksSorting: TasksSorting) =
        "${tasksSorting.type.value?.name}|${tasksSorting.order.value?.name}"

    @TypeConverter
    fun stringToTasksSorting(tasksSorting: String): TasksSorting {
        val (type, order) = tasksSorting.split('|')
        return TasksSorting(
            MutableLiveData(TasksSortingType.valueOf(type)),
            MutableLiveData(TasksSortingOrder.valueOf(order))
        )
    }
}