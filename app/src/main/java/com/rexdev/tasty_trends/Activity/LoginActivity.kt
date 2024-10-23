package com.rexdev.tasty_trends.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.rexdev.tasty_trends.R
import com.roydev.tastytrends.LoginReq
import com.roydev.tastytrends.LoginRes
import com.roydev.tastytrends.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var btnSignin: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailEditText = findViewById(R.id.login_email)
        passwordEditText = findViewById(R.id.login_password)
        btnSignin = findViewById(R.id.btnsignin)

        btnSignin.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnSignin.isEnabled = false // Disable the button while processing

            // Launch a coroutine for the login process
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Make the login request
                    val loginRes = RetrofitInstance.api.login(LoginReq(email, password))

                    // Handle successful login
                    withContext(Dispatchers.Main) {
                        if (loginRes.success) {
                            // Navigate to the next activity or handle success
                            Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()

                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        } else {
                            Toast.makeText(this@LoginActivity, loginRes.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                } catch (e: Exception) {
                    Log.e("LoginActivity", "Error: ${e.message}", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                } finally {
                    withContext(Dispatchers.Main) {
                        btnSignin.isEnabled = true // Re-enable button
                    }
                }
            }
        }

        findViewById<Button>(R.id.btnreturn).setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        findViewById<TextView>(R.id.tvforgotbtn).setOnClickListener {
            startActivity(Intent(this, ForgotPassActivity::class.java))
        }
    }
}
