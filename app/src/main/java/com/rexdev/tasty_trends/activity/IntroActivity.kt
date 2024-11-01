package com.rexdev.tasty_trends.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rexdev.tasty_trends.R
import com.rexdev.tasty_trends.domain.RetrofitInstance

class IntroActivity : AppCompatActivity() {
    private val splashScreenDuration: Long = 1000 // Adjust splash screen duration as needed

    override fun onCreate(savedInstanceState: Bundle?) {
        // Show the splash screen
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Full-screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        setContentView(R.layout.activity_intro)

        // Initialize Retrofit
        RetrofitInstance.initialize(this)

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up button listeners
        findViewById<Button>(R.id.loginbtn).setOnClickListener {
            navigateToLogin()
        }

        findViewById<Button>(R.id.signupbtn).setOnClickListener {
            navigateToSignup()
        }

        // Optional delay for splash screen duration
        Handler(Looper.getMainLooper()).postDelayed({
            // Logic to show main content can go here, if needed
        }, splashScreenDuration)
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish() // Call finish() to prevent returning to this activity
    }

    private fun navigateToSignup() {
        startActivity(Intent(this, SignupActivity::class.java))
        finish() // Call finish() to prevent returning to this activity
    }
}
