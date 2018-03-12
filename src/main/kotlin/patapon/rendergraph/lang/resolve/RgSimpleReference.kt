package patapon.rendergraph.lang.resolve

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiPolyVariantReferenceBase
import com.intellij.psi.ResolveResult
import patapon.rendergraph.ide.services.RenderGraphCompilerService
import patapon.rendergraph.lang.Compiler
import patapon.rendergraph.lang.diagnostics.Diagnostic
import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.psi.RgFile
import patapon.rendergraph.lang.psi.RgSimpleReferenceExpression

class RgSimpleReference(element: RgSimpleReferenceExpression): PsiPolyVariantReferenceBase<PsiElement>(element) {
    companion object {
        var LOG = Logger.getInstance(Compiler::class.java)
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        LOG.info("enter multiResolve")
        val containingFile = element.containingFile
        if (containingFile !is RgFile) return ResolveResult.EMPTY_ARRAY
        val compilerService = RenderGraphCompilerService.getInstance(element.project)
        val diags = object : DiagnosticSink {
            override fun report(diag: Diagnostic) {
                // ignore diagnostics
            }
        }
        val bindingContext = compilerService.analyzeFile(containingFile, diags)
        val decl = bindingContext.simpleReferences[element]
        if (decl != null) {
            val source = decl.sourceElement
            if (source != null) {
                LOG.info("found $element->$source")
                return PsiElementResolveResult.createResults(decl.sourceElement)
            }
        }
        LOG.info("found nothing for $element")
        return ResolveResult.EMPTY_ARRAY
    }

    override fun getVariants(): Array<Any> {
        return arrayOf(element)
    }

}
