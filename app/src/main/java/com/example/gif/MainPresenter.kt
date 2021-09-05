package com.example.gif

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainPresenter {
    private lateinit var image: ImageView
    private lateinit var textView: TextView
    private lateinit var buttons: RelativeLayout
    private lateinit var view:Fragment

    fun setParam( image: ImageView, textView: TextView, buttons: RelativeLayout, view:Fragment) {
        this.image=image
        this.textView=textView
        this.buttons=buttons
        this.view=view
    }

    fun GET(url:String, context: Context?): String {
     if (isInternetAvailable(context)) {
        val myService: ExecutorService = Executors.newFixedThreadPool(2)
        val result = myService.submit(Callable {
         URL(url).readText()
        })
        return result.get()
     }
     return ""
}
    private fun isInternetAvailable(context: Context?): Boolean {
        var result = false
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return result
    }

    fun parseJson(str: String, gifs: ArrayList<Gifs>, mode:Int) {
        if (str!="") {
            when (mode){
                3->{
                    val json = JSONObject(str)
                    load(gifs, json)
                }
                else-> {
                    val json = JSONObject(str).getJSONArray("result")
                    load(gifs, json)
                }
            }
        }
    }

    private fun load(gifs: ArrayList<Gifs>, json: JSONObject) {
        val gif=Gifs()
        gif.id=json.getString("id").toInt()
        gif.description=json.getString("description").toString()
        gif.gifURL=json.getString("gifURL").toString()
        gifs.add(gif)
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

    @SuppressLint("CheckResult")
    fun show(gifs: ArrayList<Gifs>, cnt: Int) {
        if (gifs.isNotEmpty()) {
            Glide.with(view).
            asGif().
            load(toHttps(gifs[cnt].gifURL)).
            placeholder(R.drawable.loading).
            error(R.drawable.warning).
            circleCrop().
            into(image)

            textView.text=gifs[cnt].description
            buttons.visibility= View.VISIBLE
        }
        else{
            image.setImageResource(R.drawable.warning)
            textView.setText(R.string.error)
            buttons.visibility= View.GONE
        }
    }

    private fun toHttps(http:String):String{
        if (http!=""){
            return if (http[4]!='s') {val tmp=http.substringAfter(":")
               "https:$tmp"
            } else http
        }
        return ""
    }
}