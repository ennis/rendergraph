package patapon.rendergraph.lang.ir

interface Literal: Expression

class IntegerLiteral(val value: Int): Expression
{
    override val type = IntegerType
}

class FloatLiteral(val value: Double): Expression
{
    override val type = FloatType
}
