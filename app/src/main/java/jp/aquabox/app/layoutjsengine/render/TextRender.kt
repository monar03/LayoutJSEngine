package jp.aquabox.app.layoutjsengine.render

import android.content.Context
import android.widget.TextView
import jp.aquabox.app.layoutjsengine.view.AquagearTextView
import jp.aquagear.layout.compiler.render.compiler.BlockRender
import jp.aquagear.layout.compiler.render.compiler.StringRender
import jp.aquagear.layout.compiler.render.lexer.result.StringVariable

class TextRender : BlockRender() {
    fun render(context: Context): TextView {
        return AquagearTextView(context).apply {
            set((renders.get(0) as StringRender).render() as StringVariable.Parameter, params, styles)
        }
    }

}