import java.util.HashMap;
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

        if (currentSymbol == 0) {
            //пропустить строку, если она пустая
            if (input.get(currentLine).isEmpty()) {
                currentLine++;
            }
            //пропустить пробелы в начале строки
            while (input.get(currentLine).charAt(currentSymbol) == ' ') {
                currentSymbol++;
            }
        }

        //считать токен до пробела
        while (currentSymbol != input.get(currentLine).length() && input.get(currentLine).charAt(currentSymbol) != ' ') {
            nextToken += String.valueOf(input.get(currentLine).charAt(currentSymbol));
            currentSymbol++;
        }

        //если символ последний, перейти на следующую строку, иначе перейти на следующий символ
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

    public void commentLine() {
        currentSymbol = 0;
        currentLine++;
    }
}

