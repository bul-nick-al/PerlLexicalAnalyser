import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by rozaliaamirova on 14.09.2018.
 */
public class Lexer {

    boolean x = false;

    LinkedList<String> input;
    int currentLine;
    int currentSymbol;
    SymbolTypeRecognizer symbolTypeRecognizer;

    public Lexer(LinkedList<String> input) {
        this.input = input;
        currentLine = 0;
        currentSymbol = 0;
        symbolTypeRecognizer = new SymbolTypeRecognizer();
    }

    private void checkEndOfString(){
        if (currentSymbol >= input.get(currentLine).length()) {
            currentLine++;
            currentSymbol = 0;
            checkEndOfString();
        }
    }

    private String getSubstring(int endSymbol){
        try{
            if (x)
                System.out.println(input.get(currentLine).substring(currentSymbol, endSymbol));
            return input.get(currentLine).substring(currentSymbol, endSymbol);
        } catch (Exception e){
//            System.out.println("FAIL");
            return "";
        }
    }
    private String getSubstring(int startSymbol, int endSymbol){
        try{
            return input.get(currentLine).substring(startSymbol, endSymbol);
        } catch (Exception e){
            return "";
        }
    }

    private int recogniseNumberToken() {
        int endSymbol = currentSymbol + 1;
        int longestMatchingPosition = 0;
        while (endSymbol <= input.get(currentLine).length()){
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
        } catch (Exception e){
            return -1;
        }
    }

    private int recogniseIdentifierToken() {
        int endSymbol = currentSymbol;
        int longestMatchingPosition = 0;
        while (endSymbol < input.get(currentLine).length()){
            if (PatternsRecogniser.isIdentifier(input.get(currentLine).substring(currentSymbol, endSymbol+1)))
                longestMatchingPosition = endSymbol+1;
            endSymbol++;
        }
        return longestMatchingPosition;
    }

