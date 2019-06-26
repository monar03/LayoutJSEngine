package jp.aquabox.app.layoutjsengine.view

import android.content.Context
import android.widget.LinearLayout
import jp.aquabox.app.layoutjsengine.JSEngineInterface
import jp.aquagear.layout.compiler.render.lexer.result.StringVariable

class AquagearViewLayout(context: Context?) : LinearLayout(context) {
    private lateinit var params: Map<String, StringVariable.Parameter>
    private lateinit var styles: Map<String, String>

    fun set(params: Map<String, StringVariable.Parameter>, styles: Map<String, String>) {
        this.params = params
        this.styles = styles

        setEvent()
        setBlockDesign()
    }

    private fun setEvent() {
        params["tap"]?.let {
            if (context is JSEngineInterface) {
                setOnClickListener { _ ->
                    (this.context as JSEngineInterface).getEngine().tap(it.value)
                }
            }
        }
    }

    private fun setBlockDesign() {
        styles["orientation"]?.let {
            when (it) {
                "horizon" -> {
                    orientation = LinearLayout.HORIZONTAL
                }
                else -> {
                    orientation = LinearLayout.VERTICAL
                }
            }
        }
    }

}