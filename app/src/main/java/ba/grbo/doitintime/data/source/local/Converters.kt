package ba.grbo.doitintime.data.source.local

import androidx.databinding.InverseMethod
import androidx.room.TypeConverter
import ba.grbo.doitintime.data.Priority
import ba.grbo.doitintime.data.Status


object Converters {
    @InverseMethod("toPriority")
    @TypeConverter
    @JvmStatic
    fun fromPriority(priority: Priority?) = priority?.name

    @TypeConverter
    @JvmStatic
    fun toPriority(priority: String?) = priority?.let { Priority.valueOf(it) }

    @InverseMethod("toStatus")
    @TypeConverter
    @JvmStatic
    fun fromStatus(status: Status?) = status?.identifier

    @TypeConverter
    @JvmStatic
    fun toStatus(status: String?) = status?.let { Status.valueOf(identifier = it) }
}