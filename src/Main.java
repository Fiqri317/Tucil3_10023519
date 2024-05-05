import java.util.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Dictionary dictionary = new Dictionary("../dict/dictionary.txt");

            System.out.print("Masukkan Start Word : ");
            String start = scanner.nextLine();
            System.out.print("Masukkan End Word : ");
            String end = scanner.nextLine();

            WordLadderSolver solver = null;
            int choice = 0;
            while (solver == null) {
                System.out.println("Pilih Algoritma :");
                System.out.println("1. UCS - Uniform Cost Search");
                System.out.println("2. Greedy BFS - Greedy Best First Search");
                System.out.println("3. A* - A Star Search");
                System.out.print("Masukkan Pilihan (1, 2, atau 3) : ");
                choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        solver = new UCSSolver(dictionary);
                        break;
                    case 2:
                        solver = new GreedyBFSSolver(dictionary);
                        break;
                    case 3:
                        solver = new AStarSolver(dictionary);
                        break;
                    default:
                        System.out.println("Pilihan tidak valid, silakan coba lagi.");
                        break;
                    }
            }

            long startTime = System.nanoTime();
            List<String> path = solver.solve(start, end);
            long endTime = System.nanoTime();

            if (path.isEmpty()) {
                System.out.println("Tidak ada path yang ditemukan.");
            } else {
                System.out.println("Path ditemukan : " + String.join(" -> ", path));
            }

            System.out.println("Node dikunjungi : " + solver.getNodesVisited());
            System.out.print("Waktu Eksekusi : " + (endTime - startTime) / 1_000_000.0 + " ms");

        } catch (IOException e) {
            System.out.println("Error membaca dictionary : " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Masukan harus berupa angka untuk pilihan algoritma.");
        } finally {
            scanner.close();
        }
    }
}
