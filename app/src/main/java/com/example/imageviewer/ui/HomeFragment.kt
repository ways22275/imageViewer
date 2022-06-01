package com.example.imageviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.imageviewer.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

  private var _binding: FragmentHomeBinding? = null
  private val binding get() = _binding!!

  private val viewModel: HomeViewModel by viewModels()

  private lateinit var tabsAdapterHome: HomeAdapter
  private lateinit var tabMediator: TabLayoutMediator

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupAdapter()
    viewModel.tabs.observe(viewLifecycleOwner) {
      if (it.isEmpty()) {
        return@observe
      }
      setupTabLayout()
      tabsAdapterHome.updateTabs(it)
      if (!tabMediator.isAttached) {
        tabMediator.attach()
      }
    }
    viewModel.syncTabsCount()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun setupAdapter() {
    tabsAdapterHome = HomeAdapter(childFragmentManager, lifecycle)
  }

  private fun setupTabLayout() {
    binding.viewPager.adapter = tabsAdapterHome
    tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
      tab.text = tabsAdapterHome.getTitle(position)
    }
  }
}