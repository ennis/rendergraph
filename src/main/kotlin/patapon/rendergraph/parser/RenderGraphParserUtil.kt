package patapon.rendergraph.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase
import patapon.rendergraph.psi.RgElementTypes.*

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
}