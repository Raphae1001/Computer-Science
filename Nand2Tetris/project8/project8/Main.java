import java.io.File;

public class Main {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java Main <file.vm | directory>");
            return;
        }

        File input = new File(args[0]);

        if (input.isFile()) {
            translateSingleFile(input);
        } else if (input.isDirectory()) {
            translateDirectory(input);
        } else {
            System.out.println("Erreur: chemin invalide.");
        }
    }

    // ===== Cas 1 : un seul fichier .vm =====
    private static void translateSingleFile(File vmFile) {

        String outputPath = vmFile.getAbsolutePath().replace(".vm", ".asm");
        File outputFile = new File(outputPath);

        CodeWriter writer = new CodeWriter(outputFile);
        writer.setFileName(stripExtension(vmFile.getName()));

        Parser parser = new Parser(vmFile.getAbsolutePath());

        translate(parser, writer);

        writer.close();
    }

    // ===== Cas 2 : un dossier =====
    private static void translateDirectory(File dir) {

        String dirName = dir.getName();
        File outputFile = new File(dir, dirName + ".asm");

        CodeWriter writer = new CodeWriter(outputFile);

        // Bootstrap obligatoire
        writer.writeInit();

        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".vm")) {

                writer.setFileName(stripExtension(file.getName()));

                Parser parser = new Parser(file.getAbsolutePath());
                translate(parser, writer);
            }
        }

        writer.close();
    }

    // ===== Traduction commune =====
    private static void translate(Parser parser, CodeWriter writer) {

        while (parser.hasMoreCommands()) {
            parser.advance();
            int type = parser.commandType();

            switch (type) {
                case Parser.C_ARITHMETIC:
                    writer.writeArithmetic(parser.arg1());
                    break;

                case Parser.C_PUSH:
                case Parser.C_POP:
                    writer.writePushPop(type, parser.arg1(), parser.arg2());
                    break;

                case Parser.C_LABEL:
                    writer.writeLabel(parser.arg1());
                    break;

                case Parser.C_GOTO:
                    writer.writeGoto(parser.arg1());
                    break;

                case Parser.C_IF:
                    writer.writeIf(parser.arg1());
                    break;

                case Parser.C_FUNCTION:
                    writer.writeFunction(parser.arg1(), parser.arg2());
                    break;

                case Parser.C_CALL:
                    writer.writeCall(parser.arg1(), parser.arg2());
                    break;

                case Parser.C_RETURN:
                    writer.writeReturn();
                    break;
            }
        }
    }

    // ===== Helper =====
    private static String stripExtension(String name) {
        int dot = name.lastIndexOf('.');
        return (dot == -1) ? name : name.substring(0, dot);
    }
}