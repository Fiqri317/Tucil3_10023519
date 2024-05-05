import java.util.List;

public interface WordLadderSolver {
    List<String> solve(String start, String end);
    int getNodesVisited();
}
