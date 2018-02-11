package patapon.rendergraph.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import patapon.rendergraph.lang.psi.RgNamedElement

abstract class RgNamedElementImpl(node: ASTNode): ASTWrapperPsiElement(node), RgNamedElement {
}