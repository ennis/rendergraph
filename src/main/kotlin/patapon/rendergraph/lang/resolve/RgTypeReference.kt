package patapon.rendergraph.lang.resolve

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import patapon.rendergraph.ide.services.RenderGraphCompilerService
import patapon.rendergraph.lang.diagnostics.Diagnostic
import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.psi.RgFile
import patapon.rendergraph.lang.psi.RgType

class RgTypeReference(element: RgType): PsiPolyVariantReferenceBase<PsiElement>(element) {
    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val containingFile = element.containingFile
        if (containingFile !is RgFile) return ResolveResult.EMPTY_ARRAY
        val compilerService = RenderGraphCompilerService.getInstance(element.project)
        val diags = object : DiagnosticSink {
            override fun report(diag: Diagnostic) {
                // ignore diagnostics
            }
        }
        val bindingContext = compilerService.analyzeFile(containingFile, diags)
        val decl = bindingContext.typeReferences[element]
        if (decl != null) {
            val source = decl.sourceElement
            if (source != null) {
                return PsiElementResolveResult.createResults(decl.sourceElement)
            }
        }
        return ResolveResult.EMPTY_ARRAY
    }

    override fun getVariants(): Array<Any> {
        return arrayOf(element.text)
    }
}
