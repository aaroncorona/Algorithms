
import java.util.*;
@SuppressWarnings("unchecked")

public class ColoredBoard {

  // Max size of the graph
  public int boardSize;
  // Array of Lists that gives the node and it's children (edges)
  public LinkedList<Integer>[] nodes; // dynamic so each class is a unique board/graph
  // Colors (note: parent-children relationships must be the same color or a winning color)
  public String[] nodeColors;
  public String winningColor;
  // Winning paths
  public LinkedList<Integer> winningPath;
  public LinkedList<Integer> winningPathParents;
  public LinkedList<Integer> winningPathShortest;
  public LinkedList<Integer> winningStarts;

  // Constructor
  public ColoredBoard(int boardSize, String winningColor) {

    this.boardSize = boardSize;
    this.winningColor = winningColor;

    // Create the class specific arrays (graph info)
    nodes = new LinkedList[boardSize];
    nodeColors = new String[boardSize];
    winningPath = new LinkedList<Integer>();
    winningPathParents = new LinkedList<Integer>();
    winningPathShortest = new LinkedList<Integer>();
    winningStarts = new LinkedList<Integer>();

    // Create the shell of the graph based on the size parameter passed (blank lists)
    for(int i=0; i<nodes.length; i++) {
      nodes[i] = new LinkedList<Integer>();
      nodeColors[i] = winningColor; // fill w the winning color by default to prevent potential null error if colors are not filled
    }
  }

  // Driver
  public static void main(String[] args) {

    // Create class (board). Specify the size and winning color
    ColoredBoard b1 = new ColoredBoard(15, "Blue");

    // Add colors first
    b1.nodeColors[0] = "Pink";
    b1.nodeColors[1] = "Pink";
    b1.nodeColors[2] = "Pink";
    b1.nodeColors[3] = "Pink";
    b1.nodeColors[4] = "Red";
    b1.nodeColors[5] = "Pink";
    b1.nodeColors[6] = "Blue";
    b1.nodeColors[7] = "Red";
    b1.nodeColors[8] = "Red";
    b1.nodeColors[9] = "Blue";
    b1.nodeColors[10] = "Red";
    b1.nodeColors[11] = "Red";
    b1.nodeColors[12] = "Red";
    b1.nodeColors[13] = "Pink";
    b1.nodeColors[14] = "Pink";

    // Add edges (only valid color combos will be allowed)
    b1.addEdge(0, 1);
    b1.addEdge(0, 2);
    b1.addEdge(0, 4); // invalid color combo, will not create an edge
    b1.addEdge(2, 3);
    b1.addEdge(2, 5);
    b1.addEdge(3, 5);
    b1.addEdge(3, 4);
    b1.addEdge(7, 8);
    b1.addEdge(4, 7);
    b1.addEdge(4, 8);
    b1.addEdge(5, 6);
    b1.addEdge(8, 9);
    b1.addEdge(9, 12);
    b1.addEdge(8, 10);
    b1.addEdge(8, 11);
    b1.addEdge(10, 11);
    b1.addEdge(11, 12);
    b1.addEdge(13, 14); // dead end nodes

    // Print graph (optional)
    // System.out.println("0: " + b1.nodeColors[0] + " " + b1.nodes[0]);
    // System.out.println("1: " + b1.nodeColors[1] + " " + b1.nodes[1]);
    // System.out.println("2: " + b1.nodeColors[2] + " " + b1.nodes[2]);
    // System.out.println("3: " + b1.nodeColors[3] + " " + b1.nodes[3]);
    // System.out.println("4: " + b1.nodeColors[4] + " " + b1.nodes[4]);
    // System.out.println("5: " + b1.nodeColors[5] + " " + b1.nodes[5]);
    // System.out.println("6: " + b1.nodeColors[6] + " " + b1.nodes[6]);
    // System.out.println("7: " + b1.nodeColors[7] + " " + b1.nodes[7]);
    // System.out.println("8: " + b1.nodeColors[8] + " " + b1.nodes[8]);
    // System.out.println("9: " + b1.nodeColors[9] + " " + b1.nodes[9]);
    // System.out.println("10: " + b1.nodeColors[10] + " " + b1.nodes[10]);
    // System.out.println("11: " + b1.nodeColors[11] + " " + b1.nodes[11]);
    // System.out.println("12: " + b1.nodeColors[12] + " " + b1.nodes[12]);
    // System.out.println("13: " + b1.nodeColors[13] + " " + b1.nodes[13]);
    // System.out.println("14: " + b1.nodeColors[14] + " " + b1.nodes[14]);

    // Check which starting points have a potential path to a winning color using the BFS source to target results
    for(int i=0; i<b1.nodes.length; i++) {
      bfs(b1, i);
      if(b1.winningPath.size()>0) { // the start node has a winning path after going through a BFS
        b1.winningStarts.add(i);
      }
    }
    System.out.println("Starting nodes you can win from: " + Arrays.toString(b1.winningStarts.toArray()));

    // Return BFS result (traverse from start to a winning color node)
    int startNode = 4;
    bfs(b1, startNode);
    System.out.println("Starting from Node # " + startNode);
    System.out.println("Winning Path:          " + Arrays.toString(b1.winningPath.toArray()));

    // Find the shortest path (direct DFS) to avoid colors where we get "stuck"
    getShortestPath(b1);
    System.out.println("Winning Path Shortest: " + Arrays.toString(b1.winningPathShortest.toArray()));

  }

