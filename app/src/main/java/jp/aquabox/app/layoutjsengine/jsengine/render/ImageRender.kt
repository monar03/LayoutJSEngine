package jp.aquabox.app.layoutjsengine.jsengine.render

import android.content.Context
import android.view.View
import jp.aquabox.app.layoutjsengine.jsengine.LayoutModule
import jp.aquabox.app.layoutjsengine.jsengine.view.AquagearImageView
import org.json.JSONObject

class ImageRender : AquagearRender() {
    override fun render(context: Context, module: LayoutModule, jsonObject: JSONObject?): View {
        return AquagearImageView(context).apply {
            set(module, params, styles, jsonObject)
        }
    }
}