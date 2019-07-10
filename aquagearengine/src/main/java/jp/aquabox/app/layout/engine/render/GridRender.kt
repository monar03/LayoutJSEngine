package jp.aquabox.app.layout.engine.render

import android.content.Context
import android.view.View
import jp.aquabox.app.layout.engine.LayoutModule
import jp.aquabox.app.layout.engine.view.AquagearGridLayout
import jp.aquabox.app.layout.engine.view.AquagearTextView
import jp.aquabox.layout.compiler.render.compiler.Render
import jp.aquabox.layout.compiler.render.compiler.StringRender
import jp.aquabox.layout.compiler.render.lexer.result.StringVariable
import org.json.JSONObject

class GridRender : AquagearRender() {
    override fun render(context: Context, module: LayoutModule, jsonObject: JSONObject?): View {
        val block = AquagearGridLayout(context).apply {
            set(module, this@GridRender.params, this@GridRender.styles, jsonObject)
            if (params.containsKey("for")) {
                setTemplateRender(renders)
                return this
            }
        }

        for (render: Render in renders) {
            when (render) {
                is StringRender -> {
                    block.addView(AquagearTextView(context).apply {
                        set(module, render.render() as StringVariable.Parameter, mapOf(), mapOf(), jsonObject)
                    })
                }
                is AquagearRender -> {
                    block.addView(render.render(context, module, jsonObject))
                }
            }
        }

        return block
    }
}