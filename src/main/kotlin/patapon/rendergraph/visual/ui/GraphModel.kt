package patapon.rendergraph.visual.ui

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections.observableArrayList
import javafx.collections.FXCollections.observableList
import javafx.collections.ObservableList
import javafx.scene.Parent
import tornadofx.getProperty
import tornadofx.onChange
import tornadofx.property

enum class ConnectorType
{
    INPUT,
    OUTPUT
}

open class GraphConnector<N>(val name: String, val type: ConnectorType)
{
    var node: GraphNode<N>? = null
}

open class GraphNode<N>(val id: String, val n: N)
{
    // layout
    var x by property<Double>()
    fun xProperty() = getProperty(GraphNode<N>::x)
    var y by property<Double>()
    fun yProperty() = getProperty(GraphNode<N>::y)

    // connectors
    val connectors = observableArrayList<GraphConnector<N>>()

    init {
        connectors.onChange { change ->
            while (change.next()) {
                if (change.wasAdded()) {
                    change.addedSubList.forEach { connector ->
                        assert(connector.node == null)    // only accept orphan connectors
                        connector.node = this
                    }
                }
                if (change.wasRemoved()) {
                    change.removed.forEach { connector ->
                        connector.node = null   // orphan
                    }
                }
            }
        }
    }

    fun createCustomUI(): Parent? {
        return null
    }
}

open class GraphConnection<N>(from: GraphConnector<N>, to: GraphConnector<N>)
{
    // From/to
    var from by property<GraphConnector<N>>()
    fun fromProperty() = getProperty(GraphConnection<N>::from)
    var to by  property<GraphConnector<N>>()
    fun toProperty() = getProperty(GraphConnection<N>::to)

    init {
        this.from = from
        this.to = to
    }

    fun createCustomUI(): Parent? {
        return null
    }
}

class GraphModel<N>
{
    val nodes = observableArrayList<GraphNode<N>>()
    val connections = observableArrayList<GraphConnection<N>>()

    init {
        nodes.onChange { changes ->
            // this might be expensive
            while (changes.next()) {
                changes.removed.forEach { removedNode ->
                    connections.removeIf { conn -> conn.from.node == removedNode || conn.to.node == removedNode }
                }
            }
        }
    }
}


// connections: observableList<GraphConnection>
// nodes: observableList<GraphNode>
// when a connection is added, add the corresponding node to the graph
// same with nodes
// a node can have input and output connectors: observableList<GraphEndpoint>
// listen on new endpoints
// inner View type: instantiate Fragment with createView
// - Fragment class?
// - JavaFX node?
// Undo/Redo?
//
// Should the node position be in the model? => Yes (node layout part of the model)