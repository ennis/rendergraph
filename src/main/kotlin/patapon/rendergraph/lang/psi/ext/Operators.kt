package patapon.rendergraph.lang.psi.ext

import patapon.rendergraph.lang.psi.*

enum class Operator(val text: String, val isAssignment: Boolean)
{
    ADD("+", false),
    SUB("-", false),
    MUL("*", false),
    DIV("/", false),
    REM("%", false),
    BIT_AND("&", false),
    BIT_OR("|", false),
    BIT_XOR("^", false),
    SHL("<<", false),
    SHR(">>", false),
    ASSIGN("=", true),
    ADD_ASSIGN("+=", true),
    SUB_ASSIGN("-=", true),
    MUL_ASSIGN("*=", true),
    DIV_ASSIGN("/=", true),
    REM_ASSIGN("%=", true),
    BIT_AND_ASSIGN("&=", true),
    BIT_OR_ASSIGN("|=", true),
    BIT_XOR_ASSIGN("^=", true),
    SHL_ASSIGN("<<=", true),
    SHR_ASSIGN(">>=", true),
    EQ("==", false),
    NOT_EQ("!=", false),
    LT("<", false),
    LTEQ("<=", false),
    GT(">", false),
    GTEQ(">=", false)
}

val RgBinaryExpression.opType get() = when (binaryOp.text) {
    "+" -> Operator.ADD
    "-" -> Operator.SUB
    "*" -> Operator.MUL
    "/" -> Operator.DIV
    "%" -> Operator.REM
    "&" -> Operator.BIT_AND
    "|" -> Operator.BIT_OR
    "^" -> Operator.BIT_XOR
    "<<" -> Operator.SHL
    ">>" -> Operator.SHR

    "=" -> Operator.ASSIGN
    "+=" -> Operator.ADD_ASSIGN
    "-=" -> Operator.SUB_ASSIGN
    "*=" -> Operator.MUL_ASSIGN
    "/=" -> Operator.DIV_ASSIGN
    "%=" -> Operator.REM_ASSIGN
    "&=" -> Operator.BIT_AND_ASSIGN
    "|=" -> Operator.BIT_OR_ASSIGN
    "^=" -> Operator.BIT_XOR_ASSIGN
    "<<=" -> Operator.SHL_ASSIGN
    ">>=" -> Operator.SHR_ASSIGN

    "<" -> Operator.LT
    "<=" -> Operator.LTEQ
    ">" -> Operator.GT
    ">=" -> Operator.GTEQ

    "==" -> Operator.EQ
    "!=" -> Operator.NOT_EQ

    else -> throw IllegalStateException("unexpected operator token")
}


