package com.example.imageviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imageviewer.CommonConfig.BUNDLE_KEY_FOR_TAB_TYPE
import com.example.imageviewer.data.model.TabType
import com.example.imageviewer.databinding.FragmentContentBinding
import com.example.imageviewer.utils.SpacingItemDecorator
import com.example.imageviewer.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContentFragment : Fragment() {

  companion object {
    fun newInstance(type: String): ContentFragment = ContentFragment().apply {
      arguments = Bundle().apply {
        putString(BUNDLE_KEY_FOR_TAB_TYPE, type)
      }
    }
  }

  private var _binding: FragmentContentBinding? = null
  private val binding get() = _binding!!

  private val viewModel: ContentViewModel by viewModels()
  private val homeViewModel: HomeViewModel by viewModels({ requireParentFragment() })

  private lateinit var dataAdapter: DataAdapter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    _binding = FragmentContentBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.refreshCount.observe(viewLifecycleOwner) {
      homeViewModel.syncTabsCount()
    }

    setupDataAdapter()
    setupRecycleView()
    setupList()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun setupDataAdapter() {
    val type = viewModel.type
    dataAdapter = DataAdapter(
      type = type,
      onItemCollect = if (TabType.ALL == type) {
        this::onItemCollect
      } else {
        null
      },
      onItemClick = this::onItemClick
    )
  }

  private fun setupRecycleView() {
    binding.recyclerView.apply {
      adapter = dataAdapter
      layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
      addItemDecoration(SpacingItemDecorator(Utils.convertDpToPixel(10f, this.context).toInt()))
      setHasFixedSize(true)
    }
  }

  private fun setupList() {
    lifecycleScope.launch {
      viewModel.dataFlow.collectLatest {
        dataAdapter.submitData(it)
      }
    }
  }

  private fun onItemCollect(id: DataId, isFavorite: Int) {
    viewModel.updateIsFavorite(id = id, isFavorite = isFavorite)
  }

  private fun onItemClick(id: DataId) {
    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToContentDetailFragment(id))
  }
}