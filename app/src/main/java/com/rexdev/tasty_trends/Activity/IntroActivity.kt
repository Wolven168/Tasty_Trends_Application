package com.rexdev.tasty_trends.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rexdev.tasty_trends.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Show the splash screen
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Optional: Full-screen mode
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        setContentView(R.layout.activity_intro)

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up button listeners
        val loginbtn = findViewById<Button>(R.id.loginbtn)
        loginbtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Call finish() to prevent returning to this activity
        }

        val signupbtn = findViewById<Button>(R.id.signupbtn)
        signupbtn.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish() // Call finish() to prevent returning to this activity
        }

        // Remove the automatic transition to LoginActivity
        // If you want to keep the splash screen for a certain duration, you can do that here
        Handler(Looper.getMainLooper()).postDelayed({
            // Here, you can optionally set up logic to display the main content
            // For example, showing a welcome message or transition effect
        }, 1000) // Adjust the duration as needed
    }
}