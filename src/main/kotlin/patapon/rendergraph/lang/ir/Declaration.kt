package patapon.rendergraph.lang.ir

interface Declaration
{
    val name: String
    val attributes: List<Attribute>
    val containingDeclaration: Declaration? get() = null
}