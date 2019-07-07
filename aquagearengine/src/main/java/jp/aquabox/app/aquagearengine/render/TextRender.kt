package jp.aquabox.app.aquagearengine.render

import android.content.Context
import android.view.View
import jp.aquabox.app.aquagearengine.LayoutModule
import jp.aquabox.app.aquagearengine.view.AquagearTextView
import jp.aquabox.layout.compiler.render.compiler.StringRender
import jp.aquabox.layout.compiler.render.lexer.result.StringVariable
import org.json.JSONObject

class TextRender : AquagearRender() {
    override fun render(context: Context, module: LayoutModule, jsonObject: JSONObject?): View {
        return AquagearTextView(context).apply {
            set(module, (renders[0] as StringRender).render() as StringVariable.Parameter, params, styles, jsonObject)
        }
    }
}