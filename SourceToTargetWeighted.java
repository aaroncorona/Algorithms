import java.util.*;

// import com.google.common.primitives.Ints;
@SuppressWarnings("unchecked")

// Implement Dijkstra's shortest path algorithm
public class SourceToTargetWeighted {

    // Non-Static variables that refer to a specific graph. Each obj should get their own. This makes them public
    // Number of total nodes on the graph (node and vertex are interchangable words)
    public int vertices;
    // Array of Lists that gives the node, plus a list for the neighbors (the nodes given node is connected to)
    public LinkedList<Integer>[] nodes;
    // Array that gives the edge weight (corresponds to the node array List position)
    public LinkedList<Integer>[] nodeEdgeWeights;
    // List that gives the edges for the shortest path through the graph
    public LinkedList<Integer> pathShortestGraph;
    // List that gives the edges for the shortest path from source to target
    public LinkedList<Integer> pathShortest;
    // Weight of the shortest path
    public int pathShortestWeight;

    // Constructor
    public SourceToTargetWeighted(int vertices) {
        // Establish parameter
        this.vertices = vertices;
        // Create the object specific arrays (which need a vertex param, shouldn't be static)
        nodes = new LinkedList[vertices];
        nodeEdgeWeights = new LinkedList[vertices];
        // Fill the arrays with Lists
        for (int i = 0; i < vertices; i++) {
            nodes[i] = new LinkedList<Integer>();
            nodeEdgeWeights[i] = new LinkedList<Integer>();
        }
    }

    // Static functions: These static methods are for general purposes on graphs that should be shared between objects
    // Build the graph
    public static void addEdge(int parent, int child, int weight, 
                               LinkedList<Integer>[] nodes, 
                               LinkedList<Integer>[] nodeEdgeWeights) {
        // Add edge to node array (add a value to the inner List that stores neighbor locations)
        nodes[parent].addLast(child);
        // Add the weight of that edge in another array (add a value that corresponds to the List positions)
        nodeEdgeWeights[parent].addLast(weight);
    }

