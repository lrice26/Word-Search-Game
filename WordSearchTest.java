import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class WordSearchTest {

   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() {
   }


   /** A test for getAllScorableWords(). */
   @Test public void getScorableTest() { 
      
      WordSearchGame game = WordSearchGameFactory.createGame();
      game.loadLexicon("words_medium.txt");
 
      game.setBoard(new String[]{"R", "E", "S", "T", "S", "T", "Q", "B", "V"});
      boolean passIfNotNull = game.getAllScorableWords(4) != null;
       
      Assert.assertTrue(passIfNotNull);
   }
   
   
   /** A test for isValidWord(). */
   @Test public void isValidWordTest() { 
      
      WordSearchGame game = WordSearchGameFactory.createGame();
      game.loadLexicon("words_medium.txt");
 
      game.setBoard(new String[]{"R", "E", "S", "T", "S", "T", "Q", "B", "V"});
      boolean passIfTrue = game.isValidWord("TEST");
      
      Assert.assertTrue(passIfTrue);
   }

   
   /** A test for isOnBoard(). */
   @Test public void isOnBoardTest() { 
      
      WordSearchGame game = WordSearchGameFactory.createGame();
      game.loadLexicon("words_medium.txt");
 
      game.setBoard(new String[]{"R", "E", "S", "T", "S", "T", "Q", "B", "V"});
      boolean passIfTrue = game.isOnBoard("TEST") != null;
      
      Assert.assertTrue(passIfTrue);
   }

   
   
}
