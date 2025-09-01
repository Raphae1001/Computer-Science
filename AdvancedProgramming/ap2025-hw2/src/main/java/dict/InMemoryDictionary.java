package dict;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * Implements a persistent dictionary that can be held entirely in memory.
 * When flushed, it writes the entire dictionary back to a file.
 * <p>
 * The file format has one keyword per line:
 * 
 * <pre>
 * word:def
 * </pre>
 * <p>
 * Note that an empty definition list is allowed (in which case the entry would
 * have the form:
 * 
 * <pre>
 * word:
 * </pre>
 *
 * @author talm
 */
public class InMemoryDictionary extends TreeMap<String, String> implements PersistentDictionary {
    private static final long serialVersionUID = 1L; // (because we're extending a serializable class)
    private final File dictFile;

    public InMemoryDictionary(File dictFile) {
        if (dictFile == null) {
            throw new IllegalArgumentException("File cannot be null");
        }
        this.dictFile = dictFile;
    }

    @Override
    public void open() throws IOException {
        this.clear(); // Clear the in-memory map
        if (!dictFile.exists()) {
            return; // No file to load
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(dictFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2); // Split at the first colon
                String key = parts[0];
                String value = parts.length > 1 ? parts[1] : ""; // Handle empty value
                this.put(key, value);
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (dictFile == null) {
            throw new IOException("Dictionary file is not specified.");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(dictFile))) {
            for (Map.Entry<String, String> entry : this.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IOException("Failed to write to the dictionary file: " + dictFile.getAbsolutePath(), e);
        }
    }
}
