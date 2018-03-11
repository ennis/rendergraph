package patapon.rendergraph.lang.resolve

import patapon.rendergraph.lang.declarations.BindingContext
import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.diagnostics.error
import patapon.rendergraph.lang.psi.*
import patapon.rendergraph.lang.types.UnresolvedType

private const val DECLARATION_NOT_BUILT: String = "declaration was not built"

class BodyResolver(val typeResolver: TypeResolver, val context: BindingContext, val d: DiagnosticSink): RgVisitor()
{
    override fun visitModule(o: RgModule) {
        o.moduleContents?.acceptChildren(this)
    }

    override fun visitComponent(o: RgComponent) {
        o.acceptChildren(this)
    }

    override fun visitFunction(o: RgFunction) {
        val decl = context.functionDeclarations[o] ?: error(DECLARATION_NOT_BUILT)

        val functionBodyResolver = object: RgVisitor() {
            override fun visitBlock(o: RgBlock) {
                typeResolver.checkBlock(decl, o, decl.resolutionScope)
            }

            override fun visitExpression(o: RgExpression) {
                typeResolver.checkExpression(o, decl.resolutionScope)
            }
        }
        o.functionBody?.acceptChildren(functionBodyResolver)
    }

    override fun visitVariable(o: RgVariable) {
        val decl = context.variableDeclarations[o] ?: error(DECLARATION_NOT_BUILT)
        val initializer = o.initializer
        if (initializer != null) {
            typeResolver.checkExpression(initializer, decl.initializerResolutionScope)
        }
    }
}