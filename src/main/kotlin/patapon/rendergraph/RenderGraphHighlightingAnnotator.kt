package patapon.rendergraph

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import patapon.rendergraph.psi.*
import patapon.rendergraph.psi.ext.RgRenderPassItem

class RenderGraphHighlightingAnnotator: Annotator
{
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {

        var textAttributes =
        when(element) {
            is RgAttribute -> RenderGraphColors.ATTRIBUTE.textAttributesKey
            is RgModulePath -> RenderGraphColors.PRIMITIVE_TYPE.textAttributesKey
            is RgRenderPassItem -> RenderGraphColors.RENDER_PASS_ITEM.textAttributesKey
            else -> null
        }

        if (textAttributes != null) {
            holder.createInfoAnnotation(element, null).textAttributes
        }
    }
}