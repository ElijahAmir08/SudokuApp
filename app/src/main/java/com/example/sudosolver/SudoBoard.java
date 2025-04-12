package com.example.sudosolver;
import java.util.ArrayList;

public class SudoBoard {

    public Cell[][] currBoard;

    // Inner class represents each cell on board
    public static class Cell {

        // Every cell has potential candidate values the cell may be
        public ArrayList<Integer> candidates;
        // Answer stores either the given clue, or current attempted value
        public int answer;
        // Tracks in the cell is a clue that's given (clues should not change)
        public boolean isClue;

        public Cell(int i) {

            answer= i;
            candidates= new ArrayList<>();
            isClue= (i != 0); // If i is a number, it is a clue
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

    // Board constructor that assigns/creates 2D array from string of 81 numbers
    public void fromString(String input) {

        currBoard= new Cell[9][9];

        // Initialize board with selected board
        int currNum;

        for (int outer= 0; outer < 9; outer++) {

            for (int inner= 0; inner < 9; inner++) {

                currNum = Character.getNumericValue(input.charAt(outer * 9 + inner));

                currBoard[outer][inner]= new Cell (currNum);
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

        // Ensure the current cell is not a clue
        if (currBoard[row][col].isClue)
            return brute(nextRow, nextCol);

        // Skip cells that are already filled
        if (getCellValue(row, col) != 0)
            return brute(nextRow, nextCol);

        ArrayList<Integer> candidates= currBoard[row][col].getCandidates();

        for (Integer i : candidates) {

            if (checkInsertion(i, row, col)) {

                currBoard[row][col].setAnswer(i);

                if (brute(nextRow, nextCol))
                    return true;

                currBoard[row][col].setAnswer(0);
            }
        }

        return false; // Backtracking
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


}
