public class UniqueChars {
    public static void main(String[] args) {
        

        String input = String.join(" ", args);
        System.out.println(uniqueChars(input));
    }

    /**
     * Returns a string which is identical to the original string, 
     * except that all duplicate characters are removed,
     * unless they are space characters.
     */
    public static String uniqueChars(String s) {
        int i = 0;
        int length = s.length();
        String result = "";

        while (i < length) {
            char c = s.charAt(i);

            // Check if the character is a space or not already present in the result
            if (c == ' ' || result.indexOf(String.valueOf(c)) == -1) {
                result += c;
            }
            
            i++;
        }

        return result;
    }
}
