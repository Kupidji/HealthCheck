package com.example.healthcheck.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.FragmentActivity

object HideKeyBoard {

    fun EditText.hideKeyboard(activity : FragmentActivity?) {
        if (requestFocus()) {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(this.windowToken, 0)
            setSelection(text.length)
        }
    }

}