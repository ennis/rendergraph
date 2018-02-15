package patapon.rendergraph.ide.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.roots.ProjectRootManager
import patapon.rendergraph.lang.Compiler
import patapon.rendergraph.lang.CompilerArguments

class CompileAction: AnAction("Compile RenderGraph project") {
    override fun actionPerformed(e: AnActionEvent?) {
        val project = e!!.project!!
        val compilerArguments = CompilerArguments(sourceDirectory=".", outputLibraryFile="output.rglib")
        val compiler = Compiler(compilerArguments, project)
        compiler.compile()
    }
}
