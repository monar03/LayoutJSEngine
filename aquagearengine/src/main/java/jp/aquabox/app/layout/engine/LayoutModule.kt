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

interface LayoutModule {
    fun update(key: String)
    fun tap(funcName: String, jsonStr: String)
    fun addListener(key: String, listener: DataListener)

    interface DataListener {
        fun onUpdate(data: JSONObject)
    }

    data class LayoutModuleData(
        val layoutStr: String,
        val designStr: String,
        val scriptStr: String
    )
}

open class LayoutModuleImpl(
    webView: WebView,
    name: String,
    data: LayoutModule.LayoutModuleData,
    onLoadListener: (v: View) -> Unit
) : LayoutModule {
    private val listenerMap: MutableMap<String, MutableList<LayoutModule.DataListener>> = mutableMapOf()
    private val command: AquagearCommand by lazy {
        AquagearCommand(name, webView)
    }

    init {
        command.load(data.scriptStr) { context: Context ->
            val renders = Compiler(
                getTagMap()
            ).compile(
                data.layoutStr,
                data.designStr
            )

            if (renders != null) {
                for (render: Render in renders) {
                    if (render is AquagearRender) {
                        val o = render.render(
                            context,
                            this@LayoutModuleImpl,
                            null
                        )
                        onLoadListener(o)
                    }
                }
            }
        }
    }

    override fun update(key: String) {
        command.update(key) {
            // XXX 変なエスケープ文字列をサニタイズする
            val reader = JsonReader(StringReader(it))
            reader.isLenient = true
            refresh(key, JSONObject(reader.nextString()))
        }
    }

    override fun tap(funcName: String, jsonStr: String) {
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

    override fun addListener(key: String, listener: LayoutModule.DataListener) {
        if (listenerMap.containsKey(key)) {
            listenerMap[key]?.add(listener)
        } else {
            listenerMap[key] = mutableListOf(listener)
        }
    }
}

