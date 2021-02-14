/*
 #############################################################################
 ###                                                                       ###
 ### Title:         Sudoku Solver                                          ###
 ###                                                                       ###
 ### Files:         BasicSudoku.java|GUISudoku.java                        ###
 ### Author(s):     Michael Metz (mime9599@colorado.edu)                   ###
 ### Class:         ATLAS 2519                                             ###
 ### Written:       February 12, 2021                                      ###
 ### Description:                                                          ###
 ### License:                                                              ###
 ### Credits:                                                              ###
 #############################################################################
*/

/* INCLUDES */
import java.util.*;



/**
 * (Write a succinct description of this class here. You should avoid wordiness
 * and redundancy. If necessary, additional paragraphs should be preceded by
 * <p>
 * , the html tag for a new paragraph.)
 *
 * <p>
 * Bugs: (a list of bugs and other problems)
 *
 * @author (your name)
 */
public class BasicSudoku {
		static ArrayList<ErrorPair> errors = new ArrayList<ErrorPair>();


	/* 
	
	*/
	static class ErrorPair {
		private int column = 0;
		private int row = 0;

		ErrorPair() {}

		ErrorPair(int row, int column) {
			this.column = column;
			this.row = row;
		}

		public void makePair(int row, int column) {
			this.column = column;
			this.row = row;
		}

		public int getX() {
			return column;
		}

		public int getY() {
			return row;
		}
	};

	/**
	 * Main entry point of the program. Prints out a hardcoded sudoku board, then
	 * tries to check if the board is valid (i.e., no duplicates in any rows,
	 * columns, or boxes) then tries to solve the board.
	 *
	 * @param args No command-line arguments expected
	 */
	public static void main(String[] args) {

		// Here we've hardcoded 2 simple Sudoku boards
		// for you to use in implementing and testing your methods
		// Feel free to change the values in these boards (or
		// create your own boards) to help in testing (e.g., for
		// testing if other components of your checkBoard method
		// works for duplicates in columns and boxes).

		// board1 is not valid (notice the duplicate 9's in the
		// first row)
		/* int[][] board1 = { { 9, 2, 3, 4, 5, 6, 7, 8, 9 }, 
						   { 4, 5, 6, 7, 8, 9, 1, 2, 3 }, 
						   { 7, 8, 9, 1, 2, 3, 0, 5, 0 },
						   { 2, 1, 4, 3, 6, 5, 8, 9, 7 }, 
						   { 3, 6, 5, 8, 9, 7, 2, 1, 4 }, 
						   { 8, 9, 7, 2, 1, 4, 0, 6, 5 },
						   { 5, 3, 1, 6, 4, 2, 9, 7, 8 }, 
						   { 6, 4, 2, 9, 7, 8, 5, 3, 1 }, 
						   { 9, 7, 8, 5, 3, 1, 6, 4, 0 } }; */

		int[][] board1 = { 
			{ 1, 2, 3, 4, 5, 6, 7, 8, 9 }, 
			{ 4, 5, 6, 7, 8, 9, 1, 2, 3 }, 
			{ 7, 8, 9, 1, 2, 3, 4, 5, 6 },
			{ 2, 1, 4, 3, 6, 5, 8, 9, 7 }, 
			{ 3, 6, 5, 8, 9, 7, 2, 1, 4 }, 
			{ 8, 9, 7, 2, 1, 4, 3, 6, 5 },
			{ 5, 3, 1, 6, 4, 2, 9, 7, 8 }, 
			{ 6, 4, 2, 9, 7, 8, 5, 3, 1 }, 
			{ 9, 7, 8, 5, 3, 1, 6, 4, 2 } };

		// board2 is valid and corresponds to the board in the
		// easy.txt file. This one should take your computer less
		// than a second to solve.
		int[][] board2 = { { 0, 2, 3, 4, 5, 6, 7, 8, 9 }, { 4, 5, 6, 7, 8, 9, 1, 2, 3 }, { 7, 8, 9, 1, 2, 3, 0, 5, 0 },
				{ 2, 1, 4, 3, 6, 5, 8, 9, 7 }, { 3, 6, 5, 8, 9, 7, 2, 1, 4 }, { 8, 9, 7, 2, 1, 4, 0, 6, 5 },
				{ 5, 3, 1, 6, 4, 2, 9, 7, 8 }, { 6, 4, 2, 9, 7, 8, 5, 3, 1 }, { 9, 7, 8, 5, 3, 1, 6, 4, 0 } };

		printBoard(board1);
		if (checkBoard(board1)) {
			System.out.println("The board is valid");
		} else {
			System.out.println("The board is not valid!");
		}

		if (solve(board1)) {
			System.out.println("Solved! Solution:");
			printBoard(board1);
		} else {
			System.out.println("Couldn't solve the board!");
		}

	}

