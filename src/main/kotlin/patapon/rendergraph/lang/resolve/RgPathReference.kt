package patapon.rendergraph.lang.resolve

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import patapon.rendergraph.lang.psi.RgComponent
import java.util.ArrayList
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.PsiManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.FileTypeIndex
import com.intellij.util.indexing.FileBasedIndex
import patapon.rendergraph.lang.RenderGraphFileType
import patapon.rendergraph.lang.psi.RgFile

class RgPathReference(element: PsiElement, textRange: TextRange): PsiReferenceBase<PsiElement>(element, textRange)
{
    companion object {
        val LOG = Logger.getInstance(RgPathReference::class.java)
    }

    override fun resolve(): PsiElement? {
        val project = element.project
        var result: MutableList<RgComponent>? = null
        val virtualFiles = FileBasedIndex.getInstance().getContainingFiles<FileType, Void>(FileTypeIndex.NAME, RenderGraphFileType.INSTANCE,
                GlobalSearchScope.allScope(project))
        for (virtualFile in virtualFiles) {
            val simpleFile = PsiManager.getInstance(project).findFile(virtualFile) as RgFile?
            if (simpleFile != null) {
                val components = PsiTreeUtil.getChildrenOfType(simpleFile, RgComponent::class.java)
                if (components != null) {
                    for (c in components) {
                        return c
                    }
                }
            }
        }
        return null
    }

    override fun getVariants(): Array<Any> {
        TODO("not implemented")
    }
}