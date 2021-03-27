package ba.grbo.doitintime.di

import ba.grbo.doitintime.data.source.DefaultToDosRepository
import ba.grbo.doitintime.data.source.ToDosRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
abstract class ToDosRepositoryModule {
    @Singleton
    @Binds
    abstract fun bindToDosRepository(implementation: DefaultToDosRepository): ToDosRepository
}