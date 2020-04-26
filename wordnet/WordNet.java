import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class WordNet {
    private HashMap<String, Set<Integer>> strToInt;
    private HashMap<Integer, String> intTostr;
    private Digraph digraph;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();

        this.strToInt = new HashMap<String, Set<Integer>>();
        this.intTostr = new HashMap<Integer, String>();
        In synIO = new In(synsets);
        In hyperIO = new In(hypernyms);
        while (synIO.hasNextLine()) {
            String[] line = synIO.readLine().split(",");
            int i = Integer.parseInt(line[0]);
            String[] words = line[1].split("\\s++");
            intTostr.put(i, line[1]);
            for (String w : words) {
                if (!strToInt.containsKey(w))
                    strToInt.put(w, new HashSet<Integer>());

                strToInt.get(w).add(i);
            }
        }

        this.digraph = new Digraph(intTostr.size());
        while (hyperIO.hasNextLine()) {
            String[] line = hyperIO.readLine().split(",");
            int index = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                int h = Integer.parseInt(line[i]);
                this.digraph.addEdge(index, h);
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.strToInt.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();

        return this.strToInt.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();

        Iterable<Integer> idA = this.strToInt.get(nounA);
        Iterable<Integer> idB = this.strToInt.get(nounB);

        BreadthFirstDirectedPaths pathA = new BreadthFirstDirectedPaths(this.digraph, idA);
        BreadthFirstDirectedPaths pathB = new BreadthFirstDirectedPaths(this.digraph, idB);

        int result = Integer.MAX_VALUE;
        for (int i : this.intTostr.keySet()) {
            if (pathA.hasPathTo(i) && pathB.hasPathTo(i)) {
                int cur = pathA.distTo(i) + pathB.distTo(i);
                if (result > cur) result = cur;
            }
        }
        if (result == Integer.MAX_VALUE) throw new IllegalArgumentException();

        return result;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();

        Iterable<Integer> idA = this.strToInt.get(nounA);
        Iterable<Integer> idB = this.strToInt.get(nounB);

        BreadthFirstDirectedPaths pathA = new BreadthFirstDirectedPaths(this.digraph, idA);
        BreadthFirstDirectedPaths pathB = new BreadthFirstDirectedPaths(this.digraph, idB);

        int result = Integer.MAX_VALUE;
        int index = -1;
        for (int i : this.intTostr.keySet()) {
            if (pathA.hasPathTo(i) && pathB.hasPathTo(i)) {
                int cur = pathA.distTo(i) + pathB.distTo(i);
                if (result > cur) {
                    index = i;
                    result = cur;
                }
            }
        }

        if (result == Integer.MAX_VALUE) throw new IllegalArgumentException();

        return this.intTostr.get(index);
    }

    public static void main(String[] args) {
        String synsets = "synsets.txt";
        String hypernyms = "hypernyms.txt";

        WordNet wordNet = new WordNet(synsets, hypernyms);
        StdOut.println(wordNet.sap(args[0], args[1]));
        StdOut.println(wordNet.distance(args[0], args[1]));
    }
}
