package patapon.rendergraph.visual.ui

import com.github.thomasnield.rxkotlinfx.toBinding
import com.github.thomasnield.rxkotlinfx.toObservable
import io.reactivex.rxkotlin.Observables
import javafx.beans.binding.Bindings
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.geometry.Point2D
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.Control
import javafx.scene.control.Skin
import javafx.scene.control.SkinBase
import javafx.scene.input.DragEvent
import javafx.scene.input.MouseDragEvent
import javafx.scene.input.MouseEvent
import javafx.scene.input.TransferMode
import javafx.scene.layout.*
import javafx.scene.paint.*
import javafx.scene.shape.*
import javafx.scene.text.FontWeight
import javafx.scene.transform.Scale
import javafx.util.Callback
import tornadofx.*
import kotlinx.coroutines.experimental.launch
import java.awt.Canvas
import java.util.*
import kotlin.coroutines.experimental.suspendCoroutine
import kotlinx.coroutines.experimental.javafx.JavaFx as UI


// * Control
//      * has the model
// * Skin
//      * Can be replaced easily
//      * Factor skin-independent behavior out of the skin
//          * Node dragging
//          * Connection drag-and-drop
//      * Who should set the handlers?
//          * Control
//          * Skin: easier
//      * get controls associated to model elements
//          * lookupNodeSkin()
//          * lookupConnectionSkin()
//          * lookpuConnectorSkin()
//      * All skins:
//          * GraphCanvasSkin (ZoomableScrollPane)
//          * GraphNodeSkin
//          * GraphConnectorSkin
//  * Controller (behavior)
//      * Handles interaction
//          * Selection
//          * Connector drag-and-drop
//          * Zoom and pan (this is layout related, so this should go in Skin -> reusable node)
//      * Handle events passed to the control
//
// GraphEditor
//      - model
//      - selection state
//      - focus state
//      - validator callback
//
// GraphViewSkin(controller,model):
//      - container for all views
//
// GraphCanvasView(skin,controller,model):
//      - listen to changes in model and update root node as needed
//          - add nodes, add connections
//      - forward events to controller (canvasPressed, canvasDragged, canvasReleased)
//      - sub-control: ZoomableScrollPane (should have its own skin and controller)
//          - sub-control has the state for the clipping rect / scale transform
//          - can add children to sub-control
// GraphNodeView(skin,controller,model):
//      - listen for changes in node model
//      - receive mouse events, forward to controller
// GraphConnectorView(skin,controller,model):
//      - listen for changes on connector model
//      - receive mouse events, forward to controller
//      - startFullDrag() on drag detected
// GraphEditingLayerView(skin,controller,model):
//      - listen to controller events for connector drag-drop
//      - when controller fires onBeginDragConnection -> create a line
//          - to get the line coordinates:
//              - get source connector from the controller
//              - get associated connector view from skin
//              - end coord: get from controller
//      - when controller fires onEndDragConnection -> delete the line
// Controller:
//      * one common class
//      * nodeClicked(node)
//          * update selection state (depending on modifier key)
//      * nodePressed(node)
//          * set node anchor position from mouse event scene coordinates
//      * nodeDragged(node)
//          * update node position using delta from anchor pos
//      * connectorPressed
//      * connectorDragged(connector)
//          * enter draggingConnection
//      * connectorDrop
//          * validate the connection between source and target connector (user callback)
//      * canvasPressed
//          * set window drag anchor
//      * canvasDragged
//          * update window position
//      * canvasReleased
//          * fire onEndDragConnection
//      * state: current connection drag position


class GraphControl<N>: Control()
{
    var nodeViewFactory by property<Parent.(GraphNode<N>) -> Unit>()
    fun nodeViewFactoryProperty() = getProperty(GraphControl<N>::nodeViewFactory)
    var model by property<GraphModel<N>>()
    fun modelProperty() = getProperty(GraphControl<N>::model)

    override fun createDefaultSkin(): Skin<*> {
        println("Create skin")
        return GraphEditorSkin<N>(this)
    }
}

