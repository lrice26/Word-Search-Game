import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

/** Class gui() is the front end for class WordSearchGame, turning the logical program into
 *  a playable game with a simple user interface and multiple board options. It uses the 
 *  breadth and depth first search algorithms, recursive backtracking, and custom positioning
 *  system defined in the class WordSearch(), to provide a fast and reliable game. 
 *  
 *  @author Lyndsey Rice
 *  @version July 10th, 2022
 */

class gui implements ActionListener {
    
    /** Creates new instance of WordSearchGame to use for logical side of game. */    
    WordSearchGame activeWordSearch = WordSearchGameFactory.createGame();
    
    /** Creates TreeSet for efficient access to all possible words on board. */
    java.util.SortedSet<String> allWordsOnBoard = new TreeSet<String>();
    
    /** Creates string array that will hold all words found by the user during a game. */
    String[] wordsFoundByUser = new String[10];
        
    /** Main method - creates new instance of gui class. 
     *  
     *  @param args - not used.
     */
    public static void main(String args[]) {
        gui newGui = new gui();
    }
        
    /** Constructor for gui class (contains logic for buttons and actions). 
     */    
    public gui() {
        
        // Loads the English language lexicon into the active word search object (fail-fast measure).
        // lexicon file courtesy of Dean Hendrix, Auburn University
        activeWordSearch.loadLexicon("words_medium.txt");
    
        // Frame container
        JFrame frame = new JFrame("Add-One Word Search Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 700);
        
        // Primary text area - where the word search boards will be shown
        JTextArea mainTextArea = new JTextArea();
        String welcomeMessageOriginal = "\n\n\n\t\t   Welcome to Add-One Word Search!\n\n\t  Please choose a board from the drop down "
         + "menu \n\t  above, or click Help to read full instructions.\n\n\t\t\t      Thanks for playing!";
        mainTextArea.setFont(new Font("Times", Font.BOLD, 20));
        mainTextArea.setTabSize(3);
        mainTextArea.setEditable(false);
        mainTextArea.setLineWrap(true);
        mainTextArea.setWrapStyleWord(true);
        mainTextArea.setText(welcomeMessageOriginal);
                
        // The bottom panel - used for taking user input and showing found words
        JPanel bottomPanel = new JPanel(); 
        JLabel enterWordLabel = new JLabel("Enter Found Word");
        JTextField foundWordTextField = new JTextField(10); // accepts up to 10 characters
                
        // Top MenuBar - structure: 3 menus with dropdown options (options have action listeners)
        JMenuBar menuBar = new JMenuBar();
        JMenu menuWordSearch = new JMenu("Word Search");
        JMenu menuHelp = new JMenu("Help");
        JMenu menuHome = new JMenu("Home");
        
        // Add menuBar and mainTextArea to frame then set to visible
        // note: bottomPanel will be added only when word search boards are visible
        frame.getContentPane().add(BorderLayout.NORTH, menuBar);
        frame.getContentPane().add(BorderLayout.CENTER, mainTextArea);
        
        // On click, displays popup menu with complete instructions
        JMenuItem menuItemHelp = new JMenuItem(new AbstractAction("Instructions") {
           @Override
           public void actionPerformed(ActionEvent e) {
              String helpMessage = "Choosing a board: click on 'Word Search' menu in top left corner and select "
               + "one of the three board options."
               + "\n\nFinding and inputting words: when you locate a word in the search grid, type it in the box on "
               + "\nthe bottom panel and click 'Check Word' to see if it is a valid word in the search."
               + "\nYou will then get a pop up message telling if the word is correct, incorrect, or already found."
               + "\n\nShowing Found Words: to view all of the correct words you have found, click on the bottom button "
               + "\n'Show Found Words' which will provide a pop up box of all the words you have found so far."
               + "\n\nThe game will end when you find 10 words, switch to a new board, or close the game window. Enjoy!";
               
               JOptionPane.showMessageDialog(frame, helpMessage, "Help", JOptionPane.PLAIN_MESSAGE);
           }
        });
        
        // On click, returns user to original home/welcome screen and removes the bottom panel from view
        JMenuItem menuItemHome = new JMenuItem(new AbstractAction("Welcome") {
           @Override
           public void actionPerformed(ActionEvent e) {
              mainTextArea.setFont(new Font("Times", Font.BOLD, 20));
              mainTextArea.setTabSize(3);
              mainTextArea.setEditable(false);
              mainTextArea.setLineWrap(true);
              mainTextArea.setWrapStyleWord(true);
              mainTextArea.setText(welcomeMessageOriginal);
               
               frame.getContentPane().remove(bottomPanel);
           }
        });
        
        // On click, displays the first word search board and bottom panel,
        // and loads the board into activeWordSearch. Also finds all valid words on given board.
        JMenuItem menuItemBoard1 = new JMenuItem(new AbstractAction("Board 1") {
           @Override
           public void actionPerformed(ActionEvent e) {
              mainTextArea.setColumns(10);
              mainTextArea.setRows(10);
              mainTextArea.setText(boardLettersString1);
              Font font = new Font("Times", mainTextArea.getFont().getStyle(), 20);
              mainTextArea.setFont(font);
              mainTextArea.setTabSize(3);
              mainTextArea.setEditable(false);
              
              frame.getContentPane().add(BorderLayout.SOUTH, bottomPanel);
              
              activeWordSearch.setBoard(boardLetters1);
              allWordsOnBoard = activeWordSearch.getAllScorableWords(4);   
              
              // reset this to empty string array to prepare for user to play
              wordsFoundByUser = new String[10];  
           }
        });
        
        // On click, displays the second word search board and bottom panel,
        // and loads the board into activeWordSearch. Also finds all valid words on given board.
        JMenuItem menuItemBoard2 = new JMenuItem(new AbstractAction("Board 2") {
           @Override
           public void actionPerformed(ActionEvent e) {
              mainTextArea.setColumns(10);
              mainTextArea.setRows(10);
              mainTextArea.setText(boardLettersString2);
              Font font = new Font("Times", mainTextArea.getFont().getStyle(), 20);
              mainTextArea.setFont(font);
              mainTextArea.setTabSize(3);
              mainTextArea.setEditable(false);
              
              frame.getContentPane().add(BorderLayout.SOUTH, bottomPanel);
              
              activeWordSearch.setBoard(boardLetters2);
              allWordsOnBoard = activeWordSearch.getAllScorableWords(4);
              
              // reset this to empty string array to prepare for user to play
              wordsFoundByUser = new String[10];
           }
        });

       
        // On click, generates and displays a random board using the randomBoard class.
        JMenuItem menuItemRandomBoard = new JMenuItem(new AbstractAction("Random Board") {
           @Override
           public void actionPerformed(ActionEvent e) {
              mainTextArea.setColumns(10);
              mainTextArea.setRows(10);
              
              // create a new dynamically generated 10 x 10 board with at least 15 valid words
              randomBoard randomBoardObj = new randomBoard(10, 15);
              randomBoardObj.generateBoard();
              
              mainTextArea.setText(randomBoardObj.getBoard());
              
              Font font = new Font("Times", mainTextArea.getFont().getStyle(), 20);
              mainTextArea.setFont(font);
              mainTextArea.setTabSize(3);
              mainTextArea.setEditable(false);
              
              frame.getContentPane().add(BorderLayout.SOUTH, bottomPanel);
              
              activeWordSearch.setBoard(randomBoardObj.getBoardArray());
              allWordsOnBoard = activeWordSearch.getAllScorableWords(4);
              
              // reset this to empty string array to prepare for user to play
              wordsFoundByUser = new String[10];
           }
        });

       
        // Add menus to menuBar        
        menuBar.add(menuWordSearch);
        menuBar.add(menuHelp);
        menuBar.add(menuHome);
                
        // Add menu items to each menu
        menuHelp.add(menuItemHelp);

        menuHome.add(menuItemHome);
        
        menuWordSearch.add(menuItemBoard1);
        menuWordSearch.add(menuItemBoard2);
        menuWordSearch.add(menuItemRandomBoard);
        
        // On click, checks user input for conditions then checks if valid word on board,
        // and adding word to first available place in wordsFoundByUser array.
        // Various popup messages possible depending on user input and conditions.
        JButton checkIsValidButton = new JButton(new AbstractAction("Check Word") {
           @Override
           public void actionPerformed(ActionEvent e) {
              
              String wordToTest = foundWordTextField.getText().toUpperCase();
              System.out.println(wordToTest);
              
              if (wordsFoundByUser[wordsFoundByUser.length - 1] != null) {
                 JOptionPane.showMessageDialog(frame, "Error: all words found. Please start a new game.", "Error", JOptionPane.PLAIN_MESSAGE);
              }
              
              if (wordToTest.length() <= 3 || wordToTest.length() > 10) {
                 JOptionPane.showMessageDialog(frame, "Word must be at least 4 letters and no more than 10.", "Error", JOptionPane.PLAIN_MESSAGE);
                 foundWordTextField.setText("");
              }
              
              else if (allWordsOnBoard.contains(wordToTest)) {
                 for (int i = 0; i < wordsFoundByUser.length; i++) {
                    if (wordsFoundByUser[i] != null) {
                       if (wordsFoundByUser[i].equalsIgnoreCase(wordToTest)) {
                          foundWordTextField.setText("");
                          JOptionPane.showMessageDialog(frame, "Word already found", "Error", JOptionPane.PLAIN_MESSAGE);
                          break;  
                       }
                    }
                    else if (wordsFoundByUser[i] == null) {
                       wordsFoundByUser[i] = wordToTest;
                       
                       if (wordsFoundByUser[wordsFoundByUser.length - 1] != null) {
                          JOptionPane.showMessageDialog(frame, "Congratulations, you won! Please return home or open a new board.",
                             "Winner!", JOptionPane.PLAIN_MESSAGE);
                       } 
                       else {
                          JOptionPane.showMessageDialog(frame, "You found a word! Great job!", "Word Found!", JOptionPane.PLAIN_MESSAGE);
                          System.out.println("Word scored");
                       }
                       
                       foundWordTextField.setText("");
                       break;
                    }
                 }
              } else {
                 JOptionPane.showMessageDialog(frame, "Not a valid word on board. Keep searching!", "Try Again", JOptionPane.PLAIN_MESSAGE);
                 System.out.println("No luck");
                 foundWordTextField.setText("");
              }
           }
        });
        
        // On click, a popup appears, displaying the contents of wordsFoundByUser in a list format.
        // If no words found, a popup appears to continue searching for words.
        JButton showAllFoundButton = new JButton(new AbstractAction("Show Found Words") {
           @Override
           public void actionPerformed(ActionEvent e) {
              if (wordsFoundByUser[0] == null) {
                 String emptyMessage = "No found words to display. Keep searching!";
                 JOptionPane.showMessageDialog(frame, emptyMessage, "All Found Words", JOptionPane.PLAIN_MESSAGE);
              }
              else {
                 String wordsMessage = "";
                 for (String str : wordsFoundByUser) {
                    if (str == null) {
                       break;
                    } else {
                       wordsMessage += "\n" + str;
                    }
                 }
                 JOptionPane.showMessageDialog(frame, wordsMessage, "All Found Words", JOptionPane.PLAIN_MESSAGE);
              }
           }  
        });
        
        // Add components to bottomPanel using standard flow layout (default)
        bottomPanel.add(enterWordLabel); 
        bottomPanel.add(foundWordTextField);
        bottomPanel.add(checkIsValidButton);
        bottomPanel.add(showAllFoundButton);
        
        // set frame to visible
        frame.setVisible(true);                       
    }
    
    
    /** Default method actionPerformed() - does nothing except print to standard output **/
    public void actionPerformed(ActionEvent e) {
       System.out.println("The default"); 
    }
    
     
    /** Logical representation of board 1 - used in getAllScorableWords(). */
    String[] boardLetters1 = new String[] {"S", "T", "A", "R", "M", "L", "R", "C", "E", "G",
                                           "M", "N", "A", "O", "D", "O", "K", "V", "G", "T",
                                           "D", "E", "I", "W", "Z", "J", "P", "I", "W", "U",
                                           "E", "R", "B", "I", "T", "E", "O", "H", "V", "S",
                                           "G", "R", "I", "V", "E", "R", "H", "L", "A", "C",
                                           "G", "O", "E", "T", "T", "P", "Q", "M", "L", "I",
                                           "O", "T", "W", "X", "U", "S", "L", "D", "Z", "Y",
                                           "L", "A", "M", "R", "E", "H", "S", "I", "F", "J",
                                           "W", "T", "J", "G", "P", "O", "P", "P", "E", "D",
                                           "D", "E", "C", "I", "D", "P", "O", "R", "T", "Z"};
    
    /** Interface-format representation of board 1 - displayed to user. */
    String boardLettersString1 = "\n\tS\tT\tA\tR\tM\tL\tR\tC\tE\tG\t" +
                               "\n\n\tM\tN\tA\tO\tD\tO\tK\tV\tG\tT\t" +
                               "\n\n\tD\tE\tI\tW\tZ\tJ\tP\tI\tW\tU\t" +
                               "\n\n\tE\tR\tB\tI\tT\tE\tO\tH\tV\tS\t" +
                               "\n\n\tG\tR\tI\tV\tE\tR\tH\tL\tA\tC\t" +
                               "\n\n\tG\tO\tE\tT\tT\tP\tQ\tM\tL\tI\t" +
                               "\n\n\tO\tT\tW\tX\tU\tS\tL\tD\tZ\tY\t" +
                               "\n\n\tL\tA\tM\tR\tE\tH\tS\tI\tF\tJ\t" +
                               "\n\n\tW\tT\tJ\tG\tP\tO\tP\tP\tE\tD\t" +
                               "\n\n\tD\tE\tC\tI\tD\tP\tO\tR\tT\tZ\t";
         
             
    /** Logical representation of board 2 - used in getAllScorableWords(). */                           
    String[] boardLetters2 = new String[] {"B", "S", "A", "D", "W", "A", "T", "E", "R", "L",
                                           "B", "L", "A", "C", "K", "B", "E", "R", "R", "Y",
                                           "K", "I", "U", "O", "E", "T", "N", "A", "H", "E",
                                           "C", "I", "I", "E", "A", "T", "T", "S", "A", "J",
                                           "S", "H", "W", "N", "B", "S", "I", "P", "E", "A",
                                           "S", "F", "E", "I", "I", "E", "O", "B", "Y", "L",
                                           "K", "T", "O", "R", "O", "I", "R", "E", "C", "E",
                                           "A", "E", "R", "B", "R", "B", "L", "R", "N", "M",
                                           "R", "K", "H", "A", "R", "Y", "A", "R", "Y", "O",
                                           "L", "I", "M", "E", "W", "H", "N", "Y", "A", "N"};
        
    /** Interface-format representation of board 2 - displayed to user. */             
    String boardLettersString2 = "\n\tB\tS\tA\tD\tW\tA\tT\tE\tR\tL\t" +
                               "\n\n\tB\tL\tA\tC\tK\tB\tE\tR\tR\tY\t" +
                               "\n\n\tK\tI\tU\tO\tE\tT\tN\tA\tH\tE\t" +
                               "\n\n\tC\tI\tI\tE\tA\tT\tT\tS\tA\tJ\t" +
                               "\n\n\tS\tH\tW\tN\tB\tS\tI\tP\tE\tA\t" +
                               "\n\n\tS\tF\tE\tI\tI\tE\tO\tB\tY\tL\t" +
                               "\n\n\tK\tT\tO\tR\tO\tI\tR\tE\tC\tE\t" +
                               "\n\n\tA\tE\tR\tB\tR\tB\tL\tR\tN\tM\t" +
                               "\n\n\tR\tK\tH\tA\tR\tY\tA\tR\tY\tO\t" +
                               "\n\n\tL\tI\tM\tE\tW\tH\tN\tY\tA\tN\t";                   

}
