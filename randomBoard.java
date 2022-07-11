import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;

/** A class to generate "random" boards for "Add One Word Search Game." 
 *
 *  @author Lyndsey Rice
 *  @version July 8th, 2022
 *
 */

class randomBoard {
   
   // boardSize is N, where the grid is an N x N board. (must be at least 2)
   private int boardSize = 0;
   
   // minWordsPossible is the minimum number of words that a board should contain (of length > 3)
   private int minWordsPossible = 0;
   
   // the alphabet in a handy format (for int -> letter conversions)
   private final String[] ALPHABET = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                                             "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
                       
   // currentBoard holds the current board for the game
   private String[] currentBoard = new String[boardSize * boardSize];
                                             

   /** Constructor for randomBoard class. 
    *  
    *  @param boardSizeIn - The integer size N of an N x N board
    *  @param minWordsPossibleIn - The lower bound for a board to qualify as valid.
    *  @throws IllegalArgumentException - If either parameter is erroneous.
    */
   public randomBoard(int boardSizeIn, int minWordsPossibleIn) {
      
      // boardSize - min: 2 x 2 / max: 20 x 20
      if (boardSizeIn < 2 || boardSizeIn > 20) {
         throw new IllegalArgumentException("Error: boardSizeIn must be between 2 and 20 (inclusive)");
      }
      
      // can specify a min amount of words for the board to be valid (reasonable range since it's only
      // stating the minimum is 1-20).
      if (minWordsPossibleIn < 1 && minWordsPossibleIn > 20) {
         throw new IllegalArgumentException("Error: minWordsPossibleIn must be between 1 and 20 (inclusive)");
      }
      
      boardSize = boardSizeIn;
      minWordsPossible = minWordsPossibleIn;
   }
   
   
   /** getBoardSize() returns the current board size (one direction/length).
    * 
    *  @return boardSize - an int representing the board size N, in an N x N board.
    */
   public int getBoardSize() {
      return boardSize;
   }
   

   /** setBoardSize() sets the board size variable to a new integer.
    *  
    *  @param boardSizeIn - an integer N for an N x N board (must be between 2 and 20, inclusive)
    *  @throws IllegalArgumentException() if boardSizeIn is outside of bounds
    *  @return boolean - true if successful.
    */
   public boolean setBoardSize(int boardSizeIn) {
      // boardSize - min: 2 x 2 / max: 20 x 20
      if (boardSizeIn < 2 || boardSizeIn > 20) {
         throw new IllegalArgumentException("Error: boardSizeIn must be between 2 and 20 (inclusive)");
      }
      else {
         boardSize = boardSizeIn;
         return true;
      }
   }
   
   
   /** getMinWordsPossible() gets the current value of minWordsPossible, or the numerical lower limit on
    *  valid words contained on the board. 
    *  
    *  @return minWordsPossible - an int representing the minimum amount of valid words needed on the board
    *  for it to be a valid new board.
    */
   public int getMinWordsPossible() {
      return minWordsPossible;
   }
   
   
   /** setMinWordsPossible() sets the minimum words on the board to qualify as a valid new board to an integer.
    *
    *  @param minWordsPossibleIn - an integer between 1 and 20 (inclusive) that represents the minimum number
    *  of valid english words for a new board to qualify.
    *  @throws IllegalArgumentException - if the parameter is outside of the bounds
    *  @return boolean - true if successful.
    */
   public boolean setMinWordsPossible(int minWordsPossibleIn) {
      if (minWordsPossibleIn < 1 && minWordsPossibleIn > 20) {
         throw new IllegalArgumentException("Error: minWordsPossibleIn must be between 1 and 20 (inclusive)");
      }
      else {
         minWordsPossible = minWordsPossibleIn;
         return true;
      }
   }

   
   /** generateRow() randomly generates a single row of letters for the board.
    *  
    *  @return newRow - a string array of letters of size N representing a single board row.
    */
   private String[] generateRow() {
      
      String[] newRow = new String[boardSize];
      List<Integer> rowInts = new ArrayList<Integer>(boardSize);
      
      Random rand = new Random();
      final int UPPER_BOUND = 25;
      
      for (int i = 0; i < boardSize; i++) {
         int newInt = rand.nextInt(UPPER_BOUND);
         rowInts.add(newInt);
      }
      
      int tracker = 0;
      for (int integer : rowInts) {
         String newLetter = ALPHABET[integer];
         newRow[tracker] = newLetter;
         tracker++;
      }
      
      return newRow;
   }
   
   
   /** generateBoard() creates a new random board using the generateRow method to make N rows.
    *
    *  @return currentBoard - a randomly generated unique board of size N x N.
    */
   public String[] generateBoard() {
      
      String[] newBoard = new String[boardSize * boardSize];
      String[] emptyBoard = new String[1];
      boolean isValid = false;
      
      // FOR UNFORSEEN CIRCUMSTANCES OF INFINITE LOOPS
      int FAILSAFE = 0;
      
      while (!isValid) {
  
         int tracker = 0;
         for (int i = 1; i <= boardSize; i++) {
            String[] newRow = generateRow();
            for (int j = 0; j < newRow.length; j++) {
               newBoard[tracker] = newRow[j];
               tracker++;
            }
         }
         
         if (validateBoard(newBoard)) {
            currentBoard = newBoard;
            return currentBoard;
         }
         
         FAILSAFE++;
         
         if (FAILSAFE >= 5) {
            break;
         }
      }
      
      return emptyBoard;
   }
   
   
   /** validateBoard() uses validation logic and WordSearch methods to ensure the randomly
    *  generated board has at least the minimum specified number of possible words on the board.
    *
    *  @param boardToCheck - a string array representing a board
    *  @return boolean - true if valid, false otherwise.
    */
   private boolean validateBoard(String[] boardToCheck) {
      
      /** Uses an instance of WordSearchGame to validate the board. */
      WordSearchGame wordSearchObj = WordSearchGameFactory.createGame();
      wordSearchObj.loadLexicon("words_medium.txt");
    
      /** Creates TreeSet for efficient access to all possible words on board. */
      java.util.SortedSet<String> allWordsOnBoard = new TreeSet<String>();
      
      // find all possible words of length >= 4 on boardToCheck
      wordSearchObj.setBoard(boardToCheck);
      allWordsOnBoard = wordSearchObj.getAllScorableWords(4);
      System.out.println(allWordsOnBoard);
      
      // test if length of allWordsOnBoard is at least minWordsPossible (if yes, return true)
      if (allWordsOnBoard.size() >= minWordsPossible) {
         return true;
      }
      
      return false;
   }
   
   
   /** getBoard() returns the current random board in an easy-to-read grid format.
    *  
    *  @return currentBoard - the current random board in a grid format
    */
   public String getBoard() {
      String output = "\n\t";
      
      for (int i = 0; i < currentBoard.length; i++) {
         if (i % boardSize == 0 && i != 0) {
            output += "\n\n\t";
         } 
         output += currentBoard[i] + "\t";
      }
      
      return output;
   }
   
   
   /** getBoardArray() returns the current random board as a string array - for usage in gui class.
    *
    *  @return currentBoard - the current random board as a string array
    */
   public String[] getBoardArray() {
      return currentBoard;
   }
   

}