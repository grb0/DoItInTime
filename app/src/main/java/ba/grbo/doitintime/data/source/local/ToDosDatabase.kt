package ba.grbo.doitintime.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ba.grbo.doitintime.data.Info
import ba.grbo.doitintime.data.Task

@Database(entities = [Info::class, Task::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ToDosDatabase : RoomDatabase() {
    abstract val toDoDao: ToDoDao
    abstract val infoDao: InfoDao
    abstract val taskDao: TaskDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: ToDosDatabase

        fun getInstance(appContext: Context): ToDosDatabase =
            if (Companion::INSTANCE.isInitialized) INSTANCE
            else synchronized(this) { buildDatabase(appContext).also { INSTANCE = it } }

        private fun buildDatabase(appContext: Context) = Room.databaseBuilder(
            appContext,
            ToDosDatabase::class.java,
            "to_dos_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}