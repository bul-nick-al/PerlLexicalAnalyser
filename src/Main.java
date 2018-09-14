import com.sun.javafx.fxml.expression.Expression;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by rozaliaamirova on 14.09.2018.
 */
public class Main {

    public static Tokenizer tokenizer;
    public static void main(String[] args) {
        tokenizer = new Tokenizer(readFile());
        while (!tokenizer.endOfInput()) {
            System.out.println(tokenizer.getNextToken());
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
