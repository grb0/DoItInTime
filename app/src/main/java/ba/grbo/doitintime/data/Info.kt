package ba.grbo.doitintime.data

import androidx.lifecycle.MutableLiveData
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "infos_table")
data class Info(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: MutableLiveData<String>,
    val priority: MutableLiveData<Priority> = MutableLiveData(Priority.Normal),
    val status: MutableLiveData<Status> = MutableLiveData(Status.Active)
) {
    constructor(
        title: MutableLiveData<String>,
        priority: MutableLiveData<Priority> = MutableLiveData(Priority.Normal),
        status: MutableLiveData<Status> = MutableLiveData(Status.Active)
    ) : this(
        0,
        title,
        priority,
        status
    )
}