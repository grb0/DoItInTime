package ba.grbo.doitintime.di

import ba.grbo.doitintime.data.source.ToDosSource
import ba.grbo.doitintime.data.source.local.LocalToDosSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
abstract class ToDosSourceModule {
    @Singleton
    @Binds
    abstract fun bindToDosSource(implementation: LocalToDosSource): ToDosSource
}