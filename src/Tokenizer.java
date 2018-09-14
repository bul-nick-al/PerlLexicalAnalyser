import java.util.LinkedList;

/**
 * Created by rozaliaamirova on 14.09.2018.
 */
public class Tokenizer {

    LinkedList<String> input;
    int currentLine;
    int currentSymbol;

    public Tokenizer(LinkedList<String> input) {
        this.input = input;
        currentLine = 0;
        currentSymbol = 0;
    }

    public String getNextToken() {
        String nextToken = "";
        while (currentSymbol != input.get(currentLine).length() && input.get(currentLine).charAt(currentSymbol) != ' ') {
            nextToken += String.valueOf(input.get(currentLine).charAt(currentSymbol));
            currentSymbol++;
        }

        if (currentSymbol == input.get(currentLine).length()) {
            if (currentLine != input.size() - 1) {
                currentSymbol = 0;
                currentLine++;
            }
        } else {
            currentSymbol++;
        }
        return nextToken;
    }

    public Boolean endOfInput() {
        if (currentLine == input.size() - 1 && currentSymbol == input.get(currentLine).length()) {
            return true;
        } else {
            return false;
        }
    }
}
