package patapon.rendergraph.lang.parser

import com.intellij.testFramework.ParsingTestCase
import patapon.rendergraph.lang.parser.RenderGraphParserDefinition

class RgBasicParsingTest: ParsingTestCase("", "rg", true, RenderGraphParserDefinition())
{
    fun testBasic() = doTest(true)
    fun testEmpty() = doTest(true)
    fun testFunction() = doTest(true)
    fun testAttributes() = doTest(true)

    override fun getTestDataPath() = "src/test/resources/codeSamples"
    override fun skipSpaces() = true
}