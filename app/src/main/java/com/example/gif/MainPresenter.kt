package com.example.gif

import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.gif.fragments.HotFragment
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainPresenter {

 fun GET(url:String): String {
     val myService: ExecutorService = Executors.newFixedThreadPool(2)
     val result = myService.submit(Callable {
         URL(url).readText()
     })
     return result.get()
}
    fun parseJson(str: String, gifs: ArrayList<Gifs>) {
        val json = JSONObject(str).getJSONArray("result")
        load(gifs, json)
    }
    private fun load(gifs: ArrayList<Gifs>, json: JSONArray) {
        for (i in 0 until json.length()) {
            val gif=Gifs()
            gif.id=json.getJSONObject(i).getString("id").toInt()
            gif.description=json.getJSONObject(i).getString("description").toString()
            gif.gifURL=json.getJSONObject(i).getString("gifURL").toString()
            gifs.add(gif)
        }
    }

fun createUrl(category:String, num:Int):String {
    return "https://developerslife.ru/$category/$num?json=true"
}

    fun show(gifs: ArrayList<Gifs>, cnt: Int, image: ImageView, view: Fragment) {
        Glide.with(view).asGif().
        load(toHttps(gifs[cnt].gifURL)).
        
        into(image);
    }
    private fun toHttps(http:String):String{
        return if (http[4]!='s') {val tmp=http.substringAfter(":")
            "https:$tmp"
        } else http
    }
}