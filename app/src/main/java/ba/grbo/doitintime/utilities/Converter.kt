package ba.grbo.doitintime.utilities

import androidx.databinding.InverseMethod
import ba.grbo.doitintime.data.Priority
import ba.grbo.doitintime.data.Status

object Converter {
    @InverseMethod("stringToPriority")
    @JvmStatic
    fun priorityToString(priority: Priority?): String? = priority?.identifier

    @JvmStatic
    fun stringToPriority(priority: String?): Priority? = priority?.let {
        Priority.valueOf(identifier = it)
    }

    @InverseMethod("stringToStatus")
    @JvmStatic
    fun statusToString(status: Status?): String? = status?.identifier

    @JvmStatic
    fun stringToStatus(status: String?): Status? = status?.let { Status.valueOf(identifier = it) }
}