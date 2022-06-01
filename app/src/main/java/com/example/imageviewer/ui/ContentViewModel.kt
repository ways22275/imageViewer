package com.example.imageviewer.ui

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.imageviewer.CommonConfig
import com.example.imageviewer.data.entity.Data
import com.example.imageviewer.data.model.DataPagingSource
import com.example.imageviewer.data.model.TabType
import com.example.imageviewer.data.repository.DataRepository
import com.example.imageviewer.database.DataDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  dataDao: DataDao,
  private val repo: DataRepository
) : ViewModel() {

  val type = TabType.values().find {
    it.name == savedStateHandle.get<String>(CommonConfig.BUNDLE_KEY_FOR_TAB_TYPE)
  } ?: TabType.ALL

  val dataFlow = Pager(
    config = PagingConfig(pageSize = 20, enablePlaceholders = false, prefetchDistance = 5),
    pagingSourceFactory = { DataPagingSource(dataDao = dataDao, type = type) }
  ).flow

  private val _refreshCount = MutableLiveData<Unit>()
  val refreshCount: LiveData<Unit> = _refreshCount

  fun updateIsFavorite(id: Int, isFavorite: Int) {
    viewModelScope.launch {
      repo.updateIsFavorite(id, isFavorite)
      _refreshCount.value = Unit
    }
  }
}