package patapon.rendergraph.ide.visual

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*
import com.intellij.util.messages.MessageBusConnection
import javafx.application.Platform
import patapon.rendergraph.lang.RenderGraphLanguage
import patapon.rendergraph.ide.visual.ui.GraphModel
import patapon.rendergraph.ide.visual.ui.GraphNode
import patapon.rendergraph.lang.psi.RgComponent

class GraphViewController(val project: Project, val graphPanel: GraphViewToolPanel): FileEditorManagerListener, CaretListener
{
    companion object {
        val LOG = Logger.getInstance(GraphViewController::class.java)
    }

    var msgbus: MessageBusConnection? = null
    val treeChangeListener = TreeChangeListener()
    // All access to the presentation model must be done on the JavaFX application thread
    val presentationModel = GraphModel<String>()
    var currentFileIsRenderGraph = false
    val nodesToAdd = arrayListOf<GraphNode<String>>()

    inner class TreeChangeListener: PsiTreeChangeAdapter() {
        override fun childAdded(event: PsiTreeChangeEvent) {
            LOG.info("PSI change: child added // ${event.element}")
            if (currentFileIsRenderGraph) {
                if (event.element != null) {
                    psiElementAdded(event.element)
                    updateUi()
                }
            }
            super.childAdded(event)
        }

        override fun childRemoved(event: PsiTreeChangeEvent) {
            //LOG.info("PSI change: child removed")
            super.childRemoved(event)
        }

        override fun childrenChanged(event: PsiTreeChangeEvent) {
            //LOG.info("PSI change: children changed")
            super.childrenChanged(event)
        }
    }

    // Add a node to the presentation model
    fun psiElementAdded(element: PsiElement)
    {
        if (element is RgComponent) {
            val componentName = element.name!!         // TODO use binding?
            LOG.info("Found component: ${componentName}")
            nodesToAdd.add(GraphNode(componentName, ""))
        }
    }

    fun psiElementRemoved(element: PsiElement)
    {
        if (element is RgComponent) {
            //val componentName = element.identifier.text
            //LOG.info("Remove component: ${componentName}")
            // TODO find matching element in presentation model
            // nodesToAdd.add(GraphNode(componentName, ""))
        }
    }

    //
    fun updateUi() {
        Platform.runLater {
            with(presentationModel) {
                nodes.addAll(nodesToAdd)
                nodesToAdd.clear()
            }
        }
    }

    // Build a presentation model from a PSI tree
    fun buildPsiPresentationModel(psi: PsiFile)
    {
        // clear all nodes in the current presentation model (we rebuild it from scratch on every file change)
        LOG.info("Rebuilding presentation model")

        val visitor = object : PsiRecursiveElementWalkingVisitor(true) {
            override fun visitElement(element: PsiElement?) {
                super.visitElement(element)
                if (element != null)
                    psiElementAdded(element)
            }
        }
        visitor.visitFile(psi)

        Platform.runLater {
            with(presentationModel) {
                nodes.clear()
                connections.clear()
                nodes.setAll(nodesToAdd)
                nodesToAdd.clear()
            }
        }
    }

    // Checks that the current file is a RenderGraph file, if so, then
    // update the current file
    override fun selectionChanged(event: FileEditorManagerEvent) {
        val newFile = event.newFile
        val newEditor = event.manager.selectedTextEditor
        selectedEditorChanged(newEditor, newFile)
    }

    fun selectedEditorChanged(editor: Editor?, file: VirtualFile?) {
        // get PSI
        if (file != null) {
            val psifile = PsiManager.getInstance(project).findFile(file)
            if (psifile != null) {
                if (psifile.language is RenderGraphLanguage) {
                    LOG.info("Switched to a RenderGraph file: ${file.name}")
                    currentFileIsRenderGraph = true
                    buildPsiPresentationModel(psifile)
                    return
                }
            }
        }
        currentFileIsRenderGraph = false
    }

    fun start()
    {
        LOG.info("Starting GraphViewController")
        PsiManager.getInstance(project).addPsiTreeChangeListener(treeChangeListener)
        msgbus = project.messageBus.connect()
        msgbus?.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, this)
        val fileEditorManager = FileEditorManager.getInstance(project)
        val curEditor = fileEditorManager.selectedTextEditor
        val curFile = fileEditorManager.selectedFiles.get(0)
        selectedEditorChanged(curEditor, curFile)
    }

    fun stop()
    {
        PsiManager.getInstance(project).removePsiTreeChangeListener(treeChangeListener)
        msgbus?.disconnect()
        msgbus = null
    }

    init {
        Platform.runLater {
            graphPanel.graphView.model = presentationModel
        }
        start()
    }
}