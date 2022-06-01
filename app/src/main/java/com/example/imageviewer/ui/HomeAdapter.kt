package com.example.imageviewer.ui

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.imageviewer.data.model.Tab

class HomeAdapter constructor(
  fragmentManager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

  private var tabs: List<Tab> = emptyList()

  override fun getItemCount() = 2

  override fun createFragment(position: Int): Fragment {
    return tabs[position].let {
      ContentFragment.newInstance(it.type.name)
    }
  }

  fun getTitle(position: Int) = tabs[position].name

  @SuppressLint("NotifyDataSetChanged")
  fun updateTabs(tabs: List<Tab>) {
    this.tabs = tabs
    notifyDataSetChanged()
  }
}