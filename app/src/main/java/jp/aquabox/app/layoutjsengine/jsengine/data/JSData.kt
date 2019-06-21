package jp.aquabox.app.layoutjsengine.jsengine.data

private class JSData {
    private lateinit var dataMap: Map<String, Any>
    private val listenerMap: MutableMap<String, DataListener> = mutableMapOf()

    companion object {
        val jsData: MutableMap<String, JSData> = mutableMapOf()

        fun valueOf(key: String) {
            jsData[key] = JSData()
        }

        fun remove(key: String) {
            jsData.remove(key)
        }
    }

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