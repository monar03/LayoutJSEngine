package jp.aquabox.app.layoutjsengine.jsengine.view

import android.view.View
import android.view.ViewGroup

interface AquagearDesign {
    fun setDesign(styles: Map<String, String>?, view: View) {
        styles?.get("padding")?.let {
            view.setPadding(
                it.toInt(),
                it.toInt(),
                it.toInt(),
                it.toInt()
            )
        }

        styles?.get("margin")?.let {
            view.setPadding(
                it.toInt(),
                it.toInt(),
                it.toInt(),
                it.toInt()
            )
        }

        val width = styles?.get("width")?.toInt() ?: ViewGroup.LayoutParams.WRAP_CONTENT
        val height = styles?.get("height")?.toInt() ?: ViewGroup.LayoutParams.WRAP_CONTENT
        view.layoutParams = ViewGroup.LayoutParams(width, height)
    }
}