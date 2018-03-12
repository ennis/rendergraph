package patapon.rendergraph.lang.psi

import com.intellij.psi.PsiElement

interface RgReferenceElement: PsiElement {
    val referenceNameElement: PsiElement
}

