package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
class QuizViewModel : ViewModel() {

    init {
        Log.d(TAG, "ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        // called when ViewModel is destroyed
        // useful place to do any cleanup (ex: unobserving a data source)
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }
}