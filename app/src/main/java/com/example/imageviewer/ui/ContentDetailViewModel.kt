package com.example.imageviewer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imageviewer.data.entity.Data
import com.example.imageviewer.data.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentDetailViewModel @Inject constructor(
  private val repo: DataRepository
) : ViewModel() {

  private val _itemData = MutableLiveData<Data>()
  val itemData: LiveData<Data> = _itemData

  fun fetchDetail(id: DataId) {
    viewModelScope.launch {
      _itemData.value = repo.fetchDetail(id)
    }
  }
}