package com.example.imageviewer.database

import androidx.room.*
import com.example.imageviewer.data.entity.Data
import com.example.imageviewer.ui.DataId

@Dao
interface DataDao {

  @Query("SELECT * FROM data")
  fun getAll(): List<Data>

  @Query("SELECT * FROM data WHERE id = :id")
  fun getItemData(id: DataId): Data

  @Query("SELECT COUNT(*) FROM data WHERE is_favorite = :isFavorite")
  fun getCount(isFavorite: Int): Int

  @Query("SELECT * FROM data WHERE is_favorite = :isFavorite ORDER BY id ASC LIMIT :limit OFFSET :offset")
  fun getByLimit(limit: Int, offset: Int, isFavorite: Int): List<Data>

  @Query("SELECT * FROM data ORDER BY id ASC LIMIT :limit OFFSET :offset")
  fun getAllByLimit(limit: Int, offset: Int): List<Data>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertAll(list: List<Data>)

  @Query("UPDATE data SET is_favorite = :isFavorite WHERE id = :id")
  fun updateIsFavorite(id: Int, isFavorite: Int)
}