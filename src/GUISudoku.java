/* *****************************************************************************
 * Title:            GUISudoku
 * 
 * Files:            GUISudoku.java - this file
 * 					 sudoku.css - a stylesheet for the sudoku app
 * 					 BasicSudoku.java - contains checkBoard and solve methods
 * 					 easy.txt - an example board that can be loaded
 * 					 moderate.txt - an example board that can be loaded
 * 					 hard.txt - an example board that can be loaded
 * 					
 * 
 * Semester:         Spring 2021
 * 
 * Author:           Daniel Szafir (daniel.szafir@colorado.edu)
 * 
 * Description:		 GUI Sudoku uses JavaFX to display a graphical sudoku
 * 					 application with 5 buttons: Clear, Load, Save,
 * 					 Check, and Solve. The application allows users to 
 * 					 try to solve sudoku puzzles on their own, enter them
 * 					 into the application, save sudoku boards, load a
 * 					 saved board, and try a brute force recursive backtracking
 * 					 solution to automatically solve the board. The program
 * 					 relies on students completing the checkBoard and solve
 * 					 methods in BasicSudoku.java. 
 * 
 * Written:       	 February 3, 2020
 * 
 * Credits:          Sudoku appearance using a GridPane Layout inspired by
 * 					 https://stackoverflow.com/questions/34218434/sudoku-gui-grid-lines
 **************************************************************************** */

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * A simple sudoku solver application that runs on top of JavaFX.
 * 
 * Bugs: Does not implement multithreading so application may appear to hang
 * 		 if attempting to bruteforce a hard puzzle.
 *
 * @author Daniel Szafir
 */
public class GUISudoku extends Application {

	// 2D Array of TexFields for displaying the Sudoku values
	// (static to allow BasicSudoku to call updateSudokuGUI
	// without having to pass a object reference to the solve method)
	private static TextField[][] sudokuFields;
	
	// TextArea for displaying program output
	private TextArea sudokuTextArea;

	// List of buttons the user could click on
	private ArrayList<Button> buttons;

