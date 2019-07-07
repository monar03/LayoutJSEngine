package jp.aquabox.app.layoutjsengine

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import jp.aquabox.app.layout.engine.JSEngine
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStream

class MainActivity : AppCompatActivity(), JSEngine.JSEngineInterface {
    lateinit var jsEngine: JSEngine

    override fun getEngine(): JSEngine {
        return jsEngine
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jsEngine = JSEngine(
            this,
            object : JSEngine.JSLoadListener() {
                override fun onReady() {
                    jsEngine.loadModule(
                        "test",
                        getFileString("index/index.vxml"),
                        getFileString("index/index.vcss"),
                        getFileString("index/index.js"),
                        object : JSEngine.JSViewLoadListener {
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
            })
    }
}


