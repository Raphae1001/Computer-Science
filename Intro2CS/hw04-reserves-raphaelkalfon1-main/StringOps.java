public class StringOps {

    public static void main(String[] args) {
        
    }

    public static String capVowelsLowRest(String string) {
        String result = ""; 
    
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            int value = (int) ch;
    
            if ("aeiou".indexOf(ch) >= 0) { 
                value -= 32; 
                result += (char) value;
            } else if ("AEIOU".indexOf(ch) >= 0) { 
                result += ch;
            } else {
                result += ch;
            }
        }
    
        return result;
    }
    
        

    public static String camelCase(String string) {
        String result = "";
        boolean newWord = false; 
        boolean firstWord = true; 

        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (ch == ' ') { 
                newWord = true;
            } else {
                int Value = (int) ch;
                if (newWord || i == 0) { 
                    if (firstWord) { 
                        result += (Value >= 65 && Value <= 90) ? (char)(Value + 32) : ch; 
                        firstWord = false; 
                    } else { 
                        result += (Value >= 97 && Value <= 122) ? (char)(Value - 32) : ch; 
                    }
                    newWord = false;
                } else { 
                    result += (Value >= 65 && Value <= 90) ? (char)(Value + 32) : ch; 
                }
            }
        }
    
        return result;
    }
    

    public static int[] allIndexOf(String string, char chr) {

        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == chr) {
                count++;
            }
        }
    
        int[] indices = new int[count]; 
        int index = 0; 

        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == chr) {
                indices[index] = i; 
                index++; 
            }
        }
    
        return indices;
    }
}
    
