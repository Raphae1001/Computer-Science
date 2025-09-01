import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
        In input = new In(fileName); 
        String text = input.readAll();
        for (int i = 0; i <= text.length() - windowLength - 1; i++) {
            String window = text.substring(i, i + windowLength);
            char nextChar = text.charAt(i + windowLength);
            List charList = CharDataMap.getOrDefault(window, new List());
            charList.update(nextChar);
            CharDataMap.put(window, charList);
        }
	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	public void calculateProbabilities(List probs) {				
        int total = 0;
        for (int i = 0; i < probs.getSize(); i++) {
            total += probs.get(i).count;
        }
        for (int i = 0; i < probs.getSize(); i++) {
            CharData charData = probs.get(i);
            charData.p = (double) charData.count / total;
        }
	}
    //

    // Returns a random character from the given probabilities list.
	public char getRandomChar(List probs) {
        double p = randomGenerator.nextDouble();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < probs.getSize(); i++) {
            cumulativeProbability += probs.get(i).p;
            if (p <= cumulativeProbability) {
                return probs.get(i).chr;
            }
        }
        return ' ';
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
        StringBuilder generatedText = new StringBuilder(initialText);
        for (int i = 0; i < textLength; i++) {
            String window = generatedText.substring(generatedText.length() - windowLength);
            if (!CharDataMap.containsKey(window)) break;
            List charList = CharDataMap.get(window);
            calculateProbabilities(charList);
            char nextChar = getRandomChar(charList);
            generatedText.append(nextChar);
        }
        return generatedText.toString();
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
        StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
		
    }
}
