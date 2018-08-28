/******************************************************************************
 *  Compilation:  javac BaseballElimination.java
 *  Execution:    java BaseballElimination
 *  Dependencies: 
 *  
 *  
 *  For use on Coursera, Algorithms Part II programming assignment.
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

import java.util.Arrays;
import java.util.ArrayList;
public class BaseballElimination {
    private final int teamsN;
    private final String[] teamsName;
    private final int[] w;
    private final int[] loss;
    private final int[] r;
    private final int[][] g;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        teamsN = in.readInt();
        teamsName = new String[teamsN]; 
        w = new int[teamsN];
        loss = new int[teamsN];
        r = new int[teamsN];
        g = new int[teamsN][teamsN];
        for (int i = 0; i < teamsN; i++) {
            teamsName[i] = in.readString();
            w[i] = in.readInt();
            loss[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < teamsN; j++)
                g[i][j] = in.readInt();
        }
    }
    // number of teams
    public int numberOfTeams() {
        return teamsN;
    }
    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(teamsName);
    }
    private int teamToI(String team) {
        if (team == null)
            throw new IllegalArgumentException();
        for (int i = 0; i < teamsN; i++)
            if (teamsName[i].equals(team))
                return i;
        throw new IllegalArgumentException();
    }
    // number of wins for given team
    public int wins(String team) {
        return w[teamToI(team)];
    }
    // number of losses for given team
    public int losses(String team) {
        return loss[teamToI(team)];
    }
    // number of remaining games for given team
    public int remaining(String team) {
        return r[teamToI(team)];
    }
    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return g[teamToI(team1)][teamToI(team2)];
    }

    private FlowNetwork buildFlowNetwork(int team, int v) {
        FlowNetwork ret = new FlowNetwork(v);
        int count = 1;
        final int gameN = (teamsN-1)*(teamsN-2)/2 + 1;
        for (int i = 0; i < teamsN; i++) {
            if (i == team)
                continue;
            for (int j = i+1; j < teamsN; j++) {
                if (j == team)
                    continue;
                ret.addEdge(new FlowEdge(0, count, g[i][j]));
                if (i < team)
                    ret.addEdge(new FlowEdge(count, gameN+i, Double.POSITIVE_INFINITY));
                else
                    ret.addEdge(new FlowEdge(count, gameN+i-1, Double.POSITIVE_INFINITY));

                if (j < team) 
                    ret.addEdge(new FlowEdge(count++, gameN+j, Double.POSITIVE_INFINITY));
                else 
                    ret.addEdge(new FlowEdge(count++, gameN+j-1, Double.POSITIVE_INFINITY));
            }
        }

        for (int i = 0; i < teamsN; i++) {
            if (i == team)
                continue;
            if (i < team)
                ret.addEdge(new FlowEdge(count + i, v-1, w[team] + r[team] - w[i]));
            else
                ret.addEdge(new FlowEdge(count + i-1, v-1, w[team] + r[team] - w[i]));
        }
        return ret;    
    }
    // is given team eliminated?
    public boolean isEliminated(String team) {
        int teami = teamToI(team);
        for (int i = 0; i < teamsN; i++) {
            if (i == teami)
                continue;
            if (w[teami] + r[teami] < w[i])
                return true;
        }
        int v = teamsN + 2 + (teamsN*teamsN - 3*teamsN) / 2;
        FlowNetwork network = buildFlowNetwork(teami, v);
        // StdOut.print(network.toString());
        FordFulkerson ff = new FordFulkerson(network, 0, v-1);
        for (FlowEdge edge : network.adj(0))
            if (edge.flow() != edge.capacity())
                return true;

        return false;
    }
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        ArrayList<String> ret = new ArrayList<String>();
        int teami = teamToI(team);
        for (int i = 0; i < teamsN; i++) {
            if (i == teami)
                continue;
            if (w[teami] + r[teami] < w[i])
                ret.add(teamsName[i]);
        }
        if (!ret.isEmpty())
            return ret;
        
        int v = teamsN + 2 + (teamsN*teamsN - 3*teamsN) / 2;
        FlowNetwork network = buildFlowNetwork(teami, v);
        FordFulkerson ff = new FordFulkerson(network, 0, v-1);
        boolean eliminated = false;
        for (FlowEdge edge : network.adj(0))
            if (edge.flow() != edge.capacity())
                eliminated = true;
        if (!eliminated)
            return null;

        for (int i = 0; i < teamsN; i++) {
            if (i == teami)
                continue;
            if (i < teami && ff.inCut((teamsN-1)*(teamsN-2)/2 + 1 + i))
                ret.add(teamsName[i]);
            else if (i > teami && ff.inCut((teamsN-1)*(teamsN-2)/2 + 1 + i - 1))
                ret.add(teamsName[i]);
        }
        return ret;
    }
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {

            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
