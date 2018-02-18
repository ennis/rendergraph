package patapon.rendergraph.lang.ir

class FunctionBody(val containingDeclaration: Declaration, val statements: Lazy<List<Statement>>)
{
}