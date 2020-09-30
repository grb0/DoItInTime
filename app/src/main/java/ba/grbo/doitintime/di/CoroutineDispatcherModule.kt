package ba.grbo.doitintime.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.Dispatchers

@InstallIn(ApplicationComponent::class)
@Module
object CoroutineDispatcherModule {
    @Provides
    fun provideIOCoroutineDispatcher() = Dispatchers.IO
}