package patapon.rendergraph.lang.utils

class Printer(private val outString: StringBuilder) {
    private var indent = 0
    private var mustIndent = false

    private fun printIndent()
    {
        for (i in 0..indent*2) {
            outString.append(' ')
        }
    }

    fun appendln(ln: String)
    {
        if (mustIndent) printIndent()
        outString.appendln(ln)
        mustIndent = true
    }

    fun appendln()
    {
        outString.appendln()
        mustIndent = true
    }

    fun append(str: String)
    {
        if (mustIndent) printIndent()
        mustIndent = false
        outString.append(str)
    }

    fun withIndent(body: () -> Unit)
    {
        indent++
        body()
        indent--
    }

}