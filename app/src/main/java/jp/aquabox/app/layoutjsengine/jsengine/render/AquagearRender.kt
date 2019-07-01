package jp.aquabox.app.layoutjsengine.jsengine.render

import android.content.Context
import android.view.View
import jp.aquagear.layout.compiler.render.compiler.BlockRender
import org.json.JSONObject

abstract class AquagearRender : BlockRender() {
    abstract fun render(context: Context, jsonObject: JSONObject?): View
}