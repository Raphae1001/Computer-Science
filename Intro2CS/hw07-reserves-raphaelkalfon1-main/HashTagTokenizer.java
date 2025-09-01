public class HashTagTokenizer {

    public static void main(String[] args) {

        String hashTag = args[0].toLowerCase(); 
        String[] dictionary = readDictionary("dictionary.txt");
        breakHashTag(hashTag, dictionary);
    }

    public static String[] readDictionary(String fileName) {
        String[] dictionary = new String[3000];
        In in = new In(fileName);
        int index = 0;
        while (!in.isEmpty() && index < 3000) {
            dictionary[index++] = in.readLine().toLowerCase(); 
        }
        return dictionary;
    }

    public static boolean existInDictionary(String word, String[] dictionary) {
        for (String dictWord : dictionary) {
            if (dictWord.equals(word)) {
                return true;
            }
        }
        return false; 
    }

    public static void breakHashTag(String hashtag, String[] dictionary) {
        for (int i = 1; i <= hashtag.length(); i++) {
            String prefix = hashtag.substring(0, i); 
            if (existInDictionary(prefix, dictionary)) {
                System.out.println(prefix); 
                breakHashTag(hashtag.substring(i), dictionary); 
                return; 
            }
        }

        if (hashtag.length() > 0) {
            System.out.println(hashtag + " (not found in dictionary)");
        }
    }
}
