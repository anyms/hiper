package app.spidy.hiperexample

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import app.spidy.hiper.controllers.Hiper
import app.spidy.kookaburra.controllers.PermissionHandler
import java.io.File
import java.io.RandomAccessFile

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val hiper = Hiper()
        PermissionHandler.requestStorage(this, "") {
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = RandomAccessFile("${dir.absolutePath}${File.separator}hello.png", "rw")
            var sizeWritten = 0L
            file.seek(sizeWritten)
            hiper.get("https://httpbin.org/image", isStream = true)
                .addHeader("user-agent", "hello")
                .addArg("name", "jeeva")
                .addFormData("age", 24)
                .ifException {
                    Log.d("hello", "Err: $it")
                }
                .ifFailed {
                    Log.d("hello", "Failed: $it")
                }
                .ifFailedOrException {
                    Log.d("hello", "Failed or Exception")
                }
                .ifStream { buffer, byteSize ->
                    if (buffer != null) {
                        file.write(buffer, 0, byteSize)
                    } else {
                        Log.d("hello", "File written")
                        file.close()
                    }
                }
                .finally {
                    Log.d("hello", it.text.toString())
                }
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
