package jp.aquabox.app.layoutjsengine.view

import android.content.Context
import android.view.View
import android.widget.TextView
import jp.aquabox.app.layoutjsengine.JSEngineInterface
import jp.aquabox.app.layoutjsengine.jsengine.data.DataListener
import jp.aquagear.layout.compiler.render.compiler.BlockRender
import jp.aquagear.layout.compiler.render.compiler.StringRender
import jp.aquagear.layout.compiler.render.lexer.result.StringVariable
import jp.aquagear.layout.compiler.render.lexer.result.Type

class TextRender : BlockRender() {
    fun render(context: Context): TextView {
        return TextView(context).apply {
            setEvent(this, context)
            setTextViewDesign((renders.get(0) as StringRender).render() as StringVariable.Parameter)
        }
    }

    // TODO ViewRenderも含め設定の所は最適化する
    private fun setEvent(view: View, context: Context) {
        params["tap"]?.let {
            if (context is JSEngineInterface) {
                view.setOnClickListener { _ ->
                    context.getEngine().tap(it.value)
                }
            }
        }
    }

    private fun TextView.setTextViewDesign(parameter: StringVariable.Parameter) {
        when (parameter.type) {
            Type.CONST -> {
                text = parameter.value
            }
            Type.VARIABLE -> {
                if (context is JSEngineInterface) {
                    (context as JSEngineInterface).getEngine().run {
                        jsData.addListener(
                            parameter.value,
                            object : DataListener {
                                override fun onUpdate(data: Any?) {
                                    this@setTextViewDesign.text = data as String
                                }
                            }
                        )
                        update(parameter.value)
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