package com.example.imageviewer.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imageviewer.R
import com.example.imageviewer.data.model.Tab
import com.example.imageviewer.data.model.TabType
import com.example.imageviewer.data.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  @ApplicationContext private val context: Context,
  private val repo: DataRepository
) : ViewModel() {

  private val _tabs = MutableLiveData<List<Tab>>(emptyList())
  val tabs: LiveData<List<Tab>> = _tabs

  private val updateTitles: (Int, Int) -> Unit = { allCount, favoriteCount ->
    _tabs.value = mutableListOf(
      Tab(
        name = context.getString(R.string.home_tab_name_all_with_count, allCount),
        type = TabType.ALL
      ),
      Tab(
        name = context.getString(R.string.home_tab_name_favorite_with_count, favoriteCount),
        type = TabType.FAVORITE
      ),
    )
  }

  fun syncTabsCount() {
    viewModelScope.launch {
      updateTitles.invoke(repo.getCount(0), repo.getCount(1))
    }
  }
}