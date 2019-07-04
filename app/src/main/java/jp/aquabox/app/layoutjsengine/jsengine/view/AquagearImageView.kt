package jp.aquabox.app.layoutjsengine.jsengine.view

import android.content.Context
import android.graphics.BitmapFactory
import android.widget.ImageView
import jp.aquabox.app.layoutjsengine.jsengine.JSEngine
import jp.aquabox.app.layoutjsengine.jsengine.data.DataListener
import jp.aquagear.layout.compiler.render.lexer.result.StringVariable
import jp.aquagear.layout.compiler.render.lexer.result.Type
import org.json.JSONException
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

    private fun setEvent() {
        params["tap"]?.let {
            setTapEvent(this@AquagearImageView, it.value, jsonObject)
        }
    }

    private fun setImageViewDesign() {
        setDesign(styles, this)

        params["src"]?.let {
            if (it.type == Type.CONST) {
                loadImage(it.value)
            } else {
                if (jsonObject != null) {
                    try {
                        loadImage(jsonObject!!.getString(it.value))
                    } catch (e: JSONException) {
                    }
                } else {
                    (context as JSEngine.JSEngineInterface).getEngine().run {
                        jsData.addListener(
                            it.value,
                            object : DataListener {
                                override fun onUpdate(data: JSONObject) {
                                    loadImage(data.getString(it.value))
                                }
                            }
                        )
                        update(it.value)
                    }
                }
            }
        }
    }

    private fun loadImage(str: String) {
        thread {
            val url = URL(str)
            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            handler.post {
                setImageBitmap(bmp)
            }
        }
    }
}