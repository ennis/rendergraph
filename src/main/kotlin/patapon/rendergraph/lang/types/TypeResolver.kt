package patapon.rendergraph.lang.types

import patapon.rendergraph.lang.declarations.BindingContext
import patapon.rendergraph.lang.declarations.TypeDeclaration
import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.diagnostics.Diagnostics
import patapon.rendergraph.lang.diagnostics.error
import patapon.rendergraph.lang.psi.*
import patapon.rendergraph.lang.psi.ext.Operator
import patapon.rendergraph.lang.psi.ext.opType
import patapon.rendergraph.lang.resolve.Scope
import patapon.rendergraph.lang.resolve.resolvePath

class TypeResolver(val context: BindingContext, val d: DiagnosticSink) {
    fun checkVariableDeclaration(variable: RgVariable, declarationResolutionScope: Scope, initializerResolutionScope: Scope): Type {
        // we have a variable decl node, and we want to know its type:
        // if not found, resolve:
        // resolve the type annotation (get a resolution scope)
        //TODO()
        val typeAnnotation = variable.type
        if (typeAnnotation != null) {
            return resolveTypeReference(typeAnnotation, declarationResolutionScope)
        } else {
            return UnresolvedType
        }
    }

    fun resolveTypeReference(typeRef: RgType, resolutionScope: Scope): Type {
        val lookupResult = resolvePath(typeRef.path, resolutionScope)
        if (lookupResult.isEmpty()) {
            d.report(Diagnostics.UNDECLARED_IDENTIFIER.with(typeRef, typeRef.text))
            return UnresolvedType
        }
        if (lookupResult.size > 1) {
            // Ambiguous result
            d.report(Diagnostics.AMBIGUOUS_TYPE_REFERENCE.with(typeRef, typeRef.text))
            return UnresolvedType
        } else {
            val typeDecl = lookupResult.first() as? TypeDeclaration
            if (typeDecl != null) {
                context.typeReferences.put(typeRef, typeDecl)
                return typeDecl.type
            } else {
                // Not a type declaration
                d.report(Diagnostics.EXPECTED_X_FOUND_Y.with(typeRef, "type reference", typeRef.text))
                return UnresolvedType
            }
        }
    }

    fun checkFunctionReturnType(function: RgFunction, declarationResolutionScope: Scope, bodyResolutionScope: Scope): Type {
        val returnType = function.returnType
        if (returnType != null) {
            return resolveTypeReference(returnType, declarationResolutionScope)
        }
        return UnresolvedType
    }

    fun checkFunctionArgumentType(parameter: RgParameter, resolutionScope: Scope): Type {
        return resolveTypeReference(parameter.type!!, resolutionScope)
    }

    fun compareTypes(typeA: Type, typeB: Type): Boolean {
        // same instance: this works if both typeA and typeB are primitive types without qualifiers
        if (typeA == typeB) {
            return true
        } else {
            // TODO: more complex type checking
            return false
        }
    }

    fun checkLiteralExpr(lit: RgLiteral): Type {
        val type = when (lit) {
            is RgFloatLiteral -> FloatType
            is RgIntLiteral -> IntegerType
            is RgDoubleLiteral -> DoubleType
            is RgBoolLiteral -> BooleanType
            is RgUintLiteral -> IntegerType
            else -> throw IllegalStateException("unsupported literal type")
        }
        context.expressionTypes.put(lit, type)
        return type
    }

    fun checkReturnExpr(expr: RgReturnExpression): NothingType {
        // typecheck the return value
        val returnValueExpr = expr.expression
        if (returnValueExpr != null)
            checkExpression(returnValueExpr)
        context.expressionTypes.put(expr, NothingType)
        return NothingType
    }

    fun checkEqualityExprTypes(op: Operator, tyLeft: Type, tyRight: Type): Type {
        val sameTypes = compareTypes(tyLeft, tyRight)
        if (!sameTypes) {
            d.error("Wrong operand types to ${op}: the two subexpressions do not have the same type")
        }
        return BooleanType
    }

    fun checkBinaryExpression(expr: RgBinaryExpression): Type {
        // TYPECHECK: left and right subexpressions must have the same types
        val op = expr.opType
        val tyLeft = checkExpression(expr.left)
        val tyRight = checkExpression(expr.right!!)

        val tyResult = when (op) {
            Operator.ADD -> TODO()
            Operator.SUB -> TODO()
            Operator.MUL -> TODO()
            Operator.DIV -> TODO()
            Operator.REM -> TODO()
            Operator.BIT_AND -> TODO()
            Operator.BIT_OR -> TODO()
            Operator.BIT_XOR -> TODO()
            Operator.SHL -> TODO()
            Operator.SHR -> TODO()
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
            Operator.EQ -> checkEqualityExprTypes(op, tyLeft, tyRight)
            Operator.NOT_EQ -> checkEqualityExprTypes(op, tyLeft, tyRight)
            Operator.LT -> TODO()
            Operator.LTEQ -> TODO()
            Operator.GT -> TODO()
            Operator.GTEQ -> TODO()
        }

        context.expressionTypes.put(expr, tyResult)
        return tyResult
    }

    fun checkExpression(expr: RgExpression): Type {
        return when (expr) {
            is RgBinaryExpression -> checkBinaryExpression(expr)
            is RgReturnExpression -> checkReturnExpr(expr)
            is RgLiteral -> checkLiteralExpr(expr)
            else -> TODO()
        }
    }

}

