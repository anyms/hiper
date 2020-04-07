package app.spidy.hiperexample

import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import app.spidy.hiper.Hiper
import app.spidy.kookaburra.controllers.PermissionHandler
import java.io.File
import java.io.OutputStream
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val hiper = Hiper.getSyncInstance()

        thread {
            val response = hiper.head("https://httpbin.org/basic-auth/anyms/rockjeev", username = "anys", password = "rockjeev",
                cookies = hashMapOf("name" to "jeeva"))
            Log.d("hello", response.statusCode.toString())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PermissionHandler.STORAGE_PERMISSION_CODE ||
            requestCode == PermissionHandler.LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                PermissionHandler.execute()
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