  public void addEdge(int parent, int child) {

    String parentColor = this.nodeColors[parent];
    String childColor = this.nodeColors[child];

    if(parentColor.equals(childColor) // verify it's a valid parent-child relationship
            || parentColor.equals(this.winningColor)
            || childColor.equals(this.winningColor)) {

      this.nodes[parent].add(child);
      this.nodes[child].add(parent); // make bidirectional
    }
  }

  public static void bfs(ColoredBoard b, int startNode) {

    // Reset class specfic list (in case it's used multiple times)
    b.winningPath = new LinkedList<Integer>();
    b.winningPathParents = new LinkedList<Integer>();

    // Create queue and add starting node
    Queue<Integer> queue = new LinkedList<Integer>();
    queue.add(startNode);
    b.winningPathParents.addLast(startNode); // make the start node a parent of itself (to maintain the same list sizes)

    // To track if we are logging a winning path
    boolean won = false;

    // Traverse through the node list from the start
    while(!queue.isEmpty()){

      int currentNode = queue.poll(); // get the first in the list (next in line)
      b.winningPath.add(currentNode);
      // System.out.println("Winning Path: " + Arrays.toString(b.winningPath.toArray()));

      // Check for a win. Otherwise, keep traversing
      if(b.nodeColors[currentNode] == b.winningColor){
        won = true;
        break; // if the winning color is reached, end traversal/loop to log final path and not check the children of the winning path
      }

      // Add the given node's children to the queue to visit
      for(int i=0; i<b.nodes[currentNode].size(); i++) {
        int child = b.nodes[currentNode].get(i);
        if(!b.winningPath.contains(child) // only add unvisited children to the queue
                && !queue.contains(child)) {
          queue.add(b.nodes[currentNode].get(i));
          b.winningPathParents.addLast(currentNode); // log best parent so we can later find the shortest path
        }
      }
    }
    if(won == false) {
      b.winningPath = new LinkedList<Integer>(); // only return a list with a winning path, otherwise blank out the list besides the start node
      b.winningPathParents = new LinkedList<Integer>();
      // System.out.println("No possible winning path.");
    }

  }

  // Method that returns the shortest path (direct traversal) from source to target
  public static void getShortestPath(ColoredBoard b) {

    // First, check for a valid path to prevent an error
    if(b.winningPath.size() == 0) {
      System.out.println("No possible winning path.");
    } else {

      // Infer the starting and winning nodes from the path provided
      int winNode = b.winningPath.get(b.winningPath.size()-1); // start w winning node
      int startNode = b.winningPath.get(0); // until reaching the start

      // Start with the winning node and work backwards to 1 parent per level (by default what BFS provides)
      b.winningPathShortest.add(winNode); // start from the winning node and go backwards

      while(!b.winningPathShortest.contains(startNode)) { // lookup the best parent of the given node, then traverse up to it

        int currentNode = b.winningPathShortest.get(b.winningPathShortest.size()-1);

        int nextNode = b.winningPathParents.get(b.winningPath.indexOf(currentNode));

        b.winningPathShortest.add(nextNode);
      }
      Collections.reverse(b.winningPathShortest); // put in same order as BFS
    }
  }

}