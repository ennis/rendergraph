package patapon.rendergraph.lang.psi

import com.intellij.psi.tree.IElementType
import patapon.rendergraph.lang.RenderGraphLanguage

class RgElementType(debugName: String) : IElementType(debugName, RenderGraphLanguage.INSTANCE)