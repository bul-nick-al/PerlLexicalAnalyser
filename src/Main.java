import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Class Launches the program, creates and starts Lexical Recognizer
 */
public class Main {

    /**
     * Main method of the program, which launches the main algorithm of lexical analysis
     * @param args default command line arguments
     */
    public static void main(String[] args) {
        Lexer lexer = new Lexer(readFile());
        Token token = lexer.getNextToken();
        System.out.print(token.getType() + " ");
        System.out.println(token.getValue());
        while (token.getType() != Token.PerlTokens.END_OF_INPUT&&token.getType() != Token.PerlTokens.ERROR) {
            token = lexer.getNextToken();
            System.out.print(token.getType() + " ");
            System.out.println(token.getValue());
        }
    }

    /**
     * Method for reading string from input.txt
     * @return linked list of strings
     */
     static LinkedList<String> readFile() {
        LinkedList<String> result = new LinkedList<>();
        try {
            Scanner in = new Scanner(new File("STD.pm6.txt"));
            while (in.hasNext())
                result.add(in.nextLine());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * Method for writing output to output.txt
     */
     static void writeFile(String output) {
        try (final FileWriter writer = new FileWriter("out.txt", false)) {
            writer.write(output);
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
