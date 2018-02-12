package patapon.rendergraph.ide.visual

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import javafx.application.Application
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.Scene
import javafx.stage.Stage
import patapon.rendergraph.ide.visual.ui.Styles
import tornadofx.*


class GraphViewToolWindowFactory : ToolWindowFactory {

    class GraphViewToolApplication : Application() {
        override fun start(primaryStage: Stage?) {
            importStylesheet(Styles::class)
            FX.registerApplication(this,primaryStage!!)
        }
    }

    companion object {
        val LOG = Logger.getInstance(GraphViewToolWindowFactory::class.java)
    }

    var containerPanel: JFXPanel? = null
    var graphPanel: GraphViewToolPanel? = null
    lateinit var toolWin: ToolWindow

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow)
    {
        LOG.info("GraphViewToolWindowFactory.createToolWindowContent")

        // Create swing container for javafx scenes
        // This will also initialize the FX runtime,
        // which is necessary for constructing the GraphViewToolPanel below
        val jfxp = JFXPanel()
        containerPanel = jfxp

        // Create graph view panel and add it to the JFXPanel
        val gp = GraphViewToolPanel()
        graphPanel = gp
        jfxp.scene = Scene(gp.root)

        // Init TornadoFX after initializing JavaFX with a JFXPanel.
        Platform.setImplicitExit(false)
        Platform.runLater {
            val stage = Stage()
            val app = GraphViewToolApplication()
            app.start(stage)
            FX.applyStylesheetsTo(containerPanel!!.scene)
        }

        // Add jfxpanel to tool window contents
        toolWin = toolWindow
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(containerPanel, "", false)
        toolWindow.contentManager.addContent(content)

        // Create the controller
        val controller = GraphViewController(project, gp)
    }
}