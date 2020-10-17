package ba.grbo.doitintime.data

import androidx.lifecycle.MutableLiveData
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "infos_table")
data class Info(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: MutableLiveData<String>,
    val priority: MutableLiveData<Priority> = MutableLiveData(Priority.NORMAL),
    val status: MutableLiveData<Status> = MutableLiveData(Status.ACTIVE),
    val tasksSorting: MutableLiveData<TasksSorting> = MutableLiveData(TasksSorting())
) {
    @Ignore
    var expanded = MutableLiveData(false)

    @Ignore
    var focusedView = MutableLiveData<Triple<Int, Any?, Int?>>()

    constructor(
        title: MutableLiveData<String>,
        priority: MutableLiveData<Priority> = MutableLiveData(Priority.NORMAL),
        status: MutableLiveData<Status> = MutableLiveData(Status.ACTIVE),
        tasksSorting: MutableLiveData<TasksSorting> = MutableLiveData(TasksSorting())
    ) : this(
        0,
        title,
        priority,
        status,
        tasksSorting
    )

    override fun equals(other: Any?) = when (other) {
        null,
        !is Info -> false
        else -> id == other.id &&
                title.value == other.title.value &&
                priority.value.toString() == other.priority.value.toString() &&
                status.value.toString() == other.status.value.toString() &&
                tasksSorting.value == other.tasksSorting.value
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + priority.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + tasksSorting.hashCode()
        return result
    }
}