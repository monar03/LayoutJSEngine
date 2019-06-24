package jp.aquabox.app.layoutjsengine.jsengine.data

class JSData {
    private lateinit var dataMap: Map<String, Any>
    private val listenerMap: MutableMap<String, DataListener> = mutableMapOf()

    fun update(key: String, data: Any) {
        listenerMap[key]?.onUpdate(data)
    }

    fun addListener(key: String, listener: DataListener) {
        listenerMap[key] = listener
    }


}

interface DataListener {
    fun onUpdate(data: Any?)
}