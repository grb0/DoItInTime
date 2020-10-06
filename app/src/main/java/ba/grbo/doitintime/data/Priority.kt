package ba.grbo.doitintime.data

import androidx.annotation.IdRes
import ba.grbo.doitintime.R

enum class Priority(val identifier: String) {
    HIGH("High"),
    NORMAL("Normal"),
    LOW("Low");

    companion object {
        val priorities = listOf(
            HIGH.identifier,
            NORMAL.identifier,
            LOW.identifier
        )

        fun getPriority(@IdRes itemId: Int) = when (itemId) {
            R.id.high_priority -> HIGH
            R.id.normal_priority -> NORMAL
            R.id.low_priority -> LOW
            else -> throw IllegalArgumentException("Unknown itemId: $itemId")
        }

        fun getDrawables(identifier: String) = when (identifier) {
            HIGH.identifier -> R.drawable.ic_priority_high
            NORMAL.identifier -> R.drawable.ic_priority_normal
            LOW.identifier -> R.drawable.ic_priority_low
            else -> throw IllegalArgumentException("Unknown priority: $identifier")
        }

        fun getDrawables(priority: Priority) = when (priority) {
            HIGH -> R.drawable.ic_priority_high
            NORMAL -> R.drawable.ic_priority_normal
            LOW -> R.drawable.ic_priority_low
        }

        fun valueOf(identifier: String) = when (identifier) {
            "High" -> HIGH
            "Normal" -> NORMAL
            "Low" -> LOW
            else -> throw IllegalArgumentException("Unknown identifier: $identifier")
        }
    }
}