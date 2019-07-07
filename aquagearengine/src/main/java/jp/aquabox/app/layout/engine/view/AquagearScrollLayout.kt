package jp.aquabox.app.layout.engine.view

import android.content.Context
import android.widget.LinearLayout
import android.widget.ScrollView
import jp.aquabox.app.layout.engine.JSEngine
import jp.aquabox.app.layout.engine.LayoutModule
import jp.aquabox.app.layout.engine.render.AquagearRender
import jp.aquabox.layout.compiler.render.compiler.Render
import jp.aquabox.layout.compiler.render.lexer.result.StringVariable
import jp.aquabox.layout.compiler.render.lexer.result.Type
import org.json.JSONObject

class AquagearScrollLayout(context: Context) : ScrollView(context),
    AquagearViewInterface {
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
            setTapEvent(this@AquagearScrollLayout, it.value, module, jsonObject)
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
                else -> {

                }
            }
        }

    }

    private fun setDataListener(it: StringVariable.Parameter) {
        module.addListener(
            it.value,
            object : LayoutModule.DataListener {
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
                                    val v = render.render(
                                        context,
                                        module, jsons.getJSONObject(i)
                                    )
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
