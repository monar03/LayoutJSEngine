package jp.aquabox.app.layout.engine

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.webkit.JavascriptInterface
import android.widget.Toast

class JsInterface(private val context: Context) {
    val hanlder: Handler = Handler()

    @JavascriptInterface
    fun request(url: String) {

    }

    @JavascriptInterface
    fun navigateTo(uri: String) {
    }

    @JavascriptInterface
    fun back() {
        if (context is Activity) {
            context.onBackPressed()
        }
    }

    @JavascriptInterface
    fun showToast(title: String) {
        Toast.makeText(context, title, Toast.LENGTH_LONG).show()
    }

    @JavascriptInterface
    fun update(name: String, key: String) {
        hanlder.post {
            if (context is JSEngine.JSEngineInterface) {
                context.getEngine().update(name, key)
            }
        }
    }

}