	/**
	 * Main entry point of the program. Simply launches the GUI
	 * as a standalone application.
	 *
	 * @param args  No command-line arguments expected
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}

	/**
	 * This method is automatically called by JavaFX when the GUI 
	 * starts via the Application.launch call from the main method.
	 * As a result, this is the main entry point for all JavaFX
	 * applications.
	 * 
	 * @param primaryStage The primary stage for this application, 
	 * 		  onto which the application scene can be set. 
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		// Set the application title
		primaryStage.setTitle("ATLS 2719 Sudoku App");

		// The overall application utilizes a boarder pane
		// with buttons on the top, the sudoku board in the middle
		// and a text area for text output on the bottom
		BorderPane bp = new BorderPane();

		// Set up the clear, load, save, check and solve buttons //
		buttons = new ArrayList<Button>();

		// When the Clear button is clicked, reset the board to empty
		Button clearButton = new Button("Clear");
		clearButton.setOnAction(actionEvent -> {
			clearBoard();
			sudokuTextArea.setText("Board Cleared");

		});

		// When the load button is clicked, open a file dialog and
		// attempt to load the selected file
		Button loadButton = new Button("Load");
		loadButton.setOnAction(actionEvent -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File("."));
			File selectedFile = fileChooser.showOpenDialog(primaryStage);

			if (selectedFile != null) {
				sudokuTextArea.clear();
				if (load(selectedFile)) {
					sudokuTextArea.appendText("Loaded Correctly\n");

				} else {
					sudokuTextArea.appendText("Error - could not load file " + selectedFile.getName() + "\n");
				}
			}

		});

		// When the save button is clicked, open a file dialog and attempt to
		// save the board state to the selected file
		Button saveButton = new Button("Save");
		saveButton.setOnAction(actionEvent -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File("."));

			// Set extension filter for text files
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
			fileChooser.getExtensionFilters().add(extFilter);

			// Show save file dialog
			File file = fileChooser.showSaveDialog(primaryStage);

			if (file != null) {
				if (save(file)) {
					sudokuTextArea.setText("Saved board successfully to " + file.getName());
				} else {
					sudokuTextArea.appendText("Error saving board to " + file.getName() + "\n");
				}
			}
		});

		// When the check button is clicked, check to see if the board
		// is currently valid (no duplicates in rows, columns, or boxes)
		Button checkButton = new Button("Check");
		checkButton.setOnAction(actionEvent -> {
			//if (checkBoard(getIntBoard())) {
			if (BasicSudoku.checkBoard(getIntBoard())) {
				sudokuTextArea.setText("Board is valid!");
			} else {
				sudokuTextArea.setText("Board is not valid!");
			}
		});

		// When the solve button is clicked, attempt to solve the puzzle
		Button solveButton = new Button("Solve");
		solveButton.setOnAction(actionEvent -> {
			sudokuTextArea.setText("Solving...");
			if (BasicSudoku.solve(getIntBoard())) {
				sudokuTextArea.setText("Board solved!");
			} else { // We couldn't solve the board
				sudokuTextArea.setText("Board is impossible to solve!");										
			}
		});

		// Add all the buttons to a grid pane for layout
		GridPane topGrid = new GridPane();
		topGrid.setHgap(5);
		topGrid.setAlignment(Pos.CENTER);

		topGrid.add(clearButton, 0, 0);
		topGrid.add(loadButton, 1, 0);
		topGrid.add(saveButton, 2, 0);
		topGrid.add(checkButton, 3, 0);
		topGrid.add(solveButton, 4, 0);

		buttons.add(clearButton);
		buttons.add(loadButton);
		buttons.add(saveButton);
		buttons.add(checkButton);
		buttons.add(solveButton);

		// Add the button grid pane to the top of the overall border pane
		bp.setTop(topGrid);
		Insets topMargin = new Insets(10);
		BorderPane.setMargin(topGrid, topMargin);

		// Set up the sudoku board using a GridPane Layout inspired by:
		// https://stackoverflow.com/questions/34218434/sudoku-gui-grid-lines
		GridPane sudokuGridPane = new GridPane();
		PseudoClass right = PseudoClass.getPseudoClass("right");
		PseudoClass bottom = PseudoClass.getPseudoClass("bottom");

		sudokuFields = new TextField[9][9];
		for (int row = 0; row < 9; ++row) {
			for (int col = 0; col < 9; ++col) {

				StackPane cell = new StackPane();
				cell.getStyleClass().add("cell");
				cell.pseudoClassStateChanged(right, col == 2 || col == 5);
				cell.pseudoClassStateChanged(bottom, row == 2 || row == 5);
				TextField tf = new TextField();
				// restrict textfield input to integers:
				tf.setTextFormatter(new TextFormatter<Integer>(c -> {
					if (c.getControlNewText().matches("[1-9]?")) {
						return c;
					} else {
						return null;
					}
				}));
				sudokuFields[row][col] = tf;
				cell.getChildren().add(tf);
				sudokuGridPane.add(cell, col, row);
			}
		}

		// Add the sudoku board in the grid pane to the center 
		// of the overall border pane
		sudokuGridPane.setAlignment(Pos.CENTER);
		bp.setCenter(sudokuGridPane);

		// Create the text area for text output and add it to the 
		// bottom of the overall border pane
		sudokuTextArea = new TextArea();
		sudokuTextArea.setEditable(false);
		bp.setBottom(sudokuTextArea);

		// Add the overall border pane to scene 
		// and add the scene to the primary stage to show it
		Scene scene = new Scene(bp);
		scene.getStylesheets().add("sudoku.css");

		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
	}

	/**
	 * Converts the current board state, as saved in the GUI 
	 * text fields to 2D int array. Empty cells are stored as 0's
	 * in the int array
	 * @return A representation of the sudoku board as an int array
	 * 		   with 0's representing empty cells
	 */
	private int[][] getIntBoard() {
		int[][] board = new int[9][9];
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				try {
					board[row][col] = Integer.parseInt(sudokuFields[row][col].getText());
				} catch (NumberFormatException e) {
					board[row][col] = 0;
				}
			}
		}

		return board;
	}
	
	/**
	 * Takes in a representation of a sudoku board as a 2D int array
	 * and updates the GUI text fields to show the board. 0's are
	 * interpreted as empty cells. Static so that we can call it from
	 * the BasicSudoku.solve method without having to pass in an
	 * object reference.
	 * 
	 * @param board A 2D array of ints representing a sudoku board,
	 * 				with 0's indicating empty cells
	 */
	public static void updateSudokuGUI(int[][] board)
	{
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				try {
					sudokuFields[row][col].setText(""+board[row][col]);
				} catch (NullPointerException e) {
					//do nothing...
				}
			}
		}
	}

	/**
	 * Attempts to save the current state of the sudoku board, 
	 * as indicated in the GUI text fields, to a file on the hard drive.
	 * The file will list the contents of the board, with 0's indicating
	 * empty cells.
	 * 
	 * @param file The file to save
	 * @return True if the save was successful, false otherwise
	 */
	private boolean save(File file) {
		// Set up the output stream for saving the data
		PrintWriter outStream = null;
		try {
			outStream = new PrintWriter(new BufferedOutputStream(new FileOutputStream(file)));
		} catch (FileNotFoundException e) {
			sudokuTextArea.appendText("Error saving file: " + e + "\n");
			return false;
		}

		// Iterate through all values in the board, writing them to the file
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				String value = sudokuFields[row][col].getText();
				if (value == null || value.equals("")) {
					value = "0";
				}
				if (col < 8)
					outStream.print(value + " ");
				else
					outStream.print(value);
			}
			if (row < 8)
				outStream.println();
		}

		outStream.close(); // close the file
		return true;
	}

	/**
	 * Clears the GUI sudoku board (i.e., sets all the text fields to empty).
	 */
	private void clearBoard() {
		for (int row = 0; row < sudokuFields.length; ++row) {
			for (int col = 0; col < sudokuFields[row].length; ++col) {
				sudokuFields[row][col].setText("");
				sudokuFields[row][col].setStyle("-fx-control-inner-background: #fff");
			}
		}

	}

	/**
	 * Attempts to load a file indicating a sudoku board. The file format
	 * should be 9 lines of data, with each line consisting of 9 integers
	 * with a space between them (e.g., see included easy.txt file for the
	 * format). Each integer should be 0 - 9, with 0's indicating that the
	 * sudoku cell is empty.
	 * 
	 * @param selectedFile The file to load
	 * @return True if board was successfully loaded from the file, false otherwise
	 */
	private boolean load(File selectedFile) {
		boolean loadedSuccesfully = true;

		//Try to open the input stream for reading the file
		BufferedReader inStream = null;
		try {
			inStream = new BufferedReader(new FileReader(selectedFile));
		} catch (FileNotFoundException e) {
			sudokuTextArea.appendText("Error loading file: " + e.getMessage() + "\n");
			return false; // file not found, give up.
		}

		// Iterate through the file reading each line into a 9x9 grid
		String[][] loadedValues = new String[9][9];
		try {
			// Go through each line of the file (should be 9 lines)
			for (int row = 0; row < 9; row++) {
				String line = inStream.readLine();
				if (line == null) { // will be null if end-of-file
					sudokuTextArea.appendText("Cannot load file (bad format): the file only has " + row + " lines\n");
					sudokuTextArea.appendText("Expecting 9 lines, each with 9 numbers\n");
					return false;
				}
				
				// Divide the line into separate Strings based on spaces
				// (should result in 9 "words" where each "word" is an integer
				// in the range 0-9 inclusive)
				String[] words = line.split(" ");
				if (words.length != 9) {
					sudokuTextArea.appendText("Cannot load file (bad format): line " + (row + 1) + " is:\n");
					sudokuTextArea.appendText(line + "\n");
					sudokuTextArea.appendText("but we are expecting 9 numbers\n");
					return false;
				}
				
				// For each parsed "word" try to see if it is an int in the
				// range [0, 9]
				for (int col = 0; col < words.length; col++) 
				{
					if (words[col].matches("[0-9]?")) {
						loadedValues[row][col] = words[col];
					} else {
						sudokuTextArea.appendText("Cannot load file (bad format): line " + (row + 1) + " contains "
								+ "invalid character:\n");
						sudokuTextArea.appendText(words[col] + "\nExpecting only numbers 0-9\n");
						return false;
					}
				}
			}
		} catch (IOException e) {
			sudokuTextArea.appendText("Problem reading file: " + e + "\n");
			loadedSuccesfully = false; // keep going, try to close file
		}

		try {
			inStream.close(); // closes file
		} catch (IOException e) {
			sudokuTextArea.appendText("Unable to close the input file: " + e + "\n");
			return false;
		}

		// If we successfully parsed the file, update the GUI based on
		// the values we read in from the file
		if (loadedSuccesfully) {
			clearBoard();
			for (int row = 0; row < loadedValues.length; row++) {
				for (int col = 0; col < loadedValues[row].length; col++) {
					sudokuFields[row][col].setText(loadedValues[row][col]);
				}
			}
		}

		return loadedSuccesfully;
	}

}
