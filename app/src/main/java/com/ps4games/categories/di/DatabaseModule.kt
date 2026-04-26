package com.ps4games.categories.di

import android.content.Context
import androidx.room.Room
import com.ps4games.categories.data.db.AppDatabase
import com.ps4games.categories.data.db.GameDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "ps4_games.db")
            .createFromAsset("ps4_games.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideGameDao(db: AppDatabase): GameDao = db.gameDao()
}
