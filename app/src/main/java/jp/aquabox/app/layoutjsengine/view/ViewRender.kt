package jp.aquabox.app.layoutjsengine.view

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import jp.aquagear.layout.compiler.render.compiler.BlockRender
import jp.aquagear.layout.compiler.render.compiler.Render
import jp.aquagear.layout.compiler.render.compiler.StringRender

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
                        setTextViewDesign(render.render() as String)
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
            if (context is TouchEventListener) {
                view.setOnClickListener { context.onTap(this) }
            }
        }
    }

    private fun LinearLayout.setBlockDesign() {
        params["orientation"]?.let {
            when (it.value) {
                "horizon" -> {
                    orientation = LinearLayout.HORIZONTAL
                }
                else -> {
                    orientation = LinearLayout.VERTICAL
                }
            }
        }
    }

    private fun TextView.setTextViewDesign(str: String) {
        text = str
        params["padding"]?.let {
            setPadding(
                it.value.toInt(),
                it.value.toInt(),
                it.value.toInt(),
                it.value.toInt()
            )
        }
    }
}

interface TouchEventListener {
    fun onTap(viewRender: ViewRender)
}

