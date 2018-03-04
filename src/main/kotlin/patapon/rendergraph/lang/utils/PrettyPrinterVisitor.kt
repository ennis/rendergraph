package patapon.rendergraph.lang.utils

import patapon.rendergraph.lang.declarations.BindingContext
import patapon.rendergraph.lang.psi.*
import patapon.rendergraph.lang.resolve.DeclarationResolver
import patapon.rendergraph.lang.types.TypeResolver

class PrettyPrinterVisitor(
        private val declarationResolver: DeclarationResolver,
        private val context: BindingContext,
        private val typeResolver: TypeResolver,
        private val outString: StringBuilder): RgVisitor()
{
    private var indent = 0
    private var mustIndent = false

    override fun visitModule(o: RgModule) {
        val decl = declarationResolver.resolveModuleDeclaration(o)
        decl.members.getAllDeclarations()
        appendln("Module '${o.name}' name '${decl.name}' fqName TODO")
        withIndent {
            o.moduleContents!!.acceptChildren(this)
        }
    }

    override fun visitAttribute(o: RgAttribute) {
        appendln("Attribute '${o.path.text}'")
    }

    override fun visitComponent(o: RgComponent) {
        val decl = context.componentDeclarations[o]

        append("Component '${o.name}'")
        decl?.apply { members.getAllDeclarations() }

        if (decl != null) { appendln(" name '${decl.name}'") }
        else { appendln(" <unresolved>") }

        withIndent {
            o.acceptChildren(this)
        }
    }

    override fun visitVariable(o: RgVariable) {
        val decl = context.variableDeclarations[o]

        append("Variable '${o.name}'")

        if (decl != null) { appendln("name '${decl.name}'") }
        else { appendln(" <unresolved>") }

    }

    override fun visitFunction(o: RgFunction) {
        val decl = context.functionDeclarations[o]

        append("Function '${o.name}'")

        if (decl != null) { appendln(" name '${decl.name}' returnType ${decl.returnType}") }
        else { appendln(" <unresolved>") }

        withIndent {
            o.acceptChildren(this)
        }
    }

    override fun visitParameter(o: RgParameter) {
        val decl = context.valueParameters[o]

        append("Parameter '${o.name}'")

        if (decl != null) { appendln(" name '${decl.name}' returnType ${decl.type}") }
        else { appendln(" <unresolved>") }
    }

    private fun printIndent()
    {
        for (i in 0..indent*2) {
            outString.append(' ')
        }
    }

    private fun appendln(ln: String)
    {
        if (mustIndent) printIndent()
        outString.appendln(ln)
        mustIndent = true
    }

    private fun appendln()
    {
        outString.appendln()
        mustIndent = true
    }

    private fun append(str: String)
    {
        if (mustIndent) printIndent()
        mustIndent = false
        outString.append(str)
    }

    private inline fun withIndent(body: () -> Unit)
    {
        indent++
        body()
        indent--
    }

}
