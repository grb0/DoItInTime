package ba.grbo.doitintime.data

import androidx.room.Embedded
import androidx.room.Relation

data class ToDo(
    @Embedded
    val info: Info,

    @Relation(parentColumn = "id", entityColumn = "infoId")
    val task: List<Task>
)