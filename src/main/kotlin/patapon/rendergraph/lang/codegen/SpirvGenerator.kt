package patapon.rendergraph.lang.codegen

import patapon.rendergraph.lang.declarations.BindingContext
import patapon.rendergraph.lang.psi.RgVisitor
import patapon.rendergraph.lang.utils.Printer

class SpirvGenerator(val context: BindingContext, val p: Printer): RgVisitor() {

}