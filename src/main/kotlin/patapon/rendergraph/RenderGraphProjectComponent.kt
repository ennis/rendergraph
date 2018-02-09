package patapon.rendergraph

import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import patapon.rendergraph.visual.EditorListener

class RenderGraphProjectComponent(val project: Project): ProjectComponent
{
    companion object {
        val LOG = Logger.getInstance(RenderGraphProjectComponent::class.java)
    }

    var editorListener: EditorListener? = null

    override fun projectOpened()
    {
        LOG.info("projectOpened")
        editorListener = EditorListener(project)
    }
}
