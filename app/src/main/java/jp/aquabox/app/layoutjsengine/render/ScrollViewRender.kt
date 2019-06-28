package jp.aquabox.app.layoutjsengine.render

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import jp.aquabox.app.layoutjsengine.view.AquagearHorizonalScrollViewLayout
import jp.aquabox.app.layoutjsengine.view.AquagearScrollViewLayout
import jp.aquabox.app.layoutjsengine.view.AquagearTextView
import jp.aquagear.layout.compiler.render.compiler.BlockRender
import jp.aquagear.layout.compiler.render.compiler.Render
import jp.aquagear.layout.compiler.render.compiler.StringRender
import jp.aquagear.layout.compiler.render.lexer.result.StringVariable
import org.json.JSONObject

class ScrollViewRender : BlockRender() {
    fun render(context: Context, jsonObject: JSONObject?): Any {
        val scrollView = if (params.containsKey("horizonal")) {
            AquagearHorizonalScrollViewLayout(context).apply {
                set(this@ScrollViewRender.params, this@ScrollViewRender.styles, jsonObject)
                if (params.containsKey("for")) {
                    setTemplateRender(renders)
                    return this
                }
            }
        } else {
            AquagearScrollViewLayout(context).apply {
                set(this@ScrollViewRender.params, this@ScrollViewRender.styles, jsonObject)
                if (params.containsKey("for")) {
                    setTemplateRender(renders)
                    return this
                }
            }
        }

        val block = LinearLayout(context).apply {
            scrollView.addView(this)
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

        return scrollView
    }
}
