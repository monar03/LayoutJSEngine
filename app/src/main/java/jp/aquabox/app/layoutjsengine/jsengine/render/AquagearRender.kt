package jp.aquabox.app.layoutjsengine.jsengine.render

import android.content.Context
import android.view.View
import jp.aquabox.app.layoutjsengine.jsengine.LayoutModule
import jp.aquagear.layout.compiler.render.compiler.BlockRender
import org.json.JSONObject

abstract class AquagearRender : BlockRender() {
    abstract fun render(context: Context, module: LayoutModule, jsonObject: JSONObject?): View
}