package SudoBoard;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class Board {
    
    public Cell[][] currBoard;
    public int boardNumber;
    
    public class Cell {
        
        public ArrayList<Integer> candidates;
        public int answer;
        
        public Cell(int i) {
            
            answer= i;
            candidates= new ArrayList<>();
        }
        
        public int getAnswer() {
            
            return answer;
        }
        
        public ArrayList<Integer> getCandidates() {
            
            return candidates;
        }
        
        // If Cells candidate ArrayList does not contain value, add it
        public void addCandidate(int value) {
            
            if (!candidates.contains(value))
                candidates.add(value);
        }
        
        public void setAnswer(int i) {
            
            answer= i;
        }
    }
    
    public static void main(String[] args) {
        
        getBoardFromFile();
    }
    
    // Method asks user what puzzle to solve, calls to instantiate board,
    // and asks user if it should execute brute force solving algorithm
    public static void getBoardFromFile() {
        
        
        // File containing empty puzzles
        File allBoards= new File("39puz2650.txt");
        
        Scanner userIn= new Scanner(System.in);
        
        // User Input
        System.out.print("Enter a number 1-2650: ");
        if (!userIn.hasNextInt()) {
            
            System.out.println("Invalid input. Restarting...\n");
            getBoardFromFile();
        }
        
        int selectedBoard= userIn.nextInt();
        
        // Verify input
        if (selectedBoard < 1 || selectedBoard > 2650) {
            
            System.out.println("Invalid selection. Please choose a number "
                               + "between 1 and 2650. " + "\n");
            
            getBoardFromFile();
        }
        
        else try (Scanner fileScanner = new Scanner(allBoards)) {
            
            int currentLine= 1; // Start at first line
            
            // Iterate through all lines until get desired board
            while (fileScanner.hasNextLine()) {
                
                String line= fileScanner.nextLine();
                
                // When we reach the selected line, print it and exit the loop
                if (currentLine == selectedBoard) {
                    
                    System.out.println("Board #" + currentLine);

                    // Turn line string into ArrayList for processing
                    ArrayList<Integer> arr= new ArrayList<>();
                    
                    for (char c : line.toCharArray()) {
                        
                        if (c == '.')
                            arr.add(0);
                        else
                            arr.add(Character.getNumericValue(c));
                        
                    }
                    
                    Board current= new Board(arr, currentLine);
                    
                    // Display initial board
                    current.displayBoard();
                    
                    // Ask user if candidates should be solved
                    System.out.println("\nWould you like the puzzle to"
                            + " be solved? (y/n) ");
                    
                    String ans= userIn.next();
                    ans= ans.trim().toLowerCase();
                    
                    if (ans.equals("y")) {
                        
                        current.solveCandidates();
                        
                        System.out.println("\nCandidates solved successfylly!"
                                + "\nInitializing brute force attempt...");
                        Thread.sleep(1500);
                        
                        current.brute(0, 0);
                        
                        System.out.println("\nBoard #" + currentLine + " solved successfully!");
                        
                        current.displayBoard();
                    } 
                    
                    else 
                        System.out.println("\nProgram terminating...");
                    
                    break;
                }    
                currentLine++;
            }
        } catch (Exception e) {
            
            System.out.println("File not found: ");
        }
        
        // Input scanner no longer needed
        userIn.close();
    }
    
    // Board constructor that assigns/creates 2D array and tracks what line 
    // from 39puz2650.txt puzzle came from
    public Board(ArrayList<Integer> boardIn, int name) {
        
        currBoard= new Cell[9][9];
        boardNumber= name;
        
        // Initialize board with selected board
        int index= 0;
        Integer currNum;
        
        for (int outer= 0; outer < 9; outer++) {
            
            for (int inner= 0; inner < 9; inner++) {
                
                currNum= boardIn.get(index);
                
                currBoard[outer][inner]= new Cell (currNum);
                
                ++index;
            }
        }
    }
    
    // Method takes in a row and column value (usually called from a nested
    // for loop) and returns the value of the cell 
    public int getCellValue(int row, int column) {
        
        return currBoard[row][column].getAnswer();
    }
    
    // Gets array of values in a row (0-8)
    public int[] getRow(int rowNum) {
        
        int[] row= new int[9];
        
        for (int i= 0; i < 9; i++) {
            
            row[i]= getCellValue(rowNum, i);
        }
        
        return row;
    }
    
    // Gets array of values in a column
    public int[] getColumn(int colNum) {
        
        int[] column= new int[9];
        
        for (int i= 0; i < 9; i++) {
            
            column[i]= getCellValue(i, colNum);
        }
        
        return column;
    }
    
    // Gets 2D array representing sector a certain cell is within
    public int[][] getSector(int rowNum, int colNum) {
        
        int[][] sector= new int[3][3];
        
        int startRow = (rowNum / 3) * 3;
        int startCol = (colNum / 3) * 3;
        
        for (int outer= 0; outer < 3; outer++) {
            
            for (int inner= 0; inner < 3; inner ++) {
                
                sector[outer][inner]= 
                        getCellValue(startRow + outer, startCol + inner);
            }
        }
        
        return sector;
    }
    
    // Executed to recursively brute force trying all candidate values
    // for every unsolved cell until puzzle is solved
    public boolean brute(int row, int col) {
        
        if (row == 9)
            return true;
        
        // Determine next row and column to be called
        int nextRow= (col == 8) ? row + 1 : row;
        int nextCol= (col == 8) ? 0 : col + 1;
        
        // Skip cells that are already filled
        if (getCellValue(row, col) != 0)
            return brute(nextRow, nextCol);
        
        ArrayList<Integer> candidates= currBoard[row][col].getCandidates();
        
        for (Integer i : candidates) {
            
            if (checkInsertion(i, row, col)) {
                
                System.out.println("Row " + row + ", column " + col + ", testing: " + i);
                currBoard[row][col].setAnswer(i);
                
                if (brute(nextRow, nextCol))
                    return true;
                
                currBoard[row][col].setAnswer(0);
            }    
        }
        
        System.out.println("No solutions found in branch. Backtracking...");
        return false;
    }
    
    // Method checks if newNum (a candidate value) can be inserted at a 
    // specified row and column or if it would be a duplicate value
    public boolean checkInsertion(int newNum, int row, int column) {
        
        int[] currRow= getRow(row);
        int[] currColumn= getColumn(column);
        int[][] currSector= getSector(row, column);
        
        // Check row for duplicates
        for (int i= 0; i < 9; i++) {
            
            if (currRow[i] == newNum)
                return false;
        }
        
        // Check column for duplicates
        for (int i= 0; i < 9; i++) {
            
            if (currColumn[i] == newNum)
                return false;
        }
        
        // Check Sector for duplicates
        for (int[] arr : currSector) {
            
            for (int i= 0; i < 3; i++) {
                
                if (arr[i] == newNum)
                    return false;
            }
        }
        
        // If haven't returned false by now, insertion is valid
        return true;
    }
    
    // Method goes through all empty cells (where value is 0) and solves
    // values of all potential candidates
    public void solveCandidates() {
                        
        for (int row= 0; row < 9; row++) 
            
            for (int col= 0; col < 9; col++) {
                
                int curr= currBoard[row][col].getAnswer();
                
                if (curr == 0) 
                    
                    for (int i = 1; i <= 9; i++) 
                        
                        if (checkInsertion(i, row, col)) 
                            currBoard[row][col].addCandidate(i);
            }    
    }
    
    // Method displays a sudoku board to console
    public void displayBoard() {
        
        StringBuffer ans= new StringBuffer();
        
        ans.append("\n");
        
        for (int outer= 0; outer < 9; outer++) {
            
            for (int inner= 0; inner < 9; inner++) {
                
                int currNum= getCellValue(outer, inner);
                
                ans.append(Integer.toString(currNum));
                
                if (inner == 8)
                    ans.append("\n");
                else
                    ans.append("|");
            }
            
            if (outer !=8 )
                ans.append("-|-|-|-|-|-|-|-|- \n");
        }
        
        System.out.print(ans.toString());
    }
}
