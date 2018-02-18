package patapon.rendergraph.lang.ir

import patapon.rendergraph.lang.psi.RgBlock
import patapon.rendergraph.lang.psi.RgExpression
import patapon.rendergraph.lang.psi.RgFunction

class Function(
        val psi: RgFunction,
        override val containingDeclaration: Declaration,
        val enclosingScope: LazyValue<Scope>): LazyDeclaration
{
    val arguments: List<Argument>
    val _returnType: LazyValue<Type>
    val body: NullableLazy<FunctionBody>
    val bodyResolutionScope = Lazy<Scope> {
        buildBodyResolutionScope()
    }

    val returnType: Type
        get() = _returnType.value

    init {
        arguments = psi.argumentList.map { psiArgument -> Argument(psiArgument, this) }
        _returnType = Lazy { resolveReturnType() }
        body = NullableLazy { resolveBody() }
    }

    override val name = psi.identifier.text!!

    override val attributes: List<Attribute>
        get() = TODO("not implemented")

    override fun forceFullResolve() {
        _returnType.doResolve()
        body.doResolve()
        arguments.forEach { arg -> arg.forceFullResolve() }
    }

    private fun buildBodyResolutionScope(): Scope {
        return ScopeImpl(enclosingScope, this) {
            arguments.forEach { arg -> addDeclaration(arg) }
        }
    }

    private fun resolveReturnType(): Type
    {
        var psiReturnType = psi.type
        if (psiReturnType == null) {
            TODO("infer return type from expression body")
        }

        var resolver = TypeResolver(bodyResolutionScope.value)
        return resolver.resolveType(psiReturnType)
    }

    private fun resolveBody(): FunctionBody?
    {
        val body = psi.functionBody
        if (body != null) {
            return FunctionBody(containingDeclaration, Lazy {
                val block = body.block
                if (block != null) {
                    resolveFunctionBlockBody(psi, block)
                }
                else {
                    val expr = body.expression
                    resolveFunctionExpression(psi, expr!!)
                    // set returnType <- expr.type
                    TODO()
                }
            })
        } else {
            return null
        }
    }

    private fun resolveFunctionExpression(psi: RgFunction, expr: RgExpression): Expression {
        TODO("function expressions are not implemented")
    }

    private fun resolveFunctionBlockBody(psi: RgFunction, block: RgBlock): List<Statement> {
        val stmtResolver = StatementResolver()
        val outStmts = arrayListOf<Statement>()
        val blockScope = TODO("block scope") // TODO build the block scope from the parentScope and the argument scope
        block.statementList.forEach { stmt ->
            if (stmt.emptyStatement != null) {
                // do nothing
            } else if (stmt.returnStatement != null) {
                stmtResolver.resolveStatement(stmt.returnStatement!!, blockScope)
                // CONTEXT(XX.X): Warning: two return statements in the same control flow branch
                // TYPECHECK(XX.X): the type of the return expression must be compatible with the type specified in the signature
            } else {
                throw IllegalStateException("invalid statement")
            }
        }
    }

}
