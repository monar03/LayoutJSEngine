package jp.aquabox.app.layoutjsengine.jsengine

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.widget.Toast

class JsInterface(private val context: Context) {

    @JavascriptInterface
    fun request(url: String) {

    }

    @JavascriptInterface
    fun showToast(title: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, title, duration).show()
    }

    @JavascriptInterface
    fun loadData(str: String) {
        Log.d("aaa", "aaaa")
    }

}