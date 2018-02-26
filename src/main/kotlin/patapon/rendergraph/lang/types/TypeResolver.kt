package patapon.rendergraph.lang.types

import com.intellij.psi.PsiElement
import patapon.rendergraph.lang.declarations.BindingContext
import patapon.rendergraph.lang.declarations.ConstantDeclaration
import patapon.rendergraph.lang.declarations.TypeDeclaration
import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.diagnostics.Diagnostics
import patapon.rendergraph.lang.psi.RgParameter
import patapon.rendergraph.lang.psi.RgConstant
import patapon.rendergraph.lang.psi.RgFunction
import patapon.rendergraph.lang.psi.RgType
import patapon.rendergraph.lang.resolve.Scope
import patapon.rendergraph.lang.resolve.resolvePath

class TypeResolver(val bindingContext: BindingContext, val d: DiagnosticSink)
{
    fun checkConstantDeclaration(constant: RgConstant, declarationResolutionScope: Scope, initializerResolutionScope: Scope): Type
    {
        // we have a constant decl node, and we want to know its type:
        // if not found, resolve:
        // resolve the type annotation (get a resolution scope)
        //TODO()
        val typeAnnotation = constant.type
        if (typeAnnotation != null) {
            return resolveTypeReference(typeAnnotation, declarationResolutionScope)
        }
        else {
            return UnresolvedType
        }
    }

    fun resolveTypeReference(typeRef: RgType, resolutionScope: Scope): Type
    {
        val lookupResult = resolvePath(typeRef.path, resolutionScope)
        if (lookupResult.isEmpty()) {
            d.report(Diagnostics.UNDECLARED_IDENTIFIER.with(typeRef, typeRef.text))
            return UnresolvedType
        }
        if (lookupResult.size > 1) {
            // Ambiguous result
            d.report(Diagnostics.AMBIGUOUS_TYPE_REFERENCE.with(typeRef, typeRef.text))
            return UnresolvedType
        }
        else {
            val typeDecl = lookupResult.first() as? TypeDeclaration
            if (typeDecl != null) {
                return typeDecl.type
            }
            else {
                // Not a type declaration
                d.report(Diagnostics.EXPECTED_X_FOUND_Y.with(typeRef, "type reference", typeRef.text))
                return UnresolvedType
            }
        }
    }

    //fun checkConstantDeclaration(constant: RgConstant, decl: ConstantDeclaration, declarationResolutionScope: Scope, initializerResolutionScope: Scope): Type
    //{
    //    TODO()
    //}

    fun checkFunctionReturnType(function: RgFunction, declarationResolutionScope: Scope, bodyResolutionScope: Scope): Type
    {
        // if the type is specified, look it up in the scope
        // otherwise, infer it from the expression body
        // TODO()
        return UnresolvedType
    }

    fun checkFunctionArgumentType(argument: RgParameter, resolutionScope: Scope): Type
    {
        // standard type lookup in the scope
        //TODO()
        return UnresolvedType
    }


}
