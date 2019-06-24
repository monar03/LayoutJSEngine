package jp.aquabox.app.layoutjsengine

import jp.aquabox.app.layoutjsengine.jsengine.JSEngine

interface JSEngineInterface {
    fun getEngine(): JSEngine
    fun onPageFinished()
}
