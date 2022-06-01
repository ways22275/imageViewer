package com.example.imageviewer.data.repository

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.example.imageviewer.CommonConfig.SOURCE_FILE_NAME
import com.example.imageviewer.CommonConfig.SOURCE_URL
import com.example.imageviewer.data.entity.Data
import com.example.imageviewer.database.DataDao
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LaunchRepository @Inject constructor(
  @ApplicationContext private val context: Context,
  private val dataDao: DataDao
) {

  private val downloadService = (context.getSystemService(Context.DOWNLOAD_SERVICE)) as DownloadManager

  fun download() {
    val request = DownloadManager.Request(Uri.parse(SOURCE_URL))
      .setAllowedOverMetered(true)
      .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, SOURCE_FILE_NAME)

    downloadService.enqueue(request)
  }

  suspend fun checkLocalDataExist() = withContext(Dispatchers.IO) {
    dataDao.getAll().isNotEmpty()
  }

  suspend fun insert(list: List<Data>) = withContext(Dispatchers.IO) {
    dataDao.insertAll(list)
  }
}