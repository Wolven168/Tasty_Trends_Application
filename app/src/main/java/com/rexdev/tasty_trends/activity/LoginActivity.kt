package com.rexdev.tasty_trends.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.rexdev.tasty_trends.R
import com.rexdev.tasty_trends.dataClass.LoginReq
import com.rexdev.tasty_trends.domain.RetrofitInstance
import com.rexdev.tasty_trends.global.GlobalVariables
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var btnSignin: AppCompatButton
    private val app = GlobalVariables

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupWindowInsets()

        emailEditText = findViewById(R.id.login_email)
        passwordEditText = findViewById(R.id.login_password)
        btnSignin = findViewById(R.id.btnEditProfile)

        btnSignin.setOnClickListener {
            handleLogin()
        }

        findViewById<Button>(R.id.btnreturn).setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        findViewById<TextView>(R.id.tvforgotbtn).setOnClickListener {
            startActivity(Intent(this, ForgotPassActivity::class.java))
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun handleLogin() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            showToast("Missing Parameters")
            return
        }

        setButtonEnabled(false)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val loginRes = RetrofitInstance.api.login(LoginReq(email, password))
                val userdata = loginRes.userDetails
                withContext(Dispatchers.Main) {
                    if (loginRes.success) {
                        app.PROFILE_ID = userdata?.user_id.toString()
                        app.PROFILE_NAME = userdata?.user_name.toString()
                        app.PROFILE_EMAIL = userdata?.user_email.toString()
                        app.PROFILE_TYPE = userdata?.user_type.toString()
                        app.PROFILE_IMG = userdata?.user_image.toString()
                        app.SHOP_ID = userdata?.shop_id.toString()
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    } else {
                        showToast(loginRes.message)
                    }
                }

            } catch (e: Exception) {
                Log.e("LoginActivity", "Error: ${e.message}", e)
                withContext(Dispatchers.Main) {
                }
            } finally {
                withContext(Dispatchers.Main) {
                    setButtonEnabled(true)
                }
            }
        }
    }

    private fun setButtonEnabled(enabled: Boolean) {
        btnSignin.isEnabled = enabled
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(resId: Int, vararg formatArgs: Any?) {
        Toast.makeText(this, getString(resId, *formatArgs), Toast.LENGTH_SHORT).show()
    }
}
