package jp.aquabox.app.layout.engine

import android.util.JsonReader
import org.json.JSONObject
import java.io.StringReader

interface LayoutModule {
    fun update(key: String)
    fun tap(funcName: String, jsonStr: String)
    fun addListener(key: String, listener: DataListener)

    interface DataListener {
        fun onUpdate(data: JSONObject)
    }

    data class LayoutModuleData(
        val layoutStr: String,
        val designStr: String,
        val scriptStr: String
    )
}

open class LayoutModuleImpl(
    private val command: AquagearCommand,
    data: LayoutModule.LayoutModuleData,
    onLoadEndListener: (LayoutModule) -> Unit
) : LayoutModule {
    private val listenerMap: MutableMap<String, MutableList<LayoutModule.DataListener>> = mutableMapOf()

    init {
        command.load(data.scriptStr) {
            onLoadEndListener(this)
        }
    }

    override fun update(key: String) {
        command.update(key) {
            // XXX 変なエスケープ文字列をサニタイズする
            val reader = JsonReader(StringReader(it))
            reader.isLenient = true
            refresh(key, JSONObject(reader.nextString()))
        }
    }

    override fun tap(funcName: String, jsonStr: String) {
        command.tap(funcName, jsonStr)
    }

    private fun refresh(key: String, data: JSONObject) {
        listenerMap[key]?.map {
            it.onUpdate(data)
        }
    }

    override fun addListener(key: String, listener: LayoutModule.DataListener) {
        if (listenerMap.containsKey(key)) {
            listenerMap[key]?.add(listener)
        } else {
            listenerMap[key] = mutableListOf(listener)
        }
    }
}

