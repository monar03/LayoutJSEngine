package jp.aquabox.app.aquagearengine.view

import android.content.Context
import android.widget.LinearLayout
import android.widget.ScrollView
import jp.aquabox.app.aquagearengine.JSEngine
import jp.aquabox.app.aquagearengine.LayoutModule
import jp.aquabox.app.aquagearengine.render.AquagearRender
import jp.aquabox.layout.compiler.render.compiler.Render
import jp.aquabox.layout.compiler.render.lexer.result.StringVariable
import jp.aquabox.layout.compiler.render.lexer.result.Type
import org.json.JSONObject

class AquagearHorizonalScrollLayout(context: Context) : ScrollView(context),
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
            setTapEvent(this@AquagearHorizonalScrollLayout, it.value, module, jsonObject)
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
                    this@AquagearHorizonalScrollLayout.removeAllViews()
                    val block = LinearLayout(context).apply {
                        orientation = LinearLayout.HORIZONTAL
                    }

                    val jsons = data.getJSONArray(it.value)
                    for (i in 0 until jsons.length()) {
                        templateRenders?.map { render ->
                            when (render) {
                                is AquagearRender -> {
                                    val v = render.render(
                                        context,
                                        module, jsons.getJSONObject(i))
                                    block.addView(v)
                                }
                            }
                        }
                    }
                    this@AquagearHorizonalScrollLayout.addView(block)
                }
            }
        )
    }

    private fun setBlockDesign() {
        setDesign(styles, this)
    }
}