package ba.grbo.doitintime.data

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import ba.grbo.doitintime.R

enum class Status(
    val identifier: String,
    @DrawableRes val drawable: Int,
    @IdRes val id: Int
) {
    ACTIVE("Active", R.drawable.ic_status_active, R.id.popup_status_active),
    ON_HOLD("On hold", R.drawable.ic_status_on_hold, R.id.popup_status_on_hold),
    COMPLETED("Completed", R.drawable.ic_status_completed, R.id.popup_status_completed);

    companion object {
        val statuses = listOf(
            ACTIVE.identifier,
            ON_HOLD.identifier,
            COMPLETED.identifier
        )

        fun getDrawables(identifier: String) = when (identifier) {
            ACTIVE.identifier -> R.drawable.ic_status_active
            ON_HOLD.identifier -> R.drawable.ic_status_on_hold
            COMPLETED.identifier -> R.drawable.ic_status_completed
            else -> throw IllegalArgumentException("Unknown status: $identifier")
        }

        fun valueOf(@DrawableRes drawable: Int) = when (drawable) {
            R.drawable.ic_status_active -> ACTIVE
            R.drawable.ic_status_on_hold -> ON_HOLD
            R.drawable.ic_status_completed -> COMPLETED
            else -> throw IllegalArgumentException("Unknown drawable: $drawable")
        }

        fun valueOf(identifier: String) = when (identifier) {
            ACTIVE.identifier -> ACTIVE
            ON_HOLD.identifier -> ON_HOLD
            COMPLETED.identifier -> COMPLETED
            else -> throw IllegalArgumentException("Unknown identifier: $identifier")
        }
    }
}