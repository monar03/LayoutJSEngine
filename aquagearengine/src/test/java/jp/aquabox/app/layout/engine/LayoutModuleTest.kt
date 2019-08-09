package jp.aquabox.app.layout.engine

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import jp.aquabox.app.layout.engine.view.AquagearLayout
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LayoutModuleTest {
    @Before
    fun setUp() {
    }

    @Test
    fun `入力に合わせてAquagearViewが生成される`() {
        val command: AquagearCommand = mock {}
        createLayoutModule(command) {
            Assert.assertThat(it, IsInstanceOf.instanceOf(AquagearLayout::class.java))
        }
    }

    @Test
    fun `LayoutModuleのupdateメソッドが呼ばれた時にAquagearCommandのupdateが呼ばれる`() {
        val command: AquagearCommand = mock {}
        createLayoutModule(command) { }.update("test")
        verify(command).update(eq("test"), any())
    }

    private fun createLayoutModule(command: AquagearCommand, onLoad: (LayoutModule) -> Unit): LayoutModule {
        val data = LayoutModule.LayoutModuleData(
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
        return LayoutModuleImpl(
            command,
            data,
            onLoad
        )
    }
}