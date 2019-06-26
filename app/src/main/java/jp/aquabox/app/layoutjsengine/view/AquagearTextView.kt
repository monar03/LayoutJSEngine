package jp.aquabox.app.layoutjsengine.view

import android.content.Context
import android.widget.TextView
import jp.aquabox.app.layoutjsengine.JSEngineInterface
import jp.aquabox.app.layoutjsengine.jsengine.data.DataListener
import jp.aquagear.layout.compiler.render.lexer.result.StringVariable
import jp.aquagear.layout.compiler.render.lexer.result.Type

class AquagearTextView(context: Context?) : TextView(context) {
    private lateinit var textParam: StringVariable.Parameter
    private lateinit var params: Map<String, StringVariable.Parameter>
    private lateinit var styles: Map<String, String>

    fun set(
        textParam: StringVariable.Parameter,
        params: Map<String, StringVariable.Parameter>,
        styles: Map<String, String>
    ) {
        this.textParam = textParam
        this.params = params
        this.styles = styles

        setEvent()
        setTextViewDesign()
    }

    // TODO ViewRenderも含め設定の所は最適化する
    private fun setEvent() {
        params["tap"]?.let {
            if (context is JSEngineInterface) {
                setOnClickListener { _ ->
                    (this.context as JSEngineInterface).getEngine().tap(it.value)
                }
            }
        }
    }

    private fun setTextViewDesign() {
        when (textParam.type) {
            Type.CONST -> {
                text = textParam.value
            }
            Type.VARIABLE -> {
                if (this.context is JSEngineInterface) {
                    (context as JSEngineInterface).getEngine().run {
                        jsData.addListener(
                            textParam.value,
                            object : DataListener {
                                override fun onUpdate(data: Any?) {
                                    text = data as String
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

        styles["padding"]?.let {
            setPadding(
                it.toInt(),
                it.toInt(),
                it.toInt(),
                it.toInt()
            )
        }
    }
}