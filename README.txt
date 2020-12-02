input file format:
    number of vertices
    source vertex
    number of edges
    vertex, cost, vertex
    vertex, cost, vertex
    .
    .
    .
    .
    vertex, cost, vertex

output file format:
    number of vertices
    vertex, cost, parent
    vertex, cost, parent 
    (source vertex) vertex -1 -1
    .
    .
    .
    .
    vertex, cost, parent

Example:
       
       17
    1-----3
     \   
   14 \ 
       2

    number of vertices = 3
    source vertex = 2 ( can pick any )
    number of edges = 2
    1 -> 3 cost = 17
    1 -> 2 cost = 14

    input file:
    3
    2
    2
    1 17 3
    1 14 2

    output file:
    3
    1 14 2
    2 -1 -1
    3 31 1
