package patapon.rendergraph.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import static patapon.rendergraph.lang.psi.RgElementTypes.*;
import static patapon.rendergraph.lang.psi.RgTokenType.*;
import static com.intellij.psi.TokenType.*;

%%

%class RenderGraphLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}



///////////////////////////////////////////////////////////////////////////////////////////////////
// Whitespaces
///////////////////////////////////////////////////////////////////////////////////////////////////

EOL_WS           = \n | \r | \r\n
LINE_WS          = [\ \t]
WHITE_SPACE_CHAR = {EOL_WS} | {LINE_WS}
WHITE_SPACE      = {WHITE_SPACE_CHAR}+

///////////////////////////////////////////////////////////////////////////////////////////////////
// Identifier
///////////////////////////////////////////////////////////////////////////////////////////////////

IDENTIFIER = [_\p{xidstart}][\p{xidcontinue}]*
//SUFFIX = {IDENTIFIER}
//IDENTIFIER          = {NON_DIGIT}({NON_DIGIT} | {DIGIT})*

///////////////////////////////////////////////////////////////////////////////////////////////////
// Literals (GLSL spec)
///////////////////////////////////////////////////////////////////////////////////////////////////

DIGIT               = [0-9]
OCTAL_DIGIT         = [0-7]
HEX_DIGIT           = [0-9A-Fa-f]
//NON_DIGIT           = [_a-zA-Z]

UINT_SUFFIX      = [Uu]
INT_LITERAL    = ({DECIMAL_LIT} | {HEX_LIT} | {OCTAL_LIT})
DECIMAL_LIT    = (0|([1-9]({DIGIT})*))
HEX_LIT        = 0[Xx]({HEX_DIGIT})*
OCTAL_LIT      = 0({OCTAL_DIGIT})*

DOUBLE_SUFFIX = (lf|LF)
FLOAT_SUFFIX = [fF]
FLOAT_LITERAL   = (({FLOAT_LIT1})|({FLOAT_LIT2})|({FLOAT_LIT3})|({FLOAT_LIT4}))
FLOAT_LIT1  = ({DIGIT})+"."({DIGIT})*({EXPONENT_PART})?
FLOAT_LIT2  = "."({DIGIT})+({EXPONENT_PART})?
FLOAT_LIT3  = ({DIGIT})+({EXPONENT_PART})
FLOAT_LIT4  = ({DIGIT})+
EXPONENT_PART       = [Ee]["+""-"]?({DIGIT})*

///////////////////////////////////////////////////////////////////////////////////////////////////
// States
///////////////////////////////////////////////////////////////////////////////////////////////////
%state INITIAL
%state IN_BLOCK_COMMENT
%state MAYBE_SEMICOLON

%%

<YYINITIAL> {
  [^] { yybegin(INITIAL); yypushback(1); }
}


<INITIAL> {

    // Boolean literals
    true                                    {return BOOL_LITERAL; }
    false                                   {return BOOL_LITERAL; }
    // RenderGraph specific
    //pass                                    {return PASS_KW;}
    component                               {return COMPONENT_KW;}
    module                                  {return MODULE_KW;}
    import                                  {return IMPORT_KW;}
    fun                                     {return FUN;}
    val                                     {return VAL;}
    const                                   {return CONST;}
    uniform                                 {return UNIFORM_KW;}
    return                                  {return RETURN;}
    // Tokens
    "{"                                     {return LBRACE; }
    "}"                                     {return RBRACE; }
    "["                                     {return LBRACK; }
    "]"                                     {return RBRACK; }
    "("                                     {return LPAREN; }
    ")"                                     {return RPAREN; }
    "="                                     {return EQ; }
    "*="                                    {return MULEQ; }
    "*"                                     {return MUL; }
    "/="                                    {return DIVEQ; }
    "/"                                     {return DIV; }
    "+="                                    {return PLUSEQ; }
    "+"                                     {return PLUS; }
    "-="                                    {return MINUSEQ; }
    "-"                                     {return MINUS; }
    "%="                                    {return REMEQ; }
    "%"                                     {return REM; }
    "&="                                    {return ANDEQ; }
    "&"                                     {return AND; }
    "^="                                    {return XOREQ; }
    "^"                                     {return XOR; }
    "|="                                    {return OREQ; }
    "|"                                     {return OR; }
    "--"                                    {return MINUSMINUS; }
    "++"                                    {return PLUSPLUS; }
    "=="                                    {return EQEQ; }
    "!="                                    {return EXCLEQ; }
    "<"                                     {return LT; }
    ">"                                     {return GT; }
    ">="                                    {return GTEQ; }
    "<="                                    {return LTEQ; }
    "&&"                                    {return ANDAND; }
    "||"                                    {return OROR; }
    ":"                                     {return COLON; }
    "!"                                     {return EXCL; }
    "."                                     {return DOT; }
    ";"                                     {return SEMICOLON; }
    ","                                     {return COMMA; }
    "@"                                     {return AT; }
    // Comments
    "//" .*                                 {return EOL_COMMENT;}
    "/*"                                    { yybegin(IN_BLOCK_COMMENT); yypushback(2); }
    // Identifier
    {IDENTIFIER}                            { return IDENTIFIER; }
    // Literals
    {INT_LITERAL}{UINT_SUFFIX}              {return UINT_LITERAL; }
    {INT_LITERAL}                           {return INT_LITERAL; }
    {FLOAT_LITERAL}{DOUBLE_SUFFIX}          {return DOUBLE_LITERAL; }
    {FLOAT_LITERAL}{FLOAT_SUFFIX}?          {return FLOAT_LITERAL; }
    {LINE_WS}                               {return WHITE_SPACE; }
    {EOL_WS}                                {return EOL;}
}

// automatic semicolon insertion
//<MAYBE_SEMICOLON> {
//    {LINE_WS}                               { return WHITE_SPACE; }
    // This EOL is interpreted as a semicolon
//    {EOL_WS}                                { yybegin(YYINITIAL); yypushback(yytext().length()); return SYNTHETIC_SEMICOLON; }
 //   "//" .*                                 { return EOL_COMMENT; }
 //   "/*"                                    { return BLOCK_COMMENT; }
//    .                                       { yybegin(YYINITIAL); yypushback(yytext().length()); }
//}

<IN_BLOCK_COMMENT> {
  "*/"                                      { yybegin(INITIAL); return BLOCK_COMMENT; }
  <<EOF>>                                   { yybegin(INITIAL); return BLOCK_COMMENT; }   // TODO unterminated block comments are an error
  [^]                                       { }
}

///////////////////////////////////////////////////////////////////////////////////////////////////
// Catch All
///////////////////////////////////////////////////////////////////////////////////////////////////
[^]                                         { return BAD_CHARACTER; }

