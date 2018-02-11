package patapon.rendergraph.lang.psi
import com.intellij.psi.tree.IElementType
import patapon.rendergraph.lang.RenderGraphLanguage

class RgTokenType(debugName: String): IElementType(debugName, RenderGraphLanguage.INSTANCE) {
    override fun toString(): String {
        return "RgTokenType." + super.toString()
    }

    companion object {
        @JvmField val BLOCK_COMMENT = RgTokenType("<BLOCK_COMMENT>")
        @JvmField val EOL_COMMENT = RgTokenType("<EOL_COMMENT>")
        @JvmField val EOL = RgTokenType("EOL")
    }
}


