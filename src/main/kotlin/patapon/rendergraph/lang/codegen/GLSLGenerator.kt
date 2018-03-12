package patapon.rendergraph.lang.codegen

import com.intellij.psi.PsiElement
import patapon.rendergraph.lang.declarations.BindingContext
import patapon.rendergraph.lang.psi.*
import patapon.rendergraph.lang.psi.ext.Operator
import patapon.rendergraph.lang.psi.ext.opType
import patapon.rendergraph.lang.utils.Printer

class GLSLGenerator(val context: BindingContext, val p: Printer): RgVisitor()
{
    override fun visitModule(o: RgModule) {
        genModule(o)
    }

    private fun genModule(module: RgModule)
    {
        val decl = context.moduleDeclarations[module]
        module.moduleContents?.acceptChildren(ModuleGenerator())
    }

    inner class ModuleGenerator: RgVisitor()
    {
        override fun visitFunction(o: RgFunction) {
            genFunction(o)
        }
    }

    private fun genFunction(function: RgFunction)
    {
        val decl = context.functionDeclarations[function]
        val returnTypeGlsl = function.returnType?.text
        val nameGlsl = function.name!!

        p.append("${returnTypeGlsl} ${nameGlsl}(")
        // n-1 first param
        function.parameterList.forEachIndexed { i, param ->
            val paramTypeGlsl = param.type?.text
            val paramNameGlsl = param.name!!
            if (i == function.parameterList.lastIndex) {
                p.append("${paramTypeGlsl} ${paramNameGlsl}")
            } else {
                p.append("${paramTypeGlsl} ${paramNameGlsl},")
            }
        }
        // body
        p.appendln(")")
        p.appendln("{")
        genFunctionBody(function.functionBody!!)
        p.appendln("}")
        p.appendln()
    }

    fun genBinaryExpr(expr: RgBinaryExpression)
    {
        when (expr.opType)
        {
            Operator.ADD -> { genExpr(expr.left); p.append(" + "); genExpr(expr.right!!); }
            Operator.SUB -> { genExpr(expr.left); p.append(" - "); genExpr(expr.right!!); }
            Operator.MUL -> { genExpr(expr.left); p.append(" * "); genExpr(expr.right!!); }
            Operator.DIV -> { genExpr(expr.left); p.append(" / "); genExpr(expr.right!!); }
            Operator.REM -> { genExpr(expr.left); p.append(" % "); genExpr(expr.right!!); }
            Operator.BIT_AND -> { genExpr(expr.left); p.append(" & "); genExpr(expr.right!!); }
            Operator.BIT_OR -> { genExpr(expr.left); p.append(" | "); genExpr(expr.right!!); }
            Operator.BIT_XOR -> { genExpr(expr.left); p.append(" ^ "); genExpr(expr.right!!); }
            Operator.SHL -> { genExpr(expr.left); p.append(" << "); genExpr(expr.right!!); }
            Operator.SHR -> { genExpr(expr.left); p.append(" >> "); genExpr(expr.right!!); }
            Operator.ASSIGN ->  { genExpr(expr.left); p.append(" = "); genExpr(expr.right!!); }
            Operator.ADD_ASSIGN -> { genExpr(expr.left); p.append(" += "); genExpr(expr.right!!); }
            Operator.SUB_ASSIGN -> { genExpr(expr.left); p.append(" -= "); genExpr(expr.right!!); }
            Operator.MUL_ASSIGN -> { genExpr(expr.left); p.append(" *= "); genExpr(expr.right!!); }
            Operator.DIV_ASSIGN -> { genExpr(expr.left); p.append(" /= "); genExpr(expr.right!!); }
            Operator.REM_ASSIGN -> { genExpr(expr.left); p.append(" %= "); genExpr(expr.right!!); }
            Operator.BIT_AND_ASSIGN -> { genExpr(expr.left); p.append(" &= "); genExpr(expr.right!!); }
            Operator.BIT_OR_ASSIGN -> { genExpr(expr.left); p.append(" |= "); genExpr(expr.right!!); }
            Operator.BIT_XOR_ASSIGN -> { genExpr(expr.left); p.append(" ^= "); genExpr(expr.right!!); }
            Operator.SHL_ASSIGN -> { genExpr(expr.left); p.append(" <<= "); genExpr(expr.right!!); }
            Operator.SHR_ASSIGN -> { genExpr(expr.left); p.append(" >>= "); genExpr(expr.right!!); }
            Operator.EQ -> { genExpr(expr.left); p.append(" == "); genExpr(expr.right!!); }
            Operator.NOT_EQ -> { genExpr(expr.left); p.append(" != "); genExpr(expr.right!!); }
            Operator.LT -> { genExpr(expr.left); p.append(" < "); genExpr(expr.right!!); }
            Operator.LTEQ -> { genExpr(expr.left); p.append(" <= "); genExpr(expr.right!!); }
            Operator.GT -> { genExpr(expr.left); p.append(" > "); genExpr(expr.right!!); }
            Operator.GTEQ -> { genExpr(expr.left); p.append(" >= "); genExpr(expr.right!!); }
        }
    }

    private fun genIdentifier(identifier: PsiElement)
    {
        p.append(identifier.text);
    }

    private fun genLiteral(lit: RgLiteral)
    {
        p.append(lit.text)
    }

    private fun genExpr(expr: RgExpression)
    {
        when (expr)
        {
            is RgBinaryExpression -> genBinaryExpr(expr)
            is RgParensExpression -> { p.append("("); genExpr(expr.expression!!); p.append(")") }
            is RgReturnExpression -> genReturn(expr.expression!!)
            is RgSimpleReferenceExpression -> genIdentifier(expr.referenceNameElement)
        }
    }

    private fun genReturn(retExpr: RgExpression)
    {
        p.append("return ")
        genExpr(retExpr)
        p.append(";")
    }

    private fun genLocalVar(o: RgVariable)
    {
        val decl = context.variableDeclarations[o]
        val typeGlsl = decl?.type?.toString()
        val nameGlsl = decl?.name

        p.appendln("$typeGlsl $nameGlsl");
        if (o.initializer != null) {
            p.appendln(" = ")
            genExpr(o.initializer!!)
        }
    }

    private inner class BlockGenerator: RgVisitor()
    {
        override fun visitEmptyStatement(o: RgEmptyStatement) {
            // nothing
        }

        override fun visitExpression(o: RgExpression) {
            genExpr(o)
            p.appendln(";")
        }

        override fun visitVariable(o: RgVariable) {
            genLocalVar(o)
        }
    }

    fun genFunctionBody(body: RgFunctionBody)
    {
        val blockBody = body.blockBody
        if (blockBody != null) {
            blockBody.acceptChildren(BlockGenerator())
        }
        else {
            val exprBody = body.expressionBody
            if (exprBody != null) {
                genReturn(exprBody)
            }
            else {
                throw IllegalStateException("function without a body in codegen")
            }
        }
    }
}

