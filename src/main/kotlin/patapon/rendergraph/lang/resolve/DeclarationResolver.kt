package patapon.rendergraph.lang.resolve

import patapon.rendergraph.lang.declarations.*
import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.psi.*

// Responsible for resolving the contents of declarations with resolution scopes (components, functions, constants with initializers...)
class DeclarationResolver(
        private val context: BindingContext,
        private val referenceResolver: ReferenceResolver,
        private val typeResolver: TypeResolver,
        private val d: DiagnosticSink) : RgVisitor() {
    /*fun resolveModulePath(path: RgPath, scope: Scope): ModuleDeclaration
    {
        // first, resolve parent module
        PsiTreeUtil.findChildrenOfType(path, PsiIdentifier::class.java).toTypedArray().dropLast(1)
    }*/

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
        val decl = FunctionDeclarationImpl(owningDeclaration, name ?: UNNAMED_FUNCTION, this, typeResolver, function)
        // resolve parameters
        val params = function.parameterList.mapIndexed { index, param -> resolveValueParameter(param, index, decl, resolutionScope) }
        context.functionDeclarations[function] = decl
        return decl
    }

    fun resolveFunctionBody(function: RgFunction, decl: FunctionDeclaration, resolutionScope: Scope) {
        // build scope of parameters
        val paramScope = SimpleScope(enclosingScope = resolutionScope) {
            function.parameterList.forEach { param -> addDeclaration(context.valueParameters[param]!!) }
        }

        val functionBody = function.functionBody
        if (functionBody != null) {
            val blockBody = functionBody.blockBody
            if (blockBody != null) {
                typeResolver.checkBlock(decl, blockBody, paramScope)
            } else {
                val exprBody = functionBody.expressionBody
                if (exprBody != null) {
                    typeResolver.checkExpression(exprBody, paramScope)
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

    fun resolveVariableDeclaration(variable: RgVariable, owningDeclaration: DeclarationWithResolutionScope): VariableDeclaration {
        val decl = VariableDeclarationImpl(owningDeclaration, variable.name ?: UNNAMED_CONSTANT, typeResolver, variable)
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