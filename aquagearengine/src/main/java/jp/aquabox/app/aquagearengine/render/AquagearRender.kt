package jp.aquabox.app.aquagearengine.render

import android.content.Context
import android.view.View
import jp.aquabox.app.aquagearengine.LayoutModule
import jp.aquabox.layout.compiler.render.compiler.BlockRender
import org.json.JSONObject

abstract class AquagearRender : BlockRender() {
    abstract fun render(context: Context, module: LayoutModule, jsonObject: JSONObject?): View
}