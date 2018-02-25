package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.types.Type

interface ValueDeclaration: Declaration {
    val type: Type
}
