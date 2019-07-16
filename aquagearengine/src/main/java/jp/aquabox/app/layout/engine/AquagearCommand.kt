package jp.aquabox.app.layout.engine

import android.content.Context
import android.webkit.WebView

class AquagearCommand(private val name: String, private val webView: WebView) {
    fun load(script: String, callback: (context: Context) -> Unit) {
        webView.evaluateJavascript("javascript:var $name = new Module(\"$name\", $script)") {
            callback(webView.context)
        }
    }

    fun update(key: String, callback: (str: String) -> Unit) {
        webView.evaluateJavascript("javascript:$name.getData('$key')") {
            callback(it)
        }
    }

    fun tap(funcName: String, jsonStr: String) {
        webView.evaluateJavascript("javascript:$name.onTap('$funcName', $jsonStr)", null)
    }
}

