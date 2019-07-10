package jp.aquabox.app.layout.engine

import android.util.JsonReader
import android.webkit.WebView
import jp.aquabox.app.layout.engine.render.*
import jp.aquabox.layout.compiler.Compiler
import jp.aquabox.layout.compiler.render.compiler.Render
import org.json.JSONObject
import java.io.StringReader

open class LayoutModule(private val webView: WebView, val name: String) {
    private val listenerMap: MutableMap<String, MutableList<DataListener>> = mutableMapOf()

    fun load(
        layoutStr: String,
        designStr: String,
        scriptStr: String,
        onLoadListener: AquagearEngine.OnViewLoadListener
    ) {
        webView.evaluateJavascript("javascript:var $name = new Module(\"$name\", $scriptStr)") {
            it?.run {
                val renders = Compiler(
                    getTagMap()
                ).compile(
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

    open fun getTagMap(): Map<String, Class<out AquagearRender>> {
        return mapOf(
            "view" to ViewRender::class.java,
            "text" to TextRender::class.java,
            "scroll-view" to ScrollViewRender::class.java,
            "image" to ImageRender::class.java,
            "grid" to GridRender::class.java
        )
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