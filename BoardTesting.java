package SudoBoard;

import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.*;
import SudoBoard.Board;

public class testing {
    
    // Gets puzzle 1 for testing
    private Board getBoard1() {
        
        String board1= ".................1...123..4..25.1.67.4..7."
                + "5.357..38412.6871.2.52.4..5.7875..82146";
        
        char[] ch= board1.toCharArray();
        
        ArrayList<Integer> param= new ArrayList<>();
        
        for (char c : ch) {
                        
            if (c == '.')
                param.add(0);
            else
                param.add(Character.getNumericValue(c));
        }
        
        return new Board(param, 1);
    }
    
    // Test getCellValue
    @Test
    public void test1() {
        
        Board test= getBoard1();

        assertEquals(4, test.getCellValue(2, 8));
        assertEquals(2, test.getCellValue(3, 2));
        assertEquals(7, test.getCellValue(8, 0));
        assertEquals(5, test.getCellValue(8, 1));
        assertEquals(8, test.getCellValue(7, 8));
        assertEquals(1, test.getCellValue(1, 8));

    }
    
    // Test getRow
    @Test
    public void test2() {
        
        Board test= getBoard1();
        
        // Expected results below
        int[] row0= {0,0,0,0,0,0,0,0,0};
        int[] row1= {0,0,0,0,0,0,0,0,1};
        int[] row2= {0,0,0,1,2,3,0,0,4};
        int[] row3= {0,0,2,5,0,1,0,6,7};
        int[] row4= {0,4,0,0,7,0,5,0,3};
        int[] row5= {5,7,0,0,3,8,4,1,2};
        int[] row6= {0,6,8,7,1,0,2,0,5};
        int[] row7= {2,0,4,0,0,5,0,7,8};
        int[] row8= {7,5,0,0,8,2,1,4,6};

        assertArrayEquals(row0, test.getRow(0));
        assertArrayEquals(row1, test.getRow(1));
        assertArrayEquals(row2, test.getRow(2));
        assertArrayEquals(row3, test.getRow(3));
        assertArrayEquals(row4, test.getRow(4));
        assertArrayEquals(row5, test.getRow(5));
        assertArrayEquals(row6, test.getRow(6));
        assertArrayEquals(row7, test.getRow(7));
        assertArrayEquals(row8, test.getRow(8));
    }
    
    // Test getColumn
    @Test
    public void test3() {
        
        Board test= getBoard1();
        
        // Expected results below
        int[] col0= {0,0,0,0,0,5,0,2,7};
        int[] col1= {0,0,0,0,4,7,6,0,5};
        int[] col2= {0,0,0,2,0,0,8,4,0};
        int[] col3= {0,0,1,5,0,0,7,0,0};
        int[] col4= {0,0,2,0,7,3,1,0,8};
        int[] col5= {0,0,3,1,0,8,0,5,2};
        int[] col6= {0,0,0,0,5,4,2,0,1};
        int[] col7= {0,0,0,6,0,1,0,7,4};
        int[] col8= {0,1,4,7,3,2,5,8,6};

        assertArrayEquals(col0, test.getColumn(0));
        assertArrayEquals(col1, test.getColumn(1));
        assertArrayEquals(col2, test.getColumn(2));
        assertArrayEquals(col3, test.getColumn(3));
        assertArrayEquals(col4, test.getColumn(4));
        assertArrayEquals(col5, test.getColumn(5));
        assertArrayEquals(col6, test.getColumn(6));
        assertArrayEquals(col7, test.getColumn(7));
        assertArrayEquals(col8, test.getColumn(8));
    }
    
    // Test getSector
    @Test
    public void test4() {
        
        Board test= getBoard1();
        
        int[][] sec0= {{0,0,0},{0,0,0},{0,0,0}};
        int[][] sec1= {{0,0,0},{0,0,0},{1,2,3}};
        int[][] sec3= {{0,0,2},{0,4,0},{5,7,0}};
        int[][] sec4= {{5,0,1},{0,7,0},{0,3,8}};
        int[][] sec8= {{2,0,5},{0,7,8},{1,4,6}};

        // Some sector 0 tests
        assertArrayEquals(sec0, test.getSector(0, 0)); // (row, column)
        assertArrayEquals(sec0, test.getSector(2, 2));
        assertArrayEquals(sec0, test.getSector(1, 1));
        
        // Some sector 1 tests
        assertArrayEquals(sec1, test.getSector(2, 4));
        assertArrayEquals(sec1, test.getSector(2, 5));
        assertArrayEquals(sec1, test.getSector(0, 3));
        
        // Sector 3
        assertArrayEquals(sec3, test.getSector(3, 2));
        assertArrayEquals(sec3, test.getSector(5, 0));
        assertArrayEquals(sec3, test.getSector(4, 1));
        
        // Sector 4
        assertArrayEquals(sec4, test.getSector(4, 4));
        assertArrayEquals(sec4, test.getSector(3, 3));
        assertArrayEquals(sec4, test.getSector(5, 5));
        
        // Sector 8
        assertArrayEquals(sec8, test.getSector(8, 8));
        assertArrayEquals(sec8, test.getSector(7, 7));
        assertArrayEquals(sec8, test.getSector(6, 6));
    }
    
    // Test checkInsertion
    @Test
    public void test5() {
        
        Board test= getBoard1();
        
        /* Upon manually solving candidates of row 3, column 4 on board 1,
         * you will find that the numbers could be 4, or 9, and 
         * cannot be 1, 2, 3, 5, 6, 7, or 8*/ 
        
        assertTrue(test.checkInsertion(4, 3, 4));
        assertTrue(test.checkInsertion(9, 3, 4));
        
        assertFalse(test.checkInsertion(1, 3, 4));
        assertFalse(test.checkInsertion(2, 3, 4));
        assertFalse(test.checkInsertion(3, 3, 4));
        assertFalse(test.checkInsertion(5, 3, 4));
        assertFalse(test.checkInsertion(6, 3, 4));
        assertFalse(test.checkInsertion(7, 3, 4));
        assertFalse(test.checkInsertion(8, 3, 4));
    }
    
    // Test solveCandidates
    @Test
    public void test6() {
        
        Board test= getBoard1();
        
        test.solveCandidates();
        
        ArrayList<Integer> r2c2= new ArrayList<>();
        r2c2.add(5);
        r2c2.add(6);
        r2c2.add(7);
        r2c2.add(9);
        
        Integer[] expectedResult1= r2c2.toArray(new Integer[0]);
        Integer[] testResult1= test.currBoard[2][2].getCandidates().
                toArray(new Integer[0]);
        
        assertArrayEquals(expectedResult1, testResult1);
        
        ArrayList<Integer> r7c6= new ArrayList<>();
        r7c6.add(3);
        r7c6.add(9);
        
        Integer[] expectedResult2= r7c6.toArray(new Integer[0]);
        Integer[] testResult2= test.currBoard[7][6].getCandidates().
                toArray(new Integer[0]);
        
        assertArrayEquals(expectedResult2, testResult2);
    }
}
