package jp.aquabox.app.layoutjsengine.jsengine

import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient

class JSEngine(context: Context?) : WebView(context) {
    init {
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loadEnd()
            }
        }
        settings.run {
            this.javaScriptEnabled = true
        }

        addJavascriptInterface(JsInterface(context!!), "aquagear")
    }

    fun loadJS(js: String) {
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

    private fun loadEnd() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}