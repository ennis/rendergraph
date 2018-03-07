package patapon.rendergraph.lang.resolve

import patapon.rendergraph.lang.declarations.Declaration

interface Scope {
    fun getAllDeclarations(): Collection<Declaration>
    fun findDeclarations(name: String): Collection<Declaration>

    //fun getFunctionDeclarations(name: String): Collection<FunctionDeclaration>
    //fun getVariableDeclarations(name: String): Collection<VariableDeclaration>
    //fun getComponentDeclarations(name: String): Collection<ComponentDeclaration>
}