//=================================================================
// Base class of GraphView
// OK, separate and reusable
// Maybe move into its own file?
open class ZoomableScrollPane : Region() {
    private val clipRect = Rectangle()
    private val scaleTransform = Scale(1.0, 1.0, 0.0, 0.0)
    private val zoomDelta = 1.0

    // current scale factor, bound to transform property of contents
    var scaleFactor: Double by property()

    fun scaleFactorProperty() = getProperty(ZoomableScrollPane::scaleFactor)
    // (X,Y) content window position, bound to layoutX and layoutY properties of contents
    var windowX: Double by property()

    fun windowXProperty() = getProperty(ZoomableScrollPane::windowX)
    var windowY: Double by property()
    fun windowYProperty() = getProperty(ZoomableScrollPane::windowY)

    // Child pane onto which the transform is applied
    val contents = pane {
        isManaged = false
        layoutXProperty().bind(windowXProperty().onChange { 1.0 * (it ?: 0.0) })
        layoutYProperty().bind(windowYProperty().onChange { 1.0 * (it ?: 0.0) })
        transforms.add(scaleTransform)
    }

    fun panTo(x: Double, y: Double) {
        windowX = x
        windowY = y
    }

    init {
        windowX = 0.0
        windowY = 0.0
        clip = clipRect

        //
        clipRect.widthProperty().bind(widthProperty())
        clipRect.heightProperty().bind(heightProperty())

        scaleFactor = 1.0

        scaleFactorProperty().onChange {
            scaleTransform.x = it ?: 1.0
            scaleTransform.y = it ?: 1.0
        }

        setOnScroll {
            if (it.deltaY < 0) {
                scaleFactor -= zoomDelta
            } else {
                scaleFactor += zoomDelta
            }
            it.consume()
        }

        setOnMousePressed {
            val windowPosOnClickX = windowX
            val windowPosOnClickY = windowY
            val origX = it.sceneX
            val origY = it.sceneY

            setOnMouseDragged {
                val offsetX = it.sceneX - origX
                val offsetY = it.sceneY - origY
                panTo(windowPosOnClickX + offsetX, windowPosOnClickY + offsetY)
            }

            launch(UI) {
                suspendCoroutine<Unit> { cont ->
                    // releasing the mouse will end the coroutine
                    setOnMouseReleased {
                        // don't forget to reset the event handler, otherwise it might
                        // be called again, and try to continue an already finished coroutine
                        onMouseReleased = null
                        cont.resume(Unit)
                    }
                }
            }
        }
    }
}

private class GraphController<N>(val control: GraphControl<N>)
{
    private var draggingNode: GraphNode<*>? = null

    val isDraggingConnection = SimpleBooleanProperty(false)

    fun nodeClicked(node: GraphNode<*>, mouseEvent: MouseEvent) {
        TODO("nodeClicked")
    }

    fun nodePressed(node: GraphNode<*>, mouseEvent: MouseEvent) {
    }

    fun nodeReleased(node: GraphNode<*>, mouseEvent: MouseEvent) {
        TODO("nodeReleased")
    }

    fun nodeDragged(node: GraphNode<*>, positionX: Double, positionY: Double) {
        // just update the node
        node.x = positionX
        node.y = positionY
    }

    fun connect(source: GraphConnector<N>, target: GraphConnector<N>) {
        // TODO perform client-side validation
        if (source.type == target.type) {
            return
        }
        val (s,t) = if (source.type == ConnectorType.INPUT) { Pair(target,source) } else { Pair(source,target) }
        control.model.connections.add(GraphConnection(s, t))
    }

    fun connectorPressed(connector: GraphConnector<*>, mouseEvent: MouseEvent) {
        // begin a drag connector gesture
       // sourceConnector = connector
        //TODO("connectorPressed")
    }

    fun connectorReleased(connector: GraphConnector<*>, mouseEvent: MouseEvent) {
        //TODO("connectorReleased")
    }

    fun connectorDragged(connector: GraphConnector<*>, mouseEvent: MouseEvent) {
        //TODO("connectorDragged")
    }

