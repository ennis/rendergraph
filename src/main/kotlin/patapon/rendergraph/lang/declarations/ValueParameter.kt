package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.types.Type
import patapon.rendergraph.lang.types.TypeResolver
import patapon.rendergraph.lang.psi.RgParameter
import patapon.rendergraph.lang.utils.Lazy

class ValueParameter(
        override val owningDeclaration: FunctionDeclaration,
        override val name: String,
        override val type: Type,
        val index: Int): VariableDeclaration
