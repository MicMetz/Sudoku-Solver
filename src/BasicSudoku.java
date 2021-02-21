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
		static ArrayList<ErrorID> ERRORS = new ArrayList<ErrorID>();
		static boolean	unsolved		 = false;						// Help define a difference between a valid, and a solved, board state.



	/* ------------------------------------------------------------------------------------------- Start Of ErrorID OBJ ------------------------------------------------------------------------------------------------ */
	public static class ErrorID {
		private int[][]	BoardID;
		private boolean emptyUnit	= false;
		private int 	column 		= 0;
		private int 	row 		= 0;
		

		ErrorID() {}

		ErrorID(int row, int column, int[][] board) {
			this.column = column;
			this.row 	= row;
			BoardID 	= board;
		}
		ErrorID(int row, int column, int[][] board, boolean unit) {
			this.column = column;
			this.row 	= row;
			BoardID 	= board;
			emptyUnit 	= unit;
		}


		public void makePair(int row, int column, int[][] board) {
			this.column = column;
			this.row 	= row;
			BoardID 	= board;
		}
		// public void makeTuple(int row, int column, long subBox, int[][] board) {
		// 	this.column = column;
		// 	this.row 	= row;
		// 	this.subBox	= subBox;
		// 	sqUnit = true;
		// 	BoardID = board;
		// }

		//@Override 
		/* public boolean equals(ErrorID obj2) {
			if (this.row == obj2.getY() && this.column == obj2.getX() && this.BoardID == obj2.getID()) { return true; }
			return false;
		} */

		//	GETTERS | SETTERS | CHECKERS
		public int 		getX() 							{ return column; }

		public int 		getY() 							{ return row; }

		public Object	getID()							{ return BoardID; }

		public void 	setType(boolean designation) 	{ emptyUnit = designation; }

		public boolean	isEmptyUnit()					{ return emptyUnit; }


	};

	/* ------------------------------------------------------------------------------------------- End Of ErrorID OBJ ------------------------------------------------------------------------------------------------ */



	/* ------------------------------------------------------------------------------------------- Start Of HELPERS ------------------------------------------------------------------------------------------------ */

	/* -------------------------------------------------------------------------------------------HELPER (FindTile_Empty)------------------------------------------------------------------------------------------------ */
	private static BasicSudoku.ErrorID FindTile_Empty(int[][] board) {

		for (ErrorID err : ERRORS) {
			if (err.isEmptyUnit()) {
				return err; 
			}
		}

		return null;
	}


	/* -------------------------------------------------------------------------------------------HELPER (FindTile_Error)------------------------------------------------------------------------------------------------ */
	private static BasicSudoku.ErrorID FindTile_Error(int[][] board, ErrorID ID) {
		if (checkNoErrors(board) == true) { return null; }  
		else {
			for (ErrorID err : ERRORS) {
				if (ID.equals(err)) {
					return ID;
				}
			}
		}
		
		return null;
	}



	private static BasicSudoku.ErrorID getTile_Error(int[][] board) {
		if (checkNoErrors(board) == true) { return null; }  
		else {
			for (ErrorID err : ERRORS) {
				return err;
			}
		}

		return null;
	}

	/* -------------------------------------------------------------------------------------------HELPER (checkNoErrors)------------------------------------------------------------------------------------------------ */
	/**
	 * Checks to see if the current board is valid.
	 * Then prints a notification reporting if any/how many ERRORS were encountered.
	 * 
	*/
	private static boolean checkNoErrors(int[][] board) {
		boolean emptyOfErr = true;
		unsolved 		   = false;

		for (ErrorID iter : ERRORS) {
			if (iter.getID() == board) {
				if (iter.isEmptyUnit()) { 
					unsolved = true;
					continue; 
				}
				else {
					emptyOfErr = false;
					break;
				}
			}
		}

		return emptyOfErr;
	}



	/* -------------------------------------------------------------------------------------------HELPER (checkFull)------------------------------------------------------------------------------------------------ */
	private static boolean checkFull(int[][] board) {
		for (int[] level : board) {
			for (int tile : level) { 
				if (tile == 0) { return false; }
			}
		}
		return true;
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

	/* ------------------------------------------------------------------------------------------- END OF HELPERS ------------------------------------------------------------------------------------------------ */



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


		// board1 valid
		// int[][] board1 = { 
		// 	{ 1, 2, 3, 4, 5, 6, 7, 8, 9 }, 
		// 	{ 4, 5, 6, 7, 8, 9, 1, 2, 3 }, 
		// 	{ 7, 8, 9, 1, 2, 3, 4, 5, 6 },
		// 	{ 2, 1, 4, 3, 6, 5, 8, 9, 7 }, 
		// 	{ 3, 6, 5, 8, 9, 7, 2, 1, 4 }, 
		// 	{ 8, 9, 7, 2, 1, 4, 3, 6, 5 },
		// 	{ 5, 3, 1, 6, 4, 2, 9, 7, 8 }, 
		// 	{ 6, 4, 2, 9, 7, 8, 5, 3, 1 }, 
		// 	{ 9, 7, 8, 5, 3, 1, 6, 4, 2 } };

		// board1 to solve 
		// int[][] board1 = { 
		// 	{ 0, 2, 3, 4, 5, 6, 7, 8, 0 }, 
		// 	{ 4, 5, 6, 7, 8, 9, 1, 2, 3 }, 
		// 	{ 7, 8, 9, 1, 2, 3, 4, 5, 6 },
		// 	{ 2, 1, 4, 3, 6, 5, 8, 9, 7 }, 
		// 	{ 3, 6, 5, 8, 9, 7, 2, 1, 4 }, 
		// 	{ 8, 9, 7, 2, 1, 4, 3, 6, 5 },
		// 	{ 5, 3, 1, 6, 4, 2, 9, 7, 8 }, 
		// 	{ 6, 4, 2, 9, 7, 8, 5, 3, 1 }, 
		// 	{ 9, 7, 8, 5, 3, 1, 6, 4, 2 } };



		// board1 is not valid (notice the duplicate 9's in the first row)
		int[][] board1 = { 
			{ 9, 2, 3, 4, 5, 6, 7, 8, 9 }, 
			{ 4, 5, 6, 7, 8, 9, 1, 2, 3 }, 
			{ 7, 8, 9, 1, 2, 3, 0, 5, 0 },
			{ 2, 1, 4, 3, 6, 5, 8, 9, 7 }, 
			{ 3, 6, 5, 8, 9, 7, 2, 1, 4 }, 
			{ 8, 9, 7, 2, 1, 4, 0, 6, 5 },
			{ 5, 3, 1, 6, 4, 2, 9, 7, 8 }, 
			{ 6, 4, 2, 9, 7, 8, 5, 3, 1 }, 
			{ 9, 7, 8, 5, 3, 1, 6, 4, 0 } };


		// board1 error (box 2)
		// int[][] board1 = { 
		// 	{ 1, 2, 3, 4, 5, 6, 7, 8, 9 }, 
		// 	{ 0, 5, 6, 7, 4, 9, 1, 2, 3 }, 
		// 	{ 7, 8, 9, 1, 2, 3, 4, 5, 6 },
		// 	{ 2, 1, 4, 3, 6, 5, 8, 9, 7 }, 
		// 	{ 3, 6, 5, 8, 9, 7, 2, 1, 4 }, 
		// 	{ 8, 9, 7, 2, 1, 4, 3, 6, 5 },
		// 	{ 5, 3, 1, 6, 0, 2, 9, 7, 8 }, 
		// 	{ 6, 4, 2, 9, 7, 8, 5, 3, 1 }, 
		// 	{ 9, 7, 8, 5, 3, 1, 6, 4, 2 } };


		// board2 is valid and corresponds to the board in the easy.txt file. This one should take your computer less than a second to solve.
		// int[][] board2 = { 
		// 	{ 0, 2, 3, 4, 5, 6, 7, 8, 9 }, 
		// 	{ 4, 5, 6, 7, 8, 9, 1, 2, 3 }, 
		// 	{ 7, 8, 9, 1, 2, 3, 0, 5, 0 },
		// 	{ 2, 1, 4, 3, 6, 5, 8, 9, 7 }, 
		// 	{ 3, 6, 5, 8, 9, 7, 2, 1, 4 }, 
		// 	{ 8, 9, 7, 2, 1, 4, 0, 6, 5 },
		// 	{ 5, 3, 1, 6, 4, 2, 9, 7, 8 },
		// 	{ 6, 4, 2, 9, 7, 8, 5, 3, 1 }, 
		// 	{ 9, 7, 8, 5, 3, 1, 6, 4, 0 } };


		printBoard(board1);
		
		if (checkBoard(board1)) {
			System.out.print("The board is valid");
			if (unsolved) { System.out.println("....but not solved.");}
			else if (!unsolved) { 
				System.out.println(" and solved.");
				return;
			}
		} 
		else {
			System.out.println("The board is not valid!");
		}

		if (solve(board1)) {
			System.out.println("Solved! Solution:");
			printBoard(board1);
		} else {
			System.out.println("Couldn't solve the board!");
		}

	}



