package patapon.rendergraph.lang.declarations

fun Declaration.getFullyQualifiedName(): String {
    return owningDeclaration?.getFullyQualifiedName()?.plus(".$name") ?: name
}
