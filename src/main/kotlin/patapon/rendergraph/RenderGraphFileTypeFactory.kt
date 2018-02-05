package patapon.rendergraph
import com.intellij.openapi.fileTypes.*

public class RenderGraphFileTypeFactory: FileTypeFactory() {
    override fun createFileTypes(fileTypeConsumer: FileTypeConsumer) {
        fileTypeConsumer.consume(RenderGraphFileType.INSTANCE, "rg")
    }
}
