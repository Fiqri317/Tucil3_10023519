import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class Dictionary {
    private Set<String> words = new HashSet<>();

    public Dictionary(String filePath) throws IOException {
        File file = new File(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim().toLowerCase()); 
            }
        } 
    }

    public boolean isValid(String word) {
        return words.contains(word.toLowerCase());
    }
}
