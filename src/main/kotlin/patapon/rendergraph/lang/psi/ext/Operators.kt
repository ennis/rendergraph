package patapon.rendergraph.lang.psi.ext

import patapon.rendergraph.lang.psi.*

enum class Operator(val text: String)
{
    ADD("+"),
    SUB("-"),
    MUL("*"),
    DIV("/"),
    REM("%"),
    BIT_AND("&"),
    BIT_OR("|"),
    BIT_XOR("^"),
    SHL("<<"),
    SHR(">>"),
    ADD_ASSIGN("+="),
    SUB_ASSIGN("-="),
    MUL_ASSIGN("*="),
    DIV_ASSIGN("/="),
    REM_ASSIGN("%="),
    BIT_AND_ASSIGN("&="),
    BIT_OR_ASSIGN("|="),
    BIT_XOR_ASSIGN("^="),
    SHL_ASSIGN("<<="),
    SHR_ASSIGN(">>="),
    EQ("=="),
    NOT_EQ("!="),
    LT("<"),
    LTEQ("<="),
    GT(">"),
    GTEQ(">=")
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


