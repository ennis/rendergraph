package patapon.rendergraph.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import patapon.rendergraph.lang.psi.RgModule
import patapon.rendergraph.lang.psi.RgNamedElement
import patapon.rendergraph.lang.psi.RgPathRoot
import patapon.rendergraph.lang.psi.RgQualifiedPath

abstract class RgModuleImplMixin(node: ASTNode): RgNamedElementImpl(node), RgModule {
    override fun getNameIdentifier(): PsiElement? {
        val p = path
        return if (p != null) {
            when (p) {
                is RgPathRoot -> if (p.identifier.text != null) { p.identifier } else { throw IllegalStateException("path root ident is null") }
                is RgQualifiedPath -> p.right
                else -> throw IllegalStateException("unexpected child of path node")
            }
        }
        else {
            throw IllegalStateException("no path for module")
        }
    }

    override fun setName(name: String): PsiElement {
        TODO("not implemented")
    }
}
