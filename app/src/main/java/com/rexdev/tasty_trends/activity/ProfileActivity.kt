package com.rexdev.tasty_trends.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.rexdev.tasty_trends.R
import com.rexdev.tasty_trends.dataClass.UpdateUser
import com.rexdev.tasty_trends.domain.RetrofitInstance
import com.rexdev.tasty_trends.global.GlobalVariables
import com.rexdev.tasty_trends.tools.Tool
import com.rexdev.tasty_trends.utils.FileUploadManager
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var uploadImg: ImageView
    private lateinit var enterName: TextInputEditText
    private lateinit var enterStudentId: TextInputEditText
    private lateinit var enterPhoneNum: TextInputEditText
    private lateinit var confirmEditBtn: Button
    private lateinit var fileUploadManager: FileUploadManager
    private val app = GlobalVariables
    private val tool = Tool

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        uploadImg = findViewById(R.id.ivUploadImg)
        enterName = findViewById(R.id.tieEnterUserName)
        enterStudentId = findViewById(R.id.tieEnterStudentNum)
        enterPhoneNum = findViewById(R.id.tieEnterContactNum)
        confirmEditBtn = findViewById(R.id.btnEditProfile)

        var user_image : String? = null
        var user_name : String? = null
        var student_num : String? = null
        var phone_num : String? = null

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish() // Finish current activity and return to the previous one
        }

        // Initialize FileUploadManager
        fileUploadManager = FileUploadManager(this)

        // Set click listener to upload image
        uploadImg.setOnClickListener {
            fileUploadManager.openFilePicker { imageUrl ->
                if (imageUrl != null) {
                    user_image = imageUrl
                } else {
                    tool.showSnackbar(findViewById(R.id.main), "Image failed to upload")
                }
            }
        }
        user_name = enterName.text.toString()
        student_num = enterStudentId.text.toString()
        phone_num = enterPhoneNum.text.toString()

        confirmEditBtn.setOnClickListener {
            val user_name = enterName.text.toString()
            val student_num = enterStudentId.text.toString()
            val phone_num = enterPhoneNum.text.toString()
            updateProfile(user_name, student_num, phone_num, user_image)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fileUploadManager.handleActivityResult(requestCode, resultCode, data)
    }

    private fun updateProfile(name: String?, sdNum: String?, phNum: String?, image: String?) {
        lifecycleScope.launch {
            try {
                val response = app.PROFILE_ID?.let {
                    RetrofitInstance.api.updateUser(
                        user_id = it,

                        UpdateUser(
                            user_name = name,
                            email = null,
                            password = null,
                            shop_id = null,
                            student_num = sdNum,
                            phone_num = phNum,
                            user_image = image,
                            favorites = null,
                        ),
                    )
                }

                if (response != null) {
                    if (response.success == true) {
                        app.PROFILE_NAME = name
                        app.PROFILE_IMG = image
                        finish()
                    }
                }
            } catch (e: Exception) {
                tool.showSnackbar(findViewById(R.id.main), "Failed to update profile")
            }
        }

    }
}
