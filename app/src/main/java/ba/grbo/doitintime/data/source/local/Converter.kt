package ba.grbo.doitintime.data.source.local

import androidx.lifecycle.MutableLiveData
import androidx.room.TypeConverter
import ba.grbo.doitintime.data.Priority
import ba.grbo.doitintime.data.Status


class Converter {
    @TypeConverter
    fun mLDTitleToString(title: MutableLiveData<String>) = title.value

    @TypeConverter
    fun stringToMLDTitle(title: String) = MutableLiveData(title)

    @TypeConverter
    fun mLDPriorityToPriority(priority: MutableLiveData<Priority>) = priority.value

    @TypeConverter
    fun priorityToMLDPriority(priority: Priority) = MutableLiveData(priority)

    @TypeConverter
    fun priorityToString(priority: Priority) = priority.name

    @TypeConverter
    fun stringToPriority(priority: String) = Priority.valueOf(priority)

    @TypeConverter
    fun mLDStatusToStatus(status: MutableLiveData<Status>) = status.value

    @TypeConverter
    fun statusToMLDStatus(status: Status) = MutableLiveData(status)

    @TypeConverter
    fun statusToString(status: Status) = status.name

    @TypeConverter
    fun stringToStatus(status: String) = Status.valueOf(identifier = status)
}