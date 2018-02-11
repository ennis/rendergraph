package patapon.rendergraph.lang
import com.intellij.openapi.fileTypes.*
import patapon.rendergraph.lang.RenderGraphFileType

public class RenderGraphFileTypeFactory: FileTypeFactory() {
    override fun createFileTypes(fileTypeConsumer: FileTypeConsumer) {
        fileTypeConsumer.consume(RenderGraphFileType.INSTANCE, "rg")
    }
}
