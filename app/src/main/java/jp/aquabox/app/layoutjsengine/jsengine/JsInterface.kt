package jp.aquabox.app.layoutjsengine.jsengine

import android.content.Context
import android.os.Handler
import android.webkit.JavascriptInterface
import android.widget.Toast
import jp.aquabox.app.layoutjsengine.JSEngineInterface

class JsInterface(private val context: Context) {
    val hanlder: Handler = Handler()

    @JavascriptInterface
    fun request(url: String) {

    }

    @JavascriptInterface
    fun showToast(title: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, title, duration).show()
    }

    @JavascriptInterface
    fun update(key: String) {
        hanlder.post {
            if (context is JSEngineInterface) {
                context.getEngine().update(key)
            }
        }
    }

}