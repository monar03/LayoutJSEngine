package jp.aquabox.app.layout.engine

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.webkit.*
import java.io.IOException

class AquagearEngine(context: Context, onLoadListener: (engine: AquagearEngine) -> Unit) {
    private val webView: WebView = WebView(context)
    private val modules: MutableMap<String, LayoutModule> = mutableMapOf()

    init {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                onLoadListener(this@AquagearEngine)
            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                Log.d("webview", consoleMessage?.message())
                return super.onConsoleMessage(consoleMessage)
            }

            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                Log.d("webview", message)
                return super.onJsAlert(view, url, message, result)
            }
        }
        webView.settings.run {
            this.javaScriptEnabled = true
        }

        addJSInterface(JsInterface(this), "aquagear")
        loadEngine()
    }


    private fun loadEngine() {
        try {
            val input = webView.context.assets.open("Module.js")
            val buffer = ByteArray(input.available())
            input.read(buffer)
            input.close()

            val html = "<html>\n" +
                    "<head>\n" +
                    "    <script src=\"file:///android_asset/Module.js\">\n" +
                    "    </script>\n" +
                    "</head>\n" +
                    "</html>"
            webView.loadDataWithBaseURL(
                "file:///android_asset",
                html,
                "text/html",
                "UTF-8",
                null
            )
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

    }

    @SuppressLint("JavascriptInterface")
    fun addJSInterface(jsInterface: AquagearEngineInterface, key: String) {
        webView.addJavascriptInterface(jsInterface, key)
    }

    fun loadModule(
        name: String,
        data: LayoutModule.LayoutModuleData,
        onLoadListener: (v: View) -> Unit
    ) {
        modules[name] = LayoutModuleImpl(
            AquagearCommandImpl(name, webView),
            data,
            onLoadListener
        )
    }

    fun update(name: String, key: String) {
        modules[name]?.update(key)
    }

    interface OnEngineInterface {
        fun getEngine(): AquagearEngine
    }
}

