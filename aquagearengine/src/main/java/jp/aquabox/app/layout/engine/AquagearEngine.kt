package jp.aquabox.app.layout.engine

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.VisibleForTesting
import android.util.Log
import android.view.View
import android.webkit.*
import jp.aquabox.app.layout.engine.render.*
import jp.aquabox.layout.compiler.Compiler
import jp.aquabox.layout.compiler.render.RenderCreator
import jp.aquabox.layout.compiler.render.compiler.Render
import java.io.IOException

open class AquagearEngine(
    context: Context,
    private val creator: RenderCreator,
    onLoadListener: (engine: AquagearEngine) -> Unit
) {
    private val webView: WebView = WebView(context)

    @VisibleForTesting
    val modules: MutableMap<String, LayoutModule> = mutableMapOf()

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
                    "    <script>\n" +
                    String(buffer) +
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
    private fun addJSInterface(jsInterface: AquagearEngineInterface, key: String) {
        webView.addJavascriptInterface(jsInterface, key)
    }

    fun loadModule(
        name: String,
        data: LayoutModule.LayoutModuleData,
        onLoadEndListener: (View) -> Unit
    ) {
        val module = LayoutModuleImpl(
            AquagearCommandImpl(name, webView),
            data
        ) {
            val renders = Compiler(
                creator
            ).compile(
                data.layoutStr,
                data.designStr
            )

            if (renders != null) {
                for (render: Render in renders) {
                    if (render is AquagearRender) {
                        val o = render.render(
                            webView.context,
                            it,
                            null
                        )
                        onLoadEndListener(o)
                    }
                }
            }
        }
        modules[name] = module
    }

    fun update(name: String, key: String) {
        modules[name]?.update(key)
    }

    interface OnEngineInterface {
        fun getEngine(): AquagearEngine
    }

    class AquagearEngineRenderCreator : RenderCreator() {
        override fun create(tagStr: String?): Render {
            when (tagStr) {
                "text" -> return TextRender()
                "scroll-view" -> return ScrollViewRender()
                "image" -> return ImageRender()
                "grid" -> return GridRender()
            }

            return ViewRender()
        }
    }
}

