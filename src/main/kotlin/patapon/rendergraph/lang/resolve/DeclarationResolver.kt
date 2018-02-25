package patapon.rendergraph.lang.resolve

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.util.parentOfType
import patapon.rendergraph.lang.declarations.*
import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.psi.*
import patapon.rendergraph.lang.types.Type
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

// Responsible for resolving the contents of declarations with resolution scopes (components, functions, constants with initializers...)
class DeclarationResolver(val bindingContext: BindingContext, val typeResolver: TypeResolver, val d: DiagnosticSink): RgVisitor()
{
    fun resolveDeclarationWithResolutionScope(decl: RgDeclaration): DeclarationWithResolutionScope
    {
        TODO()
    }

    fun resolveModuleDeclaration(mod: RgModule): ModuleDeclaration
    {
        return bindingContext.moduleDeclarations.getOrPut(mod) {
            ModuleDeclarationImpl(mod.path?.toString() ?: "<unnamed>", this, mod)
        }
    }

    fun resolveComponentDeclaration(component: RgComponent): ComponentDeclaration
    {
        val owningDecl = resolveDeclarationWithResolutionScope(component.parentOfType<RgDeclaration>()!!)
        return resolveComponentDeclaration(owningDecl, component)
    }

    fun resolveConstantDeclaration(constant: RgConstant): ConstantDeclaration
    {
        TODO()
    }

    fun resolveFunctionDeclaration(function: RgFunction): FunctionDeclaration
    {
        TODO()
    }

    fun resolveFunctionDeclaration(owningDeclaration: DeclarationWithResolutionScope, function: RgFunction): FunctionDeclaration
    {
        return bindingContext.functionDeclarations.getOrPut(function) {
            FunctionDeclarationImpl(owningDeclaration, function.identifier.toString(), this, typeResolver, function)
        }
    }

    fun resolveConstantDeclaration(owningDeclaration: DeclarationWithResolutionScope, constant: RgConstant): ConstantDeclaration
    {
        return bindingContext.constantDeclarations.getOrPut(constant) {
            ConstantDeclarationImpl(owningDeclaration, constant.identifier.toString(), typeResolver, constant)
        }
    }

    fun resolveComponentDeclaration(owningDeclaration: DeclarationWithResolutionScope, component: RgComponent): ComponentDeclaration
    {
        return bindingContext.componentDeclarations.getOrPut(component) {
            ComponentDeclarationImpl(owningDeclaration, component.name!!, this, component)
        }
    }

    // Called when a member scope needs to be filled with declarations from the PSI tree
    fun resolveMemberScope(owningDeclaration: DeclarationWithResolutionScope, parent: PsiElement): Scope
    {
        return ScopeImpl(null, owningDeclaration) {
            parent.acceptChildren(object : RgVisitor() {
                override fun visitComponent(o: RgComponent) {
                    addDeclaration(resolveComponentDeclaration(owningDeclaration, o))
                }

                override fun visitConstant(o: RgConstant) {
                    addDeclaration(resolveConstantDeclaration(owningDeclaration, o))
                }

                override fun visitFunction(o: RgFunction) {
                    addDeclaration(resolveFunctionDeclaration(owningDeclaration, o))
                }
            })
        }
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
        component.componentBases?.pathList?.forEach { path ->
            // query the declaration associated with the base somehow
            // get its 'member scope' attribute
            val result = resolvePath(path, baseResolutionScope)
            //

        }
        TODO()
    }
}