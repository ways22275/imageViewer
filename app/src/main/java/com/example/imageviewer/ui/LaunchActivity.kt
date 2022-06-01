package com.example.imageviewer.ui

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.imageviewer.data.model.InitState
import com.example.imageviewer.databinding.ActivityLaunchBinding
import com.example.imageviewer.utils.Delayer
import com.example.imageviewer.utils.MILLISECONDS_3000
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchActivity : AppCompatActivity() {

  private lateinit var binding: ActivityLaunchBinding

  private val viewModel: LaunchViewModel by viewModels()

  private val downloadedReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
      viewModel.parseDataFromDownloadedFile()
    }
  }

  private val delayer = Delayer()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLaunchBinding.inflate(layoutInflater)
    setContentView(binding.root)

    registerReceiver(downloadedReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

    viewModel.launchState.observe(this) {
      binding.stateText.text = it.message
      when (it.state) {
        InitState.COMPLETE -> {
          binding.displayText.visibility = View.VISIBLE
          delayer.delay(MILLISECONDS_3000) {
            toMainActivity()
          }
        }
        else -> Unit
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    unregisterReceiver(downloadedReceiver)
    delayer.release()
  }

  private fun toMainActivity() {
    startActivity(
      Intent(this, MainActivity::class.java)
    )
    finish()
  }
}