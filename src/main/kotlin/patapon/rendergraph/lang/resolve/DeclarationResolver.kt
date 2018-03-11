package patapon.rendergraph.lang.resolve

import patapon.rendergraph.lang.declarations.*
import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.psi.*
import patapon.rendergraph.lang.types.DeferredType
import patapon.rendergraph.lang.types.UnitType
import patapon.rendergraph.lang.types.UnresolvedType

// Responsible for resolving the contents of declarations with resolution scopes (components, functions, constants with initializers...)
class DeclarationResolver(
        private val context: BindingContext,
        private val referenceResolver: ReferenceResolver,
        private val typeResolver: TypeResolver,
        private val d: DiagnosticSink) : RgVisitor()
{
    override fun visitModule(o: RgModule) {
        val decl = resolveModuleDeclaration(o)
        decl.forceFullResolve()
    }

    fun resolveModuleDeclaration(mod: RgModule): ModuleDeclaration {
        return context.moduleDeclarations.getOrPut(mod) {
            ModuleDeclarationImpl(mod.name ?: UNNAMED_MODULE, this, mod, d)
        }
    }

    fun resolveFunctionDeclaration(
            function: RgFunction,
            owningDeclaration: DeclarationWithResolutionScope,
            resolutionScope: Scope): FunctionDeclaration {
        val name = function.name
        val returnTypeAnnotation = function.returnType

        val decl = FunctionDeclarationImpl(owningDeclaration, name ?: UNNAMED_FUNCTION)

        // build scope of parameters
        val paramScope = SimpleScope(enclosingScope = resolutionScope) {
            function.parameterList.forEachIndexed { i,param -> addDeclaration(resolveValueParameter(param, i, decl, resolutionScope)) }
        }

        val returnType = if (returnTypeAnnotation != null) {
                // function has a return type annotation
                referenceResolver.resolveTypeReference(returnTypeAnnotation, resolutionScope)
            } else {
                val body = function.functionBody
                if (body != null) {
                    val exprBody = body.expressionBody
                    if (exprBody != null) {
                        DeferredType {
                            typeResolver.getExpressionType(exprBody, paramScope)
                        }
                    } else {
                        UnitType
                    }
                } else {
                    UnitType
                }
            }

        decl.bodyResolutionScope = paramScope
        decl.returnType = returnType
        context.functionDeclarations[function] = decl
        return decl
    }

    fun resolveFunctionBody(function: RgFunction, decl: FunctionDeclaration, resolutionScope: Scope) {
        val functionBody = function.functionBody
        if (functionBody != null) {
            val blockBody = functionBody.blockBody
            if (blockBody != null) {
                typeResolver.checkBlock(decl, blockBody, resolutionScope)
            } else {
                val exprBody = functionBody.expressionBody
                if (exprBody != null) {
                    typeResolver.checkExpression(exprBody, resolutionScope)
                } else {
                    throw IllegalStateException("function has no body")
                }
            }
        }
    }

    private fun resolveValueParameter(param: RgParameter, index: Int, owningDeclaration: FunctionDeclaration, resolutionScope: Scope): ValueParameter {
        val decl = ValueParameter(owningDeclaration, param.name
                ?: UNNAMED_VALUE_PARAMETER, referenceResolver.resolveTypeReference(param.type!!, resolutionScope), index)
        context.valueParameters[param] = decl
        return decl
    }

    fun resolveVariableDeclaration(variable: RgVariable, owningDeclaration: DeclarationWithResolutionScope, resolutionScope: Scope): VariableDeclaration {
        val typeAnnotation = variable.type
        val type = if (typeAnnotation != null) {
            referenceResolver.resolveTypeReference(typeAnnotation, resolutionScope)
        } else {
            val initializer = variable.initializer
            if (initializer != null) {
                DeferredType {
                    typeResolver.getExpressionType(initializer, resolutionScope)
                }
            } else {
                UnresolvedType
            }
        }

        val decl = VariableDeclarationImpl(owningDeclaration, variable.name ?: UNNAMED_CONSTANT, type, owningDeclaration.resolutionScope)
        context.variableDeclarations[variable] = decl
        return decl
    }

    fun resolveComponentDeclaration(component: RgComponent, owningDeclaration: DeclarationWithResolutionScope): ComponentDeclaration {
        val decl = ComponentDeclarationImpl(owningDeclaration, component.name ?: UNNAMED_COMPONENT, this, component, d)
        context.componentDeclarations[component] = decl
        return decl
    }

    // Build the resolution scope for a component declaration
    fun buildComponentMemberResolutionScope(declaration: ComponentDeclaration, component: RgComponent): Scope {
        val baseResolutionScope = declaration.resolutionScope
        component.baseComponentList?.pathList?.forEach { path ->
            // query the declaration associated with the base somehow
            // get its 'member scope' attribute
            //val result = resolvePath(path, baseResolutionScope)
            //

        }
        TODO()
    }
}