/******************************************************************************
 *  Compilation:  javac BoggleSolver.java
 *  Execution:    java BoggleSolver
 *  Dependencies: BoggleBoard FastPrefixTST
 *  
 *  
 *  For use on Coursera, Algorithms Part II programming assignment.
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.TreeSet;
public class BoggleSolver {
    private FastPrefixTST[][] dictionary; //  = new TST<Integer>[26][26];
    private int m = 0, n = 0;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {       
        this.dictionary = new FastPrefixTST[26][26]; // Array.newInstance(new TST<Integer>().getClass(), 26, 26);

        for (int i = 0; i < 26; i++)
            for (int j = 0; j < 26; j++)
                this.dictionary[i][j] = new FastPrefixTST();

        for (String word : dictionary)
            if (word.length() >= 3)
                this.dictionary[word.charAt(0) - 'A'][word.charAt(1) - 'A'].put(word.substring(2));
    }

    private TreeSet<String> getAllWords(int i, int j, String word, char[][] boardChars, TreeSet<String> ret) {
        word += boardChars[i][j];
        if (boardChars[i][j] == 'Q')
            word += 'U';

        if (word.length() >= 3) {
            int getID = dictionary[word.charAt(0) - 'A'][word.charAt(1) - 'A'].get(word.substring(2));
            if (getID < 0)
                return ret;
            else if (getID > 0)
                ret.add(word);
        }

       // if (word.length() >= 3 
       //     && !dictionary[word.charAt(0) - 'A'][word.charAt(1) - 'A'].hasPrefix(word.substring(2)))
            // && !dictionary[word.charAt(0) - 'A'][word.charAt(1) - 'A'].keysWithPrefix(word.substring(2)).iterator().hasNext())
       //     return ret;

       //  if (word.length() >= 3 && dictionary[word.charAt(0) - 'A'][word.charAt(1) - 'A'].contains(word.substring(2)))
       //     ret.add(word);
    
        char tempChar = boardChars[i][j];
        boardChars[i][j] = 0;

        if (i+1 < m && boardChars[i+1][j] != 0) 
            ret = getAllWords(i+1, j, word, boardChars, ret);
        if (j+1 < n && boardChars[i][j+1] != 0)
            ret = getAllWords(i, j+1, word, boardChars, ret);
        if (i-1 >= 0 && boardChars[i-1][j] != 0) 
            ret = getAllWords(i-1, j, word, boardChars, ret);
        if (j-1 >= 0 && boardChars[i][j-1] != 0)
            ret = getAllWords(i, j-1, word, boardChars, ret);
        
        if (i+1 < m && j+1 < n && boardChars[i+1][j+1] != 0) 
            ret = getAllWords(i+1, j+1, word, boardChars, ret);
        if (i-1 >= 0 && j+1 < n && boardChars[i-1][j+1] != 0)
            ret = getAllWords(i-1, j+1, word, boardChars, ret);
        if (i-1 >= 0 && j-1 >= 0 && boardChars[i-1][j-1] != 0) 
            ret = getAllWords(i-1, j-1, word, boardChars, ret);
        if (i+1 < m && j-1 >= 0 && boardChars[i+1][j-1] != 0)
            ret = getAllWords(i+1, j-1, word, boardChars, ret);

        boardChars[i][j] = tempChar;        

        return ret;
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        m = board.rows();
        n = board.cols();
        char[][] boardChars = new char[m][n];
        TreeSet<String> ret = new TreeSet<String>();

        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                boardChars[i][j] = board.getLetter(i, j);

        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                ret = getAllWords(i, j, "", boardChars, ret);

        return ret;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        int wordLength = word.length();
        if (wordLength <= 2)
            return 0;
        
        if (!dictionary[word.charAt(0) - 'A'][word.charAt(1) - 'A'].contains(word.substring(2)))
            return 0;
        
        if (wordLength == 3 || wordLength == 4)
            return 1;
        else if (wordLength == 5)
            return 2;
        else if (wordLength == 6)
            return 3;
        else if (wordLength == 7)
            return 5;
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
