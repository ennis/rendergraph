package patapon.rendergraph.lang.types

class FunctionType(val returnType: Type, vararg val argumentTypes: Type): UnwrappedType()
{
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append('(')
        argumentTypes.dropLast(1).forEach {
            sb.append(it.unwrap().toString())
            sb.append(',')
        }
        argumentTypes.lastOrNull()?.let { sb.append(it.unwrap().toString()) }
        sb.append(") -> ")
        sb.append(returnType.unwrap())
        return sb.toString()
    }
}
