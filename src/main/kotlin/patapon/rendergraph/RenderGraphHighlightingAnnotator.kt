package patapon.rendergraph

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import patapon.rendergraph.psi.*

class RenderGraphHighlightingAnnotator: Annotator
{
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when(element) {
            is RgAttribute -> {
                holder.createInfoAnnotation(element, null).textAttributes = RenderGraphColors.ATTRIBUTE.textAttributesKey
            }
            is RgModulePath -> {
                holder.createInfoAnnotation(element, null).textAttributes = RenderGraphColors.PRIMITIVE_TYPE.textAttributesKey
            }
        }
    }
}