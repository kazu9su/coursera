/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph g;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.g = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        BreadthFirstDirectedPaths pathToV = new BreadthFirstDirectedPaths(this.g, v);
        BreadthFirstDirectedPaths pathToW = new BreadthFirstDirectedPaths(this.g, w);
        int result = Integer.MAX_VALUE;
        for (int i = 0; i < this.g.V(); i++) {
            if (pathToV.hasPathTo(i) && pathToW.hasPathTo(i)) {
                int cur = pathToV.distTo(i) + pathToW.distTo(i);
                result = Math.min(result, cur);
            }
        }

        if (result == Integer.MAX_VALUE) return -1;

        return result;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths pathToV = new BreadthFirstDirectedPaths(this.g, v);
        BreadthFirstDirectedPaths pathToW = new BreadthFirstDirectedPaths(this.g, w);
        int result = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < this.g.V(); i++) {
            if (pathToV.hasPathTo(i) && pathToW.hasPathTo(i)) {
                int cur = pathToV.distTo(i) + pathToW.distTo(i);
                if (result > cur) {
                    result = cur;
                    index = i;
                }
            }
        }

        if (index == -1) return -1;

        return index;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        BreadthFirstDirectedPaths pathToV = new BreadthFirstDirectedPaths(this.g, v);
        BreadthFirstDirectedPaths pathToW = new BreadthFirstDirectedPaths(this.g, w);
        int result = Integer.MAX_VALUE;
        for (int i = 0; i < this.g.V(); i++) {
            if (pathToV.hasPathTo(i) && pathToW.hasPathTo(i)) {
                int cur = pathToV.distTo(i) + pathToW.distTo(i);
                result = Math.min(result, cur);
            }
        }

        if (result == Integer.MAX_VALUE) return -1;

        return result;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();

        BreadthFirstDirectedPaths pathToV = new BreadthFirstDirectedPaths(this.g, v);
        BreadthFirstDirectedPaths pathToW = new BreadthFirstDirectedPaths(this.g, w);
        int result = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < this.g.V(); i++) {
            if (pathToV.hasPathTo(i) && pathToW.hasPathTo(i)) {
                int cur = pathToV.distTo(i) + pathToW.distTo(i);
                if (result > cur) {
                    result = cur;
                    index = i;
                }
            }
        }

        if (index == -1) return -1;

        return index;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
