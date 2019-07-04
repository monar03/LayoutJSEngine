package jp.aquabox.app.layoutjsengine.jsengine.view

import android.content.Context
import android.graphics.BitmapFactory
import android.widget.ImageView
import jp.aquabox.app.layoutjsengine.jsengine.JSEngine
import jp.aquabox.app.layoutjsengine.jsengine.data.DataListener
import jp.aquagear.layout.compiler.render.lexer.result.StringVariable
import jp.aquagear.layout.compiler.render.lexer.result.Type
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
                thread {
                    val url = URL(it.value)
                    val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    handler.post {
                        setImageBitmap(bmp)
                    }
                }
            } else {
                if (jsonObject != null) {
                    thread {
                        val url = URL(jsonObject!!.getString(it.value))
                        val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                        handler.post {
                            setImageBitmap(bmp)
                        }
                    }
                } else {
                    (context as JSEngine.JSEngineInterface).getEngine().run {
                        jsData.addListener(
                            it.value,
                            object : DataListener {
                                override fun onUpdate(data: JSONObject) {
                                    thread {
                                        try {
                                            val url = URL(data.getString(it.value))
                                            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                                            handler.post {
                                                setImageBitmap(bmp)
                                            }
                                        } catch (e: Exception) {

                                        }
                                    }
                                }
                            }
                        )
                        update(it.value)
                    }
                }
            }
        }
    }
}