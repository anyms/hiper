package app.spidy.hiperexample

import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import app.spidy.hiper.controllers.Hiper
import app.spidy.kookaburra.controllers.PermissionHandler
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val hiper = Hiper()
//
//
//        val resolver = contentResolver
//        val contentValues = ContentValues().apply {
//            put(MediaStore.MediaColumns.DISPLAY_NAME, "CuteKitten001")
//            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//            put(MediaStore.MediaColumns.RELATIVE_PATH, "Download/PerracoLabs")
//        }
//        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
//        val outputStream: OutputStream? = if (uri == null) null else resolver.openOutputStream(uri)
//
//        hiper.get("https://httpbin.org/image", isStream = true)
//            .ifException {
//                 Log.d("hello", "Exception: ${it?.message}")
//             }
//            .ifFailed {
//                Log.d("hello", "Failed")
//            }
//            .ifStream { buffer, byteSize ->
//                if (buffer == null) {
//                    outputStream?.flush()
//                    outputStream?.close()
//                    Log.d("hello", "Done.")

//                    var attachmentUri: Uri? = null
//                    if (ContentResolver.SCHEME_FILE == uri!!.scheme && uri.path != null) {
//                        val file = File(uri.path!!)
//                        attachmentUri = FileProvider.getUriForFile(this@MainActivity, "com.freshdesk.helpdesk.provider", file)
//                    }
//
//                    val openAttachmentIntent = Intent(Intent.ACTION_VIEW)
//                    openAttachmentIntent.setDataAndType(attachmentUri, "image/jpeg")
//                    openAttachmentIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//                    try {
//                        startActivity(openAttachmentIntent)
//                    } catch (e: ActivityNotFoundException) {
//                        Log.d("hello", "Unable to open the file")
//                    }

//                } else {
//                    outputStream?.write(buffer, 0, byteSize)
//                }
//            }
//            .finally {
//                Log.d("hello", it.text.toString())
//            }



        val hiperLegacy = Hiper().Legacy()

        thread {
            val resp = hiperLegacy.post("https://httpbin.org/post",
                headers = hashMapOf("user-agent" to "hello1"), args = hashMapOf("name" to "jeeva"),
                formData = hashMapOf("age" to 25))
            Log.d("hello", resp.text.toString())
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
