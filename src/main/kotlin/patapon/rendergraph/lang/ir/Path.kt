package patapon.rendergraph.lang.ir

class Path(val pathSegments: List<String>) {
    companion object {
        fun create(vararg pathSegments: String) = Path(pathSegments.asList())
    }

    override fun toString(): String = pathSegments.joinToString(".")

}
