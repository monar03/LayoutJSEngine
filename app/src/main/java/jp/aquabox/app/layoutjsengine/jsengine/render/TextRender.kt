package jp.aquabox.app.layoutjsengine.jsengine.render

import android.content.Context
import android.widget.TextView
import jp.aquabox.app.layoutjsengine.jsengine.view.AquagearTextView
import jp.aquagear.layout.compiler.render.compiler.BlockRender
import jp.aquagear.layout.compiler.render.compiler.StringRender
import jp.aquagear.layout.compiler.render.lexer.result.StringVariable
import org.json.JSONObject

class TextRender : BlockRender() {
    fun render(context: Context, jsonObject: JSONObject?): TextView {
        return AquagearTextView(context).apply {
            set((renders[0] as StringRender).render() as StringVariable.Parameter, params, styles, jsonObject)
        }
    }

}