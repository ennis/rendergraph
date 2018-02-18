package patapon.rendergraph.lang.ir

import patapon.rendergraph.lang.psi.RgImports
import patapon.rendergraph.lang.psi.RgModule
import patapon.rendergraph.lang.psi.RgModuleContents

class Module(
        psiModule: RgModule,
        psiImports: RgImports,
        contents: RgModuleContents): LazyDeclaration
{
    val declarations = Lazy<Scope> {
        resolveDeclarations(contents)
    }

    val path = psiModule.path!!.toIr()

    override val name = path.toString()


    override val attributes: List<Attribute>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun forceFullResolve() {
        declarations.value.getAllDeclarations().forceFullResolve()
    }

    val imports: List<Path>

    init {
        imports = psiImports.importList.map { imp -> imp.path!!.toIr() }
    }

    fun resolveDeclarations(contents: RgModuleContents): Scope {
        val scope = ScopeImpl(NullLazy(), this) {
            contents.componentList.forEach { psiComponent ->
                val component = Component(psiComponent, this@Module, declarations)
                addDeclaration(component)
            }
        }

        return scope
    }
}
