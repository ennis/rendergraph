package patapon.rendergraph.lang.ir

class PrettyPrinter(val root: Declaration, val outString: StringBuilder)
{
    var indent = 0
    var mustIndent = false

    fun print()
    {
        ppDeclaration(root)
    }

    private fun printIndent()
    {
        for (i in 0..indent*2) {
            outString.append(' ')
        }
    }

    private fun appendln(ln: String)
    {
        if (mustIndent) printIndent()
        outString.appendln(ln)
        mustIndent = true
    }

    private fun appendln()
    {
        outString.appendln()
        mustIndent = true
    }

    private fun append(str: String)
    {
        if (mustIndent) printIndent()
        mustIndent = false
        outString.append(str)
    }

    inline fun withIndent(body: () -> Unit)
    {
        indent++
        body()
        indent--
    }

    fun ppDeclaration(decl: Declaration)
    {
        when(decl) {
            is Component -> ppComponent(decl)
            is Function -> ppFunction(decl)
            is Module -> ppModule(decl)
        }
    }

    fun ppFunction(function: Function)
    {
        appendln("Function '${function.name}' <${function.returnType}>")
        withIndent {
            function.arguments.forEachIndexed { i, arg ->
                append("Argument $i '${arg.name}' <${arg.type}>")
                appendln()
            }
        }
    }


    fun ppModule(module: Module)
    {
        appendln("Module '${module.name}'")
        withIndent {
            module.declarations.value.getAllDeclarations().forEach {
                ppDeclaration(it)
            }
        }
    }

    fun ppComponent(component: Component)
    {
        appendln("Component '${component.name}'")
        withIndent {
            component.members.value.getAllDeclarations().forEach {
                ppDeclaration(it)
            }
        }
    }
}
