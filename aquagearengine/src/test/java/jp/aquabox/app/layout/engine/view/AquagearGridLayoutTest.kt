package jp.aquabox.app.layout.engine.view

import com.nhaarman.mockitokotlin2.mock
import jp.aquabox.app.layout.TestActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AquagearGridLayoutTest {

    @Test
    fun set() {
        val activity = Robolectric.buildActivity(TestActivity::class.java)
        val v = AquagearGridLayout(activity.get()).apply {
            set(
                mock { },
                mapOf(),
                mapOf(),
                null
            )
        }
    }

    @Test
    fun setTemplateRender() {
    }
}