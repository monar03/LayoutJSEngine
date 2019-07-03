package jp.aquabox.app.layoutjsengine.jsengine.view

import android.content.Context
import android.widget.LinearLayout
import android.widget.ScrollView
import jp.aquabox.app.layoutjsengine.jsengine.JSEngine
import jp.aquabox.app.layoutjsengine.jsengine.data.DataListener
import jp.aquabox.app.layoutjsengine.jsengine.render.AquagearRender
import jp.aquagear.layout.compiler.render.compiler.Render
import jp.aquagear.layout.compiler.render.lexer.result.StringVariable
import jp.aquagear.layout.compiler.render.lexer.result.Type
import org.json.JSONObject

class AquagearScrollLayout(context: Context) : ScrollView(context), AquagearViewInterface {
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
            setTapEvent(this@AquagearScrollLayout, it.value, jsonObject)
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
                    this@AquagearScrollLayout.removeAllViews()
                    val jsons = data.getJSONArray(it.value)
                    val block = LinearLayout(context).apply {
                        orientation = LinearLayout.HORIZONTAL
                    }

                    for (i in 0 until jsons.length()) {
                        templateRenders?.map { render ->
                            when (render) {
                                is AquagearRender -> {
                                    val v = render.render(context, jsons.getJSONObject(i))
                                    block.addView(v)
                                }
                            }
                        }
                    }
                    this@AquagearScrollLayout.addView(block)
                }
            }
        )
    }

    private fun setBlockDesign() {
        setDesign(styles, this)
    }
}
