package jp.aquabox.app.layoutjsengine.jsengine.render

import android.content.Context
import android.view.View
import jp.aquabox.app.layoutjsengine.jsengine.view.AquagearTextView
import jp.aquabox.app.layoutjsengine.jsengine.view.AquagearViewLayout
import jp.aquagear.layout.compiler.render.compiler.BlockRender
import jp.aquagear.layout.compiler.render.compiler.Render
import jp.aquagear.layout.compiler.render.compiler.StringRender
import jp.aquagear.layout.compiler.render.lexer.result.StringVariable
import org.json.JSONObject

class ViewRender : BlockRender() {
    fun render(context: Context, jsonObject: JSONObject?): Any {
        val block = AquagearViewLayout(context).apply {
            set(this@ViewRender.params, this@ViewRender.styles, jsonObject)
            if (params.containsKey("for")) {
                setTemplateRender(renders)
                return this
            }
        }

        for (render: Render in renders) {
            when (render) {
                is ViewRender -> {
                    block.addView(render.render(context, jsonObject) as View?)
                }
                is StringRender -> {
                    block.addView(AquagearTextView(context).apply {
                        set(render.render() as StringVariable.Parameter, mapOf(), mapOf(), jsonObject)
                    })
                }
                is TextRender -> {
                    block.addView(render.render(context, jsonObject))
                }
            }
        }

        return block
    }
}
