package com.example.imageviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.imageviewer.data.entity.Data
import com.example.imageviewer.databinding.FragmentContentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentDetailFragment : Fragment() {

  private var _binding: FragmentContentDetailBinding? = null
  private val binding get() = _binding!!

  private val viewModel: ContentDetailViewModel by viewModels()

  private val args: ContentDetailFragmentArgs by navArgs()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    _binding = FragmentContentDetailBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.itemData.observe(viewLifecycleOwner) {
      bind(it)
    }

    viewModel.fetchDetail(args.dataId)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun bind(data: Data) {
    binding.hbImageView.let { imageView ->
      Glide.with(requireContext())
        .load(data.hdUrl)
        .into(imageView)

      imageView.setOnClickListener {
        findNavController().navigate(
          directions = ContentDetailFragmentDirections.actionContentDetailFragmentToImageViewerFragment(
            data.hdUrl
          ),
          navigatorExtras = FragmentNavigatorExtras(imageView to "hbImageView")
        )
      }
    }

    binding.title.text = data.title
    binding.date.text = data.date
    binding.description.text = data.description
    binding.copyRight.text = data.copyright
  }
}