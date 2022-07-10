import java.util.*;
import java.io.*;

/** Class WordSearch is the logic of a word search game, given a board of letters to make English
 *  words out of. This program uses recursive backtracking paired with both breadth-first and 
 *  depth-first search algorithms to find all possible words on the board. Other features include
 *  a custom node positioning system, the ability to search for a specific word, and scoring all
 *  found words.
 *  
 *  @author Lyndsey Rice for Dean Hendrix of Auburn University (some structure provided by Dr Hendrix)
 *  @version Last updated: July 10th, 2022
 *
 */

public class WordSearch implements WordSearchGame {
   
   /** The lexicon is the collection of valid English words, stored in a TreeSet for efficient access. */
   private TreeSet<String> lexicon;
   
   /** Double-layer string array "board" is the grid representation of the current word search board. */
   private String[][] board;
   
   /** N is the integer representation of the size of the board. */
   private int N;
   
   /** SortedSet of type String "words" will contain all the possible words on the current board. */ 
   private SortedSet<String> words;
   
   /** Constructor method will load the default board to make the game immediately playable. **/
   public WordSearch() {
      
      N = 4;
      board = new String[N][N]; // board is an N by N grid
      String[] charArr = {"E", "E", "C", "A", "A", "L", "E", "P", "H", "N", "B", "O", "Q", "T", "T", "Y"};
      
      // populate board values with the values of the char array above
      int k = 0;
      for (int i = 0; i < N; i++) {
         for (int j = 0; j < N; j++) {
            board[i][j] = charArr[k];
            k++;
         }
      }   
   }
   
   
   /**
     * Loads the lexicon into a data structure for later use. 
     * 
     * @param fileName A string containing the name of the file to be opened.
     * @throws IllegalArgumentException if fileName is null
     * @throws IllegalArgumentException if fileName cannot be opened.
     */
   public void loadLexicon(String fileName) {
      
      if (fileName == null) {
         throw new IllegalArgumentException("fileName must not be null");
      } 
      
      // try: attempt to load file into TreeSet lexicon // catch: if file is not found
      try {
         File lexFile = new File(fileName);
         Scanner scan = new Scanner(lexFile);
         
         lexicon = new TreeSet<String>();
         
         while (scan.hasNext()) {
            lexicon.add(scan.next().toUpperCase()); // add all as upper case to help with comparisons later
         }
         
         scan.close();
      } 
      catch (FileNotFoundException e) {
         throw new IllegalArgumentException("The file could not be found");
      } 
   }
   
   
   /**
     * Stores the incoming array of Strings in a grid-like data structure that will make
     * it convenient to find words.
     * 
     * @param letterArray This array of length N^2 stores the contents of the
     *     game board in row-major order. Thus, index 0 stores the contents of board
     *     position (0,0) and index length-1 stores the contents of board position
     *     (N-1,N-1). Note that the board must be square and that the strings inside
     *     may be longer than one character.
     * @throws IllegalArgumentException if letterArray is null, or is not
     *     square.
     */
   public void setBoard(String[] letterArray) {
      
      if (letterArray == null) {
         throw new IllegalArgumentException("LetterArray cannot be null");
      } 
      
      // check if letterArray's length is a perfect square by calculating sq root then checking if it's an integer
      double sqLA = Math.sqrt(letterArray.length);
      int rounded = (int) Math.floor(sqLA); // round to nearest int and see if it's equal to sqLA
      if (sqLA != rounded) {
         throw new IllegalArgumentException("LetterArray must be a square array");   
      }
      
      // set the board and size N
      board = new String[rounded][rounded];
      N = rounded;
      
      // populate board values
      int k = 0;
      for (int i = 0; i < rounded; i++) {
         for (int j = 0; j < rounded; j++) {
            board[i][j] = letterArray[k].toUpperCase();
            k++;
         }
      }     
   }
   
   
   /**
     * Creates a String representation of the board, suitable for printing to
     *   standard out. Note that this method can always be called since
     *   implementing classes should have a default board.
     * 
     * @return output - a formatted representation of the current board
     */
   public String getBoard() {
      // print by row
      int N = (int) Math.sqrt(board.length);
      String output = "";
      for (int i = 0; i < board.length; i++) {
         if (i % N == 0) {
            output += "\n";
         }
         output += board[i] + " ";
      }  
      return output;
   }
   
   
   /**
     * Retrieves all scorable words on the game board, according to the stated game
     * rules.
     * 
     * @param minimumWordLength The minimum allowed length (i.e., number of
     *     characters) for any word found on the board.
     * @return java.util.SortedSet which contains all the words of minimum length
     *     found on the game board and in the lexicon.
     * @throws IllegalArgumentException if minimumWordLength is less than 1.
     * @throws IllegalStateException if loadLexicon has not been called.
     */
    public SortedSet<String> getAllScorableWords(int minimumWordLength) {
     
      if (minimumWordLength < 1) {
         throw new IllegalArgumentException("Word length must be at least 1");
      }
      
      if (lexicon == null) {
         throw new IllegalStateException("loadLexicon() must be called first");
      }
      
      // Set words of type SortedSet<String> to a TreeSet<String> for efficiency and ease of access.
      words = new TreeSet<String>();
      
      // linked list of strings below will store strings at locations around the board
      // after each additional string is added, it will check if the word is a valid word and meets min length
      // if so, it will be added to "words". if valid prefix, use semi-recursive technique to search for words 
      // using prefix.
      LinkedList<Integer> current = new LinkedList<Integer>();
      String currentWord = turnIntoWord(current).toUpperCase();
      int k = 0;
      for (int i = 0; i < (N * N); i++) {
        current.add(i);
        // update currentWord
        currentWord = turnIntoWord(current).toUpperCase();
            
        if (isValidWord(currentWord) && currentWord.length() >= minimumWordLength) {
           words.add(currentWord.toUpperCase());
        }
            
        else if (isValidPrefix(currentWord)) {
           searchBoardPrefix(current, minimumWordLength);
        }    
        
      }
      
      // Run another check over each word to catch bad apples (special thanks to Dean Hendrix for suggestion
      // to finalize "words").
      for (String word : words) {
         if (isValidWord(word) == false) {
            words.remove(word);
         }
      }
      
      return words;
    }
    
    
    /** A second version of searchBoard, to use for a given prefix (because there's no specific position 
     *  like there is for the original searchBoard). 
     *
     *  @param intList - A linked list of integers representing the prefix to search for.
     *  @param minLength - The minimum length of the prefix. Must be greater than or equal to 1.
     *  @throws IllegalArgumentException if minLength is less than 1.
     *  @return intList - The list of integers after searching is complete.
     */
    private LinkedList<Integer> searchBoardPrefix(LinkedList<Integer> intList, int minLength) {
    
      if (minLength < 1) {
         throw new IllegalArgumentException();
      }
      
      // basically, do what is done in searchBoard method but change to test for prefix instead of whole words
      
      // start with array of neighbors that contains all the neighbors of the latest int/position in intList
      int lastInt = intList.getLast();
      Position lastPosition = new Position(lastInt);
      Position[] neighbors = lastPosition.findNeighbors(intList);
      
      // now iterate through all the neighbors and try to find valid ones
      for (Position curr : neighbors) {
         if (curr == null) {
            break; // this means we've reached the end or we just don't have neighbors
         }
         
         // add the index of curr to the visited list - intList
         intList.add(curr.i);
         String currentWord = turnIntoWord(intList);
         
         // now test if it's a valid prefix first, then check if it's a valid word and meets minLength
         if (isValidPrefix(currentWord)) {
            if (isValidWord(currentWord) && currentWord.length() >= minLength) {
               words.add(currentWord);
            } 
            // to continue finding all words with this prefix, use recursion to run this method again
            searchBoardPrefix(intList, minLength);   
         } else {
            // if it's not a valid prefix, then remove that index from intList (backtrack)
            intList.removeLast();
         }   
      }
      
      // after for loop finishes, remove the latest int from intList because we need to keep a correct record
      intList.removeLast();
      
      return intList;
    }
    
    
    /** Method to turn a linkedList of integers into one string representing the word.
     *  
     *  @param intList - a linked list of integers that represent a word together.
     *  @return result - the result of turning the integers into a word. 
     */
    private String turnIntoWord(LinkedList<Integer> intList) {
      
      String result = "";
      
      for (int pos : intList) {
         Position tempPos = new Position(pos);
         String currString = tempPos.current;
         result += currString;
      }
      
      return result;
    }
    
    
    /**
     * Determines if the given word is in the lexicon.
     * 
     * @param wordToCheck The word to validate
     * @return true if wordToCheck appears in lexicon, false otherwise.
     * @throws IllegalArgumentException if wordToCheck is null.
     * @throws IllegalStateException if loadLexicon has not been called.
     */
    public boolean isValidWord(String wordToCheck) {
     
      if (lexicon == null) {
         throw new IllegalStateException("loadLexicon must be called first");
      }
     
      if (wordToCheck == null) {
         throw new IllegalArgumentException("wordToCheck cannot be null");
      }
      
      if (lexicon.contains(wordToCheck)) {
         return true;
      }
            
      return false;
   }
   
   
   /**
     * Determines if there is at least one word in the lexicon with the 
     * given prefix.
     * 
     * @param prefixToCheck The prefix to validate
     * @return true if prefixToCheck appears in lexicon, false otherwise.
     * @throws IllegalArgumentException if prefixToCheck is null.
     * @throws IllegalStateException if loadLexicon has not been called.
     */
    public boolean isValidPrefix(String prefixToCheck) {
      
      if (prefixToCheck == null) {
         throw new IllegalArgumentException("prefix cannot be null");
      }
      
      if (lexicon == null) {
         throw new IllegalStateException("lexicon must be loaded first");
      }
      
      // since using a TreeSet, use the ceiling() method to see if there's an element that has the prefix
      String check = lexicon.ceiling(prefixToCheck);
      if (check != null) {
         // iterate through each character and see if check starts with the prefix given
         boolean tfTest = false;
         for (int i = 0; i < prefixToCheck.length(); i++) {
            if (check.charAt(i) == prefixToCheck.charAt(i)) {
               tfTest = true;
            } else {
               tfTest = false;
               break;
            }
         }
         
         // assuming tfTest is true, then return true (there is at least one word with given prefix)
         if (tfTest == true) {
            return true;
         }
         
         return false;    
      }
      
      return false;
    }

   
   /**
    * Computes the cummulative score for the scorable words in the given set.
    * To be scorable, a word must (1) have at least the minimum number of characters,
    * (2) be in the lexicon, and (3) be on the board. Each scorable word is
    * awarded one point for the minimum number of characters, and one point for 
    * each character beyond the minimum number.
    *
    * @param words The set of words that are to be scored.
    * @param minimumWordLength The minimum number of characters required per word
    * @return the cummulative score of all scorable words in the set
    * @throws IllegalArgumentException if minimumWordLength is less than 1.
    * @throws IllegalStateException if loadLexicon has not been called.
    */  
    public int getScoreForWords(SortedSet<String> words, int minimumWordLength) {
      
      if (minimumWordLength < 1) {
         throw new IllegalArgumentException("minimumWordLength must be at least 1");
      }
      
      if (lexicon == null) {
         throw new IllegalStateException("lexicon must be loaded first");
      }
      
      int points = 0;
      
      // iterate through the set of words and check all the conditions to give points
      Iterator<String> iterator = words.iterator();
      while (iterator.hasNext()) {
         String current = iterator.next();
         if (isOnBoard(current) != null && isValidWord(current) && current.length() >= minimumWordLength) {
            points += current.length();
         }
      }
      
      return points;
    }
 
   
   /**
     * Determines if the given word is in on the game board. If so, it returns
     * the path that makes up the word.
     * @param wordToCheck The word to validate
     * @return java.util.List containing java.lang.Integer objects with  the path
     *     that makes up the word on the game board. If word is not on the game
     *     board, return an empty list. Positions on the board are numbered from zero
     *     top to bottom, left to right (i.e., in row-major order). Thus, on an NxN
     *     board, the upper left position is numbered 0 and the lower right position
     *     is numbered N^2 - 1.
     * @throws IllegalArgumentException if wordToCheck is null.
     * @throws IllegalStateException if loadLexicon has not been called.
     */
    public List<Integer> isOnBoard(String wordToCheck) {
      
      if (wordToCheck == null) {
         throw new IllegalArgumentException("wordToCheck cannot be null");
      }
      
      if (lexicon == null) {
         throw new IllegalStateException("loadLexicon() must be called first");
      }
      
      LinkedList<Integer> intList = new LinkedList<Integer>();
      
      // Uses recursive method searchBoard() to find the word, if it exists.
      List<Integer> path = searchBoard(wordToCheck, intList, 0);
      
      return path;
    }


