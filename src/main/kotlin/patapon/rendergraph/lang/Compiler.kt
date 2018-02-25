package patapon.rendergraph.lang

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import patapon.rendergraph.lang.declarations.BindingContextImpl
import patapon.rendergraph.lang.diagnostics.Diagnostic
import patapon.rendergraph.lang.diagnostics.DiagnosticSink
import patapon.rendergraph.lang.utils.PrettyPrinterVisitor
import patapon.rendergraph.lang.psi.RgFile
import patapon.rendergraph.lang.psi.RgImports
import patapon.rendergraph.lang.psi.RgModule
import patapon.rendergraph.lang.psi.RgModuleContents
import patapon.rendergraph.lang.resolve.DeclarationResolver
import patapon.rendergraph.lang.types.TypeResolver

// Compiler parameters
data class CompilerArguments(val sourceDirectory: String, val outputLibraryFile: String)

// Contains all services and code to perform compilation of a set of rendergraph files
// that are contained in a directory (and its subdirectories).
// The rendergraph files are compiled to a 'RenderGraph library file' (.rglib)
class Compiler(val compilerArguments: CompilerArguments, val project: Project) {
    companion object {
        var LOG = Logger.getInstance(Compiler::class.java)

        // Recursively traverse a VFS directory and add all source files to the list
        fun traverseDirectory(dir: VirtualFile, outList: MutableList<VirtualFile>) {
            dir.children.forEach { child ->
                if (child.isDirectory) {
                    traverseDirectory(child, outList)
                } else {
                    if (child.extension != null && child.extension == "rg") {
                        LOG.info("Found source file: ${child}")
                        outList.add(child)
                    } else {
                        LOG.info("File of unknown type found in source root: ${child}")
                    }
                }
            }
        }
    }

    fun compileFile(file: RgFile)
    {
        // get module declaration
        val module = file.findChildByClass(RgModule::class.java)
        val imports = file.findChildByClass(RgImports::class.java)
        val moduleContents = file.findChildByClass(RgModuleContents::class.java)

        if (module == null) {
            TODO("Source files must have a module directive at the beginning")
        }

        val diagnosticSink = object : DiagnosticSink {
            override fun report(diag: Diagnostic) {
                LOG.info("compilation diag: " + diag.message)
            }
        }

        val bindingContext = BindingContextImpl()
        val typeResolver = TypeResolver(bindingContext, diagnosticSink)
        val declarationResolver = DeclarationResolver(bindingContext, typeResolver, diagnosticSink)
        val ppBuffer = StringBuilder()
        val prettyPrinter = PrettyPrinterVisitor(declarationResolver, typeResolver, ppBuffer)
        file.acceptChildren(prettyPrinter)

        LOG.info("After declaration pass:")
        LOG.info(ppBuffer.toString())

    }

    fun compile() {
        // resolve the set of files to compile
        LOG.info("Input arguments: ${compilerArguments}")
        val sourceRoots = ProjectRootManager.getInstance(project).contentSourceRoots
        LOG.info("source roots:")

        val sourceList = arrayListOf<VirtualFile>()
        sourceRoots.forEach { sourceRoot ->
            LOG.info("\t${sourceRoot}")
            traverseDirectory(sourceRoot, sourceList)
        }

        // Now we have the list of source files
        // Load/Generate the PSI representation of the files
        // From this PSI, generate the (unresolved) context tree
        val psiManager = PsiManager.getInstance(project)
        sourceList.forEach { vf ->
            val psi = psiManager.findFile(vf)
            if (psi != null) {
                val rgpsi = psi as RgFile
                // PSI found
                LOG.info("PSI: [${vf}], ${rgpsi.children.size} root nodes")
                compileFile(rgpsi)
            } else {
                TODO("Handle not-yet-generated PSIs")
            }
        }



        //TODO("Generate context tree from PSI file")



    }
}

