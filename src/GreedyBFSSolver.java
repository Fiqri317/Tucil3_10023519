import java.util.*;

public class GreedyBFSSolver implements WordLadderSolver {
    private Dictionary dictionary;
    private int nodesVisited;

    public GreedyBFSSolver(Dictionary dictionary) {
        this.dictionary = dictionary;
        this.nodesVisited = 0;
    }

    @Override
    public List<String> solve(String start, String end) {
        if (!dictionary.isValid(start) || !dictionary.isValid(end)) {
            return new ArrayList<>();  
        }

        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(n -> n.h));
        Map<String, Integer> visited = new HashMap<>();
        Node startNode = new Node(start, null, heuristic(start, end));

        frontier.add(startNode);
        visited.put(start, startNode.h);

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();
            nodesVisited++;

            if (current.word.equals(end)) {
                return constructPath(current);
            }

            for (String neighbor : getNeighbors(current.word)) {
                if (!dictionary.isValid(neighbor)) continue;
                int h = heuristic(neighbor, end);

                if (!visited.containsKey(neighbor) || h < visited.get(neighbor)) {
                    visited.put(neighbor, h);
                    frontier.add(new Node(neighbor, current, h));
                }
            }
        }

        return new ArrayList<>(); 
    }

    @Override
    public int getNodesVisited() {
        return nodesVisited;
    }
    
    private List<String> constructPath(Node node) {
        LinkedList<String> path = new LinkedList<>();
        while (node != null) {
            path.addFirst(node.word);
            node = node.prev;
        }
        return new ArrayList<>(path);
    }

    private int heuristic(String current, String end) {
        int difference = 0;
        for (int i = 0; i < current.length(); i++) {
            if (current.charAt(i) != end.charAt(i)) {
                difference++;
            }
        }
        return difference;
    }

    private List<String> getNeighbors(String word) {
        List<String> neighbors = new ArrayList<>();
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char oldChar = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (c == oldChar) continue;
                chars[i] = c;
                String newWord = new String(chars);
                if (dictionary.isValid(newWord)) {
                    neighbors.add(newWord);
                }
            }
            chars[i] = oldChar; 
        }
        return neighbors;
    }

    static class Node {
        String word;
        Node prev;
        int h; 

        Node(String word, Node prev, int h) {
            this.word = word;
            this.prev = prev;
            this.h = h;
        }
    }
}
