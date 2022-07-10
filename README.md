# Add-One Word Search Game by Lyndsey Rice
A java-based word search game with both backend and frontend.

# About the Backend
The backend, WordSearchGame class, was originally created in April 2022 for a computing data structures class at Auburn University, prof. Dean Hendrix. Some of the method descriptions were provided, while others were not. The assignment was to create a word search program that could take a square grid of letters, find all the words/prefixes on the board or find a specific word/prefix on the board, and keep a running total of what has been found and score it. In addition, some methods had time complexity constraints that had to be met.

# What Makes "Add-One Word Search" Unique?
Add-One Word Search (recent name update) is a special type of word search game, as users can find words that are horizontal, vertical, and diagonal as normal...OR you can play a fun new type of game where one makes a "path" in any combination of directions, starting with any letter on the board and choosing the next letter from the former's "radius of neighbors." This makes the game much harder and much more fun! This functionality is able to exist due to the depth and breadth first searching algorithms present.

# How It Works - The Backend
1. Tracking positions on the grid/board of letters: I designed a custom Node class, "Position", which has a few methods to assist the WordSearchGame class in tracking and backtracking while searching. This class was not specified in the assignment instructions, but was created to improve the ease and efficiency of the assignment's required functionality.
2. Searching: The WordSearchGame class uses a combination of breadth-first and depth-first search styles, with the primary one being breadth-first. We had the option to choose either one as the primary search for the SearchBoard() method. I chose breadth-first because of the efficiency increase I saw in preliminary testing. This makes heavy use of the "Position" class. It is also a recursive method. Additionally, one can choose to search the board for all valid words, all valid prefixes, a specific word/prefix, or words of a specific length.
3. Scoring: The program has built-in scoring methods that can be adapted to the user's needs, including options to change the minimum length of what makes a scorable word or changing the amount that is scored per word/letter.
4. Checking Word Validity: To ensure that the words found by the program for the given board are valid in the English language, they are compared to a lexicon - this is a file provided by Dean Hendrix of Auburn University, containing a large segment of the Merriam-Webster Dictionary's words.

# About the Frontend
About a month after creating the original Java, I decided I wanted to make this game playable by adding a user interface, which was not part of the original assignment, but simply a desired personal project. The interface uses Java Swing to create a simple window with the game and different screens as detailed below. It is simple, but functional, and it matches the user's operating system "look". When the user reaches 10 found words on a given game board, the game recognizes this and tells them that they have won the game!

# How It Works - The Frontend
1. The Menubar: At the top of game window is a bar with three dropdown menus - Word Search, Help, and Home. The Word Search menu has two options ("Board 1" and "Board 2") that when clicked, display the given board to the user and load the board into the program behind the scenes. The Help menu, when clicked, displays a pop-up message with detailed instructions of every aspect of the game and interface. The Home menu takes the user back to the welcome screen that the game opens on.
2. The Text Display Window: The majority of the window is occupied by the mainTextArea, which is exactly what it sounds like. It is used for displaying the welcome message and the game boards, depending on what the user selects.
3. The Bottom Panel: The bottom panel is only visible when a game board is visible, as this panel contains the input area for users to check words they have found on the word search board. There is a button to check the validity of the word, and depending on the result of this check, it displays different pop-up messages giving the user feedback. There is an additional button on this panel that is used to display to the user a simple pop-up list of all the words they have found on the board so far.

# UPDATES
10 July 2022 - Finished implementing and testing the "random board" feature, which means that the game can be infinitely played, as the program generates a new random board each time the user presses the "Random Board" button.

# Future Improvements 
(as of original publish date - see UPDATES for ideas that have already been implemented from this list)
In the future, I would like to expand the number of boards, add a scoring system on the user interface to make use of the scoring logic in the backend program, and perhaps find a way for the program to generate new boards on its own, without having to "hard-code" them.

# Special Thanks
Special thanks to Dr. Dean Hendrix of Auburn University for the original assignment.
