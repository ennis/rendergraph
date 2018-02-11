package patapon.rendergraph.lang.psi

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import patapon.rendergraph.lang.psi.RgElementTypes.*

object RgTokenTypes {
    val KEYWORDS = TokenSet.create(CONST, UNIFORM_KW, IMPORT_KW, MODULE_KW, COMPONENT_KW, PASS_KW, FUN, VAL)
    val PRIMITIVE_TYPES = TokenSet.create(INT, FLOAT, DOUBLE)
    val NUMBER_LITERALS = TokenSet.create(INT_LITERAL, FLOAT_LITERAL, DOUBLE_LITERAL, UINT_LITERAL)
    val RENDER_PASS_ITEMS = TokenSet.create(DEPTH_TEST_ITEM, VERTEX_SHADER_ITEM, FRAGMENT_SHADER_ITEM, GEOMETRY_SHADER_ITEM, TESS_CONTROL_SHADER_ITEM, TESS_EVAL_SHADER_ITEM, PRIMITIVE_TOPOLOGY_ITEM)

    /*@JvmField val IDENTIFIER: IElementType = RgTokenType("IDENTIFIER")

    @JvmField val INTEGER_CONSTANT: IElementType = RgTokenType("INTEGER_CONSTANT")
    @JvmField val UINT_CONSTANT: IElementType = RgTokenType("UINT_CONSTANT")
    @JvmField val FLOAT_CONSTANT: IElementType = RgTokenType("FLOAT_CONSTANT")
    @JvmField val DOUBLE_CONSTANT: IElementType = RgTokenType("DOUBLE_CONSTANT")
    @JvmField val BOOL_CONSTANT: IElementType = RgTokenType("BOOL_CONSTANT")

    @JvmField val VOID_TYPE: IElementType = RgTokenType("VOID_TYPE")
    @JvmField val FLOAT_TYPE: IElementType = RgTokenType("FLOAT_TYPE")
    @JvmField val DOUBLE_TYPE: IElementType = RgTokenType("DOUBLE_TYPE")
    @JvmField val INT_TYPE: IElementType = RgTokenType("INT_TYPE")
    @JvmField val UINT_TYPE: IElementType = RgTokenType("UINT_TYPE")
    @JvmField val BOOL_TYPE: IElementType = RgTokenType("BOOL_TYPE")
    @JvmField val VEC2_TYPE: IElementType = RgTokenType("VEC2_TYPE")
    @JvmField val VEC3_TYPE: IElementType = RgTokenType("VEC3_TYPE")
    @JvmField val VEC4_TYPE: IElementType = RgTokenType("VEC4_TYPE")
    @JvmField val DVEC2_TYPE: IElementType = RgTokenType("DVEC2_TYPE")
    @JvmField val DVEC3_TYPE: IElementType = RgTokenType("DVEC3_TYPE")
    @JvmField val DVEC4_TYPE: IElementType = RgTokenType("DVEC4_TYPE")
    @JvmField val IVEC2_TYPE: IElementType = RgTokenType("IVEC2_TYPE")
    @JvmField val IVEC3_TYPE: IElementType = RgTokenType("IVEC3_TYPE")
    @JvmField val IVEC4_TYPE: IElementType = RgTokenType("IVEC4_TYPE")
    @JvmField val UVEC2_TYPE: IElementType = RgTokenType("UVEC2_TYPE")
    @JvmField val UVEC3_TYPE: IElementType = RgTokenType("UVEC3_TYPE")
    @JvmField val UVEC4_TYPE: IElementType = RgTokenType("UVEC4_TYPE")
    @JvmField val BVEC2_TYPE: IElementType = RgTokenType("BVEC2_TYPE")
    @JvmField val BVEC3_TYPE: IElementType = RgTokenType("BVEC3_TYPE")
    @JvmField val BVEC4_TYPE: IElementType = RgTokenType("BVEC4_TYPE")
    @JvmField val MAT2_TYPE: IElementType = RgTokenType("MAT2_TYPE")
    @JvmField val MAT3_TYPE: IElementType = RgTokenType("MAT3_TYPE")
    @JvmField val MAT4_TYPE: IElementType = RgTokenType("MAT4_TYPE")
    @JvmField val MAT2X2_TYPE: IElementType = RgTokenType("MAT2X2_TYPE")
    @JvmField val MAT2X3_TYPE: IElementType = RgTokenType("MAT2X3_TYPE")
    @JvmField val MAT2X4_TYPE: IElementType = RgTokenType("MAT2X4_TYPE")
    @JvmField val MAT3X2_TYPE: IElementType = RgTokenType("MAT3X2_TYPE")
    @JvmField val MAT3X3_TYPE: IElementType = RgTokenType("MAT3X3_TYPE")
    @JvmField val MAT3X4_TYPE: IElementType = RgTokenType("MAT3X4_TYPE")
    @JvmField val MAT4X2_TYPE: IElementType = RgTokenType("MAT4X2_TYPE")
    @JvmField val MAT4X3_TYPE: IElementType = RgTokenType("MAT4X3_TYPE")
    @JvmField val MAT4X4_TYPE: IElementType = RgTokenType("MAT4X4_TYPE")
    @JvmField val DMAT2_TYPE: IElementType = RgTokenType("DMAT2_TYPE")
    @JvmField val DMAT3_TYPE: IElementType = RgTokenType("DMAT3_TYPE")
    @JvmField val DMAT4_TYPE: IElementType = RgTokenType("DMAT4_TYPE")
    @JvmField val DMAT2X2_TYPE: IElementType = RgTokenType("DMAT2X2_TYPE")
    @JvmField val DMAT2X3_TYPE: IElementType = RgTokenType("DMAT2X3_TYPE")
    @JvmField val DMAT2X4_TYPE: IElementType = RgTokenType("DMAT2X4_TYPE")
    @JvmField val DMAT3X2_TYPE: IElementType = RgTokenType("DMAT3X2_TYPE")
    @JvmField val DMAT3X3_TYPE: IElementType = RgTokenType("DMAT3X3_TYPE")
    @JvmField val DMAT3X4_TYPE: IElementType = RgTokenType("DMAT3X4_TYPE")
    @JvmField val DMAT4X2_TYPE: IElementType = RgTokenType("DMAT4X2_TYPE")
    @JvmField val DMAT4X3_TYPE: IElementType = RgTokenType("DMAT4X3_TYPE")
    @JvmField val DMAT4X4_TYPE: IElementType = RgTokenType("DMAT4X4_TYPE")
    @JvmField val SAMPLER_TYPE: IElementType = RgTokenType("SAMPLER_TYPE")
    @JvmField val ATOMIC_UINT_TYPE: IElementType = RgTokenType("ATOMIC_UINT_TYPE")
    @JvmField val NAMED_TYPE: IElementType = RgTokenType("NAMED_TYPE")*/


