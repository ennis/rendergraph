module deferred;
import std;
import brdf;

// Separate the interface (the signature of the filter function) from the implementation (the pass)
@Attrib
component ImageFilterPass {
    @Attrib const stuff: int = 30
    @Attrib2 const stuff2 = 50

}
