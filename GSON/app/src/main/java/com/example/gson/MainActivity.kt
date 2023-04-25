package com.example.gson

import android.content.ClipData
import android.content.ClipboardManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.*
import timber.log.Timber
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private val url = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        setContentView(R.layout.activity_main)
        val recyclerView: RecyclerView = findViewById(R.id.rView)
        val gridLayoutManager = GridLayoutManager(this,2)

        Timber.plant(Timber.DebugTree())
        val listPhoto = arrayListOf<String>()

        Thread {
            val connection = URL(url).openConnection() as HttpURLConnection
            try {
                val data = connection.inputStream.bufferedReader().readText()
                val wrapper: Wrapper = Gson().fromJson(data, Wrapper::class.java)
                val page: PhotoPage = Gson().fromJson(wrapper.photos, PhotoPage::class.java)
                val photo = Gson().fromJson(page.photo, Array<Photo>::class.java).toList()

                for (i in photo.indices) {
                    if (i % 5 == 4) {
                        Timber.d(photo[i].toString())
                    }
                    listPhoto.add("https://farm${photo[i].farm}.staticflickr.com/${photo[i].server}/${photo[i].id}_${photo[i].secret}_z.jpg")
                }
                runOnUiThread {
                    recyclerView.setHasFixedSize(true)
                    recyclerView.layoutManager = gridLayoutManager
                    recyclerView.adapter = Adapter(this, listPhoto, this)
                }
            } catch (e: Exception) {
                Timber.e(e.toString())
            } finally {
                connection.disconnect()
            }
        }.start()
    }
    fun onCellClickListener(data: String){
        val clipboardmanager: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipBoard = ClipData.newPlainText("Link", data)
        clipboardmanager.setPrimaryClip(clipBoard)
        Timber.i(data)
    }
}
data class Photo(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    val ispublic: Int,
    val isfriend: Int,
    val isfamily: Int
)
data class PhotoPage(
    val page: Int,
    val pages: Int,
    val perpages: Int,
    val total: Int,
    val photo: JsonArray
)
data class Wrapper(
    val photos: JsonObject,
    val stat: String
)