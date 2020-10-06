package ba.grbo.doitintime.data

import ba.grbo.doitintime.R

enum class TasksSortingType {
    DESCRIPTION,
    PRIORITY,
    STATUS,
    CUSTOM;

    companion object {
        fun getCheckedRadioButtonId(tasksSortingType: TasksSortingType) = when (tasksSortingType) {
            DESCRIPTION -> R.id.description_radio_button
            PRIORITY -> R.id.priority_radio_button
            STATUS -> R.id.status_radio_button
            CUSTOM -> R.id.custom_radio_button
        }
    }
}