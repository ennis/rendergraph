package patapon.rendergraph.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.diagnostic.Logger
import com.intellij.psi.PsiReference
import patapon.rendergraph.lang.Compiler
import patapon.rendergraph.lang.psi.RgSimpleReferenceExpression
import patapon.rendergraph.ide.references.RgSimpleReference

abstract class RgSimpleReferenceExpressionImplMixin(node: ASTNode): ASTWrapperPsiElement(node), RgSimpleReferenceExpression {
    companion object {
        var LOG = Logger.getInstance(Compiler::class.java)
    }

    override fun getReference(): PsiReference? {
        RgSimpleReference.LOG.info("enter getReference")
        return RgSimpleReference(this)
    }
}