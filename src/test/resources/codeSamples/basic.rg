module M;

component C0
{
}

// single member
component C1
{
    val a = 0;
}

// two members
component C2
{
    val a = 0;
    val b = 0;
}

component C3
{
    val a = 0;
    val b = 0;

    fun c(): int {
    }
}
