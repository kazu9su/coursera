import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private MinPQ<Node> queue;
    private boolean solvable;
    private Node endNode;

    private class Node implements Comparable<Node> {
        Board board;
        Node parent;
        int moves;

        public Node(Board board, Node parent) {
            this.board = board;
            this.parent = parent;
            if (parent == null)
                moves = 0;
            else
                moves = parent.moves + 1;
        }

        @Override
        public int compareTo(Node that) {
            return this.board.manhattan() - that.board.manhattan() + this.moves - that.moves;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();

        solvable = false;
        queue = new MinPQ<Node>();
        queue.insert(new Node(initial, null));

        while (!solvable) {
            solvable = solveStep(queue);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    private boolean solveStep(MinPQ<Node> q) {
        Node current = q.delMin();

        if (current.board.isGoal()) {
            endNode = current;
            return true;
        }

        for (Board b : current.board.neighbors()) {
            if (current.parent == null || !b.equals(current.parent.board)) {
                Node neighbor = new Node(b, current);
                q.insert(neighbor);
            }
        }

        return false;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (solvable) {
            Node current = endNode;
            int moves = 0;

            while (current.parent != null) {
                moves++;
                current = current.parent;
            }

            return moves;
        }

        return -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!solvable)
            return null;

        Stack<Board> stack = new Stack<Board>();

        Node current = endNode;
        stack.push(current.board);

        while (current.parent != null) {
            current = current.parent;
            stack.push(current.board);
        }

        return stack;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
