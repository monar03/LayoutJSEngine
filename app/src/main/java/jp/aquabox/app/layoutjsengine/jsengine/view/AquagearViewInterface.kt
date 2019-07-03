package jp.aquabox.app.layoutjsengine.jsengine.view

import android.view.View
import android.view.ViewGroup
import jp.aquabox.app.layoutjsengine.jsengine.JSEngine
import org.json.JSONObject

interface AquagearViewInterface {
    fun setTapEvent(view: View, funcName: String, jsonObject: JSONObject?) {
        if (view.context is JSEngine.JSEngineInterface) {
            view.setOnClickListener {
                (it.context as JSEngine.JSEngineInterface).getEngine().tap(funcName, jsonObject.toString())
            }
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

        view.layoutParams = ViewGroup.MarginLayoutParams(width, height)
        setPadding(styles, view)

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
            val size = DisplaySizeConverter.displaySizeConvert(it, view.context)
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
            val size = DisplaySizeConverter.displaySizeConvert(it, view.context)
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
            val size = DisplaySizeConverter.displaySizeConvert(it, view.context)
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
            val size = DisplaySizeConverter.displaySizeConvert(it, view.context)
            view.setPadding(
                size.toInt(),
                size.toInt(),
                size.toInt(),
                size.toInt()
            )
        }

        styles?.get("paddingTop")?.let {
            val size = DisplaySizeConverter.displaySizeConvert(it, view.context)
            view.setPadding(
                view.paddingLeft,
                size.toInt(),
                view.paddingRight,
                view.paddingBottom
            )
        }

        styles?.get("paddingBottom")?.let {
            val size = DisplaySizeConverter.displaySizeConvert(it, view.context)
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                size.toInt()
            )
        }

        styles?.get("paddingStart")?.let {
            val size = DisplaySizeConverter.displaySizeConvert(it, view.context)
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                size.toInt(),
                view.paddingBottom
            )
        }

        styles?.get("paddingEnd")?.let {
            val size = DisplaySizeConverter.displaySizeConvert(it, view.context)
            view.setPadding(
                size.toInt(),
                view.paddingTop,
                view.paddingRight,
                view.paddingBottom
            )
        }
    }
}