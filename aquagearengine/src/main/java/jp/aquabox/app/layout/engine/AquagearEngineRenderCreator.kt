package jp.aquabox.app.layout.engine

import jp.aquabox.app.layout.engine.render.*
import jp.aquabox.layout.compiler.render.RenderCreator
import jp.aquabox.layout.compiler.render.compiler.Render

class AquagearEngineRenderCreator : RenderCreator() {
    override fun create(tagStr: String?): Render {
        when (tagStr) {
            "text" -> return TextRender()
            "scroll-view" -> return ScrollViewRender()
            "image" -> return ImageRender()
            "grid" -> return GridRender()
        }

        return ViewRender()
    }
}
