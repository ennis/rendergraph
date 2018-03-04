package patapon.rendergraph.lang.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.util.PsiTreeUtil
import patapon.rendergraph.lang.declarations.*
import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.diagnostics.error
import patapon.rendergraph.lang.psi.*
import patapon.rendergraph.lang.types.TypeResolver

/*class NameResolver()
{
    // we know the identifier must be a type reference, resolve it
    fun resolveTypeReference(identifier: PsiIdentifier): Type
    {
        // first, check if the identifier already has an associated declaration
        // if not, resolve:
        // - find the parent scope of the identifier (look for a declaration node: findParentOfType)
        // - call getResolutionScope on the containing declaration
        // - perform a scope lookup
    }

    fun getResolutionScope(decl: RgDeclaration): Scope
    {
        // get (or resolve) the declaration descriptor
        //      (the declaration descriptor contains the resolution scope (or at least how to build them))
        // get the resolution scope from there
    }


    // Get the object that holds the synthesized attributes of a declaration
    fun getDeclaration(decl: RgDeclaration): Declaration
    {
        // look in cache
        // not found? create the declaration somehow
        // (recursively create parent declaration nodes)
        TODO()
    }
}*/

fun resolvePath(path: RgPath, scope: Scope): Collection<Declaration>
{
    return scope.findDeclarations(path.toString())
}

//fun resolveAbsoluteModulePath(path: RgPath, rootScope: Scope): ModuleDeclaration
//{
//}

// Responsible for resolving the contents of declarations with resolution scopes (components, functions, constants with initializers...)
class DeclarationResolver(val context: BindingContext, val typeResolver: TypeResolver, val d: DiagnosticSink): RgVisitor()
{


    /*fun resolveModulePath(path: RgPath, scope: Scope): ModuleDeclaration
    {
        // first, resolve parent module
        PsiTreeUtil.findChildrenOfType(path, PsiIdentifier::class.java).toTypedArray().dropLast(1)
    }*/

    fun resolveModuleDeclaration(mod: RgModule): ModuleDeclaration
    {
        return context.moduleDeclarations.getOrPut(mod) {
            ModuleDeclarationImpl(mod.name ?: UNNAMED_MODULE, this, mod, d)
        }
    }

    //fun resolveComponentDeclaration(component: RgComponent): ComponentDeclaration = resolveComponentDeclaration(null, component)
    //fun resolveVariableDeclaration(variable: RgVariable): VariableDeclaration = resolveVariableDeclaration(null, variable)

    fun resolveFunctionDeclaration(
            function: RgFunction,
            owningDeclaration: DeclarationWithResolutionScope,
            resolutionScope: Scope): FunctionDeclaration
    {
        val name = function.name
        val decl = FunctionDeclarationImpl(owningDeclaration, name ?: UNNAMED_FUNCTION, this, typeResolver, function)

        /*if (owningDeclaration is ComponentDeclaration) {
            val inheritanceScope = owningDeclaration.inheritanceScope
            if (name != null) {
                val potentialOverrides = inheritanceScope.findDeclarations(name)
                potentialOverrides.forEach { decl ->
                    // TODO handle overrides
                    d.error("declaration ${name} in component ${owningDeclaration.name} " +
                            "shadows ${decl.name} in base component ${decl.owningDeclaration?.name ?: UNNAMED_COMPONENT}")
                }
            }
        }*/

        context.functionDeclarations.put(function, decl)
        return decl
    }

    fun resolveVariableDeclaration(variable: RgVariable, owningDeclaration: DeclarationWithResolutionScope): VariableDeclaration
    {
        val decl = VariableDeclarationImpl(owningDeclaration, variable.name ?: UNNAMED_CONSTANT, typeResolver, variable)
        context.variableDeclarations.put(variable, decl)
        return decl
    }

    fun resolveComponentDeclaration(component: RgComponent, owningDeclaration: DeclarationWithResolutionScope): ComponentDeclaration
    {
        val decl = ComponentDeclarationImpl(owningDeclaration, component.name ?: UNNAMED_COMPONENT, this, component, d)
        context.componentDeclarations.put(component, decl)
        return decl
    }

    // Called when a function needs a scope for its body
    fun resolveFunctionBodyScope(declaration: FunctionDeclaration, function: RgFunction): Scope
    {
        // build a scope with the arguments
        // also force resolve the parent scope
        //return ScopeImpl(decl)
        TODO()
    }

    // Build the resolution scope for a component declaration
    fun buildComponentMemberResolutionScope(declaration: ComponentDeclaration, component: RgComponent): Scope
    {
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