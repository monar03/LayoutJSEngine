package jp.aquabox.app.layoutjsengine.jsengine

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.webkit.JavascriptInterface
import android.widget.Toast
import jp.aquabox.app.layoutjsengine.MainActivity

class JsInterface(private val context: Context) {
    val hanlder: Handler = Handler()

    @JavascriptInterface
    fun request(url: String) {

    }

    @JavascriptInterface
    fun navigateTo(uri: String) {
        context.startActivity(
            Intent(context, MainActivity::class.java).apply {
                putExtra("url", uri)
            }
        )
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