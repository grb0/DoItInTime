package ba.grbo.doitintime.data

import androidx.annotation.IdRes
import ba.grbo.doitintime.R

enum class TasksSortingType(@IdRes val id: Int) {
    DESCRIPTION(R.id.description_radio_button),
    PRIORITY(R.id.priority_radio_button),
    STATUS(R.id.status_radio_button),
    CUSTOM(R.id.custom_radio_button);

    companion object {
        fun valueOf(@IdRes id: Int) = when (id) {
            R.id.description_radio_button -> DESCRIPTION
            R.id.priority_radio_button -> PRIORITY
            R.id.status_radio_button -> STATUS
            R.id.custom_radio_button -> CUSTOM
            else -> throw IllegalArgumentException("Unknown id: $id")
        }
    }
}