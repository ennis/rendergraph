package patapon.rendergraph.lang.stubs

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.*
import com.intellij.psi.tree.IStubFileElementType
import patapon.rendergraph.lang.RenderGraphLanguage
import patapon.rendergraph.lang.psi.RgComponent
import patapon.rendergraph.lang.psi.impl.RgComponentImpl

// Base class for PSI element stubs
abstract class RgElementStub<PsiT : PsiElement>(parent: StubElement<*>?, elementType: IStubElementType<out StubElement<*>, *>?) :
        StubBase<PsiT>(parent, elementType)

// Base class for PSI element stubs type objects
abstract class RsStubElementType<StubT : StubElement<*>, PsiT : PsiElement>(debugName: String) :
        IStubElementType<StubT, PsiT>(debugName, RenderGraphLanguage.INSTANCE)
{
    final override fun getExternalId(): String = "rendergraph.${super.toString()}"

    override fun indexStub(stub: StubT, sink: IndexSink) {}

    protected fun createStubIfParentIsStub(node: ASTNode): Boolean {
        val parent = node.treeParent
        val parentType = parent.elementType
        return (parentType is IStubElementType<*, *> && parentType.shouldCreateStub(parent)) ||
                parentType is IStubFileElementType<*>
    }
}


fun factory(name: String): RsStubElementType<*, *> = when (name) {
    "COMPONENT" -> RgComponentStub.Type
    else -> error("Unknown element $name")
}

//=================================================================
// Stub interface for named objects
interface RgNamedStub
{
    val name: String
}

//=================================================================
// Stub class for shader components
class RgComponentStub(parent: StubElement<*>?, elementType: IStubElementType<*, *>, override val name: String) :
        StubBase<RgComponent>(parent, elementType),
        RgNamedStub
{
    object Type : RsStubElementType<RgComponentStub, RgComponent>("COMPONENT") {
        override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?) =
                RgComponentStub(parentStub, this, dataStream.readName()!!.string)

        override fun serialize(stub: RgComponentStub, dataStream: StubOutputStream) =
                with(dataStream) {
                    writeName(stub.name)
                }

        override fun createPsi(stub: RgComponentStub) =
                RgComponentImpl(stub, this)

        override fun createStub(psi: RgComponent, parentStub: StubElement<*>?) =
                RgComponentStub(parentStub, this, psi.identifier.text)
    }
}