import java.util.*;

public class AStarSolver implements WordLadderSolver {
    private Dictionary dictionary;
    private int nodesVisited;

    public AStarSolver(Dictionary dictionary) {
        this.dictionary = dictionary;
        this.nodesVisited = 0;
    }

    @Override
    public List<String> solve(String start, String end) {
        if (!dictionary.isValid(start) || !dictionary.isValid(end)) {
            return new ArrayList<>();  
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Map<String, Node> allNodes = new HashMap<>();

        Node startNode = new Node(start, null, 0, heuristic(start, end));
        allNodes.put(start, startNode);
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            nodesVisited++;

            if (current.word.equals(end)) {
                return constructPath(current);
            }

            for (String neighbor : getNeighbors(current.word)) {
                if (!dictionary.isValid(neighbor)) continue;

                int tentativeG = current.g + 1;
                Node neighborNode = allNodes.getOrDefault(neighbor, new Node(neighbor));
                allNodes.putIfAbsent(neighbor, neighborNode);

                if (tentativeG < neighborNode.g) {
                    neighborNode.prev = current;
                    neighborNode.g = tentativeG;
                    neighborNode.f = tentativeG + heuristic(neighbor, end);

                    if (!openSet.contains(neighborNode)) {
                        openSet.add(neighborNode);
                    }
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
        List<String> path = new ArrayList<>();
        while (node != null) {
            path.add(node.word);
            node = node.prev;
        }
        Collections.reverse(path);
        return path;
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
        for (int i = 0; i < word.length(); i++) {
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
        int g;
        int f;

        Node(String word, Node prev, int g, int h) {
            this.word = word;
            this.prev = prev;
            this.g = g;
            this.f = g + h;
        }

        Node(String word) {
            this(word, null, Integer.MAX_VALUE, 0);
        }
    }
}
