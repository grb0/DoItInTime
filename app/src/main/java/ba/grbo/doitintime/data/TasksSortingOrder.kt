package ba.grbo.doitintime.data

import ba.grbo.doitintime.R

enum class TasksSortingOrder {
    ASCENDING,
    DESCENDING;

    companion object {
        fun getCheckedRadioButtonId(tasksSortingOrder: TasksSortingOrder) =
            when (tasksSortingOrder) {
                ASCENDING -> R.id.ascending_radio_button
                DESCENDING -> R.id.descending_radio_button
            }
    }
}