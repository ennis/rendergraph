package patapon.rendergraph.lang.utils

import com.intellij.psi.PsiElement
import patapon.rendergraph.lang.declarations.BindingContext
import patapon.rendergraph.lang.declarations.getFullyQualifiedName
import patapon.rendergraph.lang.psi.*
import patapon.rendergraph.lang.psi.ext.opType
import patapon.rendergraph.lang.resolve.DeclarationResolver
import patapon.rendergraph.lang.resolve.TypeResolver

class PrettyPrinterVisitor(
        private val context: BindingContext,
        outString: StringBuilder): RgVisitor()
{
    private val p = Printer(outString)

    override fun visitModule(o: RgModule) {
        val decl = context.moduleDeclarations[o]
        p.append("Module${o.textRange} '${o.name}'")
        if (decl != null) { p.append(" name='${decl.name}' fqName=?") }

        p.appendln()
        p.withIndent {
            o.moduleContents!!.acceptChildren(this)
        }
    }

    override fun visitAttribute(o: RgAttribute) {
        p.appendln("Attribute '${o.path.text}'")
    }

    override fun visitComponent(o: RgComponent) {
        val decl = context.componentDeclarations[o]

        p.append("Component${o.textRange} '${o.name}'")

        if (decl != null) { p.append(" name='${decl.name}'") }
        else { p.append(" <unresolved>") }

        p.appendln()

        p.withIndent {
            o.acceptChildren(this)
        }
    }

    override fun visitVariable(o: RgVariable) {
        val decl = context.variableDeclarations[o]

        p.append("Variable${o.textRange} '${o.name}'")

        if (decl != null)  {
            p.append(" name='${decl.name}' type=${decl.type.unwrap()}")
        }
        else { p.append(" <unresolved>") }

        p.appendln()

        p.withIndent {
            o.initializer?.accept(this)
        }
    }


    override fun visitFunction(o: RgFunction) {
        val decl = context.functionDeclarations[o]

        p.append("Function${o.textRange} '${o.name}'")

        if (decl != null) { p.append(" name='${decl.name}' signature=${decl.signature}") }
        else { p.append(" <unresolved>") }

        p.appendln()

        p.withIndent {
            o.acceptChildren(this)
        }
    }

    override fun visitParameter(o: RgParameter) {
        val decl = context.valueParameters[o]

        p.append("Parameter${o.textRange} '${o.name}'")

        if (decl != null) { p.append(" name='${decl.name}' type=${decl.type.unwrap()}") }
        else { p.append(" <unresolved>") }

        p.appendln()
    }

    override fun visitFunctionBody(o: RgFunctionBody) {
        p.append("FunctionBody${o.textRange}")
        p.appendln()
        p.withIndent {
            o.acceptChildren(this)
        }
    }

    override fun visitBlock(o: RgBlock) {
        p.append("Block${o.textRange}")
        p.appendln()
        p.withIndent {
            o.acceptChildren(this)
        }
    }

    override fun visitStatement(o: RgStatement) {
        p.append("Statement${o.textRange}")
        p.appendln()
        p.withIndent {
            o.acceptChildren(this)
        }
    }

    override fun visitEmptyStatement(o: RgEmptyStatement) {
        p.append("EmptyStatement${o.textRange}")
        p.appendln()
    }

    override fun visitBinaryExpression(o: RgBinaryExpression) {
        val type = context.expressionTypes[o]

        p.append("BinaryExpression${o.textRange} op=${o.opType}")

        type?.let { p.append(" type=$it") }

        p.appendln()

        p.withIndent {
            o.left.accept(this)
            o.right?.accept(this)
        }
    }

    override fun visitParensExpression(o: RgParensExpression) {
        val type = context.expressionTypes[o]
        p.append("ParensExpression${o.textRange}")

        type?.let { p.append(" type=$it") }

        p.appendln()

        p.withIndent {
            o.expression?.accept(this)
        }
    }

    override fun visitSimpleReferenceExpression(o: RgSimpleReferenceExpression) {
        val type = context.expressionTypes[o]
        val ref = context.simpleReferences[o]
        p.append("SimpleReferenceExpression${o.textRange} ref='${o.referenceNameElement.text}'")

        type?.let { p.append(" type=$it") }
        ref?.let { p.append(" target=${it.getFullyQualifiedName()}") }

        p.appendln()
    }

    override fun visitCallExpression(o: RgCallExpression) {
        val type = context.expressionTypes[o]
        p.append("CallExpression${o.textRange}")
        type?.let { p.append(" type=$it") }

        p.appendln()

        p.withIndent {
            p.append("+callable=")
            o.expression.accept(this)
            o.argumentList?.expressionList.orEmpty().forEach {
                p.append("+argument=")
                it.accept(this)
            }
        }

        p.appendln()
    }

    override fun visitExpression(o: RgExpression) {
        p.appendln(o.toString())
    }

    override fun visitFloatLiteral(o: RgFloatLiteral) {
        val type = context.expressionTypes[o]
        p.append("FloatLiteral${o.textRange} raw='${o.text}' ")

        type?.let { p.append(" type=$it") }

        p.appendln()
    }

    override fun visitDoubleLiteral(o: RgDoubleLiteral) {
        val type = context.expressionTypes[o]
        p.append("DoubleLiteral${o.textRange} raw='${o.text}' ")

        type?.let { p.append(" type=$it") }

        p.appendln()
    }

    override fun visitIntLiteral(o: RgIntLiteral) {
        val type = context.expressionTypes[o]
        p.append("IntLiteral${o.textRange} raw='${o.text}' ")

        type?.let { p.append(" type=$it") }

        p.appendln()
    }

    override fun visitReturnExpression(o: RgReturnExpression) {
        val type = context.expressionTypes[o]
        p.append("ReturnExpression${o.textRange}")

        type?.let { p.append(" type=$it") }

        p.appendln()

        p.withIndent {
            o.expression?.accept(this)
        }
    }

    override fun visitQualification(o: RgQualification) {
        val type = context.expressionTypes[o]
        p.append("QualificationExpression${o.textRange} ref='${o.identifier.text}'")

        type?.let { p.append(" type=$it") }

        p.appendln()

        p.withIndent {
            o.expression.accept(this)
        }
    }

    override fun visitIfExpression(o: RgIfExpression) {
        val type = context.expressionTypes[o]
        p.append("IfExpression${o.textRange}")

        type?.let { p.append(" type=$it") }

        p.appendln()

        p.withIndent {
            p.append("+condition=")
            o.condition.accept(this)
            o.thenBranch?.let {
                p.append("+thenBranch=")
                it.accept(this)
            }
            o.elseBranch?.let {
                p.append("+elseBranch=")
                it.accept(this)
            }
        }
    }


}
