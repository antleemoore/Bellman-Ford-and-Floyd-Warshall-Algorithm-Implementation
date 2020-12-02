// Anthony Moore
// Bellman and Warshall Algorithms Implemented Using Java Data Structures
// Runs with input file named "input.txt"

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Graph {
    Set<Point> points = new TreeSet<>();
    Point source;

    // creates and returns a Point to be added to the Array List graph
    Point createPoint(int vertex) {
        Point p = new Point();
        p.vertex = vertex;
        return p;
    }

    // sets the source point given the source's vertex
    void setSource(int source) {
        for (Point p : points)
            if (p.vertex == source)
                this.source = p;

        this.source.distance = 0;
    }

    // finds shortest path using Floyd Warshall Algorthm and returns the 2D array of
    // graph
    Integer[][] findShortestWarshall(int iterations, int numVertices) {
        // array of graph vertices
        Point temp[] = new Point[numVertices];
        temp = points.toArray(temp);

        // graph 2d array to store distances
        Integer arr[][] = new Integer[numVertices][numVertices];

        // initializes all values in graph as max int available
        for (int i = 0; i < numVertices; i++)
            for (int j = 0; j < numVertices; j++)
                arr[i][j] = 200000;

        // each point on the diagonal of the graph is a vertex and is set to zero
        for (int i = 0; i < numVertices; i++) {
            arr[i][i] = 0;
        }

        // for number of edges, for number of vertices x 2, if an edge is found, compare
        // distance
        // of source + edge, to distance of destination. If less, set path and add
        // distance to 2d array
        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < numVertices; j++) {
                for (int k = 0; k < numVertices; k++) {
                    if (temp[j].surroundingPoints.containsKey(temp[k])) {
                        int distCurrPoint = temp[j].surroundingPoints.get(temp[k]);
                        if (temp[j].distance + distCurrPoint < temp[k].distance) {
                            temp[k].distance = temp[j].distance + distCurrPoint;
                            temp[k].parent = temp[j];
                        }

                        arr[j][k] = temp[j].surroundingPoints.get(temp[k]);
                    }
                }
            }
        }

        // for number of vertices x 3, 2d array point becomes the smallest edge distance
        for (int k = 0; k < numVertices; k++) {
            for (int a = 0; a < numVertices; a++) {
                for (int b = 0; b < numVertices; b++) {
                    arr[a][b] = min(arr[a][b], arr[a][k] + arr[k][b]);
                }
            }
        }

        // return 2d array of graph with new distances
        return arr;
    }

    // returns min of two numbers
    int min(int a, int b) {
        if (a < b)
            return a;
        else
            return b;
    }

    // Bellman Ford algorithm for finding shortest distance
    void findShortestBellman(int iterations) {
        // used to store current point distance
        int distCurrPoint;

        // for all edges to all points, change distance to smaller distance iterations
        // (n-1) times
        for (int i = 0; i < iterations; i++) {
            for (Point p : points) {
                for (Point s : p.surroundingPoints.keySet()) {
                    distCurrPoint = p.surroundingPoints.get(s);
                    if (p.distance != Integer.MAX_VALUE) {
                        if (p.distance + distCurrPoint < s.distance) {
                            s.distance = p.distance + distCurrPoint;
                            s.parent = p;
                        }
                    }
                }
            }
        }
    }

    // returns unvisited point with the shortest distance to the source point
    Point setCurrent() {
        Point current = new Point();
        // if source point hasn't been visited, set current to the source point
        if (source.visited == false)
            return source;
        else {
            int dist = Integer.MAX_VALUE;

            // traverses through all vertices in the graph to find the shortest
            // distance point that hasn't been visited yet
            for (Point p : points) {
                if (p.distance < dist && p.visited == false) {
                    dist = p.distance;
                    current = p;
                }
            }
        }

        return current;
    }

    // class for all the vertices
    class Point implements Comparable<Point> {
        // Variable Declarations
        int vertex;
        Integer distance = Integer.MAX_VALUE;
        Map<Point, Integer> surroundingPoints = new HashMap<>();
        boolean visited = false;
        Point parent = null;

        public String toString() {
            return this.vertex + " " + ((this.vertex == source.vertex) ? "0" : this.distance) + " "
                    + ((this.parent == null) ? "" : this.parent.vertex) + (this.vertex == source.vertex ? "0" : "");
        }

        // creates all the connections to the Point and adds them to the Map for that
        // point
        void createConnection(int[] vertex, int[] connection, int[] cost, int size) {
            for (int i = 0; i < size; i++) {
                if (vertex[i] == this.vertex) {
                    for (Point p : points) {
                        if (p.vertex == connection[i])
                            this.surroundingPoints.put(p, cost[i]);
                    }
                }
            }
            for (int i = 0; i < size; i++) {
                if (connection[i] == this.vertex) {
                    for (Point p : points) {
                        if (p.vertex == vertex[i])
                            this.surroundingPoints.put(p, cost[i]);
                    }
                }
            }
        }

        // Override to compareTo() so it compares the vertices of the Points
        @Override
        public int compareTo(Point o) {
            if (this.vertex == o.vertex)
                return 0;
            else if (this.vertex > o.vertex)
                return 1;
            else
                return -1;
        }

    }

    public static void main(String[] args) {
        // Variable Declarations
        int numVertices, sourceVertex, numEdges;
        Scanner readFile;
        Graph graph = new Graph();
        PrintWriter pw, pw2;

        // Initializing Scanner and PrintWriter for File IO
        try {
            readFile = new Scanner(new File("input.txt"));
            pw = new PrintWriter("outputBellman.txt");
            pw2 = new PrintWriter("outputWarshall.txt");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Begin Read from File
        numVertices = readFile.nextInt();
        sourceVertex = readFile.nextInt();
        numEdges = readFile.nextInt();

        Integer[][] arr = new Integer[numVertices][numVertices];
        // Stores all the values from input file into separate arrays for
        // vertices, connections, and cost
        int vertex[] = new int[numEdges], connected[] = new int[numEdges], cost[] = new int[numEdges], index = 0;

        while (readFile.hasNext()) {
            vertex[index] = readFile.nextInt();
            connected[index] = readFile.nextInt();
            cost[index++] = readFile.nextInt();
        }

        readFile.close();
        // End Read from File

        // adds all vertices to the graph
        for (int i = 0; i < numEdges; i++) {
            graph.points.add(graph.createPoint(vertex[i]));
            graph.points.add(graph.createPoint(connected[i]));
        }

        // creates the connections for all the vertices
        for (Point p : graph.points)
            p.createConnection(vertex, connected, cost, numEdges);

        graph.setSource(sourceVertex);

        graph.findShortestBellman(numVertices - 1);

        arr = graph.findShortestWarshall(numEdges, numVertices);

        // begin print to file
        pw2.printf("%d\n", numVertices);
        pw.printf("%d\n", numVertices);

        int printIndex = 0;

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                pw2.printf("%d ", arr[i][j]);
            }
            pw2.printf("\n");
        }
        for (Point p : graph.points) {
            pw.printf("%s", p.toString());

            if (printIndex++ == numVertices - 1)
                break;
            else
                pw.printf("\n");
        }

        pw.close();
        pw2.close();
        // end print to file
    }
}