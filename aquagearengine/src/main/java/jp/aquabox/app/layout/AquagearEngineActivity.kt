package jp.aquabox.app.layout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import jp.aquabox.app.layout.engine.AquagearEngine

abstract class AquagearEngineActivity : AppCompatActivity(), AquagearEngine.OnEngineInterface {
    private lateinit var aquagearEngine: AquagearEngine

    override fun getEngine(): AquagearEngine {
        return aquagearEngine
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        aquagearEngine = AquagearEngine(
            this
        ) {
            onReadyEngine(aquagearEngine)
        }
    }

    abstract fun onReadyEngine(engine: AquagearEngine)
}