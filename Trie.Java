
import java.util.*;

public class Trie {

    // Public variables (Trie specific)
    public Node root;
    // Lists to keep track of all the node objects in the given Trie
    public LinkedHashMap<Node, Character> nodeChars = new LinkedHashMap<Node, Character>();; // map to char

    // Constructor for the Trie 
    public Trie(char start) {
        // Add a default Node to the Trie
        root = new Node(start);
    }

    // Inner class for Nodes (details the node's char, word, and children)
    public class Node { 

        // Public variables (Node specific) 
        public char ch;
        public boolean isWord; // does this node's char finish a word?
        public LinkedList<Node> children; // no dups allowed 

        // Node Constructor 
        public Node(char c) {
            // Initialize other Node specific variables (members)
            this.ch = c;
            isWord = false;
            children = new LinkedList<>();
            // Update the Trie level variable that keeps track of all inner class objects (nodes) created
            nodeChars.put(this, c);
        }
    }

    // Method to insert a word
    public void insert(String word) {
        // Start at the root node of the given Trie
        Node n = root;
        // Loop through the new word characters 
        for(int i=0; i<word.length(); i++) {
            char w = word.charAt(i);
            // Check if the given node has the needed char as a child already
            // Note: Order matters. All words must start at a root and not from a child node (e.g. sign word cannot resuse any nodes from design)
            Node nextNode = null;
            // loop through the children keys and check get char to see if any children have the required char
            for(int a=0; a < n.children.size(); a++) {
                Node childNode = n.children.get(a);
                char childChar = nodeChars.get(childNode); 
                if(w == childChar) {
                    nextNode = childNode;
                    break;
                }
            }
            // Create a new node with the needed char if it does not already exist
            if(nextNode == null) {
               nextNode = new Node(w);
               n.children.add(nextNode); 
            }
            // Move on to the child node that has the required char
            n = nextNode; 
        }
        n.isWord = true; // the final char of the inserting string signifies a word
    }

    // Method to check if a word exists
    // This is the same logic to see if a prefix exists, but verifies the prefix is also a word
    public boolean wordExists(String word) {
        // Start at the root node of the given Trie
        Node n = root;
        // Loop through the new word characters 
        for(int i=0; i<word.length(); i++) {
            char w = word.charAt(i);
            // Check if the given node has the needed char as a child already
            // Note: Order matters. All words must start at a root and not from a child node (e.g. sign word cannot resuse any nodes from design)
            Node nextNode = null;
            // loop through the children keys and check get char to see if any children have the required char
            for(int a=0; a < n.children.size(); a++) {
                Node childNode = n.children.get(a);
                char childChar = nodeChars.get(childNode); 
                if(w == childChar) {
                    nextNode = childNode;
                    break;
                }
            }
            // Return false if a char match cannot be found
            if(nextNode == null) {
               return false; //
            }
            n = nextNode; // otherwise, move to the next node 
        }
        if(n.isWord == true) // char must also form a word intentionally added
            return true;
        return false; 
    }

    public boolean prefixExists(String word) {
        // Start at the root node of the given Trie
        Node n = root;
        // Loop through the new word characters 
        for(int i=0; i<word.length(); i++) {
            char w = word.charAt(i);
            // Check if the given node has the needed char as a child already
            // Note: Order matters. All words must start at a root and not from a child node (e.g. sign word cannot resuse any nodes from design)
            Node nextNode = null;
            // loop through the children keys and check get char to see if any children have the required char
            for(int a=0; a < n.children.size(); a++) {
                Node childNode = n.children.get(a);
                char childChar = nodeChars.get(childNode); 
                if(w == childChar) {
                    nextNode = childNode;
                    break;
                }
            }
            // Return false if a char match cannot be found
            if(nextNode == null) {
               return false; //
            }
            n = nextNode; // otherwise, move to the next node 
        }
        return true; 
    }

    // Driver
    public static void main(String[] args){

       // Create obj
       Trie t1 = new Trie('!'); // initialize w dummy starting node (root should never be used for a word)

       // Insert words (ie create new char nodes)
       t1.insert("cattle");
       t1.insert("cat");
       t1.insert("catholic");
       t1.insert("cats");
       t1.insert("at");

       // Test the functions (optional)
      System.out.println("cat word exists? " + t1.wordExists("cat"));
      System.out.println("ca word exists? " + t1.wordExists("ca"));
      System.out.println("ca prefix exists? " + t1.prefixExists("ca"));
      System.out.println("ac prefix exists? " + t1.prefixExists("ac"));

       // Print out trie map (optional)
//       System.out.println("nodeChars: " + t1.nodeChars);
    }
}