/* ------------------------------------------------------------------------------------------- Start Of Main Class Functions ------------------------------------------------------------------------------------------------ */

	/* -------------------------------------------------------------------------------------------IMPLEMENTATION (ErrorReport)------------------------------------------------------------------------------------------------ */
		/* CALLERS
			1. solve()
			2. 	
		   
		   CALLS
		    1. FindTile_Empty()
			2. checkBoard()
		*/
	private static void setTile(int[][] board) {
		boolean check		   = false;
		ErrorID tile;

		/* for (int row = 0; row < board.length; row++) {
			for (int column = 0; column < board.length; column++) {
				if ((board[row][column] == 0) || )
			}
		} */
		/* 
			Continues to execute as long as an empty tile can be found.
		*/
		for (tile = FindTile_Empty(board); tile != null; tile = FindTile_Empty(board)) {
			// ErrorID temp = tile;
			// tile.setType(false);
			for (int i = 1; i < 10; i ++) {
				board[tile.getY()][tile.getX()] = i;

				/* DEBUG */
				System.out.println();
				System.out.println();
				System.out.println("DEBUG");
				printBoard(board);
				System.out.println();
				System.out.println();
				/* DEBUG */

				/* 
					Check if the value placed at the empty index position is a valid placement.
				*/
				check = checkBoard(board);
				if (!check) {			// If the board checker's response notifies that errors still remain,
										// check if the value placed is one of those errors.
					tile = FindTile_Error(board, tile);
					if (tile == null) {	break; } // If nothing is returned, the tested value is a valid one. So, stop testing other values.
					else { 
						continue; 	// Else proceed with further testing.
					}			 
				}
				/* 
					If no errors are found, the placed value is correct. Remove the index position from the error collections.
				*/
				else {							
					/* DEBUG */
					System.out.println("-----PASSED CHECK!-----");
					/* DEBUG */

					/* TODO
						Make a function to simplify
					 */

					break;
				}
				

			}
		}
	}
 


	private static void boardRepair(int[][] board) {
		boolean check		   = false;
		ErrorID tile;

		/* 
			Continues to execute as long as an invalid tile can be found.
		*/
		for (tile = getTile_Error(board); tile != null; tile = FindTile_Empty(board)) {
			// ErrorID temp = tile;
			// tile.setType(false);
			for (int i = 1; i < 10; i ++) {
				board[tile.getY()][tile.getX()] = i;

				/* DEBUG */
				System.out.println();
				System.out.println();
				System.out.println("DEBUG");
				printBoard(board);
				System.out.println();
				System.out.println();
				/* DEBUG */

				/* 
					Check if the value placed at the invalid index position is a valid placement.
				*/
				check = checkBoard(board);
				if (!check) {			// If the board checker's response notifies that errors still remain,
										// check if the value placed is one of those errors.
					tile = FindTile_Error(board, tile);
					if (tile == null) {	break; } // If nothing is returned, the tested value is a valid one. So, stop testing other values.
					else { 
						continue; 				// Else proceed with further testing.
					}			 
				}
				/* 
					If no errors are found, the placed value is correct. Remove the index position from the error collections.
				*/
				else {							
					/* DEBUG */
					System.out.println("-----PASSED CHECK!-----");
					/* DEBUG */

					/* TODO
						Make a function to simplify
					 */

					break;
				}
				

			}
		}
	}



	/**
	 * Checks to see if the current board is valid. A valid board is one where there
	 * are no duplicate values in: -Any row -Any column -Any of the 3x3 subgrids
	 * (boxes) This method should return true if the board is valid (no duplicates)
	 * and false otherwise. To solve this, create and implement 4 helper methods:
	 * 
	 * -checkRow - checks a single row for duplicates 
	 * 
	 * -checkColumn - checks a single column for duplicates -checkBoxes - divides the grid into 9 3x3 boxes and
	 * 		calls checkBox for each box -checkBox - checks a single 3x3 box for duplicates
	 * 
	 * Using these methods, your pseudocode for checkBoard is as follows: 
	 * 	for (each row in board): 
	 * 		if (row contains duplicate): return false endfor for (each column in board): 
	 * 
	 * 		if (column contains duplicate) return false 
	 * 
	 *		if (checkBoxes(board) indicates a box contains a duplicate): return false 
	 * 	return true;
	 * 
	 * @param board A 2D array of ints corresponding to a sudoku board where 0's
	 *              indicate an empty cell
	 * @return True if the board is valid (i.e., does not contain any duplicates in
	 *         any rows, columns, or sub 3x3 boxes
	 */
	/* -------------------------------------------------------------------------------------------IMPLEMENTATION (checkBoard)------------------------------------------------------------------------------------------------ */
		/* CALLERS
			1. solve()
			2. setTile()	
		*/
	public static boolean checkBoard(int[][] board) {
		ErrorID nuError;


		for (int row = 0; row < 9; row++) {
			for (int column = 0; column < 9; column++) { 
				nuError = new ErrorID(row, column, board);
				/* 
					Save an empty space to the error collection, but not count them as "invalidities."
					
				*/
				if (board[row][column] == 0) { 
					nuError.setType(true);
					if (ERRORS.contains(nuError)) { continue; }
					else { ERRORS.add(nuError); }
				}
				else if ((board[row][column] != 0) && (checkRow(board, column, row) == false) || (checkColumn(board, column, row) == false)) {
					nuError.setType(false);
					if (ERRORS.contains(nuError)) { continue; }
					else { ERRORS.add(nuError); }
				}	
				/* 
					If an error is not found at the index space referenced by the Error,
					Query the container of errors to find a similar error record. If a
					record is found, and an error no longer exist in this index position,
					remove the record from collection.

					*Cannot edit a collection during traversal without utilizing an iterator object*

				 */			
				else {
					int rem_column = column;
					int rem_row	   = row;
					ERRORS.removeIf(n -> (n.getX() == rem_column && n.getY() == rem_row)); 
				}
			}
		}

		return checkNoErrors(board);
	}
	


	/* -------------------------------------------------------------------------------------------IMPLEMENTATION (checkBoxes)------------------------------------------------------------------------------------------------ */
	/* 
	private static boolean checkBoxes(int[][] board) {


		return true;
	}
	 */



	/* -------------------------------------------------------------------------------------------IMPLEMENTATION (checkRow)------------------------------------------------------------------------------------------------*/
	private static boolean checkRow(int[][] board, int column,  int row) {
		for (int inner_indx = 0; inner_indx < 9; inner_indx++) { 
			if (column == inner_indx) { continue; }
			if (board[row][column] == board[row][inner_indx]) 	{ return false;	}
		}
			
		return true;
	}



	/* -------------------------------------------------------------------------------------------IMPLEMENTATION (checkColumn)------------------------------------------------------------------------------------------------ */
	private static boolean checkColumn(int[][] board, int column, int row) {
		for (int indx_start = 0; indx_start < 9; indx_start++) {
			if (row == indx_start) { continue; }
			if (board[row][column] == board[indx_start][column]) { return false; }
		}

		return true;
	}



	/* -------------------------------------------------------------------------------------------IMPLEMENTATION (checkBox)------------------------------------------------------------------------------------------------ */
		/* CALLERS
			1. checkBoxes()
		*/
	private static boolean checkBox(int[][] board/* , int value, int column, int row */) {
		ErrorID 		nuError 	= new ErrorID();
		Set<Integer> 	tileCheck   = new HashSet<>();
		int 			starter1 	= 0;
		int				starter2	= 0;
		

		for (int bIter = 0; bIter < 9; bIter++) {
			if (starter2 == 9) { 
				starter1 += 3; 
				starter2 = 0;
			}
			for (int rIter = starter1; rIter < starter1 + 3; rIter++) {
				for (int cIter = starter2; cIter != starter2 + 3; cIter++) {
					if (board[rIter][cIter] == 0) { continue; }
					if (tileCheck.add(board[rIter][cIter]) == false) {
									// Or Math.floor()
						// nuError.makeTuple(rIter, cIter, subLoc, board);
						ERRORS.add(nuError);
						return false; 
					}
				}
			}
			tileCheck.clear();
			starter2 += 3;
		}

		return true;
	}



	/* -------------------------------------------------------------------------------------------IMPLEMENTATION (checkBox)------------------------------------------------------------------------------------------------ */
	/* CALLERS
			1. setTile()
			2. 	
	*/
	private static boolean checkTilePlacement(int[][] board, int column, int row) {


		return true;
	}



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
			boolean checkSuccess = checkFull(board);
			while (!checkSuccess) {
				setTile(board);
				checkSuccess = checkFull(board);
			}
			checkSuccess = checkBoard(board);
			while (!checkSuccess) {
				boardRepair(board);
				checkSuccess = checkBoard(board);
			}
			if (checkSuccess) {
				return true;
			}

			return false;
		}




}
