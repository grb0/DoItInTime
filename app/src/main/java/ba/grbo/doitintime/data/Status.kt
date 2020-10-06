package ba.grbo.doitintime.data

import androidx.annotation.IdRes
import ba.grbo.doitintime.R

enum class Status(val identifier: String) {
    ACTIVE("Active"),
    ON_HOLD("On hold"),
    COMPLETED("Completed");

    companion object {
        val statuses = listOf(
            ACTIVE.identifier,
            ON_HOLD.identifier,
            COMPLETED.identifier
        )

        fun getStatus(@IdRes itemId: Int) = when (itemId) {
            R.id.status_active -> ACTIVE
            R.id.status_on_hold -> ON_HOLD
            R.id.status_completed -> COMPLETED
            else -> throw IllegalArgumentException("Unknown itemId: $itemId")
        }

        fun getDrawables(identifier: String) = when (identifier) {
            ACTIVE.identifier -> R.drawable.ic_status_active
            ON_HOLD.identifier -> R.drawable.ic_status_on_hold
            COMPLETED.identifier -> R.drawable.ic_status_completed
            else -> throw IllegalArgumentException("Unknown status: $identifier")
        }

        fun getDrawables(status: Status) = when (status) {
            ACTIVE -> R.drawable.ic_status_active
            ON_HOLD -> R.drawable.ic_status_on_hold
            COMPLETED -> R.drawable.ic_status_completed
            else -> throw IllegalArgumentException("Unknown status: $status")
        }

        fun valueOf(identifier: String) = when (identifier) {
            "Active" -> ACTIVE
            "On hold" -> ON_HOLD
            "Completed" -> COMPLETED
            else -> throw IllegalArgumentException("Unknown identifier: $identifier")
        }
    }
}