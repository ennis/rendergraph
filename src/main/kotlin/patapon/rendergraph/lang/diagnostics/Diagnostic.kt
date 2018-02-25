package patapon.rendergraph.lang.diagnostics

import com.intellij.psi.PsiElement
import java.text.MessageFormat

interface Diagnostic {
    val severity: Severity
    val message: String
    val psiElement: PsiElement?
}
