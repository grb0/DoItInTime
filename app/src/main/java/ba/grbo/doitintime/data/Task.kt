package ba.grbo.doitintime.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks_table",
    foreignKeys = [
        ForeignKey(
            entity = Info::class,
            parentColumns = ["id"],
            childColumns = ["infoId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val infoId: Int,
    val description: String,
    val priority: Priority,
    val status: Status = Status.Active
) {
    constructor(
        infoId: Int,
        description: String,
        priority: Priority,
        status: Status = Status.Active
    ) : this(
        0,
        infoId,
        description,
        priority,
        status
    )

//    override fun equals(other: Any?): Boolean {
//        return when (other) {
//            null,
//            !is Task -> false
//            else -> id == other.id &&
//                    infoId == other.infoId &&
//                    description == other.description &&
//        }
//    }
}