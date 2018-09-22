import java.util.Arrays;
import java.util.LinkedList;

public class Lexer {
    boolean x = false;

    private LinkedList<String> input;
    private int currentLine;
    private int currentSymbol;
    SymbolTypeRecognizer symbolTypeRecognizer;

    public Lexer(LinkedList<String> input) {
        this.input = input;
        currentLine = 0;
        currentSymbol = 0;
        symbolTypeRecognizer = new SymbolTypeRecognizer();
    }

    private void checkEndOfString() {
        if (currentSymbol >= input.get(currentLine).length()) {
            currentLine++;
            currentSymbol = 0;
            checkEndOfString();
        }
    }

    private String getSubstring(int endSymbol) {
        try {
            if (x)
                System.out.println(input.get(currentLine).substring(currentSymbol, endSymbol));
            return input.get(currentLine).substring(currentSymbol, endSymbol);
        } catch (Exception e) {
            return "";
        }
    }

    private String getSubstring(int startSymbol, int endSymbol) {
        try {
            return input.get(currentLine).substring(startSymbol, endSymbol);
        } catch (Exception e) {
            return "";
        }
    }

    private int recogniseNumberToken() {
        int endSymbol = currentSymbol + 1;
        int longestMatchingPosition = 0;
        while (endSymbol <= input.get(currentLine).length()) {
            if (PatternsRecogniser.isNumber(input.get(currentLine).substring(currentSymbol, endSymbol)))
                longestMatchingPosition = endSymbol;
            endSymbol++;
        }
        return longestMatchingPosition;
    }

    private int recogniseStringToken() {
        int endSymbol = currentSymbol + 1;
        try {
            do {
                endSymbol++;
            } while (!PatternsRecogniser.isString(input.get(currentLine).substring(currentSymbol, endSymbol)));
            return endSymbol;
        } catch (Exception e) {
            return -1;
        }
    }

    private int recogniseIdentifierToken() {
        int endSymbol = currentSymbol;
        int longestMatchingPosition = 0;
        while (endSymbol < input.get(currentLine).length()) {
            if (PatternsRecogniser.isIdentifier(input.get(currentLine).substring(currentSymbol, endSymbol + 1)))
                longestMatchingPosition = endSymbol + 1;
            endSymbol++;
        }
        return longestMatchingPosition;
    }

    private int recogniseRegexToken() {
        int endSymbol = currentSymbol;
        int longestMatchingPosition = 0;
        while (endSymbol < input.get(currentLine).length()) {
            if (PatternsRecogniser.isRegex(input.get(currentLine).substring(currentSymbol, endSymbol + 1)))
                longestMatchingPosition = endSymbol + 1;
            endSymbol++;
        }
        return longestMatchingPosition;
    }

    private int recogniseNamedRegexToken() {
        int longestMatchingPosition = 0;
        if (PatternsRecogniser.isNamedRegex(input.get(currentLine))) {
            longestMatchingPosition = input.get(currentLine).length();
        }
        return longestMatchingPosition;
    }

    private Token getNamedRegexToken() {
        String result = getNamedRegexWithEmbeddedParanthesis();
        currentLine++;
        currentSymbol = 0;
        return new Token(Token.PerlTokens.REGEX, result, currentLine, currentSymbol);
    }

    //ищем тело регулярного выражения, заключенное в { }
    private String getNamedRegexWithEmbeddedParanthesis() {
        boolean isAnglesParenthesis = false;
        String currentSubstring;
        currentSubstring = "";
        while (true) {
            checkEndOfString();
            if (currentSubstring.contains("method_def")) {
                System.out.println("AAAAAAAAAA");
                x = false;
            }
            if (input.get(currentLine).charAt(currentSymbol) == '<') {
                isAnglesParenthesis = true;
            }
            if (input.get(currentLine).charAt(currentSymbol) == '>') {
                isAnglesParenthesis = false;
            }
            if (input.get(currentLine).charAt(currentSymbol) == '{' && !isAnglesParenthesis) {
                int tempLine = currentLine;
                int tempSymbol = currentSymbol;
                currentSymbol++;
                getInnerRecursionEmbeddings("{");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(input.get(tempLine).substring(tempSymbol, input.get(tempLine).length()));
                for (int i = tempLine + 1; i < currentLine; i++) {
                    stringBuilder.append(input.get(i));
                }
                if (tempLine < currentLine)
                    stringBuilder.append(input.get(currentLine).substring(0, currentSymbol));
                return currentSubstring + stringBuilder.toString();
            } else {
                currentSubstring += String.valueOf(input.get(currentLine).charAt(currentSymbol));
                currentSymbol++;
            }
        }
    }

    private boolean isNoEscape() {
        int number_of_slashes = 0;
        int i = 0;
        while (getSubstring(currentSymbol - 1 - i, currentSymbol - i).equals("\\")) {
            number_of_slashes++;
            i++;
        }
        return number_of_slashes % 2 == 0;
    }

