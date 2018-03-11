package patapon.rendergraph.ide.annotators

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import patapon.rendergraph.ide.services.RenderGraphCompilerService
import patapon.rendergraph.lang.declarations.BindingContext
import patapon.rendergraph.lang.declarations.getFullyQualifiedName
import patapon.rendergraph.lang.diagnostics.Diagnostic
import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.diagnostics.Severity
import patapon.rendergraph.lang.psi.*

class DebugAnnotator: Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val project = element.project

        if (element is RgFile) {
            val diagnosticSink = object : DiagnosticSink {
                val diagnostics = mutableListOf<Diagnostic>()
                override fun report(diag: Diagnostic) {
                    diagnostics.add(diag)
                }
            }

            val bindingContext = RenderGraphCompilerService.getInstance(project).analyzeFile(element, diagnosticSink)
            val visitor = DebugAnnotatorVisitor(bindingContext, holder)
            element.acceptChildren(visitor)

            fun getDiagnosticsForElement(o: PsiElement): Collection<Diagnostic> {
                return diagnosticSink.diagnostics.filter { diag -> diag.psiElement == o }
            }

            val diagnosticHighlighterVisitor = object : RgVisitor() {
                override fun visitElement(element: PsiElement?) {
                    if (element == null) return
                    val diags = getDiagnosticsForElement(element)
                    diags.forEach {
                        when (it.severity) {
                            Severity.TRACE -> {}
                            Severity.INFO -> holder.createInfoAnnotation(element, it.message)
                            Severity.WARNING -> holder.createWarningAnnotation(element, it.message)
                            Severity.ERROR -> holder.createErrorAnnotation(element, it.message)
                            Severity.FATAL -> holder.createErrorAnnotation(element, it.message)
                        }
                    }
                    element.acceptChildren(this)
                }
            }

            element.acceptChildren(diagnosticHighlighterVisitor)
        }
    }
}

class DebugAnnotatorVisitor(
        val bindingContext: BindingContext,
        val holder: AnnotationHolder): RgVisitor()
{

    override fun visitModule(o: RgModule) {
        o.moduleContents?.accept(this)
    }

    override fun visitFunction(o: RgFunction) {
        super.visitFunction(o)
    }

    override fun visitSimpleReferenceExpression(o: RgSimpleReferenceExpression) {
        val ref = bindingContext.simpleReferences[o]
        if (ref != null) {
            holder.createInfoAnnotation(o, ref.getFullyQualifiedName())
        }
        super.visitSimpleReferenceExpression(o)
    }

    override fun visitType(o: RgType) {
        val ref = bindingContext.typeReferences[o]
        if (ref != null) {
            holder.createInfoAnnotation(o, ref.getFullyQualifiedName())
        }
    }

    override fun visitExpression(o: RgExpression) {
        val type = bindingContext.expressionTypes[o]
        if (type != null) {
            holder.createInfoAnnotation(o, type.unwrap().toString())
        }
    }

    override fun visitParameter(o: RgParameter) {
        super.visitParameter(o)
    }

    override fun visitVariable(o: RgVariable) {
        super.visitVariable(o)
    }

    override fun visitElement(element: PsiElement?) {
        element?.acceptChildren(this)
    }
}