package patapon.rendergraph.ide.services

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import patapon.rendergraph.lang.Compiler
import patapon.rendergraph.lang.declarations.BindingContext
import patapon.rendergraph.lang.declarations.BindingContextImpl
import patapon.rendergraph.lang.diagnostics.Diagnostic
import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.psi.RgFile
import patapon.rendergraph.lang.resolve.BodyResolver
import patapon.rendergraph.lang.resolve.DeclarationResolver
import patapon.rendergraph.lang.resolve.ReferenceResolver
import patapon.rendergraph.lang.resolve.TypeResolver

interface RenderGraphCompilerService {
    companion object {
        fun getInstance(project: Project): RenderGraphCompilerService = ServiceManager.getService(project, RenderGraphCompilerService::class.java)!!
    }

    fun analyzeFile(file: RgFile, diagnosticSink: DiagnosticSink): BindingContext

}

class RenderGraphCompilerServiceImpl : RenderGraphCompilerService
{
    override fun analyzeFile(file: RgFile, diagnosticSink: DiagnosticSink): BindingContext
    {
        val bindingContext = BindingContextImpl()
        val referenceResolver = ReferenceResolver(bindingContext, diagnosticSink)
        val typeResolver = TypeResolver(bindingContext, referenceResolver, diagnosticSink)
        val declarationResolver = DeclarationResolver(bindingContext, referenceResolver, typeResolver, diagnosticSink)
        file.acceptChildren(declarationResolver)
        val bodyResolver = BodyResolver(typeResolver, bindingContext, diagnosticSink)
        file.acceptChildren(bodyResolver)
        return bindingContext
    }
}
