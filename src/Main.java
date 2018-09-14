import com.sun.javafx.fxml.expression.Expression;

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

    public static Tokenizer tokenizer;

    public static void main(String[] args) {

        ArrayList<String> tokens = new ArrayList<>();
        tokenizer = new Tokenizer(readFile());

        while (!tokenizer.endOfInput()) {
            String token = tokenizer.getNextToken();

            if (!checkComment(token)) {
                System.out.println(token);
                tokens.add(token);
            }

        }
    }

    //но первая строка вроде не должна быть удалена

    /**
     * Method that checks if current string is comment and if it is will skip it
     * @param token
     * @return if current string is token
     */
    public static boolean checkComment(String token) {
        if (token.matches("^#")) {
            tokenizer.commentLine();
            return true;
        }
        return false;
    }

    /**
     * Method for writing string from input.txt
     * @return
     */
    public static LinkedList<String> readFile() {
        LinkedList<String> result = new LinkedList<>();
        try {
            Scanner in = new Scanner(new File("codeExample.txt"));
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
