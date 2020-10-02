package ba.grbo.doitintime.data.source.local

import androidx.lifecycle.MutableLiveData
import androidx.room.TypeConverter
import ba.grbo.doitintime.data.Priority
import ba.grbo.doitintime.data.Status


class Converters {
    @TypeConverter
    fun fromMLDTitleToString(title: MutableLiveData<String>) = title.value

    @TypeConverter
    fun fromStringToMLDTitle(title: String) = MutableLiveData(title)

    @TypeConverter
    fun fromMLDPriorityToPriority(priority: MutableLiveData<Priority>) = priority.value

    @TypeConverter
    fun fromPriorityToMLDPriority(priority: Priority) = MutableLiveData(priority)

    @TypeConverter
    fun fromPriorityToString(priority: Priority) = priority.name

    @TypeConverter
    fun fromStringToPriority(priority: String) = Priority.valueOf(priority)

    @TypeConverter
    fun fromMLDStatusToStatus(status: MutableLiveData<Status>) = status.value

    @TypeConverter
    fun fromStatusToMLDStatus(status: Status) = MutableLiveData(status)

    @TypeConverter
    fun fromStatusToString(status: Status) = status.name

    @TypeConverter
    fun fromStringToStatus(status: String) = Status.valueOf(identifier = status)
}