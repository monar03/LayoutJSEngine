package jp.aquabox.app.layout.engine

import android.content.Context
import android.util.JsonReader
import android.view.View
import android.webkit.WebView
import jp.aquabox.app.layout.engine.render.*
import jp.aquabox.layout.compiler.Compiler
import jp.aquabox.layout.compiler.render.compiler.Render
import org.json.JSONObject
import java.io.StringReader

open class LayoutModule(webView: WebView, name: String) {
    private val listenerMap: MutableMap<String, MutableList<DataListener>> = mutableMapOf()
    private val command: AquagearCommand = AquagearCommand(name, webView)

    fun load(
        layoutStr: String,
        designStr: String,
        scriptStr: String,
        onLoadListener: (v: View) -> Unit
    ) {

        command.load(scriptStr) { context: Context ->
            val renders = Compiler(
                getTagMap()
            ).compile(
                layoutStr,
                designStr
            )

            if (renders != null) {
                for (render: Render in renders) {
                    if (render is AquagearRender) {
                        val o = render.render(
                            context,
                            this@LayoutModule,
                            null
                        )
                        onLoadListener(o)
                    }
                }
            }
        }
    }

    fun update(key: String) {
        command.update(key) {
            // XXX 変なエスケープ文字列をサニタイズする
            val reader = JsonReader(StringReader(it))
            reader.isLenient = true
            refresh(key, JSONObject(reader.nextString()))
        }
    }

    fun tap(funcName: String, jsonStr: String) {
        command.tap(funcName, jsonStr)
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

    private fun refresh(key: String, data: JSONObject) {
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

    interface DataListener {
        fun onUpdate(data: JSONObject)
    }
}