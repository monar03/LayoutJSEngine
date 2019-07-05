package jp.aquabox.app.layoutjsengine.jsengine.render

import android.content.Context
import android.view.View
import jp.aquabox.app.layoutjsengine.jsengine.LayoutModule
import jp.aquabox.app.layoutjsengine.jsengine.view.AquagearTextView
import jp.aquagear.layout.compiler.render.compiler.StringRender
import jp.aquagear.layout.compiler.render.lexer.result.StringVariable
import org.json.JSONObject

class TextRender : AquagearRender() {
    override fun render(context: Context, module: LayoutModule, jsonObject: JSONObject?): View {
        return AquagearTextView(context).apply {
            set(module, (renders[0] as StringRender).render() as StringVariable.Parameter, params, styles, jsonObject)
        }
    }
}