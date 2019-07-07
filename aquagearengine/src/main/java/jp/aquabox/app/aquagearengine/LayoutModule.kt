package jp.aquabox.app.aquagearengine

import android.util.JsonReader
import android.webkit.WebView
import jp.aquabox.app.aquagearengine.render.*
import jp.aquabox.layout.compiler.Compiler
import jp.aquabox.layout.compiler.render.compiler.Render
import org.json.JSONObject
import java.io.StringReader

class LayoutModule(private val webView: WebView) {
    private val listenerMap: MutableMap<String, MutableList<DataListener>> = mutableMapOf()
    private lateinit var name: String

    fun load(
        name: String,
        layoutStr: String,
        designStr: String,
        scriptStr: String,
        onLoadListener: JSEngine.JSViewLoadListener
    ) {
        this.name = name
        webView.evaluateJavascript("javascript:var $name = new Module(\"$name\", $scriptStr)") {
            it?.run {
                val renders = Compiler(
                    mapOf(
                        "view" to ViewRender::class.java,
                        "text" to TextRender::class.java,
                        "scroll-view" to ScrollViewRender::class.java,
                        "image" to ImageRender::class.java
                    )
                )
                    .compile(
                        layoutStr,
                        designStr
                    )

                if (renders != null) {
                    for (render: Render in renders) {
                        if (render is AquagearRender) {
                            val o = render.render(webView.context, this@LayoutModule, null)
                            onLoadListener.onViewLoadEnd(o)
                        }
                    }
                }
            }
        }
    }

    fun refresh(key: String, data: JSONObject) {
        listenerMap[key]?.map {
            it.onUpdate(data)
        }
    }

    fun addListener(key: String, listener: DataListener) {
        if (listenerMap.containsKey(key)) {
            listenerMap[key]?.add(listener)
        } else {
            listenerMap[key] = mutableListOf(listener)
        }
    }

    fun update(key: String) {
        webView.evaluateJavascript("javascript:${this.name}.getData('$key')") {
            it?.run {
                // XXX 変なエスケープ文字列をサニタイズする
                val reader = JsonReader(StringReader(this))
                reader.isLenient = true
                refresh(key, JSONObject(reader.nextString()))
            }
        }
    }

    fun tap(funcName: String, jsonStr: String) {
        webView.evaluateJavascript("javascript:${this.name}.onTap('$funcName', $jsonStr)", null)
    }

    interface DataListener {
        fun onUpdate(data: JSONObject)
    }
}