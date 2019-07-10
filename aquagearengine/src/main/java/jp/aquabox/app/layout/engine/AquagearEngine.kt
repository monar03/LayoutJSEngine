package jp.aquabox.app.layout.engine

import android.content.Context
import android.util.Log
import android.view.View
import android.webkit.*
import java.io.IOException

class AquagearEngine(context: Context?, onLoadListener: OnLoadListener) {
    private val webView: WebView = WebView(context)
    private val modules: MutableMap<String, LayoutModule> = mutableMapOf()

    init {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                onLoadListener.onReady()
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

        context?.let {
            webView.addJavascriptInterface(JsInterface(context), "aquagear")
        }

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
                "UTF-0",
                null
            )
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

    }

    fun loadModule(
        name: String,
        layoutStr: String,
        designStr: String,
        scriptStr: String,
        onLoadListener: OnViewLoadListener
    ) {
        val module = LayoutModule(webView, name)
        module.load(
            layoutStr,
            designStr,
            scriptStr,
            onLoadListener
        )
        modules[name] = module
    }

    fun update(name: String, key: String) {
        modules[name]?.update(key)
    }

    abstract class OnLoadListener {
        open fun onReady() {}
    }

    interface OnViewLoadListener {
        fun onViewLoadEnd(v: View)
    }

    interface OnEngineInterface {
        fun getEngine(): AquagearEngine
    }
}
