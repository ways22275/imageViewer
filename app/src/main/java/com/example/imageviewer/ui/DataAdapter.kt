package com.example.imageviewer.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imageviewer.data.entity.Data
import com.example.imageviewer.data.model.TabType
import com.example.imageviewer.databinding.ViewDataItemBinding

class DataAdapter constructor(
  private val type: TabType,
  private val onItemCollect: ((DataId, Int) -> Unit)? = null,
  private val onItemClick: ((DataId) -> Unit)? = null
) : PagingDataAdapter<Data, DataAdapter.DataViewHolder>(DataDiffCallback) {

  inner class DataViewHolder(private val binding: ViewDataItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Data) {
      Glide.with(itemView)
        .load(item.url)
        .into(binding.imageView)

      binding.collectButton.apply {
        visibility = if (TabType.ALL == type) View.VISIBLE else View.GONE
        isSelected = (item.isFavorite == 1)
        setOnClickListener {
          val isFavorite = if (it.isSelected) 0 else 1
          onItemCollect?.invoke(item.id, isFavorite)
        }
      }

      binding.root.setOnClickListener {
        onItemClick?.invoke(item.id)
      }
    }
  }

  object DataDiffCallback : DiffUtil.ItemCallback<Data>() {
    override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
      return oldItem == newItem
    }
  }

  override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
    getItem(position)?.let {
      holder.bind(it)
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
    return DataViewHolder(
      ViewDataItemBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
      )
    )
  }
}

typealias DataId = Int
