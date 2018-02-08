package patapon.rendergraph.visual

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import javafx.application.Application
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.embed.swing.JFXPanel
import javafx.scene.Group
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.input.KeyCombination
import javafx.stage.Stage
import patapon.rendergraph.visual.ui.GraphControl
import patapon.rendergraph.visual.ui.GraphModel
import patapon.rendergraph.visual.ui.GraphNode
import patapon.rendergraph.visual.ui.Styles
import tornadofx.*

class GraphViewToolRoot : View() {
    val graphView = GraphControl<String>()
    val graphModel = GraphModel<String>()

    override val root = vbox {
        menubar {
            menu("File") {
                item("Save") {}
                item("Quit") {}
            }
            menu("Edit") {
                item("Create node", KeyCombination.keyCombination("Ctrl+T")) {
                    action {
                        //createRandomNode()
                    }
                }
            }
        }

        //listview(values = controller.codeSnippets) {}
        children.add(graphView)

        val n1 = GraphNode("N1", "N1")
        val n2 = GraphNode("N2", "N2")
        val n3 = GraphNode("N3", "N3")
        val n4 = GraphNode("N4", "N4")
        graphModel.nodes.addAll(n1, n2, n3, n4)

        graphView.model = graphModel
        graphView.nodeViewFactory = { node ->
            vbox {
                label(node.id) {
                    addClass(Styles.title)
                    isFillWidth = true
                }
                button("Delete") {
                    action {
                        graphModel.nodes.remove(node)
                    }
                }
            }
        }

        textarea {
            isEditable = false
            textProperty().bind(graphModel.nodes.sizeProperty.asString())
        }
    }

}

class GraphViewToolApplication : Application() {
    override fun start(primaryStage: Stage?) {
        FX.registerApplication(this,primaryStage!!)
        importStylesheet(Styles::class)
    }
}

class GraphViewToolWindowFactory : ToolWindowFactory {
    val fxpanel = JFXPanel()
    lateinit var toolWin: ToolWindow

    fun initFx() {
        fxpanel.scene = Scene(GraphViewToolRoot().root)

        // Init TornadoFX after initializing JavaFX with a JFXPanel.
        Platform.runLater {
            val stage = Stage()
            val app = GraphViewToolApplication()
            app.start(stage)
        }
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        initFx()
        toolWin = toolWindow
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(fxpanel, "", false)
        toolWindow.contentManager.addContent(content)
    }
}