    private void getInnerRecursionEmbeddings(String openedParenthesis) {
        switch (openedParenthesis) {
            case "'":
            case "\"":
                currentSymbol = recogniseStringToken();
                return;
            case "<?[":
                while (true) {
                    checkEndOfString();
                    if (getSubstring(currentSymbol + 2).equals("]>") && isNoEscape()) {
                        currentSymbol+=2;
                        return;
                    }
                    currentSymbol++;
                }
            case "<(":
            case "<?(":
                while (true) {
                    checkEndOfString();
                    if (getSubstring(currentSymbol + 2).equals(")>") && isNoEscape()) {
                        currentSymbol+=2;
                        return;
                    }
                    currentSymbol++;
                }
            case "{":
                while (true) {
                    checkEndOfString();
                    if (getSubstring(currentSymbol + 1).equals("#") && isNoEscape()) {
                        currentLine++;
                        currentSymbol = 0;
                        continue;
                    }
                    if (getSubstring(currentSymbol + 1).equals("'") && isNoEscape()) {
                        getInnerRecursionEmbeddings("'");
                        continue;
                    }
                    if (getSubstring(currentSymbol + 1).equals("\"") && isNoEscape()) {
                        getInnerRecursionEmbeddings("\"");
                        continue;
                    }
                    if (getSubstring(currentSymbol + 1).equals("}") && isNoEscape()) {
                        currentSymbol++;
                        return;
                    }
                    if (getSubstring(currentSymbol + 1).equals("{") && isNoEscape()) {
                        currentSymbol++;
                        getInnerRecursionEmbeddings("{");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 1).equals("[") && isNoEscape()) {
                        currentSymbol++;
                        getInnerRecursionEmbeddings("[");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 3).matches("<.\\[") && isNoEscape()) {
                        currentSymbol+=3;
                        getInnerRecursionEmbeddings("<?[");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 2).equals("<(") && isNoEscape()) {
                        currentSymbol+=2;
                        getInnerRecursionEmbeddings("<(");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 3).equals("<?(") && isNoEscape()) {
                        currentSymbol+=3;
                        getInnerRecursionEmbeddings("<?(");
                        currentSymbol--;
                    }

                    if (getSubstring(currentSymbol + 1).equals("<") && isNoEscape()) {
                        int j = currentSymbol;
                        String currentSubstring = "";
                        while (j < input.get(currentLine).length() && !currentSubstring.matches("<.*\\[.*\\]>")) {
                            currentSubstring += input.get(currentLine).charAt(j);
                            j++;
                        }
                        if (currentSubstring.matches("<.*\\[.*\\]>")) {
                            currentSymbol += currentSubstring.length();

                        }
                    }
                    currentSymbol++;
                }
            case "[":
                while (true) {
                    checkEndOfString();
                    if (getSubstring(currentSymbol + 1).equals("#") && isNoEscape()) {
                        currentLine++;
                        currentSymbol = 0;
                        continue;
                    }
                    if (getSubstring(currentSymbol + 1).equals("'") && isNoEscape()) {
                        getInnerRecursionEmbeddings("'");
                        continue;
                    }
                    if (getSubstring(currentSymbol + 1).equals("\"") && isNoEscape()) {
                        getInnerRecursionEmbeddings("\"");
                        continue;
                    }
                    if (getSubstring(currentSymbol + 1).equals("]") && isNoEscape()) {
                        currentSymbol++;
                        return;
                    }
                    if (getSubstring(currentSymbol + 1).equals("{") && isNoEscape()) {
                        currentSymbol++;
                        getInnerRecursionEmbeddings("{");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 1).equals("[") && isNoEscape()) {
                        currentSymbol++;
                        getInnerRecursionEmbeddings("[");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 2).equals("<(") && isNoEscape()) {
                        currentSymbol+=2;
                        getInnerRecursionEmbeddings("<(");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 3).equals("<?(") && isNoEscape()) {
                        currentSymbol+=3;
                        getInnerRecursionEmbeddings("<?(");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 1).equals("<") && isNoEscape()) {
                        int j = currentSymbol;
                        String currentSubstring = "";
                        while (j < input.get(currentLine).length() && !currentSubstring.matches("<.*\\[.*\\]>")) {
                            currentSubstring += input.get(currentLine).charAt(j);
                            j++;
                        }
                        if (currentSubstring.matches("<.*\\[.*\\]>")) {
                            currentSymbol += currentSubstring.length();
                            return;
                        }
                    }
                    currentSymbol++;
                }
        }
    }

    private int recogniseReservedToken() {
        int endSymbol = currentSymbol;
        int longestMatchingPosition = 0;
        while (endSymbol < input.get(currentLine).length()) {
            if (Token.getTokenMapSingleton().containsKey(input.get(currentLine).substring(currentSymbol, endSymbol + 1)))
                longestMatchingPosition = endSymbol + 1;
            endSymbol++;
        }
        return longestMatchingPosition;
    }

    private int recogniseEmbeddedToken() {
        int endSymbol = currentSymbol;
        do {
            endSymbol++;
        }
        while (endSymbol < input.get(currentLine).length() && !PatternsRecogniser.isEmbeddedComment(input.get(currentLine).substring(currentSymbol, endSymbol)));

        if (endSymbol == input.get(currentLine).length() && !PatternsRecogniser.isEmbeddedComment(input.get(currentLine).substring(currentSymbol, endSymbol))) {
            return -1;
        }
        return endSymbol;
    }

    private Token findMatchingPattern() {
        char character = input.get(currentLine).charAt(currentSymbol);
        SymbolTypeRecognizer.SymbolType type = SymbolTypeRecognizer.recognize(character);
        int endSymbol;
        int tempCurrent;
        switch (type) {
            case STRING_CANDIDATE:
                if ((endSymbol = recogniseStringToken()) != -1) {
                    tempCurrent = currentSymbol;
                    currentSymbol = endSymbol;
                    return new Token(Token.PerlTokens.STRING, input.get(currentLine).substring(tempCurrent, endSymbol), currentLine, tempCurrent);
                }
            case NUMBER_CANDIDATE:
                endSymbol = recogniseNumberToken();
                tempCurrent = currentSymbol;
                currentSymbol = endSymbol;
                return new Token(Token.PerlTokens.NUMBER, input.get(currentLine).substring(tempCurrent, endSymbol), currentLine, tempCurrent);
            case WORD_CANDIDATE:
                int namedRegexPosition = recogniseNamedRegexToken(), identifierPosition = recogniseIdentifierToken(), reservedPosition = recogniseReservedToken(), regexPosition = recogniseRegexToken();
                int[] positions = {identifierPosition, reservedPosition, regexPosition, namedRegexPosition};
                int longestMatchingPosition = Arrays.stream(positions).max().getAsInt();
                if (longestMatchingPosition == namedRegexPosition) {
                    return getNamedRegexToken();
                }
                if (longestMatchingPosition == reservedPosition) {
                    tempCurrent = currentSymbol;
                    currentSymbol = reservedPosition;
                    return new Token(Token.getTokenMapSingleton().get(input.get(currentLine).substring(tempCurrent, reservedPosition)),
                            input.get(currentLine).substring(tempCurrent, reservedPosition), currentLine, tempCurrent);
                }
                if (longestMatchingPosition == identifierPosition) {
                    tempCurrent = currentSymbol;
                    currentSymbol = identifierPosition;
                    return new Token(Token.PerlTokens.IDENTIFIER, input.get(currentLine).substring(tempCurrent, identifierPosition), currentLine, tempCurrent);
                }
                if (longestMatchingPosition == regexPosition) {
                    tempCurrent = currentSymbol;
                    currentSymbol = regexPosition;
                    return new Token(Token.PerlTokens.REGEX, input.get(currentLine).substring(tempCurrent, regexPosition), currentLine, tempCurrent);
                }
            case COMMENT_CANDIDATE:
                int commentPosition = recogniseEmbeddedToken();
                if (commentPosition == -1) {
                    currentSymbol = input.get(currentLine).length();
                } else {
                    currentSymbol = commentPosition;
                }
                return getNextToken();
            case REGEX_CANDIDATE:
                endSymbol = recogniseRegexToken();
                tempCurrent = currentSymbol;
                currentSymbol = endSymbol;
                return new Token(Token.PerlTokens.REGEX, input.get(currentLine).substring(tempCurrent, endSymbol), currentLine, tempCurrent);
            case UNDEFINED:
                endSymbol = recogniseReservedToken();
                tempCurrent = currentSymbol;
                currentSymbol = endSymbol;
                return new Token(Token.getTokenMapSingleton().get(input.get(currentLine).substring(tempCurrent, endSymbol)),
                        input.get(currentLine).substring(tempCurrent, endSymbol), currentLine, tempCurrent);
        }
        return null;
    }

    Token getNextToken() {
        if (endOfInput()) {
            return new Token(Token.PerlTokens.END_OF_INPUT, "", currentLine, currentSymbol);
        }
        checkEndOfString();
        if (currentSymbol == 0) {
            //пропустить строку, если она пустая
            if (input.get(currentLine).isEmpty()) {
                currentLine++;
                currentSymbol = 0;
            } else if (input.get(currentLine).matches("\\s*=begin comment(\\s.*)*")) {
                do {
                    currentLine++;

                } while (!input.get(currentLine).matches("\\s*=end comment\\s*"));
                currentLine++;
                return getNextToken();
            }
        }
        //пропустить пробелы
        while (input.get(currentLine).charAt(currentSymbol) == ' ') {
            currentSymbol++;
            checkEndOfString();
        }
        return findMatchingPattern();
    }

    private Boolean endOfInput() {
        return currentLine > input.size() - 1 || currentLine >= input.size() - 1 && currentSymbol >= input.get(currentLine).length();
    }
}