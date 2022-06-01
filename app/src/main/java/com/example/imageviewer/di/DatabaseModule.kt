package com.example.imageviewer.di

import android.content.Context
import androidx.room.Room
import com.example.imageviewer.database.DataDao
import com.example.imageviewer.database.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

  @Singleton
  @Provides
  fun provideDataBase(@ApplicationContext context: Context): RoomDatabase {
    return Room.databaseBuilder(context, RoomDatabase::class.java, "db")
      .fallbackToDestructiveMigration().build()
  }

  @Singleton
  @Provides
  fun provideDataDao(dataBase: RoomDatabase): DataDao {
    return dataBase.dataDao()
  }
}