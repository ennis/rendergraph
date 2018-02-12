package patapon.rendergraph.ide.visual

import javafx.scene.input.KeyCombination
import patapon.rendergraph.ide.visual.ui.GraphControl
import patapon.rendergraph.ide.visual.ui.Styles
import tornadofx.*

class GraphViewToolPanel: View()
{
    val graphView = GraphControl<String>()

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

        graphView.nodeViewFactory = { node ->
            vbox {
                label(node.id) {
                    addClass(Styles.title)
                    isFillWidth = true
                }
                /*button("Delete") {
                    action {
                        graphModel.nodes.remove(node)
                    }
                }*/
            }
        }

        /*val n1 = GraphNode("N1", "N1")
        val n2 = GraphNode("N2", "N2")
        val n3 = GraphNode("N3", "N3")
        val n4 = GraphNode("N4", "N4")
        graphModel.nodes.addAll(n1, n2, n3, n4)*/

        /*graphView.model = graphModel
        */

        /*textarea {
            isEditable = false
            textProperty().bind(graphModel.nodes.sizeProperty.asString())
        }*/
    }

}