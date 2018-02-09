package patapon.rendergraph.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase
import patapon.rendergraph.psi.RgElementTypes.*
import patapon.rendergraph.psi.RgTokenType

object RenderGraphParserUtil: GeneratedParserUtilBase()  {
    @JvmStatic
    fun unpairedToken(b: PsiBuilder, level: Int): Boolean =
        when (b.tokenType) {
            LBRACE, RBRACE -> false
            //LPAREN, RPAREN -> false
            //LBRACK, RBRACK -> false
            else -> {
                b.advanceLexer()
                true
            }
        }

    @JvmStatic
    fun itemTerminator(b: PsiBuilder, level: Int): Boolean {
        // get current token
        var encounteredEndOfLine = false
        b.setWhitespaceSkippedCallback { elem, _, _ ->
            when (elem) {
                RgTokenType.EOL -> { encounteredEndOfLine = true }
            }
        }

        // true if the next token is self-delimiting, i.e.
        // if we do not need a newline before
        val isSelfDelimiting = when (b.tokenType) {
            RBRACE -> true
            SEMICOLON -> true
            else -> false
        }

        return encounteredEndOfLine || isSelfDelimiting
    }
}