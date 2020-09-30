package ba.grbo.doitintime.di

import android.content.Context
import ba.grbo.doitintime.data.source.local.ToDoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object ToDoDatabaseModule {
    @Provides
    fun provideToDoDao(database: ToDoDatabase) = database.toDoDao

    @Singleton
    @Provides
    fun provideToDoDatabase(@ApplicationContext appContext: Context) = ToDoDatabase.getInstance(
        appContext
    )
}