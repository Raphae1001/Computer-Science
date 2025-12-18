import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CodeWriter {
    private PrintWriter writer;
    private String fileName;
    private int labelCount;

    public CodeWriter(File outputFile) {
        try {
            writer = new PrintWriter(outputFile);
            fileName = outputFile.getName();
            // On enlève l'extension .asm pour l'usage dans les variables statiques
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex != -1) {
                fileName = fileName.substring(0, dotIndex);
            }
            labelCount = 0;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void writeArithmetic(String command) {
        if (command.equals("add")) {
            writeBinaryOp("M=D+M");
        } else if (command.equals("sub")) {
            writeBinaryOp("M=M-D");
        } else if (command.equals("and")) {
            writeBinaryOp("M=D&M");
        } else if (command.equals("or")) {
            writeBinaryOp("M=D|M");
        } else if (command.equals("neg")) {
            writeUnaryOp("M=-M");
        } else if (command.equals("not")) {
            writeUnaryOp("M=!M");
        } else if (command.equals("eq") || command.equals("gt") || command.equals("lt")) {
            writeCompareOp(command);
        }
    }

    private void writeBinaryOp(String comp) {
        writer.println("@SP");
        writer.println("AM=M-1");
        writer.println("D=M");
        writer.println("A=A-1");
        writer.println(comp);
    }

    private void writeUnaryOp(String comp) {
        writer.println("@SP");
        writer.println("A=M-1");
        writer.println(comp);
    }

    private void writeCompareOp(String command) {
        String jump = command.toUpperCase();
        String labelTrue = "TRUE_" + labelCount;
        String labelEnd = "END_" + labelCount;
        labelCount++;

        writer.println("@SP");
        writer.println("AM=M-1");
        writer.println("D=M");
        writer.println("A=A-1");
        writer.println("D=M-D");
        writer.println("@" + labelTrue);
        writer.println("D;J" + jump);
        writer.println("@SP");
        writer.println("A=M-1");
        writer.println("M=0"); // False
        writer.println("@" + labelEnd);
        writer.println("0;JMP");
        writer.println("(" + labelTrue + ")");
        writer.println("@SP");
        writer.println("A=M-1");
        writer.println("M=-1"); // True
        writer.println("(" + labelEnd + ")");
    }

    public void writePushPop(int command, String segment, int index) {
        if (command == Parser.C_PUSH) {
            if (segment.equals("constant")) {
                writer.println("@" + index);
                writer.println("D=A");
                pushDToStack();
            } else if (segment.equals("local")) {
                pushSegment("LCL", index);
            } else if (segment.equals("argument")) {
                pushSegment("ARG", index);
            } else if (segment.equals("this")) {
                pushSegment("THIS", index);
            } else if (segment.equals("that")) {
                pushSegment("THAT", index);
            } else if (segment.equals("temp")) {
                writer.println("@" + (5 + index));
                writer.println("D=M");
                pushDToStack();
            } else if (segment.equals("pointer")) {
                if (index == 0) writer.println("@THIS");
                else writer.println("@THAT");
                writer.println("D=M");
                pushDToStack();
            } else if (segment.equals("static")) {
                writer.println("@" + fileName + "." + index);
                writer.println("D=M");
                pushDToStack();
            }
        } else if (command == Parser.C_POP) {
            if (segment.equals("local")) {
                popSegment("LCL", index);
            } else if (segment.equals("argument")) {
                popSegment("ARG", index);
            } else if (segment.equals("this")) {
                popSegment("THIS", index);
            } else if (segment.equals("that")) {
                popSegment("THAT", index);
            } else if (segment.equals("temp")) {
                writer.println("@" + (5 + index));
                writer.println("D=A");
                popDToAddress();
            } else if (segment.equals("pointer")) {
                if (index == 0) writer.println("@THIS");
                else writer.println("@THAT");
                writer.println("D=A");
                popDToAddress();
            } else if (segment.equals("static")) {
                writer.println("@" + fileName + "." + index);
                writer.println("D=A");
                popDToAddress();
            }
        }
    }

    // Helper: Push valeur de segment[index] sur la stack
    private void pushSegment(String label, int index) {
        writer.println("@" + label);
        writer.println("D=M");
        writer.println("@" + index);
        writer.println("A=D+A");
        writer.println("D=M");
        pushDToStack();
    }

    // Helper: Push D sur la stack
    private void pushDToStack() {
        writer.println("@SP");
        writer.println("A=M");
        writer.println("M=D");
        writer.println("@SP");
        writer.println("M=M+1");
    }

    // Helper: Pop stack vers segment[index]
    private void popSegment(String label, int index) {
        writer.println("@" + label);
        writer.println("D=M");
        writer.println("@" + index);
        writer.println("D=D+A"); // D contient l'adresse cible
        popDToAddress();
    }

    // Helper: Pop stack vers l'adresse stockée dans D
    private void popDToAddress() {
        writer.println("@R13"); // Variable temporaire
        writer.println("M=D");  // R13 contient l'adresse cible
        writer.println("@SP");
        writer.println("AM=M-1");
        writer.println("D=M");  // D contient la valeur à pop
        writer.println("@R13");
        writer.println("A=M");
        writer.println("M=D");  // Stocke la valeur à l'adresse cible
    }

    public void close() {
        writer.close();
    }
}