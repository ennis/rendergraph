package patapon.rendergraph.visual.ui
import javafx.geometry.Pos
import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.text.FontWeight
import tornadofx.*

//=================================================================
// CSS
class Styles : Stylesheet() {
    companion object {
        // Define our styles
        val graphView by cssclass()
        val graphNodeView by cssclass()
        val title by cssclass()
    }

    init {
        graphNodeView {
            backgroundColor += LinearGradient(0.0, 0.0, 0.0, 1.0, true, CycleMethod.NO_CYCLE,
                    Stop(0.0, Color.LIGHTGRAY), Stop(1.0, Color.DARKGRAY))
            borderRadius += box(5.px)
            backgroundRadius += box(5.px)
            borderWidth += box(2.px)
            borderColor += box(Color.BLACK)
            padding = box(3.px)
        }

        s(graphNodeView.contains(title)) {
            fontWeight = FontWeight.BOLD
            alignment = Pos.CENTER
        }
    }
}