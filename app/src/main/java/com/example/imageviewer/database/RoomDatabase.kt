package com.example.imageviewer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.imageviewer.data.entity.Data

@Database(entities = [Data::class], version = 1, exportSchema = false)
abstract class RoomDatabase : RoomDatabase() {
  abstract fun dataDao(): DataDao
}