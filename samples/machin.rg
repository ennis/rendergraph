module deferred;
import std;
import brdf;

// Separate the interface (the signature of the filter function) from the implementation (the pass)
component ImageFilterPass {
    //
    component vec4 apply(vec2 texcoord);

    pass {
    }
}

component Hello {
    @Attrib
    int hello (fsdfn f) {
    }

    // Eponymous member
    pass Hello {
        depth_test(true);
    }
}

// Component instance of Hello
component HelloImpl: Hello {
    int hello() {
        // code here
    }

}

component GaussianBlur: ImageFilter
{
    // set a parameter
    pixel = null

}

component Node001: ImageFilterPass
{
    // 'implement' the interface
    apply = /*<something that matches the signature of apply>*/;
    // the implementation can also be inline (anonymous component)
    vec4 apply() {  // this is invisible in the node graph
        ...
    }
}

// variant generation
variant component Node001: ImageFilterPass
{
    apply = variant GaussianBlur, RadialBlur, ...;
}
