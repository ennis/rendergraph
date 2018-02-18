package patapon.rendergraph.lang.ir

import patapon.rendergraph.lang.psi.RgPath

fun RgPath.toIr(): Path {
    return Path.create(identifier.text)
}