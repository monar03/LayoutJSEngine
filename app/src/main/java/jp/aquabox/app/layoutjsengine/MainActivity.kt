package jp.aquabox.app.layoutjsengine

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import jp.aquabox.app.layoutjsengine.jsengine.JSEngine
import jp.aquabox.app.layoutjsengine.view.TouchEventListener
import jp.aquabox.app.layoutjsengine.view.ViewRender
import jp.aquagear.layout.compiler.Compiler
import jp.aquagear.layout.compiler.render.compiler.Render
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TouchEventListener, JSEngineInterface {
    val jsEngine: JSEngine by lazy { JSEngine(this) }

    override fun getEngine(): JSEngine {
        return jsEngine
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val renders: List<Render>? = Compiler(".test { padding:10;}\n .box { orientation : vertical;}")
            .compile(
                "<view tap=\"test\" class=\"box\"><view class=\"test\">test</view><view>test1</view></view>",
                mapOf("view" to ViewRender::class.java)
            )

        if (renders != null) {
            for (render: Render in renders) {
                if (render is ViewRender) {
                    val o = render.render(this)
                    if (o is android.view.View) {
                        root.addView(o as android.view.View?)
                    }
                }
            }
        }
    }

    override fun onTap(viewRender: ViewRender) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}


