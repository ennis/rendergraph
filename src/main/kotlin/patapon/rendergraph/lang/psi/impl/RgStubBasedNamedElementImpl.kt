package patapon.rendergraph.lang.psi.impl

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import patapon.rendergraph.lang.psi.RgElementTypes.*
import patapon.rendergraph.lang.stubs.RgNamedStub

// Base class for named elements with stubs
abstract class RgStubBasedNamedElementImpl<StubT> :
        StubBasedPsiElementBase<StubT>,
        PsiNameIdentifierOwner
        where StubT : RgNamedStub, StubT : StubElement<*>
{
    constructor(node: ASTNode) : super(node)
    constructor(stub: StubT, nodeType: IStubElementType<*, *>) : super(stub, nodeType)

    override fun getNameIdentifier(): PsiElement?
            = findChildByType(IDENTIFIER)

    override fun getName(): String? {
        val stub = stub
        return if (stub != null) stub.name else nameIdentifier?.text
    }

    override fun setName(name: String): PsiElement? {
        //nameIdentifier?.replace(RsPsiFactory(project).createIdentifier(name))
        //return this
        TODO()
    }

    override fun getTextOffset(): Int = nameIdentifier?.textOffset ?: super.getTextOffset()

    //override fun getPresentation(): ItemPresentation = getPresentation(this)
}