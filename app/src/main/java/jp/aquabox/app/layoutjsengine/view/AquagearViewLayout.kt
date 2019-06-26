package jp.aquabox.app.layoutjsengine.view

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import jp.aquabox.app.layoutjsengine.JSEngineInterface
import jp.aquabox.app.layoutjsengine.jsengine.data.DataListener
import jp.aquabox.app.layoutjsengine.render.TextRender
import jp.aquabox.app.layoutjsengine.render.ViewRender
import jp.aquagear.layout.compiler.render.compiler.Render
import jp.aquagear.layout.compiler.render.lexer.result.StringVariable
import jp.aquagear.layout.compiler.render.lexer.result.Type
import org.json.JSONObject

class AquagearViewLayout(context: Context?) : LinearLayout(context) {
    private var params: Map<String, StringVariable.Parameter>? = null
    private var styles: Map<String, String>? = null
    private var templateRenders: List<Render>? = null

    fun set(params: Map<String, StringVariable.Parameter>, styles: Map<String, String>) {
        this.params = params
        this.styles = styles

        setEvent()
        setBlockDesign()
    }

    fun setTemplateRender(renders: List<Render>) {
        templateRenders = renders
    }

    private fun setEvent() {
        params?.get("tap")?.let {
            if (context is JSEngineInterface) {
                setOnClickListener { _ ->
                    (this.context as JSEngineInterface).getEngine().tap(it.value)
                }
            }
        }

        params?.get("for")?.let {
            when (it.type) {
                Type.VARIABLE -> {
                    if (this.context is JSEngineInterface) {
                        (context as JSEngineInterface).getEngine().run {
                            jsData.addListener(
                                it.value,
                                object : DataListener {
                                    override fun onUpdate(data: JSONObject) {
                                        val jsons = data.getJSONArray(it.value)
                                        for (i in 0 until jsons.length()) {
                                            templateRenders?.map { render ->
                                                when (render) {
                                                    is ViewRender -> {
                                                        val v = render.render(context, jsons.getJSONObject(i)) as View
                                                        this@AquagearViewLayout.addView(v)
                                                    }
                                                    is TextRender -> {
                                                        val v = render.render(context, jsons.getJSONObject(i)) as View
                                                        this@AquagearViewLayout.addView(v)
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                            )
                            update(it.value)
                        }
                    }
                }
                else -> {

                }
            }
        }

    }

    private fun setBlockDesign() {
        styles?.get("orientation")?.let {
            when (it) {
                "horizon" -> {
                    orientation = LinearLayout.HORIZONTAL
                }
                else -> {
                    orientation = LinearLayout.VERTICAL
                }
            }
        }
        styles?.get("padding")?.let {
            setPadding(
                it.toInt(),
                it.toInt(),
                it.toInt(),
                it.toInt()
            )
        }
    }
}