    fun connectorDrop(connector: GraphConnector<*>, mouseDragEvent: MouseDragEvent) {
        TODO("connectorDrop")
    }

    fun canvasPressed(mouseEvent: MouseEvent) {
        //TODO("canvasPressed")
    }

    fun canvasDragged(dragEvent: DragEvent) {
        //TODO("canvasDragged")
    }

    fun canvasReleased(mouseEvent: MouseEvent) {
        //TODO("canvasReleased")
    }
}


private class GraphConnectorView<N>(
        skin: GraphEditorSkin<N>,
        val nodeView: GraphNodeView<N>,
        val connector: GraphConnector<N>,
        val controller: GraphController<N>) : Circle() {
    init {
        // TODO maybe do it in the parent view?
        skin.connectorViews[connector] = this
        radius = 6.0
        hgrow = Priority.NEVER

        setOnMousePressed { mouseEvent ->
            skin.beginConnectorDrag(this)
            controller.connectorPressed(connector,mouseEvent)
            mouseEvent.consume()
        }

        setOnMouseDragged { mouseEvent ->
            skin.connectorDrag(mouseEvent)
            controller.connectorDragged(connector, mouseEvent)
            mouseEvent.consume()
        }

        setOnDragDetected { mouseEvent ->
            println("connector startFullDrag")
            startFullDrag()
            mouseEvent.consume()
        }

        setOnMouseDragReleased { dragEvent ->
            skin.droppedOnConnector(this)
            //controller.connectorDrop(connector,dragEvent)
            dragEvent.consume()
        }
    }
}

private class GraphNodeView<N>(val skin: GraphEditorSkin<N>, val node: GraphNode<N>, val controller: GraphController<N>) : VBox() {
    private val title = Region()
    private val inputConnectors = VBox()
    private val outputConnectors = VBox()
    val content = Region()

    // Create the connectors visuals for the node
    private fun setupConnectors() {
        inputConnectors.children.clear()
        outputConnectors.children.clear()
        node.connectors.forEach { connector ->
            when (connector.type) {
                ConnectorType.INPUT -> inputConnectors.apply {
                    hbox {
                        add(GraphConnectorView(skin, this@GraphNodeView, connector, controller))
                        label(connector.name) { hgrow = Priority.ALWAYS }
                    }
                }
                ConnectorType.OUTPUT -> outputConnectors.apply {
                    hbox {
                        label(connector.name) { hgrow = Priority.ALWAYS }
                        add(GraphConnectorView(skin, this@GraphNodeView, connector, controller))
                    }
                }
            }
        }
    }

    init {
        addClass(Styles.graphNodeView)

        width = 50.0
        height = 50.0

        layoutXProperty().bind(node.xProperty())
        layoutYProperty().bind(node.yProperty())

        borderpane {
            top {
                vbox {
                    add(title)
                    title.addClass(Styles.title)
                    add(content)
                }
            }
            left {
                add(inputConnectors)
            }
            right {
                add(outputConnectors)
            }
        }

        setupConnectors()

        node.connectors.onChange {
            setupConnectors()
        }
    }
}

private class GraphConnectionView<N>(val skin: GraphEditorSkin<N>, val connection: GraphConnection<N>, val controller: GraphController<N>) : CubicCurve()
{
    init {
        fill = null
        stroke = Color.BLACK
        strokeWidth = 3.0
        controlX1Property().bind(startXProperty().add(100.0))
        controlX2Property().bind(endXProperty().subtract(100.0))
        controlY1Property().bind(startYProperty())
        controlY2Property().bind(endYProperty())
    }
}

private class GraphEditorSkin<N>(control: GraphControl<N>): SkinBase<GraphControl<N>>(control)
{
    val connectorViews = WeakHashMap<GraphConnector<*>, GraphConnectorView<N>>()
    val connectionViews = WeakHashMap<GraphConnection<*>, GraphConnectionView<N>>()
    val nodeViews = WeakHashMap<GraphNode<*>, GraphNodeView<N>>()

    val gridLayer = GraphEditorGrid()
    val nodeLayer = Group()
    val connectionLayer = Group()
    val controller = GraphController(control)
    var root = ZoomableScrollPane()

