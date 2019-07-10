package jp.aquabox.app.layoutjsengine

import android.os.Bundle
import android.view.View
import jp.aquabox.app.layout.AquagearEngineActivity
import jp.aquabox.app.layout.engine.AquagearEngine
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStream

class MainActivity : AquagearEngineActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onReadyEngine(engine: AquagearEngine) {
        engine.loadModule(
            "test",
            getFileString("index/index.vxml"),
            getFileString("index/index.vcss"),
            getFileString("index/index.js"),
            object : AquagearEngine.OnViewLoadListener {
                override fun onViewLoadEnd(v: View) {
                    root.addView(v)
                }
            }
        )
    }

    private fun getFileString(path: String): String {
        val input: InputStream = assets.open(path)
        val buffer = ByteArray(input.available())
        input.read(buffer)
        input.close()

        return String(buffer, Charsets.UTF_8)
    }

}


