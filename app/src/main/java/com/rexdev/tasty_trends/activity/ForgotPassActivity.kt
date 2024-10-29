package com.rexdev.tasty_trends.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.rexdev.tasty_trends.R
import com.rexdev.tasty_trends.dataClass.ForgotPass
import com.rexdev.tasty_trends.domain.RetrofitInstance
import com.rexdev.tasty_trends.global.GlobalVariables
import com.rexdev.tasty_trends.tools.Tool
import kotlinx.coroutines.launch

class ForgotPassActivity : AppCompatActivity() {

    private lateinit var email: TextInputEditText
    private lateinit var confirm: AppCompatButton
    private val tool = Tool

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_pass)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        email = findViewById(R.id.ForgotPassEmail)
        confirm = findViewById(R.id.btnForgotPass)

        val btnreturn = findViewById<TextView>(R.id.btnreturn)
        btnreturn.setOnClickListener {
            finish()
        }

        confirm.setOnClickListener{
            lifecycleScope.launch {
                try {
                    RetrofitInstance.api.forgotPass(ForgotPass(email.text.toString()))
                    tool.showSnackbar(findViewById(R.id.main), "Request sent")
                } catch (e: Exception) {
                    tool.showSnackbar(findViewById(R.id.main), "Failed to send request")
                }
            }
        }

    }
}