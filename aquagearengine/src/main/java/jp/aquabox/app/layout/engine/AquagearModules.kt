package jp.aquabox.app.layout.engine

import android.webkit.WebView

open class AquagearModules {
    fun createEngineInterface(engine: AquagearEngine): AquagearEngineInterface {
        return AquagearEngineInterface(engine)
    }

    fun createRenderCreator(): AquagearEngineRenderCreator {
        return AquagearEngineRenderCreator()
    }

    fun createCommand(name: String, webview: WebView): AquagearCommand {
        return AquagearCommandImpl(name, webview)
    }
}