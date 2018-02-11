package patapon.rendergraph.ide.annotators

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import patapon.rendergraph.ide.colors.RenderGraphColors
import patapon.rendergraph.lang.psi.*
import patapon.rendergraph.lang.psi.ext.RgRenderPassItem

class RenderGraphHighlightingAnnotator: Annotator
{
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {

        var textAttributes =
        when(element) {
            is RgAttribute -> RenderGraphColors.ATTRIBUTE.textAttributesKey
            is RgRenderPassItem -> RenderGraphColors.RENDER_PASS_ITEM.textAttributesKey
            else -> null
        }

        if (textAttributes != null) {
            holder.createInfoAnnotation(element, null).textAttributes
        }
    }
}