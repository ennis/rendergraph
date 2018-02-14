# NOTES

* Packages
    * lang: core language functionality, IDE-independent
        * parser: parser implementation
        * psi: PSI structure
        * resolve: name resolution utilities and reference classes
        * sema: semantic checks and annotators
    * ide: everything IDE-related
        * visual: structure graph view (on hold)
            * graph-based refactoring (communicates with lang)
        * syntax highlighters, annotators, etc.
        * plugin extension classes
    * visual: tool windows, graph view (on hold)
    * parser: parser implementation
    * compiler
        * sema: semantic checks and annotators
    * ide: annotators, syntax highlighters, plugin extension classes

* PSI/AST structure
    * The base PSI interfaces (`Rg*`) can inherit from custom interfaces, and (through several layers) from `StubBasedPsiElement<StubT>`
    * `StubBasedPsiElement<StubT>` is implemented in the PSI impl by inheriting from `Rg*MixinImpl`, which in turn inherits from `StubBasedPsiElementBase<StubT>`
    * PSI nodes with a name can implement `PsiNameIdentifierOwner` by inheriting from `RgStubBasedNamedElementImpl` in the mixin `Rg*MixinImpl`
        * `RgStubBasedNamedElementImpl` implements `PsiNameIdentifierOwner` by looking in the stub first, and then automatically by finding the first child PSI element with type IDENTIFIER
    * The inheritance structure is like so:
```
Rg*Impl (generated)
    (implementation tree) -> Rg*ImplMixin -> RgStubBasedNamedElementImpl<StubT> (optional) -> StubBasedPsiElementBase<StubT> (internal)
                                                                                           -> PsiNameIdentifierOwner (implements)
                          -> (implements) Rg*
    (interface tree) -> Rg* (node interface) -> RgNamedElement (currently omitted) -> PsiNameIdentifierOwner
                                             -> StubBasedPsiElement<StubT(specified in grammar)>
```

* Context/Semantic checking
    * Symbol resolution (first at the item level, then in functions)
        * Add all symbols in scope (Scope classes: GlobalScope, ComponentScope, FunctionScope)
            * map name -> declaration PSI (inherits from RgDeclaration/RgNamedElement)
        * When entering a scope, push it on the ScopeStack
            * symbol resolution is done in the ScopeStack
        * Symbol resolution: walk the scope stack, starting from the bottom
            * Can filter out some kinds of declarations
        * Verify that the declaration kind is expected
        * Cache the resolution result in the reference
    * Resolver: PSI -> Descriptor tree
        * All references are resolved in the descriptor tree

* Code generation