    // GLSL stuff
    /*@JvmField val CONST_KEYWORD: IElementType = RgTokenType("CONST_KEYWORD")
    @JvmField val ATTRIBUTE_KEYWORD: IElementType = RgTokenType("ATTRIBUTE_KEYWORD")
    @JvmField val UNIFORM_KEYWORD: IElementType = RgTokenType("UNIFORM_KEYWORD")
    @JvmField val VARYING_KEYWORD: IElementType = RgTokenType("VARYING_KEYWORD")
    @JvmField val CENTROID_KEYWORD: IElementType = RgTokenType("CENTROID_KEYWORD")
    @JvmField val INVARIANT_KEYWORD: IElementType = RgTokenType("INVARIANT_KEYWORD")
    @JvmField val PATCH_KEYWORD: IElementType = RgTokenType("PATCH_KEYWORD")
    @JvmField val SAMPLE_KEYWORD: IElementType = RgTokenType("SAMPLE_KEYWORD")
    @JvmField val BUFFER_KEYWORD: IElementType = RgTokenType("BUFFER_KEYWORD")
    @JvmField val SHARED_KEYWORD: IElementType = RgTokenType("SHARED_KEYWORD")
    @JvmField val COHERENT_KEYWORD: IElementType = RgTokenType("COHERENT_KEYWORD")
    @JvmField val VOLATILE_KEYWORD: IElementType = RgTokenType("VOLATILE_KEYWORD")
    @JvmField val RESTRICT_KEYWORD: IElementType = RgTokenType("RESTRICT_KEYWORD")
    @JvmField val READONLY_KEYWORD: IElementType = RgTokenType("READONLY_KEYWORD")
    @JvmField val WRITEONLY_KEYWORD: IElementType = RgTokenType("WRITEONLY_KEYWORD")
    @JvmField val SUBROUTINE_KEYWORD: IElementType = RgTokenType("SUBROUTINE_KEYWORD")
    @JvmField val PRECISE_KEYWORD: IElementType = RgTokenType("PRECISE_KEYWORD")
    @JvmField val SMOOTH_KEYWORD: IElementType = RgTokenType("SMOOTH_KEYWORD")
    @JvmField val FLAT_KEYWORD: IElementType = RgTokenType("FLAT_KEYWORD")
    @JvmField val NOPERSPECTIVE_KEYWORD: IElementType = RgTokenType("NOPERSPECTIVE_KEYWORD")
    @JvmField val HIGHP_KEYWORD: IElementType = RgTokenType("HIGHP_KEYWORD")
    @JvmField val MEDIUMP_KEYWORD: IElementType = RgTokenType("MEDIUMP_KEYWORD")
    @JvmField val LOWP_KEYWORD: IElementType = RgTokenType("LOWP_KEYWORD")*/

