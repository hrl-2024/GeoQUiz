package com.bignerdranch.android.geoquiz

import androidx.annotation.StringRes

// @StringRes: annotation (not required) that
// indicates the value of the field must be a valid string resource ID
// Check at compile time. So It prevents runtime crashes
// where the constructor is used with an invalid resource ID.
data class Question (@StringRes val textResId: Int, val answer: Boolean) {
}