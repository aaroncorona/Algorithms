import java.util.*;

public class WordLadder {

    // Create lists and variables needed 
    public static Set<String> dict = new HashSet<String>();
    public static List<String> queue = new LinkedList<String>(); // FIFO for BFS
    public static List<String> path = new LinkedList<String>(); 
    public static List<String> pathParent = new LinkedList<String>(); 
    public static List<String> nextWords = new LinkedList<String>(); 
    public static List<String> shortestPath = new LinkedList<String>(); 
    public static String currentWord;
    public static String start;
    public static String target;

    // Find the shortest path from source to target by visiting only 1 parent from each level
    public static void getShortestPath() {
        shortestPath.add(target);
        while(!shortestPath.contains(start)){
            String nextWord = shortestPath.get(shortestPath.size()-1); 
            String nextParent = pathParent.get(path.indexOf(nextWord)); // lookup the words parent in the parent array that corresponds to the same positions/indexes
            shortestPath.add(nextParent); // add 1 parent from each level
         }
    }

    public static void performBFS() {

        // Kickoff the queue loop by adding the start value
        queue.add(start);
        pathParent.add(start); // Add dummy parent for the starting value

        // BFS to find all word relationships
        while(!queue.isEmpty()) {
            // First, get the next word on the list and remove it from the queue (FIFO for a BFS)
            currentWord = queue.get(0); 
            queue.remove(0); 
            // Update the path
            path.add(currentWord);
            // Check if we have reached the target
            if(currentWord.equals(target)){
                break;
            }
            // Update the Dictionary to remove the given word so we can't visit it twice (it wont be found as a valid word, even if it's 1 char away)
            List<String> tempDict = new LinkedList<String>(dict); // temporarily convert to List to be able to get the position and remove the word
            if(tempDict.indexOf(currentWord) > -1) { // if the word is in the dict to potentially visit
               tempDict.remove(tempDict.indexOf(currentWord)); // remove it
            }
            dict = new HashSet<String>(tempDict); // update real dict
            // Find words that are 1 char away from the given word, effectively getting a list of the next word we can jump to)
            nextWords = findAdjacent(currentWord);
            // Add the next words to the queue 
            for (int i = 0; i < nextWords.size(); i++) { // This creates a loop through the list values (each loop is 1 list position)
                queue.add(nextWords.get(i));
                pathParent.add(currentWord); // add weight logic here
            }
        }
    }

    // Function to replace 1 char in 1 word at a given index
    public static String replace(String word, int index, char c) {
        char[] wordChar = word.toCharArray();
        wordChar[index] = c;
        String newWord = new String(wordChar);
        return newWord;
    }

    // Function to find all words 1 char away from the given word
    public static List<String> findAdjacent(String word) {
        List<String> relatedWords = new ArrayList<String>();
        for(char c = 'a'; c < 'z'; c++) { // pull 1 letter at a time looping through the whole alphabet
            for(int i=0; i<word.length(); i++) { // get 1 new word at a time where each is 1 char different from the given word
                String nextWord = replace(word, i, c);
                // check that it's a valid word (in the dict so not visited, and not already going to be visited to avoid dups)
                if (dict.contains(nextWord)
                    && !queue.contains(nextWord)) {
                     relatedWords.add(nextWord);
                }
            }
        }
        return relatedWords;
    }

    // Driver
    public static void main(String[] args) {

    // Build dictionary 
    dict.add("cat");
    dict.add("bat");
    dict.add("cit");
    dict.add("cot");
    dict.add("ban");
    dict.add("cog");
    dict.add("cod");
    dict.add("dan");
    dict.add("lan");
    dict.add("dod");
    dict.add("dog");
    // Add start and end words 
    start = "cat";
    target = "dog";

    // Run BFS to find all relationships (building the graph)
    performBFS();
    System.out.println("Parents:    " + Arrays.toString(pathParent.toArray()));
    System.out.println("Path taken: " + Arrays.toString(path.toArray()));
 
    // Get the shortest path from the source to target now that we have all parent-child relationships to the target
    getShortestPath();
    System.out.println("Shortest Path: " + Arrays.toString(shortestPath.toArray()));
    System.out.println("Levels traversed: " + shortestPath.size());
  }

}
