package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.resolve.Scope

interface DeclarationWithResolutionScope: Declaration {
    // Scope for the resolution of all child elements of the declaration
    val resolutionScope: Scope
}
