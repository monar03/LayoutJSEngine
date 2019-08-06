package jp.aquabox.app.layout.engine.view

import android.view.ViewGroup
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import jp.aquabox.app.layout.engine.LayoutModule
import jp.aquabox.layout.compiler.render.lexer.result.StringVariable
import org.hamcrest.core.Is
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class AquagearTextViewTest {
    @Test
    fun `タップできない`() {
        val v = aquagearGridLayout(mock {}, mapOf(), mapOf())
        Assert.assertThat(v.performClick(), Is.`is`(false))
    }

    @Test
    fun `タップできる`() {
        val module: LayoutModule = mock {}
        val v = aquagearGridLayout(
            module,
            mapOf("tap" to StringVariable.variableParse("test")),
            mapOf()
        )
        v.performClick()
        verify(module).tap(any(), any())
    }

    @Test
    fun `Widthをpxで設定できる`() {
        val module: LayoutModule = mock {}
        val v = aquagearGridLayout(
            module,
            mapOf(),
            mapOf("width" to "100px")
        )
        Assert.assertThat(v.layoutParams.width, Is.`is`(100))
    }

    @Test
    fun `WidthをMATCH_PARENTを設定できる`() {
        val module: LayoutModule = mock {}
        val v = aquagearGridLayout(
            module,
            mapOf(),
            mapOf("width" to "fill")
        )
        Assert.assertThat(v.layoutParams.width, Is.`is`(ViewGroup.LayoutParams.MATCH_PARENT))
    }

    @Test
    fun `WidthをWRAP_CONTENTを設定できる`() {
        val module: LayoutModule = mock {}
        val v = aquagearGridLayout(
            module,
            mapOf(),
            mapOf("width" to "wrap")
        )
        Assert.assertThat(v.layoutParams.width, Is.`is`(ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    @Test
    fun `Heightをpxで設定できる`() {
        val module: LayoutModule = mock {}
        val v = aquagearGridLayout(
            module,
            mapOf(),
            mapOf("height" to "100px")
        )
        Assert.assertThat(v.layoutParams.height, Is.`is`(100))
    }

    @Test
    fun `HeigheをMATCH_PARENTを設定できる`() {
        val module: LayoutModule = mock {}
        val v = aquagearGridLayout(
            module,
            mapOf(),
            mapOf("height" to "fill")
        )
        Assert.assertThat(v.layoutParams.height, Is.`is`(ViewGroup.LayoutParams.MATCH_PARENT))
    }

    @Test
    fun `HeigheをWRAP_CONTENTを設定できる`() {
        val module: LayoutModule = mock {}
        val v = aquagearGridLayout(
            module,
            mapOf(),
            mapOf("height" to "wrap")
        )
        Assert.assertThat(v.layoutParams.height, Is.`is`(ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    @Test
    fun `Marginを設定`() {
        val v = aquagearGridLayout(
            mock {},
            mapOf(),
            mapOf("margin" to "2px")
        )
        Assert.assertThat((v.layoutParams as ViewGroup.MarginLayoutParams).topMargin, Is.`is`(2))
        Assert.assertThat((v.layoutParams as ViewGroup.MarginLayoutParams).leftMargin, Is.`is`(2))
        Assert.assertThat((v.layoutParams as ViewGroup.MarginLayoutParams).rightMargin, Is.`is`(2))
        Assert.assertThat((v.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin, Is.`is`(2))
    }

    @Test
    fun `Paddingを設定`() {
        val v = aquagearGridLayout(
            mock {},
            mapOf(),
            mapOf("padding" to "2px")
        )
        Assert.assertThat(v.paddingTop, Is.`is`(2))
        Assert.assertThat(v.paddingStart, Is.`is`(2))
        Assert.assertThat(v.paddingEnd, Is.`is`(2))
        Assert.assertThat(v.paddingBottom, Is.`is`(2))
    }

    private fun aquagearGridLayout(
        module: LayoutModule,
        params: Map<String, StringVariable.Parameter>,
        styles: Map<String, String>
    ): AquagearGridLayout {
        return AquagearGridLayout(RuntimeEnvironment.application).apply {
            set(
                module,
                params,
                styles,
                null
            )
        }
    }
}
