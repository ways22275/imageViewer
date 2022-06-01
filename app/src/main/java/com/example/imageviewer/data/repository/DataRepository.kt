package com.example.imageviewer.data.repository

import com.example.imageviewer.database.DataDao
import com.example.imageviewer.ui.DataId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataRepository @Inject constructor(
  private val dataDao: DataDao
) {

  suspend fun getCount(isFavorite: Int) = withContext(Dispatchers.IO) {
    dataDao.getCount(isFavorite)
  }

  suspend fun updateIsFavorite(id: Int, isFavorite: Int) {
    withContext(Dispatchers.IO) {
      dataDao.updateIsFavorite(id, isFavorite)
    }
  }

  suspend fun fetchDetail(id: DataId) = withContext(Dispatchers.IO) {
    dataDao.getItemData(id)
  }
}