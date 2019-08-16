package jp.aquabox.app.layoutjsengine

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import jp.aquabox.app.layout.engine.AquagearEngine
import jp.aquabox.app.layout.engine.LayoutModule
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStream

class MainActivity : AppCompatActivity(), AquagearEngine.OnEngineInterface {
    private lateinit var aquagearEngine: AquagearEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        aquagearEngine = AquagearEngine(
            this,
            AquagearEngine.AquagearEngineRenderCreator()
        ) {
            it.loadModule(
                "test",
                LayoutModule.LayoutModuleData(
                    getFileString("index/index.vxml"),
                    getFileString("index/index.vcss"),
                    getFileString("index/index.js")
                )
            ) {
                root.addView(it)
            }

            it.loadModule(
                "test1",
                LayoutModule.LayoutModuleData(
                    getFileString("index/index.vxml"),
                    getFileString("index/index.vcss"),
                    getFileString("index/index.js")
                )
            ) {
                root.addView(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        aquagearEngine.onResume()
    }

    override fun onPause() {
        super.onPause()
        aquagearEngine.onPause()
    }

    private fun getFileString(path: String): String {
        val input: InputStream = assets.open(path)
        val buffer = ByteArray(input.available())
        input.read(buffer)
        input.close()

        return String(buffer, Charsets.UTF_8)
    }

    override fun getEngine(): AquagearEngine {
        return aquagearEngine
    }
}


