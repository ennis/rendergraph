package patapon.rendergraph.psi

import com.intellij.psi.tree.IElementType
import patapon.rendergraph.RenderGraphLanguage
import org.jetbrains.annotations.*

class RgElementType(debugName: String) : IElementType(debugName, RenderGraphLanguage.INSTANCE)