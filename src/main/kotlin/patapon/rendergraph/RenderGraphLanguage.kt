package patapon.rendergraph
import com.intellij.lang.Language
import com.intellij.openapi.util.IconLoader
import javax.swing.*

class RenderGraphLanguage private constructor() : Language("RenderGraph") {
    companion object {
        val INSTANCE = RenderGraphLanguage()
    }
}


