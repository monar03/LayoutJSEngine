package jp.aquabox.app.layout.engine

import android.os.Handler
import android.webkit.JavascriptInterface

class JsInterface(private val engine: AquagearEngine) : AquagearEngineInterface {
    val hanlder: Handler = Handler()

    @JavascriptInterface
    fun update(name: String, key: String) {
        hanlder.post {
            engine.update(name, key)
        }
    }
}