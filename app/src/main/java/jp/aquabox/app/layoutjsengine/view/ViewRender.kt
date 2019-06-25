package jp.aquabox.app.layoutjsengine.view

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import jp.aquabox.app.layoutjsengine.JSEngineInterface
import jp.aquabox.app.layoutjsengine.jsengine.data.DataListener
import jp.aquagear.layout.compiler.render.compiler.BlockRender
import jp.aquagear.layout.compiler.render.compiler.Render
import jp.aquagear.layout.compiler.render.compiler.StringRender
import jp.aquagear.layout.compiler.render.lexer.result.StringVariable
import jp.aquagear.layout.compiler.render.lexer.result.Type

class ViewRender : BlockRender() {
    fun render(context: Context): Any {
        val block = LinearLayout(context)
        for (render: Render in renders) {
            when (render) {
                is ViewRender -> {
                    block.addView(render.render(context) as View?)
                }
                is StringRender -> {
                    return TextView(context).apply {
                        setEvent(this, context)
                        setTextViewDesign(render.render() as StringVariable.Parameter)
                    }
                }
            }
        }
        return block.apply {
            setEvent(this, context)
            setBlockDesign()
        }
    }

    private fun setEvent(view: View, context: Context) {
        params["tap"]?.let {
            if (context is JSEngineInterface) {
                view.setOnClickListener { _ ->
                    context.getEngine().tap(it.value)
                }
            }
        }
    }

    private fun LinearLayout.setBlockDesign() {
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

