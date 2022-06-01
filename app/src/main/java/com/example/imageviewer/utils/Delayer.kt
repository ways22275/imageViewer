package com.example.imageviewer.utils

import android.util.Log
import kotlinx.coroutines.*

const val MILLISECONDS_3000 = 3000L

class Delayer {

  private val backgroundScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

  private var countDownJob: Job? = null

  /**
   * This method can delay milliseconds in background thread, then invoke your callback.
   * @throws IllegalStateException when this is still in background thread.
   * */
  fun delay(milliseconds: Long, onTime: () -> Unit) {
    if (isOccupied()) {
      Log.i("Delayer", "The end of waiting time has not yet come.")
      return
    }
    countDownJob = backgroundScope.launch {
      delay(milliseconds)
      reset()
      onTime()
    }
  }

  fun release() {
    if (backgroundScope.isActive) {
      backgroundScope.cancel("Delayer(${hashCode()})#release() cancels this job.")
    }
  }

  private fun isOccupied() = countDownJob?.isActive == true

  private fun reset() {
    if (countDownJob?.isActive == true) {
      countDownJob?.cancel()
    }
  }
}