    private int recogniseRegexToken() {
        int endSymbol = currentSymbol;
        int longestMatchingPosition = 0;
        while (endSymbol < input.get(currentLine).length()){
            if (PatternsRecogniser.isRegex(input.get(currentLine).substring(currentSymbol, endSymbol+1)))
                longestMatchingPosition = endSymbol+1;
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
        String result = getnamedRegexWithEmbeddedParanthesis();
        currentLine++;
        currentSymbol = 0;
        return new Token(Token.PerlTokens.REGEX, result);
    }

    //ищем тело регулярного выражения, заключенное в { }
    private String getnamedRegexWithEmbeddedParanthesis() {
        boolean isAnglesParethesis = false;
        String currentSubstring = "";
        String result = "";
        currentSubstring = "";
//        currentSymbol = 0;
//        input.set(currentLine, input.get(currentLine).replaceAll("  ", ""));
        while (true) {
            checkEndOfString();
            if (currentSubstring.contains("method_def")){
                System.out.println("AAAAAAAAAA");
                x = false;
            }
            if (input.get(currentLine).charAt(currentSymbol) == '<') {
                isAnglesParethesis = true;
            }
            if (input.get(currentLine).charAt(currentSymbol) == '>') {
                isAnglesParethesis = false;
            }
            if (input.get(currentLine).charAt(currentSymbol) == '{'&&!isAnglesParethesis) {
                int tempLine = currentLine;
                int tempSymbol = currentSymbol;
                currentSymbol++;
                getInnerRecursionEmbeddings("{");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(input.get(tempLine).substring(tempSymbol, input.get(tempLine).length()));
                for (int i = tempLine+1; i < currentLine; i++) {
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

    public boolean isNoEscape(){
        int number_of_slashes = 0;
        int i = 0;
        while (getSubstring(currentSymbol-1-i, currentSymbol-i).equals("\\")){
            number_of_slashes++;
            i++;
        }
        return number_of_slashes%2 == 0;
    }

    private void getInnerRecursionEmbeddings(String openedParenthesis) {
        switch (openedParenthesis) {
            case "'":
            case "\"":
                int endSymbol = recogniseStringToken();
                currentSymbol = endSymbol;
                return;
            case "<[":
            case "<?[":
                while (true) {
                    checkEndOfString();
                    if (getSubstring(currentSymbol + 2).equals("]>") && isNoEscape()) {
                        currentSymbol++;
                        currentSymbol++;
                        return;
                    }
                    currentSymbol++;
                }
            case "<(":
            case "<?(":
                while (true) {
                    checkEndOfString();
                    if (getSubstring(currentSymbol + 2).equals(")>") && isNoEscape()) {
                        currentSymbol++;
                        currentSymbol++;
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
                    if (getSubstring(currentSymbol + 2).equals("<[") && isNoEscape()) {
                        currentSymbol++;
                        currentSymbol++;
                        getInnerRecursionEmbeddings("<[");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 3).matches("<.\\[") && isNoEscape()) {
                        currentSymbol++;
                        currentSymbol++;
                        currentSymbol++;
                        getInnerRecursionEmbeddings("<?[");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 2).equals("<(") && isNoEscape()) {
                        currentSymbol++;
                        currentSymbol++;
                        getInnerRecursionEmbeddings("<(");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 3).equals("<?(") && isNoEscape()) {
                        currentSymbol++;
                        currentSymbol++;
                        currentSymbol++;
                        getInnerRecursionEmbeddings("<?(");
                        currentSymbol--;
                    }
                   if (getSubstring(currentSymbol + 1).equals("<") && isNoEscape()) {
                        currentSymbol++;
                        getInnerRecursionEmbeddings("<");
                       currentSymbol--;
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
                    if (getSubstring(currentSymbol + 2).equals("<[") && isNoEscape()) {
                        currentSymbol++;
                        currentSymbol++;
                        getInnerRecursionEmbeddings("<[");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 3).matches("<.\\[") && isNoEscape()) {
                        currentSymbol++;
                        currentSymbol++;
                        currentSymbol++;
                        getInnerRecursionEmbeddings("<?[");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 2).equals("<(") && isNoEscape()) {
                        currentSymbol++;
                        currentSymbol++;
                        getInnerRecursionEmbeddings("<(");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 3).equals("<?(") && isNoEscape()) {
                        currentSymbol++;
                        currentSymbol++;
                        currentSymbol++;
                        getInnerRecursionEmbeddings("<?(");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 1).equals("<") && isNoEscape()) {
                        currentSymbol++;
                        getInnerRecursionEmbeddings("<");
                        currentSymbol--;
                    }
                    currentSymbol++;
                }
            case "<":

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
                    if (getSubstring(currentSymbol + 1).equals(">") && isNoEscape()) {
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
                    if (getSubstring(currentSymbol + 2).equals("<[") && isNoEscape()) {
                        currentSymbol++;
                        currentSymbol++;
                        getInnerRecursionEmbeddings("<[");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 3).matches("<.\\[") && isNoEscape()) {
                        currentSymbol++;
                        currentSymbol++;
                        currentSymbol++;
                        getInnerRecursionEmbeddings("<?[");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 2).equals("<(") && isNoEscape()) {
                        currentSymbol++;
                        currentSymbol++;
                        getInnerRecursionEmbeddings("<(");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 3).equals("<?(") && isNoEscape()) {
                        currentSymbol++;
                        currentSymbol++;
                        currentSymbol++;
                        getInnerRecursionEmbeddings("<?(");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 1).equals("<") && isNoEscape()) {
                        currentSymbol++;
                        getInnerRecursionEmbeddings("<");
                        currentSymbol--;
                    }
                    currentSymbol++;
                }
        }
    }


    //разрешаем внутренние вложенные скобки : [ ] и { }. Скобки < > не совпадают в этом языке
