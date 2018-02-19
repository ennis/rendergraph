package patapon.rendergraph.ide.highlight

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.*
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import patapon.rendergraph.lang.psi.RgTokenType
import patapon.rendergraph.lang.psi.RgTokenTypes
import patapon.rendergraph.ide.colors.RenderGraphColors
import patapon.rendergraph.lang.lexer.RenderGraphLexerAdapter

class RenderGraphSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer {
        return RenderGraphLexerAdapter()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
        when(tokenType) {
            in RgTokenTypes.KEYWORDS -> KEYWORD_KEYS
            in RgTokenTypes.NUMBER_LITERALS -> NUMBER_LITERAL_KEYS
            RgTokenType.BLOCK_COMMENT, RgTokenType.EOL_COMMENT -> COMMENT_KEYS
            TokenType.BAD_CHARACTER -> BAD_CHAR_KEYS
            else -> EMPTY_KEYS
        }

    companion object {
        private val BAD_CHAR_KEYS = arrayOf(HighlighterColors.BAD_CHARACTER)
        private val NUMBER_LITERAL_KEYS = arrayOf(RenderGraphColors.NUMBER.textAttributesKey)
        private val KEYWORD_KEYS = arrayOf(RenderGraphColors.KEYWORD.textAttributesKey)
        private val COMMENT_KEYS = arrayOf(RenderGraphColors.EOL_COMMENT.textAttributesKey)
        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    }
}