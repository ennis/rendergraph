package patapon.rendergraph.lang.diagnostics

import com.intellij.psi.PsiElement
import java.text.MessageFormat

open class DiagnosticFactory(val severity: Severity, val messageFormat: String)
{
    fun createAt(psiElement: PsiElement?, vararg arguments: Any): Diagnostic {
        return object : Diagnostic {
            override val severity = this@DiagnosticFactory.severity
            override val message: String = MessageFormat.format(messageFormat, *arguments)
            override val psiElement = psiElement
        }
    }

    fun create(vararg arguments: Any) = createAt(null, arguments)
}

class DiagnosticFactory1<A: Any>(severity: Severity, messageFormat: String): DiagnosticFactory(severity, messageFormat)
{
    fun with(psiElement: PsiElement?, a: A) = createAt(psiElement, a)
}

class DiagnosticFactory2<A: Any, B: Any>(severity: Severity, messageFormat: String): DiagnosticFactory(severity, messageFormat)
{
    fun with(psiElement: PsiElement?, a: A, b: B) = createAt(psiElement, a, b)
}

class DiagnosticFactory3<A: Any, B: Any, C: Any>(severity: Severity, messageFormat: String): DiagnosticFactory(severity, messageFormat)
{
    fun with(psiElement: PsiElement?, a: A, b: B, c: C) = createAt(psiElement, a, b, c)
}

