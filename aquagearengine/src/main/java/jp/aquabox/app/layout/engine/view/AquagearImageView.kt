package jp.aquabox.app.layout.engine.view

import android.content.Context
import android.graphics.BitmapFactory
import android.widget.ImageView
import jp.aquabox.app.layout.engine.JSEngine
import jp.aquabox.app.layout.engine.LayoutModule
import jp.aquabox.layout.compiler.render.lexer.result.StringVariable
import jp.aquabox.layout.compiler.render.lexer.result.Type
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import kotlin.concurrent.thread

class AquagearImageView(context: Context) : ImageView(context),
    AquagearViewInterface {
    private lateinit var module: LayoutModule
    private lateinit var params: Map<String, StringVariable.Parameter>
    private lateinit var styles: Map<String, String>
    private var jsonObject: JSONObject? = null

    fun set(
        module: LayoutModule,
        params: Map<String, StringVariable.Parameter>,
        styles: Map<String, String>,
        jsonObject: JSONObject?
    ) {
        this.module = module
        this.params = params
        this.styles = styles
        this.jsonObject = jsonObject

        setEvent()
        setImageViewDesign()
    }

    private fun setEvent() {
        params["tap"]?.let {
            setTapEvent(this@AquagearImageView, it.value, module, jsonObject)
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
                        module.addListener(
                            it.value,
                            object : LayoutModule.DataListener {
                                override fun onUpdate(data: JSONObject) {
                                    loadImage(data.getString(it.value))
                                }
                            }
                        )
                        module.update(it.value)
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