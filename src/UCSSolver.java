import java.util.*;

public class UCSSolver implements WordLadderSolver {
    private Dictionary dictionary;
    private int nodesVisited;

    public UCSSolver(Dictionary dictionary) {
        this.dictionary = dictionary;
        this.nodesVisited = 0;
    }

    @Override
    public List<String> solve(String start, String end) {
        if (!dictionary.isValid(start) || !dictionary.isValid(end)) {
            return new ArrayList<>();  
        }

        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
        Map<String, Integer> visited = new HashMap<>();
        Node startNode = new Node(start, null, 0);

        frontier.add(startNode);
        visited.put(start, 0);

        while (!frontier.isEmpty()) {
            Node current = frontier.poll();
            nodesVisited++;

            if (current.word.equals(end)) {
                return constructPath(current);
            }

            for (String neighbor : getNeighbors(current.word)) {
                if (!dictionary.isValid(neighbor)) continue;
                int newCost = current.cost + 1;

                if (!visited.containsKey(neighbor) || newCost < visited.get(neighbor)) {
                    visited.put(neighbor, newCost);
                    frontier.add(new Node(neighbor, current, newCost));
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
        int cost;  

        Node(String word, Node prev, int cost) {
            this.word = word;
            this.prev = prev;
            this.cost = cost;
        }
    }
}
