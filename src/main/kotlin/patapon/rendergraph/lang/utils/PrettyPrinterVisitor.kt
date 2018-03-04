package patapon.rendergraph.lang.utils

import patapon.rendergraph.lang.declarations.BindingContext
import patapon.rendergraph.lang.psi.*
import patapon.rendergraph.lang.psi.ext.opType
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
        append("Module '${o.name}' name='${decl.name}' fqName=?")

        appendln()
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

        if (decl != null) { append(" name='${decl.name}'") }
        else { append(" <unresolved>") }

        appendln()

        withIndent {
            o.acceptChildren(this)
        }
    }

    override fun visitVariable(o: RgVariable) {
        val decl = context.variableDeclarations[o]

        append("Variable '${o.name}'")

        if (decl != null) { append(" name='${decl.name}'") }
        else { append(" <unresolved>") }

        appendln()

        withIndent {
            o.initializer?.accept(this)
        }
    }


    override fun visitFunction(o: RgFunction) {
        val decl = context.functionDeclarations[o]

        append("Function '${o.name}'")

        if (decl != null) { append(" name='${decl.name}' returnType=${decl.returnType}") }
        else { append(" <unresolved>") }

        appendln()

        withIndent {
            o.acceptChildren(this)
        }
    }

    override fun visitParameter(o: RgParameter) {
        val decl = context.valueParameters[o]

        append("Parameter '${o.name}'")

        if (decl != null) { append(" name='${decl.name}' type=${decl.type}") }
        else { append(" <unresolved>") }

        appendln()
    }

    override fun visitBinaryExpression(o: RgBinaryExpression) {
        val type = context.expressionTypes[o]

        append("BinaryExpression op=${o.opType}")

        if (type != null) { append(" type=${type}") }

        appendln()

        withIndent {
            o.left.accept(this)
            o.right?.accept(this)
        }
    }

    override fun visitParensExpression(o: RgParensExpression) {
        val type = context.expressionTypes[o]
        append("ParensExpression")

        if (type != null) { append(" type=${type}") }

        appendln()

        withIndent {
            o.expression?.accept(this)
        }
    }

    override fun visitSimpleReferenceExpression(o: RgSimpleReferenceExpression) {
        val type = context.expressionTypes[o]
        append("SimpleReferenceExpression ident='${o.identifier.text}'")

        if (type != null) { append(" type=${type}") }

        appendln()
    }

    override fun visitFloatLiteral(o: RgFloatLiteral) {
        val type = context.expressionTypes[o]
        append("FloatLiteral raw='${o.text}' ")

        if (type != null) { append(" type=${type}") }

        appendln()
    }

    override fun visitDoubleLiteral(o: RgDoubleLiteral) {
        val type = context.expressionTypes[o]
        append("DoubleLiteral raw='${o.text}' ")

        if (type != null) { append(" type=${type}") }

        appendln()
    }

    override fun visitIntLiteral(o: RgIntLiteral) {
        val type = context.expressionTypes[o]
        append("IntLiteral raw='${o.text}' ")

        if (type != null) { append(" type=${type}") }

        appendln()
    }

    override fun visitReturnExpression(o: RgReturnExpression) {
        val type = context.expressionTypes[o]
        append("ReturnExpression")

        if (type != null) { append(" type=${type}") }

        appendln()

        withIndent {
            o.expression?.accept(this)
        }
    }

    override fun visitQualification(o: RgQualification) {
        val type = context.expressionTypes[o]
        append("QualificationExpression ident='${o.identifier.text}'")

        if (type != null) { append(" type=${type}") }

        appendln()

        withIndent {
            o.expression.accept(this)
        }
    }

    override fun visitIfExpression(o: RgIfExpression) {
        val type = context.expressionTypes[o]
        append("IfExpression")

        if (type != null) { append(" type=${type}") }

        appendln()

        withIndent {
            append("+condition=")
            o.condition.accept(this)
            o.thenBranch?.let {
                append("+thenBranch=")
                it.accept(this)
            }
            o.elseBranch?.let {
                append("+elseBranch=")
                it.accept(this)
            }
        }
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
