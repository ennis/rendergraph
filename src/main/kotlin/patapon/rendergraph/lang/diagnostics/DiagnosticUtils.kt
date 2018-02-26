package patapon.rendergraph.lang.diagnostics

import com.intellij.psi.PsiElement

class SimpleDiagnostic(override val severity: Severity, override val message: String, override val psiElement: PsiElement?) : Diagnostic

fun DiagnosticSink.error(message: String, psiElement: PsiElement? = null) {
    report(SimpleDiagnostic(Severity.ERROR, message, psiElement))
}

fun DiagnosticSink.warning(message: String, psiElement: PsiElement? = null) {
    report(SimpleDiagnostic(Severity.WARNING, message, psiElement))
}

fun DiagnosticSink.info(message: String, psiElement: PsiElement? = null) {
    report(SimpleDiagnostic(Severity.INFO, message, psiElement))
}

fun DiagnosticSink.trace(message: String, psiElement: PsiElement? = null) {
    report(SimpleDiagnostic(Severity.TRACE, message, psiElement))
}