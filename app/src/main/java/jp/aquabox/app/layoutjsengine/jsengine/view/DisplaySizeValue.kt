package jp.aquabox.app.layoutjsengine.jsengine.view

import android.content.Context

class DisplaySizeConverter() {
    companion object {
        fun displaySizeConvert(str: String, context: Context): Float {
            var index = 0
            var num = ""
            var unit = ""
            val tstr = str.trim()
            for (i in 0 until tstr.length) {
                val c: Char = tstr.get(i)
                if (!c.isDigit()) {
                    index = i
                    break
                } else {
                    num += c
                }
            }

            for (i in index until tstr.length) {
                val c: Char = tstr.get(i)
                if (!c.isDigit()) {
                    unit += c
                } else {
                    break
                }
            }

            when (unit) {
                "dp" -> {
                    return num.toFloat() * context.resources.displayMetrics.density
                }
                "sp" -> {
                    return num.toFloat() * context.resources.displayMetrics.scaledDensity
                }
                else -> {
                    return num.toFloat()
                }
            }
        }
    }
}