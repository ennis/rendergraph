package patapon.rendergraph.visual

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeListener
import com.intellij.util.messages.MessageBusConnection
import patapon.rendergraph.RenderGraphLanguage
import patapon.rendergraph.visual.ui.GraphControl

class EditorListener(val project: Project): FileEditorManagerListener, CaretListener {

    companion object {
        val LOG = Logger.getInstance(EditorListener::class.java)
    }

    var msgbus: MessageBusConnection? = null
    val treeChangeListener = TreeChangeListener()

    class TreeChangeListener: PsiTreeChangeAdapter() {

    }

    override fun selectionChanged(event: FileEditorManagerEvent) {
        val newEditor = event.manager.selectedTextEditor
        // get PSI
        val newFile = event.newFile
        if (newFile != null) {
            val psifile = PsiManager.getInstance(project).findFile(newFile)
            if (psifile != null) {
                if (psifile.language is RenderGraphLanguage) {
                    LOG.info("Switched to a RenderGraph file: ${newFile.name}")
                }
            }
        }
    }

    fun start()
    {
        LOG.info("Starting EditorListener")
        PsiManager.getInstance(project).addPsiTreeChangeListener(treeChangeListener)
        msgbus = project.messageBus.connect()
        msgbus?.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, this);
    }

    fun stop()
    {
        PsiManager.getInstance(project).removePsiTreeChangeListener(treeChangeListener)
        msgbus?.disconnect()
        msgbus = null
    }

    init {
        start()
    }
}