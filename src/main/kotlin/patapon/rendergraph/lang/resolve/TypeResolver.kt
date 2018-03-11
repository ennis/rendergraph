package patapon.rendergraph.lang.resolve

import com.intellij.psi.util.PsiTreeUtil
import patapon.rendergraph.lang.declarations.*
import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.diagnostics.error
import patapon.rendergraph.lang.psi.*
import patapon.rendergraph.lang.psi.ext.Operator
import patapon.rendergraph.lang.psi.ext.opType
import patapon.rendergraph.lang.types.*

class TypeResolver(private val context: BindingContext, private val referenceResolver: ReferenceResolver, private val d: DiagnosticSink)
{
    enum class ValueCategory {
        LVALUE,
        RVALUE
    }

    fun checkVariableDeclaration(variable: RgVariable, declarationResolutionScope: Scope, initializerResolutionScope: Scope): Type {
        // we have a variable decl node, and we want to know its type:
        // if not found, resolve:
        // resolve the type annotation (get a resolution scope)
        //TODO()
        val typeAnnotation = variable.type
        return if (typeAnnotation != null) {
            referenceResolver.resolveTypeReference(typeAnnotation, declarationResolutionScope)
        } else {
            val initializer = variable.initializer
            if (initializer != null) {
                checkExpression(initializer,initializerResolutionScope)
            } else {
                d.error("Cannot deduce local variable type without type annotation or initializer", variable)
                UnresolvedType
            }
        }
    }

    private fun compareTypes(typeA: Type, typeB: Type): Boolean {
        // same instance: this works if both typeA and typeB are primitive types without qualifiers
        // TODO: more complex type checking
        val a = typeA.unwrap()
        val b = typeB.unwrap()
        return when (a) {
        is PrimitiveType -> a == b
        else -> false
        }
    }

    private fun getValueCategory(expr: RgExpression) = when (expr) {
        is RgQualification -> ValueCategory.LVALUE
        is RgBinaryExpression -> {
            if (expr.opType.isAssignment) {
                ValueCategory.LVALUE
            } else {
                ValueCategory.RVALUE
            }
        }
        is RgSimpleReferenceExpression -> ValueCategory.LVALUE
        else -> ValueCategory.RVALUE
    }

    private fun checkLiteralExpr(lit: RgLiteral, resolutionScope: Scope): Type {
        val type = when (lit) {
            is RgFloatLiteral -> FloatType
            is RgIntLiteral -> IntegerType
            is RgDoubleLiteral -> DoubleType
            is RgBoolLiteral -> BooleanType
            is RgUintLiteral -> IntegerType
            else -> throw IllegalStateException("unsupported literal type")
        }
        context.expressionTypes[lit] = type
        return type
    }

    private fun checkReturnExpr(expr: RgReturnExpression, resolutionScope: Scope): NothingType {
        val returnExprType = expr.expression?.let { checkExpression(it, resolutionScope) }
        context.expressionTypes[expr] = NothingType

        // are we in a function?
        val containingDecl = PsiTreeUtil.getParentOfType(expr, RgDeclaration::class.java)
        if (containingDecl != null && containingDecl is RgFunction) {
            // check return type against provided type in function signature
            val functionDecl = context.functionDeclarations[containingDecl]!!
           // functionDecl.returnType

        } else {
            d.error("Return not allowed here", expr)
        }

        return NothingType
    }

    private fun checkEqualityExprTypes(op: Operator, expr: RgBinaryExpression, resolutionScope: Scope): Type {
        val tyLeft = checkExpression(expr.left, resolutionScope)
        val tyRight = checkExpression(expr.right!!, resolutionScope)
        val sameTypes = compareTypes(tyLeft, tyRight)
        if (!sameTypes) {
            d.error("Wrong operand types to $op: left- and right-hand side have different types", expr)
        }
        val tyResult = BooleanType
        context.expressionTypes[expr] = tyResult
        return tyResult
    }

    private fun checkArithmeticExpression(op: Operator, expr: RgBinaryExpression, resolutionScope: Scope): Type {
        // TODO int to float implicit conversions, etc.
        val tyLeft = checkExpression(expr.left, resolutionScope)
        val tyRight = checkExpression(expr.right!!, resolutionScope)
        val sameTypes = compareTypes(tyLeft, tyRight)
        if (!sameTypes) {
            d.error("Wrong operand types to $op: left- and right-hand side have different types", expr)
            return UnresolvedType
        }

        context.expressionTypes[expr] = tyLeft
        return tyLeft
    }

    private fun checkBinaryExpression(expr: RgBinaryExpression, resolutionScope: Scope): Type {
        val op = expr.opType
        return when (expr.opType) {
            Operator.ADD -> checkArithmeticExpression(op, expr, resolutionScope)
            Operator.SUB -> checkArithmeticExpression(op, expr, resolutionScope)
            Operator.MUL -> checkArithmeticExpression(op, expr, resolutionScope)
            Operator.DIV -> checkArithmeticExpression(op, expr, resolutionScope)
            Operator.REM -> TODO()
            Operator.BIT_AND -> TODO()
            Operator.BIT_OR -> TODO()
            Operator.BIT_XOR -> TODO()
            Operator.SHL -> TODO()
            Operator.SHR -> TODO()
            Operator.ASSIGN -> checkAssignExpression(expr, resolutionScope)
            Operator.ADD_ASSIGN -> TODO()
            Operator.SUB_ASSIGN -> TODO()
            Operator.MUL_ASSIGN -> TODO()
            Operator.DIV_ASSIGN -> TODO()
            Operator.REM_ASSIGN -> TODO()
            Operator.BIT_AND_ASSIGN -> TODO()
            Operator.BIT_OR_ASSIGN -> TODO()
            Operator.BIT_XOR_ASSIGN -> TODO()
            Operator.SHL_ASSIGN -> TODO()
            Operator.SHR_ASSIGN -> TODO()
            Operator.EQ -> checkEqualityExprTypes(op, expr, resolutionScope)
            Operator.NOT_EQ -> checkEqualityExprTypes(op, expr, resolutionScope)
            Operator.LT -> TODO()
            Operator.LTEQ -> TODO()
            Operator.GT -> TODO()
            Operator.GTEQ -> TODO()
        }
    }

