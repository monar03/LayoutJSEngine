package jp.aquabox.app.layout.engine

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import jp.aquabox.app.layout.engine.view.AquagearLayout
import org.hamcrest.core.Is
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowWebView

@RunWith(RobolectricTestRunner::class)
class AquagearEngineTest {

    @Test
    @Ignore
    @Config(manifest = "src/main/AndroidManifest.xml")
    fun `AquagearEngineでモジュールをロードする`() {
        val engine = AquagearEngine(
            RuntimeEnvironment.application,
            AquagearModules()
        ) {
            it.loadModule(
                "test",
                LayoutModule.LayoutModuleData(
                    "<view class=\"box\">\n" +
                            "   <text class=\"text\" tap=\"tap\">{{text1}}</text>\n" +
                            "   <text class=\"text\">{{text2}}</text>\n" +
                            "</view>\n",
                    ".box {\n" +
                            "    orientation : horizon;\n" +
                            "    width :fill;\n" +
                            "}\n" +
                            "\n" +
                            ".text {\n" +
                            "    width: 0dp;\n" +
                            "    textSize:5sp;\n" +
                            "    foreground : gray;\n" +
                            "    weight: 1;\n" +
                            "}\n",
                    "{\n" +
                            "  data: {\n" +
                            "    text1: \"aaaaaa1\",\n" +
                            "    text2: \"bbbbbb2\"\n" +
                            "  },\n" +
                            "  tap: function(data) {\n" +
                            "    this.setData(\"text2\", \"ccccccc2\")\n" +
                            "  }\n" +
                            "}"
                )
            ) {
                Assert.assertThat(it, IsInstanceOf.instanceOf(AquagearLayout::class.java))
            }
        }

        val swebview: ShadowWebView = Shadows.shadowOf(engine.webView)
        Assert.assertThat(swebview.lastLoadDataWithBaseURL.baseUrl, Is.`is`("file:///android_asset"))
        //swebview.webViewClient.onPageFinished(engine.webView, "file:///android_asset")
    }

    @Test
    @Config(manifest = "src/main/AndroidManifest.xml")
    fun `モジュールの更新を行う`() {
        val engine = AquagearEngine(
            RuntimeEnvironment.application,
            mock {}
        ) {
            it.modules["test"] = mock { }
            it.update("test", "key")
            verify(it.modules["test"])?.update("key")
        }
        val swebview: ShadowWebView = Shadows.shadowOf(engine.webView)
        swebview.webViewClient.onPageFinished(engine.webView, "file:///android_asset")
    }
}