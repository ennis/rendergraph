package patapon.rendergraph.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import patapon.rendergraph.lang.RenderGraphFileType
import patapon.rendergraph.lang.RenderGraphLanguage

import javax.swing.*

class RgFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, RenderGraphLanguage.INSTANCE) {
    override fun getFileType(): FileType {
        return RenderGraphFileType.INSTANCE
    }

    override fun toString(): String {
        return "RenderGraph File"
    }

    override fun getIcon(flags: Int): Icon? {
        return super.getIcon(flags)
    }
}