package com.rexdev.tasty_trends.tools

import android.view.View
import com.google.android.material.snackbar.Snackbar

object Tool {
    fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }
}