package jp.aquabox.app.layoutjsengine.jsengine

import android.content.Context
import android.util.JsonReader
import android.util.Log
import android.view.View
import android.webkit.*
import jp.aquabox.app.layoutjsengine.jsengine.data.JSData
import jp.aquabox.app.layoutjsengine.jsengine.render.ScrollViewRender
import jp.aquabox.app.layoutjsengine.jsengine.render.TextRender
import jp.aquabox.app.layoutjsengine.jsengine.render.ViewRender
import jp.aquagear.layout.compiler.Compiler
import jp.aquagear.layout.compiler.render.compiler.Render
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.io.StringReader

class JSEngine(context: Context?, onLoadListener: JSLoadListener) {
    val jsData: JSData = JSData()
    val webView: WebView = WebView(context)
    lateinit var module: String

    init {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                val layoutStr = loadFile(module + "/index.vxml")
                val designStr = loadFile(module + "/index.vcss")
                val renders: List<Render>? = Compiler(
                    mapOf(
                        "view" to ViewRender::class.java,
                        "text" to TextRender::class.java,
                        "scroll-view" to ScrollViewRender::class.java
                    )
                ).compile(
                    layoutStr,
                    designStr
                )

                if (renders != null) {
                    // TODO あとで継承で整理
                    for (render: Render in renders) {
                        if (render is ViewRender) {
                            val o = render.render(this@JSEngine.webView.context, null)
                            onLoadListener.onLoadFinish(o)
                        } else if (render is ScrollViewRender) {
                            val o = render.render(this@JSEngine.webView.context, null)
                            onLoadListener.onLoadFinish(o)
                        }
                    }
                }
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
    }

    fun load(module: String?) {
        this.module = module ?: "index"
        loadJS(this.module + "/index.js")
    }

    private fun loadFile(scriptFile: String): String {
        val input: InputStream = webView.context.assets.open(scriptFile)
        val buffer = ByteArray(input.available())
        input.read(buffer)
        input.close()

        return String(buffer, Charsets.UTF_8)
    }

    private fun loadJS(scriptFile: String) {
        try {
            val input: InputStream = webView.context.assets.open(scriptFile)
            val buffer = ByteArray(input.available())
            input.read(buffer)
            input.close()

            val input1 = webView.context.assets.open("Page.js")
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

    fun update(key: String) {
        webView.evaluateJavascript("javascript:page.getData('$key')") {
            it?.run {
                // XXX 変なエスケープ文字列をサニタイズする
                val reader = JsonReader(StringReader(this))
                reader.isLenient = true
                jsData.update(key, JSONObject(reader.nextString()))
            }
        }
    }

    fun tap(funcName: String, jsonStr: String) {
        webView.evaluateJavascript("javascript:page.onTap('$funcName', $jsonStr)", null)
    }

    fun onLaunch() {
        webView.loadUrl("javascript:App.onLaunch()")
    }

    fun onShow() {
        webView.loadUrl("javascript:App.onShow()")
    }

    fun onHide() {
        webView.loadUrl("javascript:App.onHide()")
    }

    fun onError() {
        webView.loadUrl("javascript:App.onError()")
    }

    interface JSLoadListener {
        fun onLoadFinish(v: View)
    }

    interface JSEngineInterface {
        fun getEngine(): JSEngine
    }
}

