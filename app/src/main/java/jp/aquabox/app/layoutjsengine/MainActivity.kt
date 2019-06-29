package jp.aquabox.app.layoutjsengine

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import jp.aquabox.app.layoutjsengine.jsengine.JSEngine
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), JSEngine.JSEngineInterface {
    val jsEngine: JSEngine by lazy {
        JSEngine(this, object : JSEngine.JSLoadListener {
            override fun onLoadFinish(v: View) {
                root.addView(v)
            }
        })
    }

    val module: String? by lazy {
        this.intent.getStringExtra("url") ?: "index"
    }

    override fun getEngine(): JSEngine {
        return jsEngine
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jsEngine.load(module)
    }
}


