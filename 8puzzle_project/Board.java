import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int[][] tiles;
    private int hamming = -1;
    private int manhattan = -1;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles.length < 2 || tiles.length > 127)
            throw new IllegalArgumentException();

        this.tiles = tiles;
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder(dimension() + " \n ");
        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles.length; j++) {
                sb.append(this.tiles[i][j]);
                sb.append(" ");
            }
            sb.append("\n ");
        }

        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return this.tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        if (hamming != -1)
            return hamming;

        int count = 0;
        for (int i = 0; i < this.tiles.length; i++)
            for (int j = 0; j < this.tiles[0].length; j++) {
                if (this.tiles[i][j] != 0 && getGoalValue(i, j) != this.tiles[i][j])
                    count++;
            }

        hamming = count;

        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manhattan != -1)
            return manhattan;

        int sum = 0;
        for (int i = 0; i < this.tiles.length; i++)
            for (int j = 0; j < this.tiles[0].length; j++) {
                if (this.tiles[i][j] != 0) {
                    int expectedRow = (this.tiles[i][j] - 1) / dimension();
                    int expectedCol = this.tiles[i][j] - 1 - (expectedRow * dimension());
                    sum += Math.abs(expectedRow - i) + Math.abs(expectedCol - j);
                }
            }

        manhattan = sum;

        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < this.tiles.length; i++)
            for (int j = 0; j < this.tiles[0].length; j++)
                if (getGoalValue(i, j) != this.tiles[i][j])
                    return false;

        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null)
            return false;
        if (this.getClass() != y.getClass())
            return false;
        if (this == y)
            return true;

        Board that = (Board) y;

        if (this.dimension() != that.dimension())
            return false;

        for (int i = 0; i < this.tiles.length; i++)
            for (int j = 0; j < this.tiles[0].length; j++)
                if (this.tiles[i][j] != that.tiles[i][j])
                    return false;

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int baseRow = 0;
        int baseCol = 0;

        for (int i = 0; i < this.tiles.length; i++)
            for (int j = 0; j < this.tiles[0].length; j++) {
                if (this.tiles[i][j] == 0) {
                    baseRow = i;
                    baseCol = j;
                }
            }

        List<Board> neighbors = new ArrayList<Board>();

        // left
        if (baseCol != 0) {
            int[][] block = new int[dimension()][dimension()];

            for (int i = 0; i < this.tiles.length; i++)
                for (int j = 0; j < this.tiles[0].length; j++) {
                    block[i][j] = this.tiles[i][j];
                }

            block[baseRow][baseCol] = this.tiles[baseRow][baseCol - 1];
            block[baseRow][baseCol - 1] = this.tiles[baseRow][baseCol];

            neighbors.add(new Board(block));
        }

        // right
        if (baseCol != dimension() - 1) {
            int[][] block = new int[dimension()][dimension()];

            for (int i = 0; i < this.tiles.length; i++)
                for (int j = 0; j < this.tiles[0].length; j++) {
                    block[i][j] = this.tiles[i][j];
                }

            block[baseRow][baseCol] = this.tiles[baseRow][baseCol + 1];
            block[baseRow][baseCol + 1] = this.tiles[baseRow][baseCol];

            neighbors.add(new Board(block));
        }

        // top
        if (baseRow != 0) {
            int[][] block = new int[dimension()][dimension()];

            for (int i = 0; i < this.tiles.length; i++)
                for (int j = 0; j < this.tiles[0].length; j++) {
                    block[i][j] = this.tiles[i][j];
                }

            block[baseRow][baseCol] = this.tiles[baseRow - 1][baseCol];
            block[baseRow - 1][baseCol] = this.tiles[baseRow][baseCol];

            neighbors.add(new Board(block));
        }

        // bottom
        if (baseRow != dimension() - 1) {
            int[][] block = new int[dimension()][dimension()];

            for (int i = 0; i < this.tiles.length; i++)
                for (int j = 0; j < this.tiles[0].length; j++) {
                    block[i][j] = this.tiles[i][j];
                }

            block[baseRow][baseCol] = this.tiles[baseRow + 1][baseCol];
            block[baseRow + 1][baseCol] = this.tiles[baseRow][baseCol];

            neighbors.add(new Board(block));
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] new_tiles = new int[dimension()][dimension()];

        for (int i = 0; i < this.tiles.length; i++)
            for (int j = 0; j < this.tiles[0].length; j++) {
                new_tiles[i][j] = this.tiles[i][j];
            }

        int swapRow = 0;
        if (new_tiles[0][0] == 0 || new_tiles[0][1] == 0)
            swapRow = 1;

        int tmp = new_tiles[swapRow][0];
        new_tiles[swapRow][0] = new_tiles[swapRow][1];
        new_tiles[swapRow][1] = tmp;

        return new Board(new_tiles);
    }

    private int getGoalValue(int row, int col) {
        if (row == dimension() - 1 && col == dimension() - 1)
            return 0;

        return (row * dimension()) + col + 1;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] input = new int[][] {
                { 1, 2, 3, 4 }, { 5, 6, 0, 8 }, { 9, 10, 11, 12 }, { 13, 14, 15, 7 }
        };
        int[][] input2 = new int[][] {
                { 1, 2, 3, 4 }, { 5, 6, 8, 0 }, { 9, 10, 11, 12 }, { 13, 14, 15, 7 }
        };

        Board testBoard = new Board(input);
        Board sameBoard = new Board(input);
        Board diffBoard = new Board(input2);

        StdOut.println(testBoard.toString());

        Iterable<Board> result = testBoard.neighbors();

        for (Board b : result) {
            StdOut.println(b.toString());
        }

        StdOut.println(testBoard.equals(sameBoard));
        StdOut.println(testBoard.equals(diffBoard));

        StdOut.println(testBoard.twin());

        int[][] input3 = new int[][] {
                { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 }
        };

        Board board = new Board(input3);

        StdOut.println(board.hamming());
        StdOut.println("hoge");
        StdOut.println(board.manhattan());

    }
}
