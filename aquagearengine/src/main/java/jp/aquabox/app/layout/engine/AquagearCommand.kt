package jp.aquabox.app.layout.engine

import android.webkit.WebView

interface AquagearCommand {
    fun load(script: String, callback: () -> Unit);
    fun update(key: String, callback: (str: String) -> Unit);
    fun tap(funcName: String, jsonStr: String);

}

open class AquagearCommandImpl(private val name: String, private val webView: WebView) : AquagearCommand {
    override fun load(script: String, callback: () -> Unit) {
        webView.evaluateJavascript("javascript:var $name = new Module(\"$name\", $script)") {
            callback()
        }
    }

    override fun update(key: String, callback: (str: String) -> Unit) {
        webView.evaluateJavascript("javascript:$name.getData('$key')") {
            callback(it)
        }
    }

    override fun tap(funcName: String, jsonStr: String) {
        webView.evaluateJavascript("javascript:$name.onTap('$funcName', $jsonStr)", null)
    }
}

