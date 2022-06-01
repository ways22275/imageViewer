package com.example.imageviewer.ui

import android.content.Context
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imageviewer.CommonConfig
import com.example.imageviewer.R
import com.example.imageviewer.data.entity.Data
import com.example.imageviewer.data.model.InitState
import com.example.imageviewer.data.model.LaunchState
import com.example.imageviewer.data.repository.LaunchRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
  @ApplicationContext private val context: Context,
  private val repo: LaunchRepository
) : ViewModel() {

  private val _launchState = MutableLiveData(
    LaunchState(state = InitState.INIT)
  )
  val launchState = _launchState

  private val gson = Gson()

  init {
    checkData()
  }

  private fun checkData() {
    viewModelScope.launch {
      if (!repo.checkLocalDataExist()) {
        repo.download()
        launchState.value =
          LaunchState(state = InitState.UPDATE, message = context.getString(R.string.launch_state_update))

      } else {
        launchState.value =
          LaunchState(state = InitState.COMPLETE, message = context.getString(R.string.launch_state_update_success))

      }
    }
  }

  private fun saveData(list: List<Data>) {
    viewModelScope.launch {
      repo.insert(list.map {
        it.copy(isFavorite = 0)
      })
      launchState.value =
        LaunchState(state = InitState.COMPLETE, message = context.getString(R.string.launch_state_update_success))
    }
  }

  fun parseDataFromDownloadedFile() {
    val file = File(
      Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        .toString() + "/" + CommonConfig.SOURCE_FILE_NAME
    )
    if (!file.exists()) {
      launchState.value = LaunchState(
        state = InitState.ERROR,
        message = context.getString(R.string.launch_state_update_error, "檔案不存在")
      )
      return
    }

    val builder = StringBuilder()
    try {
      val isr = InputStreamReader(FileInputStream(file), "UTF-8")
      val br = BufferedReader(isr)
      var line = br.readLine()
      while (line != null) {
        builder.append(line)
        line = br.readLine()
      }
      br.close()
      isr.close()
    } catch (e: Exception) {
      launchState.value = LaunchState(
        state = InitState.ERROR,
        message = context.getString(R.string.launch_state_update_error, "讀檔失敗")
      )
      removeFile()
      return
    }

    val source: List<Data>
    try {
      val type = object : TypeToken<List<Data>>() {}.type
      source = gson.fromJson(builder.toString(), type)
    } catch (e: Exception) {
      launchState.value = LaunchState(
        state = InitState.ERROR,
        message = context.getString(R.string.launch_state_update_error, "解析資料失敗")
      )
      removeFile()
      return
    }

    saveData(source)
  }

  private fun removeFile() {
    val file = File(
      Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        .toString() + "/" + CommonConfig.SOURCE_FILE_NAME
    )
    if (file.exists()) file.delete()
  }

  override fun onCleared() {
    super.onCleared()
    removeFile()
  }
}