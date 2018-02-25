package patapon.rendergraph.lang.diagnostics

interface DiagnosticSink {
    fun report(diag: Diagnostic)
}