    private var sourceConnector: GraphConnectorView<N>? = null
    private var targetConnector: GraphConnectorView<N>? = null
    private var connectorLine: Line? = null

    // node position at the beginning of drag
    var preDragNodePos = Point2D(0.0, 0.0)
    // mouse position at the end of drag
    var preDragMousePos = Point2D(0.0, 0.0)

    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val modelNodeChangeListener = ListChangeListener<GraphNode<N>> { change ->
        // for now, just rebuild the whole scene each time
        redrawGraph()
    }

    // Called when the connections of the model have changed.
    private val modelConnectionChangeListener = ListChangeListener<GraphConnection<N>> { change ->
        // see above
        redrawGraph()
    }

    // Clear all node and connection visuals and re-create them from the model
    private fun redrawGraph() {
        // empty all visual layers
        nodeLayer.children.clear()
        connectionLayer.children.clear()
        // re-create all nodes
        val m = skinnable.model
        if (m != null) {
            m.nodes.forEach { addNode(it) }
            m.connections.forEach { addConnection(it) }
        }
    }

    // Called by the connectors when a drag operation begins
    fun beginConnectorDrag(connector: GraphConnectorView<N>) {
        println("beginConnectorDrag")
        sourceConnector = connector
        // create the temporary connector line
        val sceneCenter = connector.localToScene(connector.centerX, connector.centerY)
        val layoutCenter = root.contents.sceneToLocal(sceneCenter)
        connectorLine = Line(layoutCenter.x, layoutCenter.y, layoutCenter.x, layoutCenter.y).apply { isMouseTransparent = true }
        root.contents.children.add(connectorLine)
    }

    fun connectorDrag(mouseEvent: MouseEvent) {
        println("connectorDrag")
        // TODO refactor this (put sourceConnector and connectorLine into a struct, DragConnectionState)
        if (sourceConnector != null) {
            val localPos = root.contents.sceneToLocal(mouseEvent.sceneX, mouseEvent.sceneY)
            connectorLine?.endX = localPos.x
            connectorLine?.endY = localPos.y
        }
        else {
            println("Connector dragged but no source was set")
        }
    }

    fun droppedOnConnector(connector: GraphConnectorView<N>) {
        println("droppedOnConnector")
        if (sourceConnector != null) {
            // call the controller that a new connection should be made between two connectors
            controller.connect(sourceConnector!!.connector, connector.connector)
        } else {
            println("drag-dropped into connector but no source was set")
        }
        endConnectorDrag()
    }

    fun endConnectorDrag() {
        println("endConnectorDrag")
        sourceConnector = null
        targetConnector = null
        connectorLine?.removeFromParent()
        connectorLine = null
    }


    // Create a GraphNodeView visual for the given GraphNode and adds it
    // to the scene
    private fun addNode(node: GraphNode<N>) {
        val visual = GraphNodeView(this, node, controller)
        // setup handlers
        visual.setOnMousePressed { mouseEvent ->
            val mousePosCanvas = root.contents.sceneToLocal(mouseEvent.sceneX, mouseEvent.sceneY)
            preDragNodePos = Point2D(visual.layoutX, visual.layoutY)
            preDragMousePos = Point2D(mousePosCanvas.x, mousePosCanvas.y)
            mouseEvent.consume()
        }
        // setup handlers
        visual.setOnMouseDragged { mouseEvent ->
            val mousePosCanvas = root.contents.sceneToLocal(mouseEvent.sceneX, mouseEvent.sceneY)
            val positionX = preDragNodePos.x - preDragMousePos.x + mousePosCanvas.x
            val positionY = preDragNodePos.y - preDragMousePos.y + mousePosCanvas.y
            // signal to the controller that a node has been dragged by some delta
            // The node position on the canvas is part of the model
            // (the presentation model), which is watched by the skin,
            // so when the controller changes it, the visual will also
            // move (no need to relocate here)
            controller.nodeDragged(node, positionX, positionY)
            mouseEvent.consume()
        }
        // call the nodeViewFactory on the content area to create a view
        val nodeViewFactory = skinnable.nodeViewFactory
        if (nodeViewFactory != null) {
            visual.content.nodeViewFactory(node)
        }
        nodeLayer.add(visual)
        nodeViews[node] = visual
    }

