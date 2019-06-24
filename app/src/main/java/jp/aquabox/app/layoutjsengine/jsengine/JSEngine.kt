package jp.aquabox.app.layoutjsengine.jsengine

import android.content.Context
import android.util.Log
import android.webkit.*
import jp.aquabox.app.layoutjsengine.JSEngineInterface
import jp.aquabox.app.layoutjsengine.jsengine.data.JSData
import java.io.IOException
import java.io.InputStream


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
        val input: InputStream
        try {
            input = this@JSEngine.context.assets.open(scriptFile)
            val buffer = ByteArray(input.available())
            input.read(buffer)
            input.close()

            val html = "<html>\n" +
                    "<head>\n" +
                    "    <script type=\"text/javascript\" src=\"Page.js\">\n" +
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
        evaluateJavascript("javascript:page.getData('$key');") {
            jsData.update(key, it)
        }
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