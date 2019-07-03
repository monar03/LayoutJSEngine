package jp.aquabox.app.layoutjsengine.jsengine.view

import android.content.Context
import android.graphics.BitmapFactory
import android.widget.ImageView
import jp.aquagear.layout.compiler.render.lexer.result.StringVariable
import org.json.JSONObject
import java.net.URL
import kotlin.concurrent.thread

class AquagearImageView(context: Context) : ImageView(context), AquagearViewInterface {
    private lateinit var params: Map<String, StringVariable.Parameter>
    private lateinit var styles: Map<String, String>
    private var jsonObject: JSONObject? = null

    fun set(
        params: Map<String, StringVariable.Parameter>,
        styles: Map<String, String>,
        jsonObject: JSONObject?
    ) {
        this.params = params
        this.styles = styles
        this.jsonObject = jsonObject

        setEvent()
        setImageViewDesign()
    }

    // TODO ViewRenderも含め設定の所は最適化する
    private fun setEvent() {
        params["tap"]?.let {
            setTapEvent(this@AquagearImageView, it.value, jsonObject)
        }
    }

    private fun setImageViewDesign() {
        setDesign(styles, this)

        styles["src"]?.let {
            thread {
                val url = URL(it)
                val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                handler.post {
                    setImageBitmap(bmp)
                }
            }
        }
    }
}