package ru.ikrom.companion

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ikrom.player_handler.utils.IAudioDataListener
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CompanionModule(){

    @Singleton
    @Provides
    fun provideCompanionService(): IAudioDataListener = CompanionService()
}