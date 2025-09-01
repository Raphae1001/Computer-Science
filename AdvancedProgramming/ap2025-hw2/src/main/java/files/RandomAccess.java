package files;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccess {
    /**
     * Treat the file as an array of (unsigned) 8-bit values and sort them
     * in-place using a bubble-sort algorithm.
     * You may not read the whole file into memory!
     *
     * @param file RandomAccessFile to be sorted.
     */
    public static void sortBytes(RandomAccessFile file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        long fileSize = file.length();
        if (fileSize < 2) {
            return;
        }

        boolean swapped;
        do {
            swapped = false;
            for (long i = 0; i < fileSize - 1; i++) {
                file.seek(i);
                int firstByte = file.readUnsignedByte();
                int secondByte = file.readUnsignedByte();

                if (firstByte > secondByte) {
                    file.seek(i);
                    file.writeByte(secondByte);
                    file.writeByte(firstByte);
                    swapped = true;
                }
            }
        } while (swapped);
    }

    /**
     * Treat the file as an array of unsigned 24-bit values (stored MSB first) and
     * sort
     * them in-place using a bubble-sort algorithm.
     * You may not read the whole file into memory!
     *
     * @param file RandomAccessFile to be sorted.
     * @throws IOException
     */
    public static void sortTriBytes(RandomAccessFile file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null");
        }

        long fileSize = file.length();
        if (fileSize < 3 || fileSize % 3 != 0) {
            throw new IllegalArgumentException("File size must be a multiple of 3 bytes.");
        }

        boolean swapped;
        do {
            swapped = false;
            for (long i = 0; i < fileSize - 3; i += 3) {
                // Read two consecutive 24-bit values
                file.seek(i);
                int firstValue = (file.readUnsignedByte() << 16) |
                        (file.readUnsignedByte() << 8) |
                        file.readUnsignedByte();
                int secondValue = (file.readUnsignedByte() << 16) |
                        (file.readUnsignedByte() << 8) |
                        file.readUnsignedByte();

                // Swap if necessary
                if (firstValue > secondValue) {
                    file.seek(i);
                    file.writeByte((secondValue >> 16) & 0xFF);
                    file.writeByte((secondValue >> 8) & 0xFF);
                    file.writeByte(secondValue & 0xFF);

                    file.writeByte((firstValue >> 16) & 0xFF);
                    file.writeByte((firstValue >> 8) & 0xFF);
                    file.writeByte(firstValue & 0xFF);

                    swapped = true;
                }
            }
        } while (swapped);
    }
}