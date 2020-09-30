package ba.grbo.doitintime.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "to_dos_table")
data class ToDo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val priority: Priority,
    val description: String,
    val status: Status = Status.Active
) {
    constructor(
        title: String,
        priority: Priority,
        description: String,
        status: Status = Status.Active
    ) : this(
        0,
        title,
        priority,
        description,
        status
    )
}