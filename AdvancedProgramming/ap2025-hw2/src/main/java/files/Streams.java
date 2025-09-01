package files;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Streams {
    /**
     * Read from an InputStream until a quote character (") is found, then read
     * until another quote character is found and return the bytes in between the
     * two quotes.
     * If no quote character was found return null, if only one, return the bytes
     * from the quote to the end of the stream.
     *
     * @param in InputStream to read from
     * @return A list containing the bytes between the first occurrence of a quote
     *         character and the second.
     */
    public static List<Byte> getQuoted(InputStream in) throws IOException {
        if (in == null) {
            return null;
        }

        List<Byte> result = new ArrayList<>();
        boolean firstQuoteFound = false;
        int currentByte;

        while ((currentByte = in.read()) != -1) {
            if (currentByte == '"') {
                if (firstQuoteFound) {
                    return result;
                } else {
                    firstQuoteFound = true;
                }
            } else if (firstQuoteFound) {
                result.add((byte) currentByte);
            }
        }

        return firstQuoteFound ? result : null;
    }

    /**
     * Read from the input until a specific string is read, return the string read
     * up to (not including) the endMark.
     *
     * @param in      the Reader to read from
     * @param endMark the string indicating to stop reading.
     * @return The string read up to (not including) the endMark (if the endMark is
     *         not found, return up to the end of the stream).
     */
    public static String readUntil(Reader in, String endMark) throws IOException {
        if (in == null || endMark == null || endMark.isEmpty()) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        int currentChar;
        StringBuilder buffer = new StringBuilder();

        while ((currentChar = in.read()) != -1) {
            result.append((char) currentChar);
            buffer.append((char) currentChar);

            if (buffer.toString().endsWith(endMark)) {
                return result.substring(0, result.length() - endMark.length());
            }

            if (buffer.length() > endMark.length()) {
                buffer.deleteCharAt(0);
            }
        }

        return result.toString();
    }

    /**
     * Copy bytes from input to output, ignoring all occurrences of badByte.
     *
     * @param in      InputStream to read from
     * @param out     OutputStream to write to
     * @param badByte the byte to ignore
     */
    public static void filterOut(InputStream in, OutputStream out, byte badByte) throws IOException {
        if (in == null || out == null) {
            return;
        }

        int currentByte;
        int unsignedBadByte = badByte & 0xFF; // Traite badByte comme non signé
        while ((currentByte = in.read()) != -1) {
            if ((currentByte & 0xFF) != unsignedBadByte) {
                out.write(currentByte);
            }
        }
    }

    /**
     * Read a 40-bit (unsigned) integer from the stream and return it. The number is
     * represented as five bytes,
     * with the most-significant byte first.
     * If the stream ends before 5 bytes are read, return -1.
     *
     * @param in InputStream to read from
     * @return the number read from the stream
     */
    public static long readNumber(InputStream in) throws IOException {
        if (in == null) {
            return -1;
        }

        long number = 0;
        for (int i = 0; i < 5; i++) {
            int currentByte = in.read();
            if (currentByte == -1) {
                return -1; // Stream ended before 5 bytes were read.
            }
            number = (number << 8) | (currentByte & 0xFF);
        }
        return number;
    }
}