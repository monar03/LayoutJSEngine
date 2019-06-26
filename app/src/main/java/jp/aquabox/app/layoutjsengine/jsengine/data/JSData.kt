package jp.aquabox.app.layoutjsengine.jsengine.data

import org.json.JSONObject

class JSData {
    private val listenerMap: MutableMap<String, MutableList<DataListener>> = mutableMapOf()

    fun update(key: String, data: JSONObject) {
        listenerMap[key]?.map {
            it.onUpdate(data)
        }
    }

    fun addListener(key: String, listener: DataListener) {
        if (listenerMap.containsKey(key)) {
            listenerMap[key]?.add(listener)
        } else {
            listenerMap[key] = mutableListOf(listener)
        }
    }


}

interface DataListener {
    fun onUpdate(data: JSONObject)
}