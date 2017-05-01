package com.artemkopan.movie.ui.detail

import android.annotation.SuppressLint
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import com.artemkopan.movie.R
import com.artemkopan.movie.data.entity.Video
import kotlinx.android.synthetic.main.item_trailer.view.*
import javax.inject.Inject

/**
 * Created by Artem Kopan for TheMovie
 * 01.05.2017
 */

class TrailersAdapter @Inject constructor() : PagerAdapter() {

    var items: List<Video>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    @SuppressLint("SetJavaScriptEnabled")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_trailer, container, false)
        view.webView.apply {
            settings.javaScriptEnabled = true
            setWebChromeClient(WebChromeClient())
            val mimeType = "text/html"
            val encoding = "UTF-8"
            val html = getHTML(items!![position].key ?: "")
            loadDataWithBaseURL("", html, mimeType, encoding, "")
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun isViewFromObject(view: View?, obj: Any?): Boolean = view == obj

    override fun getCount(): Int = if (items == null) 0 else items!!.size

    fun getHTML(key: String): String {
        val html = "<iframe class=\"youtube-player\" style=\"border: 0; width: 100%; height: 95%; padding:0px; margin:0px\" id=\"ytplayer\" type=\"text/html\" src=\"http://www.youtube.com/embed/$key?fs=0\" frameborder=\"0\">\n</iframe>\n"
        return html
    }
}