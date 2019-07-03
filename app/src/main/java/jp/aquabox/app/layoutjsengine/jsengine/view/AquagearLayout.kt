package jp.aquabox.app.layoutjsengine.jsengine.view

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import jp.aquabox.app.layoutjsengine.jsengine.JSEngine
import jp.aquabox.app.layoutjsengine.jsengine.data.DataListener
import jp.aquabox.app.layoutjsengine.jsengine.render.AquagearRender
import jp.aquagear.layout.compiler.render.compiler.Render
import jp.aquagear.layout.compiler.render.lexer.result.StringVariable
import jp.aquagear.layout.compiler.render.lexer.result.Type
import org.json.JSONObject

class AquagearLayout(context: Context?) : LinearLayout(context), AquagearViewInterface {
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
            setTapEvent(this@AquagearLayout, it.value, jsonObject)
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
            }
        }

    }

    private fun JSEngine.setDataListener(it: StringVariable.Parameter) {
        jsData.addListener(
            it.value,
            object : DataListener {
                override fun onUpdate(data: JSONObject) {
                    this@AquagearLayout.removeAllViews()
                    val jsons = data.getJSONArray(it.value)
                    for (i in 0 until jsons.length()) {
                        templateRenders?.map { render ->
                            var v: View? = null
                            when (render) {
                                is AquagearRender -> v = render.render(context, jsons.getJSONObject(i))
                            }
                            if (v != null) {
                                this@AquagearLayout.addView(v)
                            }
                        }
                    }
                }
            }
        )
    }

    private fun setBlockDesign() {
        setDesign(styles, this)
        styles?.get("orientation")?.let {
            when (it) {
                "horizon" -> {
                    orientation = HORIZONTAL
                }
                else -> {
                    orientation = VERTICAL
                }
            }
        }
    }
}