    /*@JvmField val LAYOUT_KEYWORD: IElementType = RgTokenType("LAYOUT_KEYWORD")

    @JvmField val PRECISION_KEYWORD: IElementType = RgTokenType("PRECISION_KEYWORD")

    @JvmField val IN_KEYWORD: IElementType = RgTokenType("IN_KEYWORD")
    @JvmField val OUT_KEYWORD: IElementType = RgTokenType("OUT_KEYWORD")
    @JvmField val INOUT_KEYWORD: IElementType = RgTokenType("INOUT_KEYWORD")

    @JvmField val WHILE_KEYWORD: IElementType = RgTokenType("WHILE_KEYWORD")
    @JvmField val DO_KEYWORD: IElementType = RgTokenType("DO_KEYWORD")
    @JvmField val FOR_KEYWORD: IElementType = RgTokenType("FOR_KEYWORD")

    @JvmField val BREAK_JUMP_STATEMENT: IElementType = RgTokenType("BREAK_JUMP_STATEMENT")
    @JvmField val CONTINUE_JUMP_STATEMENT: IElementType = RgTokenType("CONTINUE_JUMP_STATEMENT")
    @JvmField val RETURN_JUMP_STATEMENT: IElementType = RgTokenType("RETURN_JUMP_STATEMENT")
    @JvmField val DISCARD_JUMP_STATEMENT: IElementType = RgTokenType("DISCARD_JUMP_STATEMENT")

    @JvmField val STRUCT: IElementType = RgTokenType("STRUCT")

    @JvmField val IF_KEYWORD: IElementType = RgTokenType("IF_KEYWORD")
    @JvmField val ELSE_KEYWORD: IElementType = RgTokenType("ELSE_KEYWORD")
    @JvmField val SWITCH_KEYWORD: IElementType = RgTokenType("SWITCH_KEYWORD")
    @JvmField val CASE_KEYWORD: IElementType = RgTokenType("CASE_KEYWORD")
    @JvmField val DEFAULT_KEYWORD: IElementType = RgTokenType("DEFAULT_KEYWORD")

    @JvmField val COMMENT_LINE: IElementType = RgTokenType("COMMENT_LINE")
    @JvmField val COMMENT_BLOCK: IElementType = RgTokenType("COMMENT_BLOCK")

    @JvmField val LEFT_BRACE: IElementType = RgTokenType("LEFT_BRACE")
    @JvmField val RIGHT_BRACE: IElementType = RgTokenType("RIGHT_BRACE")
    @JvmField val LEFT_PAREN: IElementType = RgTokenType("LEFT_PAREN")
    @JvmField val RIGHT_PAREN: IElementType = RgTokenType("RIGHT_PAREN")
    @JvmField val LEFT_BRACKET: IElementType = RgTokenType("LEFT_BRACKET")
    @JvmField val RIGHT_BRACKET: IElementType = RgTokenType("RIGHT_BRACKET")

    @JvmField val EQUAL: IElementType = RgTokenType("EQUAL")

    @JvmField val MUL_ASSIGN: IElementType = RgTokenType("MUL_ASSIGN")
    @JvmField val STAR: IElementType = RgTokenType("STAR")
    @JvmField val DIV_ASSIGN: IElementType = RgTokenType("DIV_ASSIGN")
    @JvmField val SLASH: IElementType = RgTokenType("SLASH")
    @JvmField val ADD_ASSIGN: IElementType = RgTokenType("ADD_ASSIGN")
    @JvmField val PLUS: IElementType = RgTokenType("PLUS")
    @JvmField val SUB_ASSIGN: IElementType = RgTokenType("SUB_ASSIGN")
    @JvmField val DASH: IElementType = RgTokenType("DASH")
    @JvmField val MOD_ASSIGN: IElementType = RgTokenType("MOD_ASSIGN")
    @JvmField val PERCENT: IElementType = RgTokenType("PERCENT")
    @JvmField val LEFT_ASSIGN: IElementType = RgTokenType("LEFT_ASSIGN")
    @JvmField val LEFT_OP: IElementType = RgTokenType("LEFT_OP")
    @JvmField val RIGHT_ASSIGN: IElementType = RgTokenType("RIGHT_ASSIGN")
    @JvmField val RIGHT_OP: IElementType = RgTokenType("RIGHT_OP")
    @JvmField val AND_ASSIGN: IElementType = RgTokenType("AND_ASSIGN")
    @JvmField val AMPERSAND: IElementType = RgTokenType("AMPERSAND")
    @JvmField val XOR_ASSIGN: IElementType = RgTokenType("XOR_ASSIGN")
    @JvmField val CARET: IElementType = RgTokenType("CARET")
    @JvmField val OR_ASSIGN: IElementType = RgTokenType("OR_ASSIGN")
    @JvmField val VERTICAL_BAR: IElementType = RgTokenType("VERTICAL_BAR")

    @JvmField val TILDE: IElementType = RgTokenType("TILDE")
    @JvmField val DEC_OP: IElementType = RgTokenType("DEC_OP")
    @JvmField val INC_OP: IElementType = RgTokenType("INC_OP")

    @JvmField val EQ_OP: IElementType = RgTokenType("EQ_OP")
    @JvmField val LEFT_ANGLE: IElementType = RgTokenType("LEFT_ANGLE")
    @JvmField val RIGHT_ANGLE: IElementType = RgTokenType("RIGHT_ANGLE")
    @JvmField val GE_OP: IElementType = RgTokenType("GE_OP")
    @JvmField val LE_OP: IElementType = RgTokenType("LE_OP")
    @JvmField val NE_OP: IElementType = RgTokenType("NE_OP")
    @JvmField val AND_OP: IElementType = RgTokenType("AND_OP")
    @JvmField val OR_OP: IElementType = RgTokenType("OR_OP")
    @JvmField val XOR_OP: IElementType = RgTokenType("XOR_OP")

    @JvmField val QUESTION: IElementType = RgTokenType("QUESTION")
    @JvmField val COLON: IElementType = RgTokenType("COLON")
    @JvmField val BANG: IElementType = RgTokenType("BANG")
    @JvmField val DOT: IElementType = RgTokenType("DOT")
    @JvmField val SEMICOLON: IElementType = RgTokenType("SEMICOLON")
    @JvmField val COMMA: IElementType = RgTokenType("COMMA")

    @JvmField val WHITE_SPACE = TokenType.WHITE_SPACE
    @JvmField val UNKNOWN: IElementType = RgTokenType("UNKNOWN")

    @JvmField val PREPROCESSOR_BEGIN: IElementType = RgTokenType("PREPROCESSOR_BEGIN")
    @JvmField val PREPROCESSOR_END: IElementType = RgTokenType("PREPROCESSOR_END")
    @JvmField val PREPROCESSOR_DEFINE: IElementType = RgTokenType("PREPROCESSOR_DEFINE")
    @JvmField val PREPROCESSOR_UNDEF: IElementType = RgTokenType("PREPROCESSOR_UNDEF")
    @JvmField val PREPROCESSOR_IF: IElementType = RgTokenType("PREPROCESSOR_IF")
    @JvmField val PREPROCESSOR_IFDEF: IElementType = RgTokenType("PREPROCESSOR_IFDEF")
    @JvmField val PREPROCESSOR_IFNDEF: IElementType = RgTokenType("PREPROCESSOR_IFNDEF")
    @JvmField val PREPROCESSOR_ELSE: IElementType = RgTokenType("PREPROCESSOR_ELSE")
    @JvmField val PREPROCESSOR_ELIF: IElementType = RgTokenType("PREPROCESSOR_ELIF")
    @JvmField val PREPROCESSOR_ENDIF: IElementType = RgTokenType("PREPROCESSOR_ENDIF")
    @JvmField val PREPROCESSOR_ERROR: IElementType = RgTokenType("PREPROCESSOR_ERROR")
    @JvmField val PREPROCESSOR_PRAGMA: IElementType = RgTokenType("PREPROCESSOR_PRAGMA")
    @JvmField val PREPROCESSOR_EXTENSION: IElementType = RgTokenType("PREPROCESSOR_EXTENSION")
    @JvmField val PREPROCESSOR_VERSION: IElementType = RgTokenType("PREPROCESSOR_VERSION")
    @JvmField val PREPROCESSOR_LINE: IElementType = RgTokenType("PREPROCESSOR_LINE")
    @JvmField val PREPROCESSOR_DEFINED: IElementType = RgTokenType("PREPROCESSOR_DEFINED")
    @JvmField val PREPROCESSOR_CONCAT: IElementType = RgTokenType("PREPROCESSOR_CONCAT")
    @JvmField val PREPROCESSOR_STRING: IElementType = RgTokenType("PREPROCESSOR_STRING")
    @JvmField val PREPROCESSOR_RAW: IElementType = RgTokenType("PREPROCESSOR_RAW")*/