//    private String getInnerRecursionEmbeddings(int symbol, char openedParanthesis) {
//        String currentSubstring = "";
//        String result = "";
//        currentSubstring = "";
//        currentSymbol = symbol;
//        while (currentLine < input.size()) {
//            input.set(currentLine, input.get(currentLine).replaceAll("  ", ""));
//
//            for (int i = currentSymbol; i < input.get(currentLine).length(); i++) {
//
//                switch (input.get(currentLine).charAt(i)){
//                    case '\'':
//                        int j = i;
//                        String substring = "";
//                        while (j < input.get(currentLine).length()) {
//                            substring += String.valueOf(input.get(currentLine).charAt(j));
//                            if (PatternsRecogniser.isString(substring)) {
//                                i += substring.length() - 1;
//                                currentSubstring += substring;
//                                break;
//                            }
//                            j++;
//                        }
//                        break;
//                    case '"':
//                        j = i;
//                        substring = "";
//                        while (j < input.get(currentLine).length()) {
//                            substring += String.valueOf(input.get(currentLine).charAt(j));
//                            if (PatternsRecogniser.isString(substring)) {
//                                i += substring.length() - 1;
//                                currentSubstring += substring;
//                                break;
//                            }
//                            j++;
//                        }
//                        break;
//                    case '\\':
//                        currentSubstring += "\\";
//                        i++;
//                        currentSubstring += String.valueOf(input.get(currentLine).charAt(i));
//                        break;
//                    case '{':
//                        result = "{";
//                        result += getInnerRecursionEmbeddings(i+1, '{');
//                        currentSubstring += result;
//                        i += result.length() - 1;
//                        break;
//                    case '}':
//                        if (openedParanthesis == '{') {
//                            currentSubstring += "}";
//                            return currentSubstring;
//                        } else {
//                            currentSubstring += "}";
//                            break;
//                        }
//                    case '[':
//                        result = "[";
//                        result += getInnerRecursionEmbeddings(i+1, '[');
//                        currentSubstring += result;
//                        i += result.length() - 1;
//                        break;
//                    case ']':
//                        currentSubstring += "]";
//                        return currentSubstring;
//                    default:
//                        currentSubstring += String.valueOf(input.get(currentLine).charAt(i));
//                }
//            }
//            currentLine++;
//            currentSymbol = 0;
//        }
//        return currentSubstring;
//    }

    private int recogniseReservedToken() {
        int endSymbol = currentSymbol ;
        int longestMatchingPosition = 0;
        while (endSymbol < input.get(currentLine).length()){
            if (Token.getTokenMapSingleton().containsKey(input.get(currentLine).substring(currentSymbol, endSymbol+1)))
                longestMatchingPosition = endSymbol+1;
            endSymbol++;
        }
        return longestMatchingPosition;
    }

    private int recogniseEmbeddedToken() {
        int endSymbol = currentSymbol;
        do {
            endSymbol++;
        } while (endSymbol < input.get(currentLine).length()&&!PatternsRecogniser.isEmbeddedComment(input.get(currentLine).substring(currentSymbol, endSymbol)));

        if (endSymbol == input.get(currentLine).length()&&!PatternsRecogniser.isEmbeddedComment(input.get(currentLine).substring(currentSymbol, endSymbol))){
            return -1;
        }
        return endSymbol;
    }

    private Token findMatchingPattern(){
        char character = input.get(currentLine).charAt(currentSymbol);
        SymbolTypeRecognizer.SymbolType type = SymbolTypeRecognizer.recognize(character);
        int endSymbol;
        int tempCurrent;
        switch (type){

            case STRING_CANDIDATE:
                if ((endSymbol = recogniseStringToken()) != -1){
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
                if (longestMatchingPosition == namedRegexPosition){
                    return getNamedRegexToken();
                }
                if (longestMatchingPosition == reservedPosition){
                    tempCurrent = currentSymbol;
                    currentSymbol = reservedPosition;
                    return new Token(Token.getTokenMapSingleton().get(input.get(currentLine).substring(tempCurrent, reservedPosition)),
                            input.get(currentLine).substring(tempCurrent, reservedPosition), currentLine, tempCurrent);
                }
                if (longestMatchingPosition == identifierPosition){
                    tempCurrent = currentSymbol;
                    currentSymbol = identifierPosition;
                    return new Token(Token.PerlTokens.IDENTIFIER, input.get(currentLine).substring(tempCurrent, identifierPosition), currentLine, tempCurrent);
                }
                if (longestMatchingPosition == regexPosition){
                    tempCurrent = currentSymbol;
                    currentSymbol = regexPosition;
                    return new Token(Token.PerlTokens.REGEX, input.get(currentLine).substring(tempCurrent, regexPosition), currentLine, tempCurrent);
                }

            case COMMENT_CANDIDATE:
                int commentPosition = recogniseEmbeddedToken();
                if (commentPosition == -1){
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


    public Token getNextToken() {



        //System.out.println(currentSymbol);
        if (endOfInput()) {
            return new Token(Token.PerlTokens.END_OF_INPUT, "", currentLine, currentSymbol);
        }

        checkEndOfString();


        if (currentSymbol == 0) {
            //пропустить строку, если она пустая
            if (input.get(currentLine).isEmpty()) {
                currentLine++;
                currentSymbol = 0;
            }
            else if (input.get(currentLine).matches("\\s*=begin comment(\\s.*)*")) {
                do {
                    currentLine++;

                } while (!input.get(currentLine).matches("\\s*=end comment\\s*"));
                currentLine++;
                return getNextToken();
            }
        }

//        checkComment(String.valueOf(input.get(currentLine).charAt(currentSymbol)));


        //пропустить пробелы

        while (input.get(currentLine).charAt(currentSymbol) == ' ') {
            currentSymbol++;
            checkEndOfString();
        }
        int endSymbol = 0;
        return findMatchingPattern();


//        int endSymbol = currentSymbol + 1;
//        while (endSymbol <= input.get(currentLine).length()&&
//                PatternsRecogniser.isMatchingAnyPattern(input.get(currentLine).substring(currentSymbol, endSymbol))){
//            endSymbol++;
//        }
//        endSymbol--;
//        if (PatternsRecogniser.isReserved(input.get(currentLine).substring(currentSymbol, endSymbol))){
//            System.out.println("RESER");
//            System.out.println(input.get(currentLine).substring(currentSymbol, endSymbol));
//            currentSymbol = endSymbol;
//            return new Token(Token.PerlTokens.ERROR, "", currentLine, currentSymbol);
//        }
//        else if (PatternsRecogniser.isIdentifier(input.get(currentLine).substring(currentSymbol, endSymbol))){
//            System.out.println("ID");
//            System.out.println(input.get(currentLine).substring(currentSymbol, endSymbol));
//            currentSymbol = endSymbol;
//            return new Token(Token.PerlTokens.ERROR, "", currentLine, currentSymbol);
//        }
//        else if (PatternsRecogniser.isRegex(input.get(currentLine).substring(currentSymbol, endSymbol))){
//            System.out.println("REG");
//            System.out.println(input.get(currentLine).substring(currentSymbol, endSymbol));
//            currentSymbol = endSymbol;
//            return new Token(Token.PerlTokens.ERROR, "", currentLine, currentSymbol);
//        }
//        else if (PatternsRecogniser.isEmbeddedComment(input.get(currentLine).substring(currentSymbol, endSymbol))){
//            System.out.println("EMBB");
//            System.out.println(input.get(currentLine).substring(currentSymbol, endSymbol));
//            currentSymbol = endSymbol;
//            return new Token(Token.PerlTokens.ERROR, "", currentLine, currentSymbol);
//        }
//        else if (PatternsRecogniser.isNumber(input.get(currentLine).substring(currentSymbol, endSymbol))){
//            System.out.println("NUM");
//            System.out.println(input.get(currentLine).substring(currentSymbol, endSymbol));
//            currentSymbol = endSymbol;
//            return new Token(Token.PerlTokens.ERROR, "", currentLine, currentSymbol);
//        }
//        else if (PatternsRecogniser.isString(input.get(currentLine).substring(currentSymbol, endSymbol))){
//            System.out.println("STR");
//            System.out.println(input.get(currentLine).substring(currentSymbol, endSymbol));
//            currentSymbol = endSymbol;
//            return new Token(Token.PerlTokens.ERROR, "", currentLine, currentSymbol);
//        }
//        else if (PatternsRecogniser.isSingleComment(input.get(currentLine).substring(currentSymbol, endSymbol))){
//            System.out.println("SINGCOM");
//            System.out.println(input.get(currentLine).substring(currentSymbol, endSymbol));
//            currentSymbol = endSymbol;
//            return new Token(Token.PerlTokens.ERROR, "", currentLine, currentSymbol);
//        }
//
//        char character = input.get(currentLine).charAt(currentSymbol);
//
//        Token result;
        //В зависимости от первого символа токана распознать токен
//        switch (symbolTypeRecognizer.recognize(character)) {
//            case DIGIT:
//                break;
//            case CHARACTER:
//                result = lexemeRecognizer.recognizeIdentifier(currentSymbol, input.get(currentLine));
//                currentSymbol += result.value.length();
//                return result;
//            case ARITHMETIC_OPERATION:
//                result = lexemeRecognizer.recognizeArithmeticOperator(currentSymbol, input.get(currentLine));
//                currentSymbol += result.value.length();
//                return result;
//            case COMPARISON_OPERATION:
//                result = lexemeRecognizer.recognizeTokenStartingWithComparison(currentSymbol, input.get(currentLine));
//                currentSymbol += result.value.length();
//                return result;
//            case UNDEFINED:
//                break;
//        }

//        return new Token(Token.PerlTokens.ERROR, "", currentLine, currentSymbol);
    }

    public Boolean endOfInput() {

        if (currentLine > input.size() - 1||currentLine >= input.size() - 1 && currentSymbol >= input.get(currentLine).length()) {
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