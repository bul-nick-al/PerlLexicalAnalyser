import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    public static Lexer lexer;

    public static void main(String[] args) {

        LinkedList<String> tokens = readFile();

        lexer = new Lexer(readFile());

        Token token = lexer.getNextToken();
        System.out.print(token.getType() + " ");
        System.out.println(token.getValue());
        while (token.getType() != Token.PerlTokens.END_OF_INPUT) {
            token = lexer.getNextToken();
            if (token.getValue().contains("token term:sym<undef>"))
                System.out.println("XXX");
            System.out.print(token.getType() + " ");
            System.out.println(token.getValue());
        }
    }

    /**
     * Method for writing string from input.txt
     * @return
     */
    public static LinkedList<String> readFile() {
        LinkedList<String> result = new LinkedList<>();
        try {
            Scanner in = new Scanner(new File("STD.pm6.txt"));
            StringBuffer data = new StringBuffer();
            while (in.hasNext())
                result.add(in.nextLine());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * Method for reading output to output.txt
     * @param output
     */
    public static void writeFile(String output) {
        try (final FileWriter writer = new FileWriter("out.txt", false)) {
            writer.write(output);
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
