package com.example.imageviewer.data.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.imageviewer.data.entity.Data
import com.example.imageviewer.database.DataDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataPagingSource constructor(
  type: TabType,
  private val dataDao: DataDao
) : PagingSource<Int, Data>() {

  private val isFavorite = if (type == TabType.FAVORITE) 1 else 0

  override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
    return state.anchorPosition?.let { anchorPosition ->
      val anchorPage = state.closestPageToPosition(anchorPosition)
      anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }
  }

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
    val page = params.key ?: 0
    val data = withContext(Dispatchers.IO) {
      if (isFavorite == 0) {
        dataDao.getAllByLimit(params.loadSize, page * params.loadSize)
      } else {
        dataDao.getByLimit(params.loadSize, page * params.loadSize, isFavorite)
      }
    }
    return LoadResult.Page(
      data = data,
      prevKey = if (page == 0) null else page - 1,
      nextKey = if (data.isEmpty()) null else page + 1
    )
  }
}