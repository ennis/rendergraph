package patapon.rendergraph.lang.diagnostics

object Diagnostics {
    val EXPECTED_X_FOUND_Y = DiagnosticFactory2<String, String>(Severity.ERROR, "expected a {0}: found '{1}'")
    val UNDECLARED_IDENTIFIER = DiagnosticFactory1<String>(Severity.ERROR, "undeclared identifier: '{0}'")
    val AMBIGUOUS_TYPE_REFERENCE = DiagnosticFactory1<String>(Severity.ERROR, "ambiguous type reference: '{0}'")
}