	/**
	 * Prints out a sudoku board to the console.
	 * 
	 * @param board A 2D array of ints corresponding to a sudoku board, where 0's
	 *              indicate empty cells
	 */
	public static void printBoard(int[][] board) {
		System.out.println("---------------------"); // Top of the board
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				// Print out an empty space if the cell is 0
				if (board[row][col] == 0) {
					System.out.print("_ ");
				} else // Otherwise print the sudoku value
				{
					System.out.print(board[row][col] + " ");
				}

				// Divide the boxes vertically by inserting a
				// vertical line every 3 columns
				if (col % 3 == 2) {
					System.out.print("| ");
				}
			}
			// Divide the boxes horizontally by inserting a horizontal
			// line every 3 rows
			if (row % 3 == 2) {
				System.out.println("\n---------------------");
			} else {
				System.out.println();
			}
		}
	}

	/**
	 * Checks to see if the current board is valid. A valid board is one where there
	 * are no duplicate values in: -Any row -Any column -Any of the 3x3 subgrids
	 * (boxes) This method should return true if the board is valid (no duplicates)
	 * and false otherwise. To solve this, create and implement 4 helper methods:
	 * -checkRow - checks a single row for duplicates -checkColumn - checks a single
	 * column for duplicates -checkBoxes - divides the grid into 9 3x3 boxes and
	 * calls checkBox for each box -checkBox - checks a single 3x3 box for
	 * duplicates
	 * 
	 * Using these methods, your pseudocode for checkBoard is as follows: for (each
	 * row in board): if (row contains duplicate): return false endfor for (each
	 * column in board): if (column contains duplicate) return false if
	 * (checkBoxes(board) indicates a box contains a duplicate): return false return
	 * true;
	 * 
	 * @param board A 2D array of ints corresponding to a sudoku board where 0's
	 *              indicate an empty cell
	 * @return True if the board is valid (i.e., does not contain any duplicates in
	 *         any rows, columns, or sub 3x3 boxes
	 */
	public static boolean checkBoard(int[][] board) {
		if (checkRow(board) == false) {
			return false;
		}
		// else if (CheckBox() == false) { return false; }
		return true;
	}

	/*
	 * -----------------------------------------------------------------------------
	 * --------------IMPLEMENTATION
	 * (checkRow)-------------------------------------------------------------------
	 * -----------------------------
	 */
	private static boolean checkRow(int[][] board) {
			ErrorPair nuError = new ErrorPair();
			int 	  level	  = 	0;

			for (int[] iter : board) {
				for (int indx = 0; indx < 9; indx++) {
					// if (iter[indx] == 0) { continue; }
					for (int inner_indx = 0; inner_indx < 9; inner_indx++) {
						// if (iter[inner_indx] == 0) { continue; }
						if (iter[indx] == iter[inner_indx] && (indx != inner_indx)) { 
							nuError.makePair(level, indx);
							errors.add(nuError);
							return false;
						}
					}
					if (checkColumn(board, iter[indx], indx) == false) { 
						nuError.makePair(level, indx);
						errors.add(nuError);
						return false;
					}
				}
				level++;
			}

			return true;
		}
		/* -------------------------------------------------------------------------------------------IMPLEMENTATION (checkColumn)------------------------------------------------------------------------------------------------ */
		private static boolean checkColumn(int[][] board, int value, int column) {

			for (int[] iter : board) {
				if (iter == board[column]) { continue; }
				if (iter[column] == value) { return false; }
			}

			return true;
		}
		/* -------------------------------------------------------------------------------------------IMPLEMENTATION (checkBox)------------------------------------------------------------------------------------------------ */
/* 		private static boolean checkBox(int[][] board, int value, int column) {
			boolean chcValue = true;

			for (int[] iter : board) {
				if (iter == board[column]) { continue; }
				if (iter[column] == value) { return false; }
			}

			return true;
		}
 */
		/**
		 * Attempts to recursively solve a sudoku board using a backtracking approach.
		 * The algorithm works as follows:
		 *   1. Base case 1: if the board is invalid (contains duplicates in
		 *      any row, column, or box) return false.
		 *      
		 *   2. Base case 2: if the board is full, this means we've solved
		 *      it, so return true 
		 *      
		 *   3. Otherwise, iterate through every cell (i.e., every row and column
		 *      in the board) looking an empty cell (indicated by the
		 *      cell value being 0). 
		 *      
		 *      3.A) If the cell is empty, iteratively try all possible values 
		 *           1-9 in the cell, calling solve again each time you try a 
		 *           potential value. If this call to solve returned true,
		 *           our attempted value solved the board, so call 
		 *           GUISudoku.updateSudokuGUI(board); to update the GUI to 
		 *           show the solution and return true (if you are ready to
		 *           move on to the GUI part. If not, just return true for
		 *           now).
		 *           
		 *      3.B) If we've tried all numbers 1-9 for the cell and none of the
		 *           calls to solve returned true, then the board is not solvable
		 *           so return false.
		 *           
		 *   4. If we've finished iterating through every cell and haven't already
		 *      returned, we must have solved the board, so return true.
		 * 
		 * Putting this idea into pseudocode results in:
		 * 
		 * 1. if (board is invalid): return false;
		 * 2. if (board is full): return true;
		 * 
		 * 3. for each row in board:
		 * 		for each column in board:
		 * 3.A)		if (board[row][column] is empty):
		 * 				for each potential sudoku value 1-9:
		 * 					set board[row][column] = potential sudoku value
		 * 					if (solve(board)): 
		 * 						 GUISudoku.updateSudokuGUI(board); // Updates the GUI to show the solution
		 * 						 return true;
		 * 					endif
		 * 				endfor
		 * 
		 * 3.B)			//if we've reached here, the board isn't solveable
		 * 				set board[row][column] = 0
		 * 				return false 
		 * 			endif
		 * 		endfor
		 * 	  endfor
		 * 4. return true;
		 * 
		 * You can use the checkBoard method to see if the board is valid (base
		 * case 1). Create a helper method to determine if the board is
		 * full or not (base case 2). I've created a GUISudoku.updateSudokuGUI(board); 
		 * method for you so that you can display the solved board in the GUI, 
		 * the pseudocode above shows where to put this call.
		 * 
		 * Note: this approach may be extremely slow for complex boards with many
		 * empty cells. 
		 * 
		 * @param board A 2D array of ints corresponding to a sudoku board where
		 * 		  		0's indicate an empty cell
		 * @return true if the method successfully solves the board, false otherwise
		 */
		public static boolean solve(int[][] board) {
			boolean chcBoard = checkBoard(board);
			

			if (!chcBoard) { 
				
			}

			return false;
		}

}
