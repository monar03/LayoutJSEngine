package jp.aquabox.app.layout.engine.render

import android.content.Context
import android.view.View
import jp.aquabox.app.layout.engine.LayoutModule
import jp.aquabox.app.layout.engine.view.AquagearImageView
import org.json.JSONObject

class ImageRender : AquagearRender() {
    override fun render(context: Context, module: LayoutModule, jsonObject: JSONObject?): View {
        return AquagearImageView(context).apply {
            set(module, params, styles, jsonObject)
        }
    }
}