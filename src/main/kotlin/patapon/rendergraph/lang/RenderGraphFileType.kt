package patapon.rendergraph.lang
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader

import javax.swing.*

class RenderGraphFileType private constructor() : LanguageFileType(RenderGraphLanguage.INSTANCE) {

    override fun getName(): String {
        return "RenderGraph file"
    }

    override fun getDescription(): String {
        return "RenderGraph language file"
    }

    override fun getDefaultExtension(): String {
        return "rg"
    }

    override fun getIcon(): Icon? {
        return FILE_ICON
    }

    companion object {
        val FILE_ICON = IconLoader.getIcon("/patapon/rendergraph/icons/jar-gray.png")
        val INSTANCE = RenderGraphFileType()
    }
}

