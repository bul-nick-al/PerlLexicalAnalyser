import com.sun.javafx.fxml.expression.Expression;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by rozaliaamirova on 14.09.2018.
 */
public class Lexer {

    LinkedList<String> input;
    int currentLine;
    int currentSymbol;
    SymbolTypeRecognizer symbolTypeRecognizer;
    LexemeRecognizer lexemeRecognizer;

    public Lexer(LinkedList<String> input) {
        this.input = input;
        currentLine = 0;
        currentSymbol = 0;
        symbolTypeRecognizer = new SymbolTypeRecognizer();
        lexemeRecognizer = new LexemeRecognizer();
    }

    public Token getNextToken() {

        //System.out.println(currentSymbol);
        if (endOfInput()) {
            return new Token(Token.PerlTokens.END_OF_INPUT, "", currentLine, currentSymbol);
        }

        if (currentSymbol >= input.get(currentLine).length()) {
            currentLine++;
            currentSymbol = 0;
        }

        checkComment(String.valueOf(input.get(currentLine).charAt(currentSymbol)));

        if (currentSymbol == 0) {
            //пропустить строку, если она пустая
            if (input.get(currentLine).isEmpty()) {
                currentLine++;
            }

        }

        //пропустить пробелы
        while (input.get(currentLine).charAt(currentSymbol) == ' ') {
            currentSymbol++;
        }

        char character = input.get(currentLine).charAt(currentSymbol);

        Token result;
        switch (symbolTypeRecognizer.recognize(character)) {
            case DIGIT:
                break;
            case CHARACTER:
                result = lexemeRecognizer.recognizeIdentifier(currentSymbol, input.get(currentLine));
                currentSymbol += result.value.length();
                return result;
            case ARITHMETIC_OPERATION:
                result = lexemeRecognizer.recognizeArithmeticOperator(currentSymbol, input.get(currentLine));
                currentSymbol += result.value.length();
                return result;
            case COMPARISON_OPERATION:
                result = lexemeRecognizer.recognizeComparisonOperator(currentSymbol, input.get(currentLine));
                currentSymbol += result.value.length();
                return result;
            case UNDEFINED:
                break;
        }

        return new Token(Token.PerlTokens.ERROR, "", currentLine, currentSymbol);
    }

    public Boolean endOfInput() {

        if (currentLine >= input.size() - 1 && currentSymbol >= input.get(currentLine).length()) {
            return true;
        } else {
            return false;
        }
    }

    public void commentLine() {
        currentSymbol = 0;
        currentLine++;
    }

    /**
     * Method that checks if current string is comment and if it is will skip it
     * @param token
     * @return if current string is token
     */
    public boolean checkComment(String token) {
        if (token.matches("^#")) {
            commentLine();
            return true;
        }
        return false;
    }

}

