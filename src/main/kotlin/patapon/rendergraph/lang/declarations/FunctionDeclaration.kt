package patapon.rendergraph.lang.declarations

import patapon.rendergraph.lang.psi.RgFunction
import patapon.rendergraph.lang.resolve.DeclarationResolver
import patapon.rendergraph.lang.resolve.Scope
import patapon.rendergraph.lang.types.Type
import patapon.rendergraph.lang.types.TypeResolver
import patapon.rendergraph.lang.utils.Lazy


interface FunctionDeclaration: DeclarationWithResolutionScope
{
    //val arguments: Collection<Argument>
    val returnType: Type
    val argumentResolutionScope: Scope
    val bodyResolutionScope: Scope
}

class FunctionDeclarationImpl(
        override val owningDeclaration: DeclarationWithResolutionScope,
        override val name: String,
        val declarationResolver: DeclarationResolver,
        val typeResolver: TypeResolver,
        function: RgFunction): FunctionDeclaration
{
    //val body: NullableLazy<FunctionBody>

    override val returnType: Type
        get() = returnType_.value

    val returnType_ = Lazy { typeResolver.checkFunctionReturnType(function, owningDeclaration.resolutionScope, owningDeclaration.resolutionScope) }

    override val bodyResolutionScope: Scope
        get() = bodyResolutionScope_.value

    val bodyResolutionScope_ = Lazy {
        declarationResolver.resolveFunctionBodyScope(this, function)
    }

    override val argumentResolutionScope: Scope
        get() = owningDeclaration.resolutionScope

    override val resolutionScope: Scope
        get() = bodyResolutionScope

    init {
       // body = NullableLazy { null }
    }


    /*private fun resolveFunctionBlockBody(psi: RgFunction, block: RgBlock): List<Statement> {
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
    }*/

}
