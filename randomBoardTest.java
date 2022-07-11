import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class randomBoardTest {


   /** Fixture initialization (common initialization
    *  for all tests). **/
   @Before public void setUp() {
   }
   
      
   /** Test validateBoard() with a valid board. */
   @Test public void validateBoard1() {
      randomBoard randomBoardObj = new randomBoard(10, 15);
      randomBoardObj.generateBoard();
      System.out.println(randomBoardObj.getBoard());
   }
   
   
}
