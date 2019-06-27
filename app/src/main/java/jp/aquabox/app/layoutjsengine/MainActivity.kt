package jp.aquabox.app.layoutjsengine

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import jp.aquabox.app.layoutjsengine.jsengine.JSEngine
import jp.aquabox.app.layoutjsengine.render.TextRender
import jp.aquabox.app.layoutjsengine.render.ViewRender
import jp.aquagear.layout.compiler.Compiler
import jp.aquagear.layout.compiler.render.compiler.Render
import kotlinx.android.synthetic.main.activity_main.*
import java.io.InputStream

class MainActivity : AppCompatActivity(), JSEngineInterface {
    val jsEngine: JSEngine by lazy { JSEngine(this) }
    val param_url = "url"

    override fun getEngine(): JSEngine {
        return jsEngine
    }

    override fun onPageFinished() {
        val layoutStr = loadFile(intent.getStringExtra(param_url)?.let {
            "$it.vxml"
        } ?: "index/index.vxml")
        val designStr = loadFile(intent.getStringExtra(param_url)?.let {
            "$it.vcss"
        } ?: "index/index.vcss")
        val renders: List<Render>? = Compiler(
            mapOf(
                "view" to ViewRender::class.java,
                "text" to TextRender::class.java
            )
        ).compile(
            layoutStr,
            designStr
        )

        if (renders != null) {
            for (render: Render in renders) {
                if (render is ViewRender) {
                    val o = render.render(this, null)
                    if (o is android.view.View) {
                        root.apply {
                            this.addView(o as android.view.View?)
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jsEngine.loadJS(intent.getStringExtra(param_url)?.let {
            "$it.js"
        } ?: "index/index.js")
    }

    private fun loadFile(scriptFile: String): String {
        val input: InputStream = assets.open(scriptFile)
        val buffer = ByteArray(input.available())
        input.read(buffer)
        input.close()

        return String(buffer, Charsets.UTF_8)
    }


}


