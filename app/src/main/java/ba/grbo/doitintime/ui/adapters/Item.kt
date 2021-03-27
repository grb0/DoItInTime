package ba.grbo.doitintime.ui.adapters

import ba.grbo.doitintime.data.Info
import ba.grbo.doitintime.data.Task

sealed class Item {
    abstract val id: Int

    data class TaskItem(val task: Task) : Item() {
        override val id = task.id
    }

    data class InfoItem(val info: Info) : Item() {
        override val id = info.id
    }
}