    private fun addConnection(conn: GraphConnection<N>) {
        //val fromView = nodeViews[conn.from]!!
        //val toView = nodeViews[conn.to]!!
        val visual = GraphConnectionView(this, conn, controller)
        val fromConnector = connectorViews[conn.from]!!
        val toConnector = connectorViews[conn.to]!!
        val fromNode = fromConnector.nodeView
        val toNode = toConnector.nodeView
        // listen to changes in node bounds and connector bounds
        // maybe we could assume that the position of the connector stays constant within the node?
        val boundsInCanvasFrom = Observables.combineLatest(fromNode.boundsInParentProperty().toObservable(), fromConnector.boundsInParentProperty().toObservable()).map { (_,_) ->
            root.contents.sceneToLocal(fromConnector.localToScene(fromConnector.boundsInLocal))
        }
        val boundsInCanvasTo = Observables.combineLatest(toNode.boundsInParentProperty().toObservable(), toConnector.boundsInParentProperty().toObservable()).map { (_,_) ->
            root.contents.sceneToLocal(toConnector.localToScene(toConnector.boundsInLocal))
        }
        visual.startXProperty().bind(boundsInCanvasFrom.map { bounds -> (bounds.minX + bounds.maxX) / 2.0 }.toBinding())
        visual.startYProperty().bind(boundsInCanvasFrom.map { bounds -> (bounds.minY + bounds.maxY) / 2.0 }.toBinding())
        visual.endXProperty().bind(boundsInCanvasTo.map { bounds -> (bounds.minX + bounds.maxX) / 2.0 }.toBinding())
        visual.endYProperty().bind(boundsInCanvasTo.map { bounds -> (bounds.minY + bounds.maxY) / 2.0 }.toBinding())

        connectionLayer.add(visual)
        connectionViews[conn] = visual
    }

    init {
        control.model?.nodes?.addListener(modelNodeChangeListener)
        control.model?.connections?.addListener(modelConnectionChangeListener)

        with(root) {
            prefWidth = 800.0
            prefHeight = 600.0
            contents.add(gridLayer)
            contents.add(nodeLayer)
            contents.add(connectionLayer)
            connectionLayer.isMouseTransparent = true
            gridLayer.toBack()

            widthProperty().onChange { gridLayer.draw(width, height) }
            heightProperty().onChange { gridLayer.draw(width, height) }

            setOnMouseReleased {
                println("Root onMouseReleased")
               // endConnectorDrag()
                controller.canvasReleased(it)
            }
        }

        redrawGraph()

        children.add(root)
    }

}
const val HALF_PIXEL_OFFSET = -0.5

class GraphEditorGrid : Group() {
    fun draw(width: Double, height: Double) {
        val spacing = 4.0
        val gridColor = SimpleObjectProperty(Color.BISQUE)
        children.clear()

        val hLineCount = Math.floor((height + 1) / spacing).toInt()
        val vLineCount = Math.floor((width + 1) / spacing).toInt()

        for (i in 0 until hLineCount) {
            val hLine = Line()
            hLine.startX = 0.0
            hLine.endX = width
            hLine.startY = (i + 1) * spacing + HALF_PIXEL_OFFSET
            hLine.endY = (i + 1) * spacing + HALF_PIXEL_OFFSET
            hLine.strokeProperty().bind(gridColor)
            children.add(hLine)
        }

        for (i in 0 until vLineCount) {
            val vLine = Line()
            vLine.startX = (i + 1) * spacing + HALF_PIXEL_OFFSET
            vLine.endX = (i + 1) * spacing + HALF_PIXEL_OFFSET
            vLine.startY = 0.0
            vLine.endY = height
            vLine.strokeProperty().bind(gridColor)
            children.add(vLine)
        }
    }

    init {
        isManaged = false
        isMouseTransparent = true
    }
}
