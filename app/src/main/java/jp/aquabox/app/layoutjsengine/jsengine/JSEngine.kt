package jp.aquabox.app.layoutjsengine.jsengine

import android.content.Context
import android.util.JsonReader
import android.util.Log
import android.webkit.*
import jp.aquabox.app.layoutjsengine.JSEngineInterface
import jp.aquabox.app.layoutjsengine.jsengine.data.JSData
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.io.StringReader


class JSEngine(context: Context?) : WebView(context) {
    val jsData: JSData = JSData()

    init {

        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (context is JSEngineInterface) {
                    context.onPageFinished()
                }
            }
        }
        webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                Log.d("webview", consoleMessage?.message())
                return super.onConsoleMessage(consoleMessage)
            }

            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                Log.d("webview", message)
                return super.onJsAlert(view, url, message, result)
            }
        }
        settings.run {
            this.javaScriptEnabled = true
        }

        addJavascriptInterface(JsInterface(context!!), "aquagear")
    }

    fun loadJS(scriptFile: String) {
        try {
            val input: InputStream = this@JSEngine.context.assets.open(scriptFile)
            val buffer = ByteArray(input.available())
            input.read(buffer)
            input.close()

            val input1 = this@JSEngine.context.assets.open("Page.js")
            val buffer1 = ByteArray(input1.available())
            input1.read(buffer1)
            input1.close()

            val html = "<html>\n" +
                    "<head>\n" +
                    "    <script>\n" +
                    "        \n" + String(buffer1, Charsets.UTF_8) +
                    "        \n page = new " + String(buffer, Charsets.UTF_8) +
                    "    </script>\n" +
                    "</head>\n" +
                    "</html>"
            loadDataWithBaseURL(
                "file:///android_asset",
                html,
                "text/html",
                "UTF-0",
                null
            )
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

    }

    fun update(key: String) {
        evaluateJavascript("javascript:page.getData('$key')") {
            it?.run {
                // XXX 変なエスケープ文字列をサニタイズする
                val reader = JsonReader(StringReader(this))
                reader.isLenient = true
                jsData.update(key, JSONObject(reader.nextString()))
            }
        }
    }

    fun tap(funcName: String, jsonStr: String) {
        evaluateJavascript("javascript:page.onTap('$funcName', $jsonStr)", null)
    }

    fun onLaunch() {
        loadUrl("javascript:App.onLaunch()")
    }

    fun onShow() {
        loadUrl("javascript:App.onShow()")
    }

    fun onHide() {
        loadUrl("javascript:App.onHide()")
    }

    fun onError() {
        loadUrl("javascript:App.onError()")
    }
}