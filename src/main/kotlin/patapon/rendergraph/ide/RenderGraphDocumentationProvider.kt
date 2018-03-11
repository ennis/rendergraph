package patapon.rendergraph.ide

import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager

class RenderGraphDocumentationProvider: DocumentationProvider
{
    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDocumentationElementForLink(psiManager: PsiManager?, link: String?, context: PsiElement?): PsiElement? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDocumentationElementForLookupItem(psiManager: PsiManager?, `object`: Any?, element: PsiElement?): PsiElement? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUrlFor(element: PsiElement?, originalElement: PsiElement?): MutableList<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
