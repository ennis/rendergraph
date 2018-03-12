package patapon.rendergraph.ide.references

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiPolyVariantReferenceBase
import patapon.rendergraph.lang.psi.RgReferenceElement

abstract class RgReferenceBase<T : RgReferenceElement>(element: T): PsiPolyVariantReferenceBase<T>(element) {
    final override fun calculateDefaultRangeInElement(): TextRange {
        val anchor = element.referenceNameElement
        check(anchor.parent === element)
        return TextRange.from(anchor.startOffsetInParent, anchor.textLength)
    }
}