   /** Recursive approach using depth-first search and tracking/backtracking
    *  to find a given word on the board.
    *  
    *  @param wordToCheck - The word to find, if it exists on the board.
    *  @param intList - If any part of the word has been found yet, its position lies here.
    *  @param position - The current position (node) on the grid while searching.
    *  @return intList - The most updated list of integers representing the position of the word.
    */
   private LinkedList<Integer> searchBoard(String wordToCheck, LinkedList<Integer> intList, int position) {
      
      // if the size of intList is 0, do this set instead (starting new)
      if (intList.size() == 0) {
         boolean tfTest = false;
         int letterPosition = 0;
         // while the position var is less than N^2, check recursively for words being on the board
         while (position < (N * N) && !tfTest) {
            
            // see if wordToCheck starts with the current intList but as a word
            String newString = new Position(position).current;
            String wordToCheckString = Character.toString(wordToCheck.charAt(letterPosition));
            if (wordToCheckString.equalsIgnoreCase(newString)) {
               intList.add(position);
               position++;
               letterPosition++;
               tfTest = true;
               continue;
            } else if (wordToCheckString.equalsIgnoreCase(newString) == false) {
               position++;
               tfTest = false;
               continue;
            }
         }            
         
       }
      
      // if intList already has elements, start here
      if (intList.size() > 0 && wordToCheck.equals(turnIntoWord(intList)) == false) {
         
         // use breadth-first to examine all the 1-radius neighbors for a match
         Position[] neighbors = new Position(position).findNeighbors(intList);
         
         for (Position curr : neighbors) {    
            if (curr == null) {
               break;
            }
            
            intList.add(curr.i); // add the index of curr to intList
            String intListWord = turnIntoWord(intList); // turn intList into a string word
            
            // if they are equal, stop the for loop now
            if (wordToCheck.equals(intListWord)) {
               break;
            }
            
            // see if wordToCheck starts with the current intList but as a word
            for (int i = 0; i < intListWord.length(); i++) {
               if (wordToCheck.charAt(i) != intListWord.charAt(i)) {
                  break; // break loop because we know that wordToCheck does not start with intListWord
               } 
               else if (wordToCheck.charAt(i) == intListWord.charAt(i)) {
                  if (i == (intListWord.length() - 1)) {
                     searchBoard(wordToCheck, intList, curr.i); // search recursively again
                  }
               }
            }
            
            // otherwise, backtrack and remove last element from intList
            intList.removeLast();  
         }
      }      
      
      // if wordToCheck equals the current intListWord, then we found it and can return the intList
      String intListWord = turnIntoWord(intList);
      if (wordToCheck.equals(intListWord)) {
         return intList;
      }
    
      if (intList != null) {
         intList.removeLast();
      }
      return searchBoard(wordToCheck, intList, position++);
   }
    
 
   /** Node class to represent position on the board and provide helpful structure for tracking and neighbors **/
   private class Position {
      private int x; // x-coord
      private int y; // y-coord
      private int i; // index
      private String current; // will hold the string or letter at current position
      
