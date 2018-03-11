package patapon.rendergraph.lang.resolve

import patapon.rendergraph.lang.declarations.*
import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.diagnostics.Diagnostics
import patapon.rendergraph.lang.diagnostics.error
import patapon.rendergraph.lang.psi.RgSimpleReferenceExpression
import patapon.rendergraph.lang.psi.RgType
import patapon.rendergraph.lang.types.Type
import patapon.rendergraph.lang.types.UnresolvedType

// TODO: a lot of code is redundant here, simplify
class ReferenceResolver(
        private val context: BindingContext,
        private val d: DiagnosticSink)
{
    fun resolveTypeReference(typeRef: RgType, resolutionScope: Scope): Type {
        val lookupResult = resolvePath(typeRef.path, resolutionScope)
        if (lookupResult.isEmpty()) {
            d.report(Diagnostics.UNDECLARED_IDENTIFIER.with(typeRef, typeRef.text))
            return UnresolvedType
        }

        return if (lookupResult.size > 1) {
            // Ambiguous result
            d.report(Diagnostics.AMBIGUOUS_TYPE_REFERENCE.with(typeRef, typeRef.text))
            UnresolvedType
        } else {
            val typeDecl = lookupResult.first() as? TypeDeclaration
            if (typeDecl != null) {
                context.typeReferences[typeRef] = typeDecl
                typeDecl.type
            } else {
                // Not a type declaration
                d.report(Diagnostics.EXPECTED_X_FOUND_Y.with(typeRef, "type reference", typeRef.text))
                UnresolvedType
            }
        }
    }

    fun resolveFunctionReference(ref: RgSimpleReferenceExpression, resolutionScope: Scope): FunctionDeclaration? {
        val name = ref.identifier.text
        val lookupResult = resolutionScope.findDeclarations(name)

        return if (lookupResult.isEmpty()) {
            d.error("Undeclared identifier: $name", ref)
            null
        }
        else if (lookupResult.size > 1) {
            d.error("Ambiguous reference: $name", ref)
            null
        }
        else {
            val decl = lookupResult.first()
            if (decl !is FunctionDeclaration) {
                d.error("$name does not name a function", ref)
                null
            }
            else {
                context.simpleReferences[ref] = decl
                decl
            }
        }
    }

    fun resolveSimpleVariableReference(ref: RgSimpleReferenceExpression, resolutionScope: Scope): VariableDeclaration? {
        val name = ref.identifier.text
        val lookupResult = resolutionScope.findDeclarations(name)

        return if (lookupResult.isEmpty()) {
            d.error("Undeclared identifier: $name", ref)
            null
        }
        else if (lookupResult.size > 1) {
            d.error("Ambiguous reference: $name", ref)
            null
        }
        else {
            val decl = lookupResult.first()
            if (decl !is VariableDeclaration) {
                d.error("$name does not name a variable", ref)
                null
            }
            else {
                context.simpleReferences[ref] = decl
                decl
            }
        }
    }
}