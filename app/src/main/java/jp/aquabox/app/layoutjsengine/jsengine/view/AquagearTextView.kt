package jp.aquabox.app.layoutjsengine.jsengine.view

import android.content.Context
import android.widget.TextView
import jp.aquabox.app.layoutjsengine.jsengine.JSEngine
import jp.aquabox.app.layoutjsengine.jsengine.data.DataListener
import jp.aquagear.layout.compiler.render.lexer.result.StringVariable
import jp.aquagear.layout.compiler.render.lexer.result.Type
import org.json.JSONObject

class AquagearTextView(context: Context?) : TextView(context), AquagearViewInterface {
    private lateinit var textParam: StringVariable.Parameter
    private lateinit var params: Map<String, StringVariable.Parameter>
    private lateinit var styles: Map<String, String>
    private var jsonObject: JSONObject? = null


    fun set(
        textParam: StringVariable.Parameter,
        params: Map<String, StringVariable.Parameter>,
        styles: Map<String, String>,
        jsonObject: JSONObject?
    ) {
        this.textParam = textParam
        this.params = params
        this.styles = styles
        this.jsonObject = jsonObject

        setEvent()
        setTextViewDesign()
    }

    // TODO ViewRenderも含め設定の所は最適化する
    private fun setEvent() {
        params["tap"]?.let {
            setTapEvent(this@AquagearTextView, it.value, jsonObject)
        }
    }

    private fun setTextViewDesign() {
        setDesign(styles, this)

        styles["textSize"]?.let {
            this.textSize = DisplaySizeConverter.displaySizeConvert(it, context)
        }

        when (textParam.type) {
            Type.CONST -> {
                text = textParam.value
            }
            Type.VARIABLE -> {
                if (jsonObject != null) {
                    // TODO データがない時の処理の追加
                    text = jsonObject!!.getString(textParam.value)
                } else if (this.context is JSEngine.JSEngineInterface) {
                    (context as JSEngine.JSEngineInterface).getEngine().run {
                        jsData.addListener(
                            textParam.value,
                            object : DataListener {
                                override fun onUpdate(data: JSONObject) {
                                    text = data.getString(textParam.value)
                                }
                            }
                        )
                        update(textParam.value)
                    }
                }
            }
            else -> {

            }
        }
    }
}