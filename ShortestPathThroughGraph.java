import java.util.*;

// import com.google.common.primitives.Ints;
@SuppressWarnings("unchecked")

// Implement Dijkstra's shortest path algorithm
public class ShortestPathThroughGraph {

    // Number of total nodes on the graph (node and vertex are interchangable words)
    int vertices;

    // Array of Lists that gives the node, plus a list for the neighbors (the nodes given node is connected to)
    LinkedList<Integer>[] nodes;
    // Array that gives the edge weight (corresponds to the node array List position)
    LinkedList<Integer>[] nodeEdgeWeights;
    // Array that says if a node was visited or not in the BFS traversal (this is at the edge level)
    LinkedList<Integer> traversePath;

    // Constructor
    ShortestPathThroughGraph(int vertices) {
        // Establish parameter
        this.vertices = vertices;
        // Create the object specific arrays (which need a vertex param, shouldn't be static)
        nodes = new LinkedList[vertices];
        nodeEdgeWeights = new LinkedList[vertices];
        traversePath = new LinkedList();
        // Fill the arrays with Lists
        for (int i = 0; i < vertices; i++) {
            nodes[i] = new LinkedList<Integer>();
            nodeEdgeWeights[i] = new LinkedList<Integer>();
        }
    }

    // Build the graph
    void addEdge(int parent, int child, int weight) {
        // Add edge to node array (add a value to the inner List that stores neighbor locations)
        nodes[parent].addLast(child);
        // Add the weight of that edge in another array (add a value that corresponds to the List positions)
        nodeEdgeWeights[parent].addLast(weight);
        // // Add the parents (inverse of node array)
        // node_get_parent[child].addLast(source_vertex);
    }

    // Implement Dijkstra's algo
    void Dijkstra(int root_node) {
        // Create a list (queue) to track nodes that need to be visited (needs FIFO ordering to ensure the traversal is breadth-first)
        LinkedList<Integer> queue = new LinkedList<Integer>();

        // Put the parameter node in the queue to start
        queue.add(root_node);

        while(queue.size() > 0) {
            // Create node variable for the given node we are visiting in the loop
            int node = root_node;

            // Go to the next vertex in the list (e.g. if we were at vertex 0 in the last loop, the node will now be set to 1)
            node = queue.peek();

            // Add the weight of the path we just traveled from
            int current_weight = 0;
            // Find the parent to determine the edge weight
            int parent = 0;

            // Remove the current node from the queue to avoid an infinite loop from the current node never being removed from the queue
            queue.remove();

            // Iterate through the given node's linked list (read its neighbors) to update the queue
            for (int i = 0; i < nodes[node].size(); i++) {

                // Get child to check
                int child = nodes[node].get(i);
                // Get child Weight
                int childWeight = nodeEdgeWeights[node].get(i);

                // Calculate path weight
                int pathWeight = getPathWeight(traversePath);

                // Potential path (the current path plus the potential child, substituted for the old path)
                LinkedList<Integer> potentialPath = (LinkedList) traversePath.clone();
                int potentialPathWeight = 10000; // start with dummy value that's high enough to not be disguised as a real path
                if(traversePath.contains(child)
                        && potentialPath.size() > 2) {
                    // Update Potential path so that the given child has a new parent of the current node
                    potentialPath.set(traversePath.indexOf(child)-1, node);
                    // Update Potential path weight
                    potentialPathWeight = getPathWeight(potentialPath);
                }

                // Decide if we should visit the child. We will visit the child if:
                // (1) It has never been visited, or
                // (2) it has been visited, but we discovered a shorter path to that child
                if(!traversePath.contains(child)){
                    queue.addLast(child);
                    traversePath.addLast(node); // Update the path
                    traversePath.addLast(child);
                    // The child has been visited, but this new "potential path" gives a distance improvement
                } else if (potentialPathWeight < pathWeight){
                    traversePath = potentialPath; // Update path to the potential path because it has proven to be shorter
                } // Otherwise, do not add the parent or child to the list to visit or replace in the path
            }
        }
        System.out.println("Final Path (exact edges): " + Arrays.toString(traversePath.toArray()));
        // Convert the edge level path to node path level (1 step less granular)
        HashSet bestNodes = new HashSet(); // Create a unique list of nodes
        for(int i = 0; i < traversePath.size(); i++){
            bestNodes.add(traversePath.get(i));
        }
        System.out.println("Nodes Traversed: " + Arrays.toString(bestNodes.toArray()));
        // Calculate final path weight
        int pathWeight = getPathWeight(traversePath);
        System.out.println("Final Path Weight: " + pathWeight);
    }

    // Function to loop through a given path through the graph and find the total distance/weight of that path
    public int getPathWeight (LinkedList<Integer> path) {
        int totalWeight = 0;
        for (int a = 0; a < path.size(); a = a+2) {
            // Start at index 0 (start of the graph)
            // Each loop completes the next parent to child edge
            int parent = path.get(a);
            int child = path.get(a+1);
            int childIndexInParent = nodes[parent].indexOf(child);
            int weight = nodeEdgeWeights[parent].get(childIndexInParent);
            totalWeight = totalWeight + weight;
        }
        return totalWeight;
    }

    public static void main(String[] args)  {
        ShortestPathThroughGraph g = new ShortestPathThroughGraph(8);

        // Build the graph
        // Note: The best weight would be 120 when skipping unneeded expensive edges like not going from 1 to 3
        g.addEdge(0, 1, 10);
        g.addEdge(0, 2, 20);
        g.addEdge(1, 3, 30);
        g.addEdge(1, 7, 40);
        g.addEdge(2, 3, 10);
        g.addEdge(3, 4, 10);
        g.addEdge(3, 5, 30);
        g.addEdge(4, 5, 10);
        g.addEdge(4, 6, 20);
        g.addEdge(5, 6, 50);
        g.addEdge(6, 7, 200);

        // Run Dijkstra's algorithm
        g.Dijkstra(0);
    }
}
