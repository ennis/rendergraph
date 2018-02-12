package patapon.rendergraph.lang
import com.intellij.lang.Language

class RenderGraphLanguage private constructor() : Language("RenderGraph") {
    companion object {
        val INSTANCE = RenderGraphLanguage()
    }
}


