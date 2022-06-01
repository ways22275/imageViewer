package com.example.imageviewer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.imageviewer.databinding.FragmentImageViewerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageViewerFragment : Fragment() {

  private var _binding: FragmentImageViewerBinding? = null
  private val binding get() = _binding!!

  private val args: ImageViewerFragmentArgs by navArgs()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    _binding = FragmentImageViewerBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    Glide.with(requireContext())
      .load(args.imageUrl)
      .into(binding.imageView)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}