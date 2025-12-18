import java.io.File;

public class Main {
    public static void main(String[] args) {
        
        // Vérification qu'un fichier a bien été fourni
        if (args.length != 1) {
            System.out.println("Erreur : Aucun fichier source fourni.");
            System.out.println("Usage : java Main <nom_fichier.vm>");
            return;
        }

        String inputPath = args[0];
        File inputFile = new File(inputPath);
        
        // On crée le fichier de sortie avec l'extension .asm
        String outputPath = inputPath.replace(".vm", ".asm");
        File outputFile = new File(outputPath);

        // Initialisation du CodeWriter (qui attend un File)
        CodeWriter writer = new CodeWriter(outputFile);
        
        // CORRECTION ICI : Ton Parser attend un String (le chemin), pas un File.
        // On lui passe donc 'inputPath'.
        Parser parser = new Parser(inputPath);

        // Boucle de lecture et traduction
        while (parser.hasMoreCommands()) {
            parser.advance();
            int type = parser.commandType();

            if (type == Parser.C_ARITHMETIC) {
                writer.writeArithmetic(parser.arg1());
            }
            else if (type == Parser.C_PUSH || type == Parser.C_POP) {
                writer.writePushPop(type, parser.arg1(), parser.arg2());
            }
        }

        // Fermeture du fichier
        writer.close();
        System.out.println("Traduction terminée : " + outputPath);
    }
}