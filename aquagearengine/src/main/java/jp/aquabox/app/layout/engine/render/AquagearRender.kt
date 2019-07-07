package jp.aquabox.app.layout.engine.render

import android.content.Context
import android.view.View
import jp.aquabox.app.layout.engine.LayoutModule
import jp.aquabox.layout.compiler.render.compiler.BlockRender
import org.json.JSONObject

abstract class AquagearRender : BlockRender() {
    abstract fun render(context: Context, module: LayoutModule, jsonObject: JSONObject?): View
}