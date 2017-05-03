package com.artemkopan.movie.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.support.annotation.StringRes
import android.text.SpannableStringBuilder
import com.artemkopan.utils.fonts.CustomTypefaceSpan
import com.artemkopan.widget.fonts.FontUtils


/**
 * Created by Artem Kopan for TheMovie
 * 01.05.2017
 */


object CommonUtils {

    @JvmStatic
    fun applyTypeface(context: Context, @StringRes value: Int, textStyle: Int = Typeface.NORMAL): CharSequence {
        return applyTypeface(context, context.getString(value), textStyle)
    }

    @JvmStatic
    fun applyTypeface(context: Context, value: CharSequence, textStyle: Int = Typeface.NORMAL): CharSequence {
        val builder = SpannableStringBuilder(value)
        builder.setSpan(CustomTypefaceSpan(FontUtils.selectTypeface(context, textStyle)),
                        0,
                        builder.length,
                        SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE)
        return builder
    }


    /**
     * Open Youtube app for playing video. If application wasn't found then open browser
     */
    @JvmStatic
    fun openYoutubeVideo(context: Context, id: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id))
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id))
        appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        webIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(webIntent)
        }
    }

}