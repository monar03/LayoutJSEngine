package jp.aquabox.app.layoutjsengine

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.TextView
import jp.aquagear.layout.compiler.Compiler
import jp.aquagear.layout.compiler.render.compiler.BlockRender
import jp.aquagear.layout.compiler.render.compiler.Render
import jp.aquagear.layout.compiler.render.compiler.StringRender
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val renders: List<Render>? = Compiler(".test { padding:10;}\n .box { orientation : vertical;}")
            .compile(
                "<view class=\"box\"><view class=\"test\">test</view><view>test1</view></view>",
                mapOf("view" to View::class.java)
            )

        if (renders != null) {
            for (render: Render in renders) {
                if (render is View) {
                    val o = render.render(this)
                    if (o is android.view.View) {
                        root.addView(o as android.view.View?)
                    }
                }
            }
        }
    }
}

class View : BlockRender() {
    fun render(context: Context): Any {
        val block = LinearLayout(context)
        for (render: Render in renders) {
            when {
                render is View -> {
                    val o: Any = render.render(context)
                    when {
                        o is StringRender -> {
                            block.addView(TextView(context).apply {
                                setTextViewDesign(o)
                            })
                        }
                        o is android.view.View -> {
                            block.addView(o)
                        }
                    }
                }
                render is StringRender -> {
                    block.addView(TextView(context).apply {
                        setTextViewDesign(render)
                    })
                }
            }
        }
        return block.apply {
            setBlockDesign()
        }
    }

    private fun LinearLayout.setBlockDesign() {
        params["orientation"]?.let {
            when {
                it == "horizon" -> {
                    orientation = LinearLayout.HORIZONTAL
                }
                else -> {
                    orientation = LinearLayout.VERTICAL
                }
            }
        }
    }

    private fun TextView.setTextViewDesign(render: StringRender) {
        text = render.render() as String
        params["padding"]?.let {
            setPadding(
                it.toInt(),
                it.toInt(),
                it.toInt(),
                it.toInt()
            )
        }
    }
}

