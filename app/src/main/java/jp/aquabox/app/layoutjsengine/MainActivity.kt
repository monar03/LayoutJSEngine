package jp.aquabox.app.layoutjsengine

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import jp.aquabox.app.layoutjsengine.jsengine.JSEngine
import jp.aquabox.app.layoutjsengine.view.TextRender
import jp.aquabox.app.layoutjsengine.view.ViewRender
import jp.aquagear.layout.compiler.Compiler
import jp.aquagear.layout.compiler.render.compiler.Render
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), JSEngineInterface {
    val jsEngine: JSEngine by lazy { JSEngine(this) }

    override fun getEngine(): JSEngine {
        return jsEngine
    }

    override fun onPageFinished() {
        val layoutStr = "<view class=\"box\">" +
                "<view tap=\"tap\" class=\"test\">{{test}}</view>" +
                "<view tap=\"tap1\">{{test1}}</view>" +
                "<text>{{test2}}</text>" +
                "</view>"

        val renders: List<Render>? = Compiler(".test { padding:10;}\n .box { orientation : vertical;}")
            .compile(
                layoutStr,
                mapOf(
                    "view" to ViewRender::class.java,
                    "text" to TextRender::class.java
                )
            )

        if (renders != null) {
            for (render: Render in renders) {
                if (render is ViewRender) {
                    val o = render.render(this)
                    if (o is android.view.View) {
                        root.apply {
                            this.addView(o as android.view.View?)
                        }
                    }
                }
            }
        }

        root.addView(TextView(this).apply {
            this.text = layoutStr
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jsEngine.loadJS("data.js")
    }
}


