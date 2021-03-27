package ba.grbo.doitintime.data

import androidx.annotation.IdRes
import ba.grbo.doitintime.R

enum class TasksSortingOrder(@IdRes val id: Int) {
    ASCENDING(R.id.ascending_radio_button),
    DESCENDING(R.id.descending_radio_button);

    companion object {
        fun valueOf(@IdRes id: Int) = when (id) {
            R.id.ascending_radio_button -> ASCENDING
            R.id.descending_radio_button -> DESCENDING
            else -> throw IllegalArgumentException("Unknown id: $id")
        }
    }
}