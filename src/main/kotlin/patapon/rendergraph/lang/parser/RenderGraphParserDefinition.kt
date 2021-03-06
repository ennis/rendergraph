package patapon.rendergraph.lang.parser

import com.intellij.lang.*
import com.intellij.lexer.Lexer
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.tree.*
import patapon.rendergraph.lang.lexer.RenderGraphLexerAdapter
import patapon.rendergraph.lang.RenderGraphLanguage
import patapon.rendergraph.lang.psi.*

class RenderGraphParserDefinition : ParserDefinition {

    companion object {
        val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE, RgTokenType.EOL)
        val COMMENTS = TokenSet.create(RgTokenType.BLOCK_COMMENT, RgTokenType.EOL_COMMENT)
        val FILE = IFileElementType(RenderGraphLanguage.INSTANCE)
        val LOG = Logger.getInstance(RenderGraphParserDefinition::class.java)
    }

    override fun createLexer(project: Project): Lexer {
        return RenderGraphLexerAdapter()
    }

    override fun getWhitespaceTokens(): TokenSet {
        return WHITE_SPACES
    }

    override fun getCommentTokens(): TokenSet {
        return COMMENTS
    }

    override fun getStringLiteralElements(): TokenSet {
        return TokenSet.EMPTY
    }

    override fun createParser(project: Project): PsiParser {
        LOG.info("CREATE PARSER")
        return RenderGraphParser()
    }

    override fun getFileNodeType(): IFileElementType {
        return FILE
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return RgFile(viewProvider)
    }

    override fun spaceExistanceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements {
        return ParserDefinition.SpaceRequirements.MAY
    }

    override fun createElement(node: ASTNode): PsiElement {
        return RgElementTypes.Factory.createElement(node)
    }

}