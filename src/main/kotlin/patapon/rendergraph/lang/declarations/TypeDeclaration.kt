package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.types.Type

interface TypeDeclaration: Declaration {
    val type: Type
}
