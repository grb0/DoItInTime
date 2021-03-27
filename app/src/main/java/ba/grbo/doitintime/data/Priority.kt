package ba.grbo.doitintime.data

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import ba.grbo.doitintime.R

enum class Priority(
    val identifier: String,
    @DrawableRes val drawable: Int,
    @IdRes val id: Int
) {
    HIGH("High", R.drawable.ic_priority_high, R.id.popup_priority_high),
    NORMAL("Normal", R.drawable.ic_priority_normal, R.id.popup_priority_normal),
    LOW("Low", R.drawable.ic_priority_low, R.id.popup_priority_low);

    companion object {
        val priorities = listOf(
            HIGH.identifier,
            NORMAL.identifier,
            LOW.identifier
        )

        fun getDrawables(identifier: String) = when (identifier) {
            HIGH.identifier -> R.drawable.ic_priority_high
            NORMAL.identifier -> R.drawable.ic_priority_normal
            LOW.identifier -> R.drawable.ic_priority_low
            else -> throw IllegalArgumentException("Unknown priority: $identifier")
        }

        fun valueOf(@DrawableRes drawable: Int) = when (drawable) {
            R.drawable.ic_priority_high -> HIGH
            R.drawable.ic_priority_normal -> NORMAL
            R.drawable.ic_priority_low -> LOW
            else -> throw IllegalArgumentException("Unknown drawable: $drawable")
        }

        fun valueOf(identifier: String) = when (identifier) {
            "High" -> HIGH
            "Normal" -> NORMAL
            "Low" -> LOW
            else -> throw IllegalArgumentException("Unknown identifier: $identifier")
        }
    }
}