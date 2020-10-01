package ba.grbo.doitintime.data.source.local

import androidx.databinding.InverseMethod
import androidx.lifecycle.MutableLiveData
import androidx.room.TypeConverter
import ba.grbo.doitintime.data.Priority
import ba.grbo.doitintime.data.Status


object Converters {
    @InverseMethod("fromStringToMLDTitle")
    @TypeConverter
    @JvmStatic
    fun fromMLDTitleToString(title: MutableLiveData<String>?) = title?.value

    @TypeConverter
    @JvmStatic
    fun fromStringToMLDTitle(title: String?) = MutableLiveData(title)

    @InverseMethod("fromPriorityToMLDPriority")
    @TypeConverter
    @JvmStatic
    fun fromMLDPriorityToPriority(priority: MutableLiveData<Priority>?) = priority?.value

    @TypeConverter
    @JvmStatic
    fun fromPriorityToMLDPriority(priority: Priority?) = priority?.let { MutableLiveData(it) }

    @InverseMethod("fromStringToPriority")
    @TypeConverter
    @JvmStatic
    fun fromPriorityToString(priority: Priority?) = priority?.name

    @TypeConverter
    @JvmStatic
    fun fromStringToPriority(priority: String?) = priority?.let { Priority.valueOf(it) }


    @InverseMethod("fromStatusToMLDStatus")
    @TypeConverter
    @JvmStatic
    fun fromMLDStatusToStatus(status: MutableLiveData<Status>?) = status?.value

    @TypeConverter
    @JvmStatic
    fun fromStatusToMLDStatus(status: Status?) = status?.let { MutableLiveData(it) }

    @InverseMethod("fromStringToStatus")
    @TypeConverter
    @JvmStatic
    fun fromStatusToString(status: Status?) = status?.name

    @TypeConverter
    @JvmStatic
    fun fromStringToStatus(status: String?) = status?.let { Status.valueOf(identifier = it) }
}