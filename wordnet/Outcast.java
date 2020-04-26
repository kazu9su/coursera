/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;

    public Outcast(WordNet wordnet)         // constructor takes a WordNet object
    {
        this.wordNet = wordnet;
    }

    public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
    {
        int max = -1;
        int index = -1;
        for (int i = 0; i < nouns.length; i++) {
            int dist = 0;
            for (int j = 1; j < nouns.length; j++) {
                dist += this.wordNet.distance(nouns[i], nouns[j]);
            }
            if (dist >= max) {
                max = dist;
                index = i;
            }
        }

        return nouns[index];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