      /** Constructor for position class objects.
       *  
       *  @param index - The index of the current position on the board.
       */
      Position(int index) {
         i = index;
         // if at first position, initialize x and y as zero.
         if (i == 0) {
            x = 0;
            y = 0;
         } 
         else {
            // otherwise, y will be the index / N (size of board), while x will be the remainder of that operation
            x = i % N;
            y = i / N;
         }
         
         current = board[y][x];
      }
      
      /** Second constructor - use if x and y values have already been initialized. 
       *
       *  @param xGiven - The current x value.
       *  @param yGiven - The current y value.
       */
      Position(int xGiven, int yGiven) {
         // set x and y to given values, then adjust i accordingly and set current
         x = xGiven;
         y = yGiven;
         i = (y * N) + x;
         current = board[y][x];
      }
      
      /** findNeighbors() finds all adjacent/neighboring positions on board within a radius of 1.
       *
       *  @param visited - A linked list of the visited positions (tracking).
       *  @return neighbors - An array of position objects that represent the positions within a 
       *    radius of 1.  
       */
      Position[] findNeighbors(LinkedList<Integer> visited) {
         Position[] neighbors = new Position[8]; // 8 is the maximum # of neighbors possible
         int k = 0;
         for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
               if (i != x || j != y) {
                  if (posExists(i, j) && (visited.contains((j * N) + i) == false)) {
                     Position temp = new Position(i, j);
                     neighbors[k] = temp;
                     k++;
                  }
               }
            }
         }
         
         return neighbors;
      }
      
      /** posExists() checks to see if a given position (x, y) exists on the board.
       *  
       *  @param xCheck - An x value to check for existence.
       *  @param yCheck - A y value to check for existence.
       *  @return Returns a boolean whether the position exists or does not.
       */
      boolean posExists(int xCheck, int yCheck) {
         if (xCheck >= 0 && xCheck < N) {
            if (yCheck >= 0 && yCheck < N) {
               return true;
            }
         }
         
         return false;
      }
   }
   
}
