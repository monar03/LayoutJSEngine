package jp.aquabox.app.layout.engine.view

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import jp.aquabox.app.layout.engine.JSEngine
import jp.aquabox.app.layout.engine.LayoutModule
import jp.aquabox.app.layout.engine.render.AquagearRender
import jp.aquabox.layout.compiler.render.compiler.Render
import jp.aquabox.layout.compiler.render.lexer.result.StringVariable
import jp.aquabox.layout.compiler.render.lexer.result.Type
import org.json.JSONObject

class AquagearLayout(context: Context?) : LinearLayout(context), AquagearViewInterface {
    private lateinit var module: LayoutModule
    private var params: Map<String, StringVariable.Parameter>? = null
    private var styles: Map<String, String>? = null
    private var templateRenders: List<Render>? = null
    private var jsonObject: JSONObject? = null

    fun set(
        module: LayoutModule,
        params: Map<String, StringVariable.Parameter>,
        styles: Map<String, String>,
        jsonObject: JSONObject?
    ) {
        this.module = module
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
            setTapEvent(this@AquagearLayout, it.value, module, jsonObject)
        }

        params?.get("for")?.let {
            when (it.type) {
                Type.VARIABLE -> {
                    if (this.context is JSEngine.JSEngineInterface) {
                        (context as JSEngine.JSEngineInterface).getEngine().run {
                            setDataListener(it)
                            module.update(it.value)
                        }
                    }
                }
            }
        }

    }

    private fun setDataListener(it: StringVariable.Parameter) {
        module.addListener(
            it.value,
            object : LayoutModule.DataListener {
                override fun onUpdate(data: JSONObject) {
                    this@AquagearLayout.removeAllViews()
                    val jsons = data.getJSONArray(it.value)
                    for (i in 0 until jsons.length()) {
                        templateRenders?.map { render ->
                            var v: View? = null
                            when (render) {
                                is AquagearRender -> v = render.render(
                                    context,
                                    module, jsons.getJSONObject(i)
                                )
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