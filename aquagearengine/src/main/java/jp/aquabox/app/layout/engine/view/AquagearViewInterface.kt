package jp.aquabox.app.layout.engine.view

import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.view.View
import android.view.ViewGroup
import jp.aquabox.app.layout.engine.LayoutModule
import org.json.JSONObject
import java.net.URL
import kotlin.concurrent.thread

interface AquagearViewInterface {
    fun setTapEvent(view: View, funcName: String, module: LayoutModule, jsonObject: JSONObject?) {
        view.setOnClickListener {
            module.tap(funcName, jsonObject.toString())
        }
    }

    fun setDesign(styles: Map<String, String>?, view: View) {
        val width: Int = styles?.get("width")?.run {
            if (this == "wrap") {
                ViewGroup.LayoutParams.WRAP_CONTENT
            } else if (this == "fill") {
                ViewGroup.LayoutParams.MATCH_PARENT
            } else {
                DisplaySizeConverter.displaySizeConvert(this, view.context).toInt()
            }
        } ?: ViewGroup.LayoutParams.WRAP_CONTENT

        val height: Int = styles?.get("height")?.run {
            if (this == "wrap") {
                ViewGroup.LayoutParams.WRAP_CONTENT
            } else if (this == "fill") {
                ViewGroup.LayoutParams.MATCH_PARENT
            } else {
                DisplaySizeConverter.displaySizeConvert(this, view.context).toInt()
            }
        } ?: ViewGroup.LayoutParams.WRAP_CONTENT

        view.layoutParams = AquagearLayout.LayoutParams(width, height).apply {
            styles?.get("weight")?.let {
                weight = it.toFloat()
            }
        }
        setPadding(styles, view)
        setMargin(styles, view)

        styles?.get("backgroundColor")?.let {
            view.setBackgroundColor(Color.parseColor(it))
        }
        styles?.get("backgroundImage")?.let {
            thread {
                val url = URL(it)
                val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                view.handler.post {
                    view.background = BitmapDrawable(view.resources, bmp)
                }
            }
        }

        styles?.get("foreground")?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                view.foreground = RippleDrawable(
                    ColorStateList.valueOf(Color.parseColor(it)),
                    null,
                    null
                )
            }
        }
    }

    fun setMargin(styles: Map<String, String>?, view: View) {
        styles?.get("margin")?.let {
            val size = DisplaySizeConverter.displaySizeConvert(it, view.context)
            (view.layoutParams as ViewGroup.MarginLayoutParams).setMargins(
                size.toInt(),
                size.toInt(),
                size.toInt(),
                size.toInt()
            )
        }

        styles?.get("marginTop")?.let {
            val size = DisplaySizeConverter.displaySizeConvert(it, view.context)
            (view.layoutParams as ViewGroup.MarginLayoutParams).run {
                setMargins(
                    this.leftMargin,
                    size.toInt(),
                    this.rightMargin,
                    this.bottomMargin
                )
            }
        }

        styles?.get("marginBottom")?.let {
            val size =
                DisplaySizeConverter.displaySizeConvert(it, view.context)
            (view.layoutParams as ViewGroup.MarginLayoutParams).run {
                setMargins(
                    this.leftMargin,
                    this.topMargin,
                    this.rightMargin,
                    size.toInt()
                )
            }
        }

        styles?.get("marginLeft")?.let {
            val size =
                DisplaySizeConverter.displaySizeConvert(it, view.context)
            (view.layoutParams as ViewGroup.MarginLayoutParams).run {
                setMargins(
                    size.toInt(),
                    this.topMargin,
                    this.rightMargin,
                    this.bottomMargin
                )
            }
        }

        styles?.get("marginRight")?.let {
            val size =
                DisplaySizeConverter.displaySizeConvert(it, view.context)
            (view.layoutParams as ViewGroup.MarginLayoutParams).run {
                setMargins(
                    this.leftMargin,
                    this.topMargin,
                    size.toInt(),
                    this.bottomMargin
                )
            }
        }
    }

    fun setPadding(styles: Map<String, String>?, view: View) {
        styles?.get("padding")?.let {
            val size =
                DisplaySizeConverter.displaySizeConvert(it, view.context)
            view.setPadding(
                size.toInt(),
                size.toInt(),
                size.toInt(),
                size.toInt()
            )
        }

        styles?.get("paddingTop")?.let {
            val size =
                DisplaySizeConverter.displaySizeConvert(it, view.context)
            view.setPadding(
                view.paddingLeft,
                size.toInt(),
                view.paddingRight,
                view.paddingBottom
            )
        }

        styles?.get("paddingBottom")?.let {
            val size =
                DisplaySizeConverter.displaySizeConvert(it, view.context)
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                size.toInt()
            )
        }

        styles?.get("paddingStart")?.let {
            val size =
                DisplaySizeConverter.displaySizeConvert(it, view.context)
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                size.toInt(),
                view.paddingBottom
            )
        }

        styles?.get("paddingEnd")?.let {
            val size =
                DisplaySizeConverter.displaySizeConvert(it, view.context)
            view.setPadding(
                size.toInt(),
                view.paddingTop,
                view.paddingRight,
                view.paddingBottom
            )
        }
    }
}