    private fun checkAssignExpression(expr: RgBinaryExpression, resolutionScope: Scope): Type {
        val tyLeft = checkExpression(expr.left, resolutionScope)
        val tyRight = checkExpression(expr.right!!, resolutionScope)

        if (getValueCategory(expr.left) != ValueCategory.LVALUE) {
            d.error("Left side of assignment is not an lvalue", expr.left)
        }
        if (!compareTypes(tyLeft, tyRight)) {
            d.error("Left and right side of assignment have different types", expr)
        }

        context.expressionTypes[expr] = tyLeft
        return tyLeft
    }

    private fun checkQualification(expr: RgQualification, resolutionScope: Scope): Type {
        val left = expr.expression
        val tyLeft = checkExpression(left, resolutionScope)
        TODO()
    }

    private fun checkCall(expr: RgCallExpression, resolutionScope: Scope): Type
    {
        // resolve function
        val callable = expr.expression as? RgSimpleReferenceExpression ?: error("Arbitrary callable expressions are not supported yet")
        val function = referenceResolver.resolveFunctionReference(callable, resolutionScope) ?: return UnresolvedType
        val signature = function.signature
        // compute argument types
        val args = expr.argumentList?.expressionList.orEmpty().toTypedArray()
        val argTypes = args.map { arg -> checkExpression(arg, resolutionScope) }

        // check signature
        val numParams = signature.argumentTypes.size
        // check first numParams
        for (i in 0 until numParams)
        {
            val paramDecl = function.valueParameters[i]
            val argTy = argTypes.getOrNull(i)
            val paramTy = signature.argumentTypes[i]
            if (argTy == null) {
                // not enough parameters: no value passed for XXX
                d.error("No value passed for parameter ${paramDecl.name}", expr)
            }
            else {
                // compare types
                if (!compareTypes(argTy, paramTy)) {
                    d.error("Type mismatch for parameter ${paramDecl.name} (expected ${paramDecl.type}, got $argTy)", args[i])
                }
            }
        }
        // extraneous parameters
        if (argTypes.size > numParams) {
            d.error("Wrong number of arguments for call to ${function.name}: expected $numParams, got ${argTypes.size}", expr)
        }

        // force resolve function type if it is not evaluated yet
        val returnType = signature.returnType.unwrap()
        context.expressionTypes[expr] = returnType
        return returnType
    }

    fun checkExpression(expr: RgExpression, resolutionScope: Scope): Type {
        return when (expr) {
            is RgBinaryExpression -> checkBinaryExpression(expr, resolutionScope)
            is RgReturnExpression -> checkReturnExpr(expr, resolutionScope)
            is RgLiteral -> checkLiteralExpr(expr, resolutionScope)
            is RgQualification -> checkQualification(expr, resolutionScope)
            is RgSimpleReferenceExpression -> checkSimpleReference(expr, resolutionScope)
            is RgCallExpression -> checkCall(expr, resolutionScope)
            else -> TODO()
        }
    }

    private fun checkSimpleReference(ref: RgSimpleReferenceExpression, resolutionScope: Scope): Type {
        val decl = referenceResolver.resolveSimpleVariableReference(ref, resolutionScope)
        val ty = decl?.type ?: UnresolvedType
        context.expressionTypes[ref] = ty
        return ty
    }

    fun checkBlock(owningDeclaration: DeclarationWithResolutionScope, block: RgBlock, resolutionScope: Scope): Type {
        class BindingScope(val parent: Scope, val decl: VariableDeclaration) : Scope {
            override fun findDeclarations(name: String): Collection<Declaration> {
                return if (name == decl.name) {
                    listOf(decl)
                } else parent.findDeclarations(name)
            }

            override fun getAllDeclarations(): Collection<Declaration> {
                val decls = mutableListOf<Declaration>(decl)
                parent.getAllDeclarations().toCollection(decls)
                return decls
            }
        }

        var curScope = resolutionScope

        block.statementList.forEach { stmt ->
            stmt.expression?.let { expr -> checkExpression(expr, curScope) }
            stmt.variable?.let { variable ->
                val varType = checkVariableDeclaration(variable, curScope, curScope)
                val varDecl = VariableDeclarationImpl(owningDeclaration, variable.name!!, varType, curScope)
                // introduce new binding
                curScope = BindingScope(curScope, varDecl)
            }
            stmt.emptyStatement?.let { /* nothing to do */ }
        }

        return UnitType
    }

    fun getExpressionType(expr: RgExpression, resolutionScope: Scope): Type {
        return context.expressionTypes.getOrElse(expr) {
            checkExpression(expr, resolutionScope)
        }
    }
}
