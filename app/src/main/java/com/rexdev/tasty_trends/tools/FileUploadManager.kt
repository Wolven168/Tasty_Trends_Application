package com.rexdev.tasty_trends.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.result.ActivityResultLauncher
import com.rexdev.tasty_trends.domain.RetrofitInstance
import java.io.File

class FileUploadManager(private val activity: Activity) {

    private var fileUploadCallback: ((String?) -> Unit)? = null

    fun openFilePicker(fileUploadCallback: (String?) -> Unit) {
        this.fileUploadCallback = fileUploadCallback

        // Use ActivityResultLauncher for better lifecycle handling
        val filePickerIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        activity.startActivityForResult(filePickerIntent, PICK_IMAGE_REQUEST)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            imageUri?.let {
                val file = uriToFile(it)
                file?.let { selectedFile ->
                    uploadImage(selectedFile)
                }
            }
        }
    }

    private fun uploadImage(imageFile: File) {
        RetrofitInstance.uploadImageToImgur(imageFile) { imageUrl ->
            fileUploadCallback?.invoke(imageUrl)
        }
    }

    private fun uriToFile(uri: Uri): File? {
        val cursor = activity.contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            it.moveToFirst()
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val fileName = it.getString(index)
            val file = File(activity.cacheDir, fileName)
            activity.contentResolver.openInputStream(uri)?.use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            }
            file
        }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
