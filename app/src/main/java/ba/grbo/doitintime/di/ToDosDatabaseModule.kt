package ba.grbo.doitintime.di

import android.content.Context
import ba.grbo.doitintime.data.source.local.ToDosDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object ToDosDatabaseModule {
    @Provides
    fun provideToDoDao(database: ToDosDatabase) = database.toDoDao

    @Provides
    fun provideInfoDao(database: ToDosDatabase) = database.infoDao

    @Provides
    fun provideTaskDao(database: ToDosDatabase) = database.taskDao

    @Singleton
    @Provides
    fun provideDoItInTimeDatabase(@ApplicationContext appContext: Context) =
        ToDosDatabase.getInstance(appContext)
}