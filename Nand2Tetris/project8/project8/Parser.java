import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {
    // Constantes pour identifier les types de commandes
    public static final int C_ARITHMETIC = 0;
    public static final int C_PUSH = 1;
    public static final int C_POP = 2;
    public static final int C_LABEL = 3;
    public static final int C_GOTO = 4;
    public static final int C_IF = 5;
    public static final int C_FUNCTION = 6;
    public static final int C_RETURN = 7;
    public static final int C_CALL = 8;

    private List<String> commands;
    private int currentLineIndex;
    private String currentCommand;

    public Parser(String fileName) {
        commands = new ArrayList<>();
        currentLineIndex = 0;

        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                int commentIndex = line.indexOf("//");
                if (commentIndex != -1) {
                    line = line.substring(0, commentIndex);
                }
                line = line.trim();
                if (line.length() > 0) {
                    commands.add(line);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Erreur : Fichier " + fileName + " introuvable.");
        }
    }

    public boolean hasMoreCommands() {
        return currentLineIndex < commands.size();
    }

    public void advance() {
        currentCommand = commands.get(currentLineIndex);
        currentLineIndex++;
    }

    // Devine le type de la commande actuelle
    public int commandType() {
        if (currentCommand.startsWith("push")) return C_PUSH;
        if (currentCommand.startsWith("pop")) return C_POP;
        if (currentCommand.startsWith("label")) return C_LABEL;
        if (currentCommand.startsWith("goto")) return C_GOTO;
        if (currentCommand.startsWith("if-goto")) return C_IF;
        if (currentCommand.startsWith("function")) return C_FUNCTION;
        if (currentCommand.startsWith("return")) return C_RETURN;
        if (currentCommand.startsWith("call")) return C_CALL;
        return C_ARITHMETIC; // Si ce n'est rien d'autre, c'est une opération mathématique (add, sub, eq...)
    }

    // Retourne le premier argument (ex: "constant" pour "push constant 7", ou "add" pour "add")
    public String arg1() {
        if (commandType() == C_ARITHMETIC) {
            return currentCommand.split(" ")[0]; // Retourne la commande elle-même (ex: "add")
        }
        return currentCommand.split(" ")[1]; // Retourne l'argument (ex: "constant")
    }

    // Retourne le deuxième argument (ex: 7 pour "push constant 7")
    public int arg2() {
        String[] parts = currentCommand.split(" ");
        return Integer.parseInt(parts[2]);
    }
}