package jp.aquabox.app.layoutjsengine.jsengine.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import jp.aquabox.app.layoutjsengine.jsengine.JSEngine
import jp.aquabox.app.layoutjsengine.jsengine.data.DataListener
import jp.aquabox.app.layoutjsengine.jsengine.render.TextRender
import jp.aquabox.app.layoutjsengine.jsengine.render.ViewRender
import jp.aquagear.layout.compiler.render.compiler.Render
import jp.aquagear.layout.compiler.render.lexer.result.StringVariable
import jp.aquagear.layout.compiler.render.lexer.result.Type
import org.json.JSONObject

class AquagearScrollViewLayout(context: Context) : ScrollView(context) {
    private var params: Map<String, StringVariable.Parameter>? = null
    private var styles: Map<String, String>? = null
    private var templateRenders: List<Render>? = null
    private var jsonObject: JSONObject? = null

    fun set(
        params: Map<String, StringVariable.Parameter>,
        styles: Map<String, String>,
        jsonObject: JSONObject?
    ) {
        this.params = params
        this.styles = styles
        this.jsonObject = jsonObject

        setEvent()
        setBlockDesign()
    }

    fun setTemplateRender(renders: List<Render>) {
        templateRenders = renders
    }

    private fun setEvent() {
        params?.get("tap")?.let {
            if (context is JSEngine.JSEngineInterface) {
                setOnClickListener { _ ->
                    (this.context as JSEngine.JSEngineInterface).getEngine().tap(it.value, jsonObject.toString())
                }
            }
        }

        params?.get("for")?.let {
            when (it.type) {
                Type.VARIABLE -> {
                    if (this.context is JSEngine.JSEngineInterface) {
                        (context as JSEngine.JSEngineInterface).getEngine().run {
                            setDataListener(it)
                            update(it.value)
                        }
                    }
                }
                else -> {

                }
            }
        }

    }

    private fun JSEngine.setDataListener(it: StringVariable.Parameter) {
        jsData.addListener(
            it.value,
            object : DataListener {
                override fun onUpdate(data: JSONObject) {
                    this@AquagearScrollViewLayout.removeAllViews()
                    val jsons = data.getJSONArray(it.value)
                    for (i in 0 until jsons.length()) {
                        templateRenders?.map { render ->
                            when (render) {
                                is ViewRender -> {
                                    val v = render.render(context, jsons.getJSONObject(i)) as View
                                    this@AquagearScrollViewLayout.addView(v)
                                }
                                is TextRender -> {
                                    val v = render.render(context, jsons.getJSONObject(i)) as View
                                    this@AquagearScrollViewLayout.addView(v)
                                }

                            }
                        }
                    }
                }
            }
        )
    }

    private fun setBlockDesign() {
        styles?.get("padding")?.let {
            setPadding(
                it.toInt(),
                it.toInt(),
                it.toInt(),
                it.toInt()
            )
        }

        val width = styles?.get("width")?.toInt() ?: ViewGroup.LayoutParams.WRAP_CONTENT
        val height = styles?.get("height")?.toInt() ?: ViewGroup.LayoutParams.WRAP_CONTENT

        layoutParams = ViewGroup.LayoutParams(width, height)
    }
}