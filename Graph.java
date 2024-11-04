import java.util.*;

/**
 * The Graph class represents the neighbor relationships between territories on a game board.
 * Each territory is treated as a vertex, and edges are used to denote connections between
 * neighboring territories. The graph also handles 'inactive' vertices, which are territories
 * that are not currently part of the game.
 *
 * This class provides methods to add/remove edges, manage vertex activation status, and
 * determine the size of the largest connected cluster of territories owned by a player.
 *
 * Author: Zehang Zhang
 */
public class Graph {
    private final int numVertices; // Total number of vertices in the graph
    private final HashMap<Integer, List<Integer>> adjacencyList; // Adjacency list for graph edges
    private final Set<Integer> inactiveVertices; // Set of vertices that are inactive

    /**
     * Constructor to initialize the graph with a given number of vertices.
     * Creates an empty adjacency list and marks all vertices as active.
     *
     * @param numVertices The total number of vertices in the graph.
     */
    public Graph(int numVertices) {
        this.numVertices = numVertices;
        this.adjacencyList = new HashMap<>();
        this.inactiveVertices = new HashSet<>();

        // Initialize adjacency list for each vertex
        for (int i = 0; i < numVertices; i++) {
            adjacencyList.put(i, new ArrayList<>());
        }
    }

    /**
     * Adds an edge between two vertices if both are active in the graph.
     *
     * @param v1 The first vertex.
     * @param v2 The second vertex.
     */
    public void addEdge(int v1, int v2) {
        if (isInGraph(v1) && isInGraph(v2)) {
            adjacencyList.get(v1).add(v2);
            adjacencyList.get(v2).add(v1);
        }
    }

    /**
     * Removes the edge between two vertices if both are active in the graph.
     *
     * @param v1 The first vertex.
     * @param v2 The second vertex.
     */
    public void removeEdge(int v1, int v2) {
        if (isInGraph(v1) && isInGraph(v2)) {
            adjacencyList.get(v1).remove((Integer) v2);
            adjacencyList.get(v2).remove((Integer) v1);
        }
    }

    /**
     * Retrieves the list of adjacent (neighboring) vertices for a given vertex.
     *
     * @param vertex The vertex for which to get the adjacent vertices.
     * @return A list of adjacent vertices.
     */
    public List<Integer> getAdjacent(int vertex) {
        return adjacencyList.getOrDefault(vertex, new ArrayList<>());
    }

    /**
     * Marks a vertex as inactive, effectively removing it from the graph.
     *
     * @param vertex The vertex to deactivate.
     */
    public void deactivateVertex(int vertex) {
        inactiveVertices.add(vertex);
    }

    /**
     * Checks whether a vertex is active in the graph.
     *
     * @param vertex The vertex to check.
     * @return True if the vertex is active, false otherwise.
     */
    public boolean isInGraph(int vertex) {
        return !inactiveVertices.contains(vertex);
    }

    /**
     * Finds the size of the largest connected cluster of territories owned by a specific player.
     * This method performs a depth-first search (DFS) across the graph to identify clusters.
     *
     * @param player The player whose territories to check.
     * @param map The game board's map object.
     * @return The size of the largest connected cluster of territories owned by the player.
     */
    public int largestConnectedCluster(Player player, Map map) {
        Set<Integer> visited = new HashSet<>();
        int largestClusterSize = 0;

        // Iterate over all vertices to find clusters owned by the player
        for (int vertex = 0; vertex < numVertices; vertex++) {
            if (isInGraph(vertex) && !visited.contains(vertex) &&
                    map.getTerritory(vertex / map.COLUMNS, vertex % map.COLUMNS).getOwner() == player) {
                int clusterSize = dfsClusterSize(vertex, visited, player, map);
                largestClusterSize = Math.max(largestClusterSize, clusterSize);
            }
        }

        return largestClusterSize;
    }

    /**
     * Helper method to perform depth-first search to determine the size of a connected cluster.
     *
     * @param vertex The starting vertex for the DFS.
     * @param visited A set to track visited vertices.
     * @param player The player whose territories are being counted.
     * @param map The game board's map object.
     * @return The size of the connected cluster of territories.
     */
    private int dfsClusterSize(int vertex, Set<Integer> visited, Player player, Map map) {
        visited.add(vertex);
        int clusterSize = 1;

        // Recursively explore all neighboring vertices
        for (int neighbor : adjacencyList.get(vertex)) {
            if (!visited.contains(neighbor) &&
                    map.getTerritory(neighbor / map.COLUMNS, neighbor % map.COLUMNS).getOwner() == player) {
                clusterSize += dfsClusterSize(neighbor, visited, player, map);
            }
        }

        return clusterSize;
    }
}
