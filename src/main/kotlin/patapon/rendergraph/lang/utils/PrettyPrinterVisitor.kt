package patapon.rendergraph.lang.utils

import com.intellij.psi.PsiFile
import patapon.rendergraph.lang.declarations.ComponentDeclaration
import patapon.rendergraph.lang.declarations.Declaration
import patapon.rendergraph.lang.declarations.FunctionDeclaration
import patapon.rendergraph.lang.declarations.ModuleDeclaration
import patapon.rendergraph.lang.psi.*
import patapon.rendergraph.lang.resolve.DeclarationResolver
import patapon.rendergraph.lang.types.TypeResolver

class PrettyPrinterVisitor(
        val declarationResolver: DeclarationResolver,
        val typeResolver: TypeResolver,
        val outString: StringBuilder): RgVisitor()
{
    var indent = 0
    var mustIndent = false

    override fun visitModule(o: RgModule) {
        val decl = declarationResolver.resolveModuleDeclaration(o)
        appendln("Module '${o.name}' name '${decl.name}' fqName TODO")
        withIndent {
            o.moduleContents!!.acceptChildren(this)
        }
    }

    override fun visitAttribute(o: RgAttribute) {
        appendln("Attribute '${o.path.text}'")
    }

    override fun visitComponent(o: RgComponent) {
        val decl = declarationResolver.resolveComponentDeclaration(o)
        appendln("Component '${o.name}' name '${decl.name}'")
        withIndent {
            o.acceptChildren(this)
        }
    }

    override fun visitConstant(o: RgConstant) {
        val decl = declarationResolver.resolveConstantDeclaration(o)
        appendln("Constant '${o.name}' name '${decl.name}' type ${decl.type}")
    }

    override fun visitFunction(o: RgFunction) {
        val decl = declarationResolver.resolveFunctionDeclaration(o)
        appendln("Function '${o.name}' name '${decl.name}' returnType ${decl.returnType}")
        withIndent {
            o.acceptChildren(this)
        }
    }

    override fun visitParameter(o: RgParameter) {
        appendln("Parameter '${o.name}'")
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

    inline fun withIndent(body: () -> Unit)
    {
        indent++
        body()
        indent--
    }

}
