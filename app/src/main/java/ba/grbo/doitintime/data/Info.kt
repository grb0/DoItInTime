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
    var focusedView = MutableLiveData<Triple<Int, Any?, Any?>>()

    constructor(
        title: MutableLiveData<String>,
        priority: MutableLiveData<Priority> = MutableLiveData(Priority.NORMAL),
        status: MutableLiveData<Status> = MutableLiveData(Status.ACTIVE)
    ) : this(
        0,
        title,
        priority,
        status
    )

    override fun equals(other: Any?): Boolean {
        return when (other) {
            null,
            !is Info -> false
            else -> id == other.id &&
                    title.value == other.title.value &&
                    priority.value.toString() == other.priority.value.toString() &&
                    status.value.toString() == other.status.value.toString()
        }
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + priority.hashCode()
        result = 31 * result + status.hashCode()
        return result
    }
}