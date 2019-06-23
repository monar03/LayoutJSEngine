package jp.aquabox.app.layoutjsengine.jsengine

import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient
import jp.aquabox.app.layoutjsengine.jsengine.data.JSData
import java.io.IOException
import java.io.InputStream


class JSEngine(context: Context?) : WebView(context) {
    val jsData: JSData = JSData()

    init {
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (view != null) {
                    //  injectScriptFile(view, "data.js")
                }
                loadData()
            }

            private fun injectScriptFile(view: WebView, scriptFile: String) {
                val input: InputStream
                try {
                    input = this@JSEngine.context.assets.open(scriptFile)
                    val buffer = ByteArray(input.available())
                    input.read(buffer)
                    input.close()
                    view.loadUrl(
                        "javascript:(function() {" +
                                "var parent = document.getElementsByTagName('head').item(0);" +
                                "var script = document.createElement('script');" +
                                "script.type = 'text/javascript';" +
                                // Tell the browser to BASE64-decode the string into your script !!!
                                "script.innerHTML = '" + String(buffer, Charsets.UTF_8) + "';" +
                                "parent.appendChild(script);" +
                                "})()"
                    )
                } catch (e: IOException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }

            }

        }
        settings.run {
            this.javaScriptEnabled = true
        }

        addJavascriptInterface(JsInterface(context!!), "aquagear")
    }


    fun loadHtml() {
        loadUrl("file:///android_asset/index.html")
    }

    fun loadData() {
        loadUrl("javascript:aquagear.loadData(JSON.stringify(page));")
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