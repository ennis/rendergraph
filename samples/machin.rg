module deferred;
import std;
import brdf;


// No interpolants
// One output (vec4 position)
// One input: (vec3 position)

fun vertex_shader(
    @Buffer position: vec3): vec4
{

}

component H: ij {
    pass {
        vertex(function);
    }
}
