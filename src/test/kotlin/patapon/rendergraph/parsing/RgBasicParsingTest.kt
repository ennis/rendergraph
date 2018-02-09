package patapon.rendergraph.parsing

import com.intellij.testFramework.ParsingTestCase
import patapon.rendergraph.RenderGraphParserDefinition

class RgBasicParsingTest: ParsingTestCase("", "rg", true,  RenderGraphParserDefinition())
{
    fun testBasic() = doTest(true)
    fun testEmpty() = doTest(true)

    override fun getTestDataPath() = "src/test/resources/codeSamples"
    override fun skipSpaces() = true
}