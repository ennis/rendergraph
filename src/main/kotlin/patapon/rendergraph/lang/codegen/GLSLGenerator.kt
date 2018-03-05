package patapon.rendergraph.lang.codegen

import patapon.rendergraph.lang.declarations.BindingContext
import patapon.rendergraph.lang.psi.*
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

    private fun genExpr(expr: RgExpression)
    {
        // TODO
    }

    private fun genReturn(retExpr: RgExpression)
    {
        p.append("return ")
        genExpr(retExpr)
        p.append(";")
    }

    inner class BlockGenerator: RgVisitor()
    {
        override fun visitEmptyStatement(o: RgEmptyStatement) {
            // nothing
        }

        override fun visitExpression(o: RgExpression) {
            genExpr(o)
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

