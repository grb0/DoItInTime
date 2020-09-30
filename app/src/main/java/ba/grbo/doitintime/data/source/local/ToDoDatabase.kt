package ba.grbo.doitintime.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ba.grbo.doitintime.data.ToDo

@Database(entities = [ToDo::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ToDoDatabase : RoomDatabase() {
    abstract val toDoDao: ToDoDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: ToDoDatabase

        fun getInstance(appContext: Context): ToDoDatabase = if (Companion::INSTANCE.isInitialized) INSTANCE
        else synchronized(this) { buildDatabase(appContext).also { INSTANCE = it } }

        private fun buildDatabase(appContext: Context) = Room.databaseBuilder(
            appContext,
            ToDoDatabase::class.java,
            "to_dos_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}