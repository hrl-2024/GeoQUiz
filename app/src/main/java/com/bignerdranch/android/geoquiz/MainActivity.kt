package com.bignerdranch.android.geoquiz

import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val quizViewModel : QuizViewModel by viewModels()  // by keyword indicates that a property is implemented using a property delegate
    // Property delegate: a way to delegate the functionality of a property to an external unit of code (common delegate: lazy)

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()  // basic contract: intent as input and ActivityResult as output
    ) { result ->
        // handle the result
        if (result.resultCode == RESULT_OK) {
            quizViewModel.isCheater = result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)    // inflate views and put them onscreen

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding.trueButton.setOnClickListener { view: View ->
            // Do something in response to the click here
            checkAnswer(true)
        }

        binding.falseButton.setOnClickListener { view: View ->
            // Do something in response to the click here
            checkAnswer(false)
        }

        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.cheatButton.setOnClickListener {
            // Start CheatActivity
            // Use Intent Extra to pass data to the new activity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }

        updateQuestion()

        // .S is the code for API Level 31
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            blurCheatButton()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        // Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

        // Challenge: Use a Snackbar instead of a Toast
        Snackbar.make(binding.root, messageResId, Snackbar.LENGTH_SHORT).show()
    }

    // Adding code that is from later API levels (31) than minimum API (24) will causes Android Lint error
    // However, can still run and compile on devices running API Level 31
    // How do we address this? - raise the minimum sdk. But we can also use annotation
    @RequiresApi(Build.VERSION_CODES.S)
    private fun blurCheatButton() {
        val effect = RenderEffect.createBlurEffect(10f, 10f, Shader.TileMode.CLAMP)
        binding.cheatButton.setRenderEffect(effect)
    }
}