    /** Not returned by lexer but used instead of PREPROCESSOR_[something] when the kind of directive is unknown  */
    /*@JvmField val PREPROCESSOR_OTHER: IElementType = RgTokenType("PREPROCESSOR_OTHER")

    @JvmField val RESERVED_KEYWORD: IElementType = RgTokenType("RESERVED_KEYWORD")

    @JvmField val PREPROCESSOR_DIRECTIVES = TokenSet.create(
            PREPROCESSOR_BEGIN,
            PREPROCESSOR_END,
            PREPROCESSOR_DEFINE,
            PREPROCESSOR_UNDEF,
            PREPROCESSOR_IF,
            PREPROCESSOR_IFDEF,
            PREPROCESSOR_IFNDEF,
            PREPROCESSOR_ELSE,
            PREPROCESSOR_ELIF,
            PREPROCESSOR_ENDIF,
            PREPROCESSOR_ERROR,
            PREPROCESSOR_PRAGMA,
            PREPROCESSOR_EXTENSION,
            PREPROCESSOR_VERSION,
            PREPROCESSOR_LINE,
            PREPROCESSOR_DEFINED,
            PREPROCESSOR_CONCAT,
            PREPROCESSOR_OTHER)

    // Type specifiers
    @JvmField val FLOAT_TYPE_SPECIFIER_NONARRAY = TokenSet.create(
            FLOAT_TYPE, VEC2_TYPE, VEC3_TYPE, VEC4_TYPE,
            DOUBLE_TYPE, DVEC2_TYPE, DVEC3_TYPE, DVEC4_TYPE)

    @JvmField val INTEGER_TYPE_SPECIFIER_NONARRAY = TokenSet.create(
            INT_TYPE, IVEC2_TYPE, IVEC3_TYPE, IVEC4_TYPE,
            UINT_TYPE, UVEC2_TYPE, UVEC3_TYPE, UVEC4_TYPE)

    @JvmField val BOOL_TYPE_SPECIFIER_NONARRAY = TokenSet.create(BOOL_TYPE, BVEC2_TYPE, BVEC3_TYPE, BVEC4_TYPE)

    @JvmField val MATRIX_TYPE_SPECIFIER_NONARRAY = TokenSet.create(
            MAT2_TYPE, MAT3_TYPE, MAT4_TYPE,
            MAT2X2_TYPE, MAT2X3_TYPE, MAT2X4_TYPE,
            MAT3X2_TYPE, MAT3X3_TYPE, MAT3X4_TYPE,
            MAT4X2_TYPE, MAT4X3_TYPE, MAT4X4_TYPE,
            DMAT2_TYPE, DMAT3_TYPE, DMAT4_TYPE,
            DMAT2X2_TYPE, DMAT2X3_TYPE, DMAT2X4_TYPE,
            DMAT3X2_TYPE, DMAT3X3_TYPE, DMAT3X4_TYPE,
            DMAT4X2_TYPE, DMAT4X3_TYPE, DMAT4X4_TYPE)

    @JvmField val OPAQUE_TYPE_SPECIFIER_NONARRAY = TokenSet.create(SAMPLER_TYPE, ATOMIC_UINT_TYPE)

    @JvmField val TYPE_SPECIFIER_NONARRAY_TOKENS = merge(TokenSet.create(VOID_TYPE), FLOAT_TYPE_SPECIFIER_NONARRAY, INTEGER_TYPE_SPECIFIER_NONARRAY,
            BOOL_TYPE_SPECIFIER_NONARRAY, MATRIX_TYPE_SPECIFIER_NONARRAY, OPAQUE_TYPE_SPECIFIER_NONARRAY,
            TokenSet.create(STRUCT, NAMED_TYPE))
    //

    //When modifying, don't forget to modify GLSLQualifier as well
    @JvmField val QUALIFIER_TOKENS = TokenSet.create(
            //GLSL Storage qualifiers
            CONST_KEYWORD,
            ATTRIBUTE_KEYWORD,
            UNIFORM_KEYWORD,
            VARYING_KEYWORD,
            CENTROID_KEYWORD,
            PATCH_KEYWORD,
            SAMPLE_KEYWORD,
            BUFFER_KEYWORD,
            SHARED_KEYWORD,
            //GLSL Memory qualifiers
            COHERENT_KEYWORD,
            VOLATILE_KEYWORD,
            RESTRICT_KEYWORD,
            READONLY_KEYWORD,
            WRITEONLY_KEYWORD,
            //GLSL Invariant qualifier
            INVARIANT_KEYWORD,
            //GLSL Subroutine qualifier
            SUBROUTINE_KEYWORD,
            //GLSL Precise qualifier
            PRECISE_KEYWORD,
            //GLSL Precision qualifiers
            HIGHP_KEYWORD,
            MEDIUMP_KEYWORD,
            LOWP_KEYWORD,
            //GLSL Parameter modifiers
            IN_KEYWORD,
            OUT_KEYWORD,
            INOUT_KEYWORD,
            //GLSL Interpolation modifiers
            SMOOTH_KEYWORD,
            FLAT_KEYWORD,
            NOPERSPECTIVE_KEYWORD,
            //GLSL Layout qualifiers
            LAYOUT_KEYWORD)

    @JvmField val PRECISION_QUALIFIER_TOKENS = TokenSet.create(HIGHP_KEYWORD, MEDIUMP_KEYWORD, LOWP_KEYWORD)

    // A subset of QUALIFIER_TOKENS which can be used for interface block definition
    @JvmField val INTERFACE_QUALIFIER_TOKENS = TokenSet.create(
            IN_KEYWORD,
            OUT_KEYWORD,
            UNIFORM_KEYWORD,
            BUFFER_KEYWORD
    )

    @JvmField val COMMENTS = TokenSet.create(COMMENT_BLOCK, COMMENT_LINE)

    @JvmField val ITERATION_KEYWORDS = TokenSet.create(WHILE_KEYWORD, DO_KEYWORD, FOR_KEYWORD)
    @JvmField val JUMP_KEYWORDS = TokenSet.create(BREAK_JUMP_STATEMENT, CONTINUE_JUMP_STATEMENT, RETURN_JUMP_STATEMENT, DISCARD_JUMP_STATEMENT, CASE_KEYWORD, DEFAULT_KEYWORD)

    @JvmField val SELECTION_KEYWORDS = TokenSet.create(IF_KEYWORD, ELSE_KEYWORD, SWITCH_KEYWORD)
    @JvmField val FLOW_KEYWORDS = merge(SELECTION_KEYWORDS, JUMP_KEYWORDS, ITERATION_KEYWORDS)

    //Operators in order of precedence (high to low) (Doesn't have to be here, but for clarity)
    //(missing) postfix inc & dec
    @JvmField val UNARY_OPERATORS = TokenSet.create(INC_OP, DEC_OP, PLUS, DASH, BANG, TILDE)
    @JvmField val MULTIPLICATIVE_OPERATORS = TokenSet.create(STAR, SLASH, PERCENT)
    @JvmField val ADDITIVE_OPERATORS = TokenSet.create(PLUS, DASH)
    @JvmField val BIT_SHIFT_OPERATORS = TokenSet.create(LEFT_OP, RIGHT_OP)
    @JvmField val RELATIONAL_OPERATORS = TokenSet.create(LEFT_ANGLE, RIGHT_ANGLE, LE_OP, GE_OP)
    @JvmField val EQUALITY_OPERATORS = TokenSet.create(EQ_OP, NE_OP)
    @JvmField val BIT_WISE_OPERATORS = TokenSet.create(AMPERSAND, CARET, VERTICAL_BAR)//In this order, separately
    @JvmField val LOGICAL_OPERATORS = TokenSet.create(AND_OP, XOR_OP, OR_OP)//In this order, separately
    //(missing) selection (? :)
    @JvmField val ASSIGNMENT_OPERATORS = TokenSet.create(EQUAL, MUL_ASSIGN, DIV_ASSIGN, ADD_ASSIGN, SUB_ASSIGN, MOD_ASSIGN, LEFT_ASSIGN, RIGHT_ASSIGN, AND_ASSIGN, XOR_ASSIGN, OR_ASSIGN)

    @JvmField val OPERATORS = merge(
            UNARY_OPERATORS, MULTIPLICATIVE_OPERATORS, ADDITIVE_OPERATORS,
            BIT_SHIFT_OPERATORS, RELATIONAL_OPERATORS, EQUALITY_OPERATORS,
            BIT_WISE_OPERATORS, LOGICAL_OPERATORS, ASSIGNMENT_OPERATORS)

    @JvmField val CONSTANT_TOKENS = TokenSet.create(
            BOOL_CONSTANT, INTEGER_CONSTANT, UINT_CONSTANT, FLOAT_CONSTANT, DOUBLE_CONSTANT)

    @JvmField val EXPRESSION_FIRST_SET = merge(TokenSet.create(
            INTEGER_CONSTANT, FLOAT_CONSTANT, BOOL_CONSTANT, // constants
            INC_OP, DEC_OP, PLUS, DASH, BANG, TILDE, // unary operators
            IDENTIFIER, // function call, variable name, typename
            LEFT_PAREN, // group
            SEMICOLON // empty statement
    ),
            TYPE_SPECIFIER_NONARRAY_TOKENS
    )
    @JvmField val STATEMENT_FIRST_SET = merge(TokenSet.create(
            LEFT_BRACE, // compound_statement
            BREAK_JUMP_STATEMENT, CONTINUE_JUMP_STATEMENT, RETURN_JUMP_STATEMENT, CASE_KEYWORD, DEFAULT_KEYWORD,
            DISCARD_JUMP_STATEMENT, IF_KEYWORD,
            DO_KEYWORD, FOR_KEYWORD, WHILE_KEYWORD, SWITCH_KEYWORD // flow control
    ),
            QUALIFIER_TOKENS,
            EXPRESSION_FIRST_SET
    )*/

    fun merge(vararg sets: TokenSet): TokenSet {
        var tokenSet = TokenSet.create()
        for (set in sets) {
            tokenSet = TokenSet.orSet(tokenSet, set)
        }
        return tokenSet
    }
}