    // Implement Dijkstra's algorithm
    // This is a function to get the shortest path in weight through the whole graph (not a direct path)
    // The result is a path where more expensive edges in the graph are skipped if they are redudant
    // Return the detail of the SSP
    public static LinkedList<Integer> dijkstra(int source, int target,
                                               LinkedList<Integer>[] nodes, 
                                               LinkedList<Integer>[] nodeEdgeWeights) {
        // Create a list (queue) to track nodes that need to be visited (needs FIFO ordering to ensure the traversal is breadth-first)
        LinkedList<Integer> queue = new LinkedList<Integer>();

        // Create a list to track nodes we register as our path taken (this is at the edge level)
        LinkedList<Integer> path = new LinkedList();

        // Create node variable for the given node we are visiting in the loop
        int node = source;

        // Put the parameter node in the queue to start
        queue.add(source);

        while(queue.size() > 0) {

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
                int pathWeight = getPathWeight(path, nodes, nodeEdgeWeights);

                // Print to check (optional)
                // System.out.println("Current Path Weight: " + pathWeight);
                // System.out.println("Current node: " + node);
                // System.out.println("Node to check: " + child);

                // Update Potential path (the current path plus the potential child, substituted for the old path)
                LinkedList<Integer> potentialPath = (LinkedList) path.clone();
                int potentialPathWeight = 10000; // start with dummy value that's high enough to not be disguised as a real path
                if(path.contains(child)
                        && potentialPath.size() > 2) {
                    // Update Potential path so that the given child has a new parent of the current node
                    potentialPath.set(path.indexOf(child)-1, node);
                    // Update Potential path weight
                    potentialPathWeight = getPathWeight(potentialPath, nodes, nodeEdgeWeights);
                }

                // Decide if we should visit the child. We will visit the child if:
                // (1) It is not above the target node (to limit to the source to target path)
                // (2) It has never been visited, or
                // (3) It has been visited, but we discovered a shorter path to that child
                if(child > target) {
                    // Print to check (optional)
                    // System.out.println("Don't update with " + child + " (above target).");
                } else if(!path.contains(child)){
                    queue.addLast(child);
                    path.addLast(node); // Update the path
                    path.addLast(child);
                    // System.out.println(child + " has never been visited. We will visit.");
                    // The child has been visited, but this new "potential path" gives a distance improvement
                } else if (potentialPathWeight < pathWeight){
                    path = potentialPath; // Update path to the potential path because it has proven to be shorter
                    // System.out.println(node + " has a lower distance. We will switch the path.");
                } else { // Otherwise, do not add the parent or child to the list to visit or replace in the path
                    // System.out.println("Don't update with " + child + " (path is further).");
                }
            }
        }
    return path;
    }

    // Function to loop through a given path through the graph and find the total distance/weight of that path
    public static int getPathWeight (LinkedList<Integer> path,
                                     LinkedList<Integer>[] nodes,
                                     LinkedList<Integer>[] nodeEdgeWeights) {
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

    // Function to get the shortest path in jumps from the start to finish
    // The result is a path where unneeded children are skipped (1 vertex per level), making it a direct path / DFS. 
    // It's assumed we already have a path that eliminates extra edges and we just need to eliminate extra nodes
    public static LinkedList<Integer> getShortestPath(int source, int target,
                                                      LinkedList<Integer> path) { // path param must have parent --> child sequence ordering
        // Get the parents
        LinkedList<Integer> pathParent = new LinkedList<Integer>(); 
        for(int i = 0; i < path.size(); i=i+2){ // parents are every other int
            pathParent.add(path.get(i));
        }
        // Get the children (this is a unique list, we only visit each child once per dj)
        LinkedList<Integer> pathChild = new LinkedList<Integer>(); 
        for(int i = 1; i < path.size(); i=i+2){ 
            pathChild.add(path.get(i));
        }
        LinkedList<Integer> pathShortest = new LinkedList<Integer>(); 
        pathShortest.add(target); // start w target as the first parent (to go down the correct DFS path)
        while(!pathShortest.contains(source)){ // until we reach the source node
            int nextVertexChild = pathShortest.get(pathShortest.size()-1); // get the value we last added (the one that still needs a parent to get to the next level)
            int nextVertexParent = pathParent.get(pathChild.indexOf(nextVertexChild)); // lookup the parent in the parent array that corresponds to the same positions/indexes
            pathShortest.add(nextVertexChild); // add both the parent and child to have the path at the edge granularity
            pathShortest.add(nextVertexParent); 
         }
        // Remove the dup target value entry that we needed to kick of the loop
        pathShortest.remove(0); 
        // Reverse the path to appear like a normal DFS traversal
        Collections.reverse(pathShortest);
        // Return the list
        return pathShortest;
    }

    public static void main(String[] args)  {
        SourceToTargetWeighted g = new SourceToTargetWeighted(8);

        // Build the graph, log it in lists
        g.addEdge(0, 1, 10, g.nodes, g.nodeEdgeWeights);
        g.addEdge(0, 2, 20, g.nodes, g.nodeEdgeWeights);
        g.addEdge(1, 3, 30, g.nodes, g.nodeEdgeWeights);
        g.addEdge(1, 7, 40, g.nodes, g.nodeEdgeWeights);
        g.addEdge(2, 3, 10, g.nodes, g.nodeEdgeWeights);
        g.addEdge(3, 4, 10, g.nodes, g.nodeEdgeWeights);
        g.addEdge(3, 5, 40, g.nodes, g.nodeEdgeWeights);
        g.addEdge(4, 5, 10, g.nodes, g.nodeEdgeWeights);
        g.addEdge(4, 6, 20, g.nodes, g.nodeEdgeWeights);
        g.addEdge(5, 6, 50, g.nodes, g.nodeEdgeWeights);
        g.addEdge(6, 7, 90, g.nodes, g.nodeEdgeWeights);

        // Find the shortest path through the entire graph given node constraints (eliminate extra edges)
        g.pathShortestGraph = g.dijkstra(0, 5, g.nodes, g.nodeEdgeWeights); // source and target params should be the same for each object to optimize the same traversal
        System.out.println("Shortest Path through graph (all edges):  " + Arrays.toString(g.pathShortestGraph.toArray()));

        // Find the shortest path from source to target (eliminate extra vertices)
        g.pathShortest = g.getShortestPath(0, 5, g.pathShortestGraph); 
        System.out.println("Shortest Path from to target (all edges): " + Arrays.toString(g.pathShortest.toArray()));
        
        // Get weight of the shortest path
        g.pathShortestWeight = getPathWeight(g.pathShortest, g.nodes, g.nodeEdgeWeights);
        System.out.println("Shortest Path weight: " + g.pathShortestWeight);
    }
}

