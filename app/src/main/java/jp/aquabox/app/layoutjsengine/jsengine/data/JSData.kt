package jp.aquabox.app.layoutjsengine.jsengine.data

class JSData {
    private lateinit var dataMap: Map<String, Any>
    private val listenerMap: MutableMap<String, DataListener> = mutableMapOf()

    fun addData(dataMap: Map<String, Any>) {
        this.dataMap = dataMap
        listenerMap.map {
            it.value.onUpdate(dataMap[it.key])
        }
    }

    fun addListener(key: String, listener: DataListener) {
        listenerMap[key] = listener
    }


}

interface DataListener {
    fun onUpdate(data: Any?)
}