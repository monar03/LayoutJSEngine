package jp.aquabox.app.layoutjsengine.render

import android.content.Context
import android.view.View
import jp.aquabox.app.layoutjsengine.view.AquagearTextView
import jp.aquabox.app.layoutjsengine.view.AquagearViewLayout
import jp.aquagear.layout.compiler.render.compiler.BlockRender
import jp.aquagear.layout.compiler.render.compiler.Render
import jp.aquagear.layout.compiler.render.compiler.StringRender
import jp.aquagear.layout.compiler.render.lexer.result.StringVariable

class ViewRender : BlockRender() {
    fun render(context: Context): Any {
        val block = AquagearViewLayout(context).apply {
            set(params, styles)
            if (params.containsKey("for")) {
                setTemplateRender(renders)
                return this
            }
        }

        for (render: Render in renders) {
            when (render) {
                is ViewRender -> {
                    block.addView(render.render(context) as View?)
                }
                is StringRender -> {
                    block.addView(AquagearTextView(context).apply {
                        set(render.render() as StringVariable.Parameter, mapOf(), mapOf())
                    })
                }
                is TextRender -> {
                    block.addView(render.render(context) as View?)
                }
            }
        }

        return block
    }
}

