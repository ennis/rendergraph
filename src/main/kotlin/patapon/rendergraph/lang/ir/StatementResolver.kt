package patapon.rendergraph.lang.ir

import com.intellij.psi.PsiElement
import patapon.rendergraph.lang.psi.RgReturnStatement

class StatementResolver {
    fun resolveStatement(stmt: PsiElement, scope: Scope): Statement
    {
        return when (stmt) {
            is RgReturnStatement -> resolveReturnStatement(stmt, scope)
            else -> TODO("unimplemented statement")
        }
    }

    private fun resolveReturnStatement(stmt: RgReturnStatement, scope: Scope): Statement {
        return ReturnStatement(IntegerLiteral(0))  // TODO actual expression
    }
}
