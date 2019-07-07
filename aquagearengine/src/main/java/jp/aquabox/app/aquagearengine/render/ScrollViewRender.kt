package jp.aquabox.app.aquagearengine.render

import android.content.Context
import android.view.View
import android.widget.ScrollView
import jp.aquabox.app.aquagearengine.LayoutModule
import jp.aquabox.app.aquagearengine.view.AquagearHorizonalScrollLayout
import jp.aquabox.app.aquagearengine.view.AquagearLayout
import jp.aquabox.app.aquagearengine.view.AquagearScrollLayout
import jp.aquabox.app.aquagearengine.view.AquagearTextView
import jp.aquabox.layout.compiler.render.compiler.Render
import jp.aquabox.layout.compiler.render.compiler.StringRender
import jp.aquabox.layout.compiler.render.lexer.result.StringVariable
import org.json.JSONObject

class ScrollViewRender : AquagearRender() {
    override fun render(context: Context, module: LayoutModule, jsonObject: JSONObject?): View {
        val scrollView: ScrollView = if (params.containsKey("horizonal")) {
            AquagearHorizonalScrollLayout(context).apply {
                set(module, this@ScrollViewRender.params, this@ScrollViewRender.styles, jsonObject)
                if (params.containsKey("for")) {
                    setTemplateRender(renders)
                    return this
                }
            }
        } else {
            AquagearScrollLayout(context).apply {
                set(module, this@ScrollViewRender.params, this@ScrollViewRender.styles, jsonObject)
                if (params.containsKey("for")) {
                    setTemplateRender(renders)
                    return this
                }
            }
        }

        val block: AquagearLayout = AquagearLayout(
            context
        ).apply {
            scrollView.addView(this)
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

        return scrollView
    }
}
