package patapon.rendergraph.lang.utils

class Printer(val builder: StringBuilder) {
    private var indent = 0
    private var mustIndent = false

    private fun printIndent()
    {
        for (i in 0 until indent*2) {
            builder.append(' ')
        }
    }

    fun appendln(ln: String)
    {
        if (mustIndent) printIndent()
        builder.appendln(ln)
        mustIndent = true
    }

    fun appendln()
    {
        builder.appendln()
        mustIndent = true
    }

    fun append(str: String)
    {
        if (mustIndent) printIndent()
        mustIndent = false
        builder.append(str)
    }

    fun withIndent(body: () -> Unit)
    {
        indent++
        body()
        indent--
    }

}