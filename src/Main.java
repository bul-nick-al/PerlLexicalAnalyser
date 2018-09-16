import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;


/**
 * Created by rozaliaamirova on 14.09.2018.
 */
public class Main {

    public static Lexer lexer;

    public static void main(String[] args) {
        ArrayList<String> tokens = new ArrayList<>();
        lexer = new Lexer(readFile());

        Token token = lexer.getNextToken();
        System.out.print(token.type + " ");
        System.out.println(token.value);
        while (token.type != Token.PerlTokens.END_OF_INPUT) {
            token = lexer.getNextToken();
            System.out.print(token.type + " ");
            System.out.println(token.value);
        }
    }

    /**
     * Method for writing string from input.txt
     * @return
     */
    public static LinkedList<String> readFile() {
        LinkedList<String> result = new LinkedList<>();
        try {
            Scanner in = new Scanner(new File("myExample.txt"));
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
