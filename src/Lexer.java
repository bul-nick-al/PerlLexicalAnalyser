import java.util.Arrays;
import java.util.LinkedList;

/**
 * This class is responsible for tokinisation of Perl6 code
 */
public class Lexer {

    private LinkedList<String> input;
    private int currentLine;
    private int currentSymbol;

    public Lexer(LinkedList<String> input) {
        this.input = input;
        currentLine = 0;
        currentSymbol = 0;
    }

    /**
     * Finds next token
     * @return next token
     */

    Token getNextToken() {
        //if there are no more literals, report that tokenisation is finished
        if (endOfInput())
            return new Token(Token.PerlTokens.END_OF_INPUT, "", currentLine, currentSymbol);
        checkForEmptyLinesAndSpaces();//skip spaces and emplty lines
        if (isMultilineComment()) //skip multiline comments
            return getNextToken();
        return findMatchingPattern();
    }

    /**
     * Finds to which patter the current literal corresponds and creates the corresponding token
     * @return new Token 
     */
    private Token findMatchingPattern() {
        char character = getCharacter(currentSymbol);
        SymbolTypeRecognizer.SymbolType type = SymbolTypeRecognizer.recognize(character);
        switch (type) {
            case STRING_CANDIDATE:
                return tokeniseStringCandidate();
            case NUMBER_CANDIDATE:
                return tokeniseNumberCandidate();
            case WORD_CANDIDATE:
                return tokeniseWordCandidate();
            case COMMENT_CANDIDATE:
                return tokeniseCommentCandidate();
            case REGEX_CANDIDATE:
                return tokeniseRegexCandidate();
            case UNDEFINED:
                return tokeniseReservedCandidate();
        }
        return null;
    }

    /**
     * creates a token for a word candidate
     * @return a token for a word candidate
     */
    private Token tokeniseWordCandidate(){
        //if it is a word candidate, there are four options for what it can be. We find the longest matching
        // pattern of those and return the corresponding token.
        int tempCurrent;
        int namedRegexPosition = recogniseNamedRegexToken(), identifierPosition = recogniseIdentifierToken(),
                reservedPosition = recogniseReservedToken(), regexPosition = recogniseRegexToken();
        int[] positions = {identifierPosition, reservedPosition, regexPosition, namedRegexPosition};
        int longestMatchingPosition = Arrays.stream(positions).max().getAsInt();
        if (longestMatchingPosition == namedRegexPosition) {
            return getNamedRegexToken();
        }
        if (longestMatchingPosition == reservedPosition) {
            tempCurrent = currentSymbol;
            currentSymbol = reservedPosition;
            return new Token(Token.getTokenMapSingleton().get(getSubstring(tempCurrent, reservedPosition)),
                    getSubstring(tempCurrent, reservedPosition), currentLine, tempCurrent);
        }
        if (longestMatchingPosition == identifierPosition) {
            tempCurrent = currentSymbol;
            currentSymbol = identifierPosition;
            return new Token(Token.PerlTokens.IDENTIFIER,
                    getSubstring(tempCurrent, identifierPosition), currentLine, tempCurrent);
        }
        if (longestMatchingPosition == regexPosition) {
            tempCurrent = currentSymbol;
            currentSymbol = regexPosition;
            return new Token(Token.PerlTokens.REGEX,
                    getSubstring(tempCurrent, regexPosition), currentLine, tempCurrent);
        }
        return null;
    }
    /**
     * creates a token for a number candidate
     * @return a token for a number candidate
     */
    private Token tokeniseNumberCandidate(){
        int endSymbol;
        int tempCurrent;
        endSymbol = recogniseNumberToken();
        tempCurrent = currentSymbol;
        currentSymbol = endSymbol;
        return new Token(Token.PerlTokens.NUMBER,
                getSubstring(tempCurrent, endSymbol), currentLine, tempCurrent);
    }
    /**
     * creates a token for a string candidate
     * @return a token for a string candidate
     */
    private Token tokeniseStringCandidate(){
        int endSymbol;
        int tempCurrent;
        if ((endSymbol = recogniseStringToken()) != -1) {
            tempCurrent = currentSymbol;
            currentSymbol = endSymbol;
            return new Token(Token.PerlTokens.STRING,
                    getSubstring(tempCurrent, endSymbol), currentLine, tempCurrent);
        }
        return new Token(Token.PerlTokens.ERROR,
                "string literal has no closing quotes", currentLine, currentSymbol);
    }
    /**
     * creates a token for a comment candidate
     * @return a token for a comment candidate
     */
    private Token tokeniseCommentCandidate(){
        int commentPosition = recogniseEmbeddedToken();
        currentSymbol = commentPosition == -1 ? input.get(currentLine).length() : commentPosition;
        return getNextToken();
    }
    /**
     * creates a token for a regex candidate
     * @return a token for a regex candidate
     */
    private Token tokeniseRegexCandidate(){
        int endSymbol;
        int tempCurrent;
        endSymbol = recogniseRegexToken();
        tempCurrent = currentSymbol;
        currentSymbol = endSymbol;
        return new Token(Token.PerlTokens.REGEX, getSubstring(tempCurrent, endSymbol), currentLine, tempCurrent);
    }
    /**
     * creates a token for a reserved word candidate
     * @return a token for a reserved word candidate
     */
    private Token tokeniseReservedCandidate(){
        int endSymbol;
        int tempCurrent;
        endSymbol = recogniseReservedToken();
        tempCurrent = currentSymbol;
        currentSymbol = endSymbol;
        try {
            return new Token(Token.getTokenMapSingleton().get(getSubstring(tempCurrent, endSymbol)),
                    getSubstring(tempCurrent, endSymbol), currentLine, tempCurrent);
        } catch (Exception e){
            return (new Token(Token.PerlTokens.ERROR,
                    "An error occurred while tokenisation", currentLine, currentSymbol));
        }
    }

    /**
     * finds longest possible number relative to the current position
     * @return the position of the end of the number
     */
    private int recogniseNumberToken() {
        int endSymbol = currentSymbol + 1;
        int longestMatchingPosition = 0;
        while (endSymbol <= input.get(currentLine).length()) {
            if (PatternsRecogniser.isNumber(getSubstring(currentSymbol, endSymbol)))
                longestMatchingPosition = endSymbol;
            endSymbol++;
        }
        return longestMatchingPosition;
    }

    /**
     * finds the possible string relative to the current position
     * @return the position of the end of the string
     */
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

    /**
     * finds longest possible identifier relative to the current position
     * @return the position of the end of the identifier
     */
    private int recogniseIdentifierToken() {
        int endSymbol = currentSymbol;
        int longestMatchingPosition = 0;
        while (endSymbol < input.get(currentLine).length()) {
            if (PatternsRecogniser.isIdentifier(getSubstring(currentSymbol, endSymbol + 1)))
                longestMatchingPosition = endSymbol + 1;
            endSymbol++;
        }
        return longestMatchingPosition;
    }
    /**
     * finds longest possible number relative to the current position
     * @return the position of the end of the number
     */
    private int recogniseRegexToken() {
        int endSymbol = currentSymbol;
        int longestMatchingPosition = 0;
        while (endSymbol < input.get(currentLine).length()) {
            if (PatternsRecogniser.isRegex(getSubstring(currentSymbol, endSymbol + 1)))
                longestMatchingPosition = endSymbol + 1;
            endSymbol++;
        }
        return longestMatchingPosition;
    }

    /**
     * finds longest possible regex relative to the current position
     * @return the position of the end of the number
     */
    private int recogniseNamedRegexToken() {
        int longestMatchingPosition = 0;
        if (PatternsRecogniser.isNamedRegex(input.get(currentLine))) {
            longestMatchingPosition = input.get(currentLine).length();
        }
        return longestMatchingPosition;
    }

    /**
     * finds longest possible reserved word relative to the current position
     * @return the position of the reserved word
     */
    private int recogniseReservedToken() {
        int endSymbol = currentSymbol;
        int longestMatchingPosition = 0;
        while (endSymbol < input.get(currentLine).length()) {
            if (Token.getTokenMapSingleton().containsKey(getSubstring(currentSymbol, endSymbol + 1)))
                longestMatchingPosition = endSymbol + 1;
            endSymbol++;
        }
        return longestMatchingPosition;
    }

    /**
     * finds longest possible embedded token relative to the current position
     * @return the position of the embedded token
     */
    private int recogniseEmbeddedToken() {
        int endSymbol = currentSymbol;
        do {
            endSymbol++;
        }
        while (endSymbol < input.get(currentLine).length()
                && !PatternsRecogniser.isEmbeddedComment(getSubstring(currentSymbol, endSymbol)));

        if (endSymbol == input.get(currentLine).length()
                && !PatternsRecogniser.isEmbeddedComment(getSubstring(currentSymbol, endSymbol))) {
            return -1;
        }
        return endSymbol;
    }


    /**
     * finds longest possible named regex relative to the current position
     * @return named regex token
     */
    private Token getNamedRegexToken() {
        String result = getNamedRegexWithEmbeddedParenthesis();
        currentLine++;
        currentSymbol = 0;
        return new Token(Token.PerlTokens.REGEX, result, currentLine, currentSymbol);
    }

    /**
     * finds longest possible named regex relative to the current position
     * @return the position of the end of the number
     */
    private String getNamedRegexWithEmbeddedParenthesis() {
        boolean isAngleParenthesis = false;
        String currentSubstring;
        currentSubstring = "";
        while (true) {
            checkEndOfString();
            //all the manipulation below are related to regex specifics in Perl6
            if (getCharacter(currentSymbol) == '<')
                isAngleParenthesis = true;
            if (getCharacter(currentSymbol) == '>')
                isAngleParenthesis = false;
            if (getCharacter(currentSymbol) == '{' && !isAngleParenthesis) {
                int tempLine = currentLine;
                int tempSymbol = currentSymbol;
                currentSymbol++;
                findInnerEmbeddings("{");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(input.get(tempLine).substring(tempSymbol));
                for (int i = tempLine + 1; i < currentLine; i++)
                    stringBuilder.append(input.get(i));
                if (tempLine < currentLine)
                    stringBuilder.append(getSubstring(0, currentSymbol));
                return currentSubstring + stringBuilder.toString();
            } else {
                currentSubstring += String.valueOf(getCharacter(currentSymbol));
                currentSymbol++;
            }
        }
    }

    /**
     * adjusts currentSymbol and currentLine to the end of embedded regex.
     * @param openedParenthesis contains the opening sybmol
     */
    private void findInnerEmbeddings(String openedParenthesis) {
        switch (openedParenthesis) {
            case "'":
            case "\"":
                currentSymbol = recogniseStringToken();
                return;
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
                        findInnerEmbeddings("'");
                        continue;
                    }
                    if (getSubstring(currentSymbol + 1).equals("\"") && isNoEscape()) {
                        findInnerEmbeddings("\"");
                        continue;
                    }
                    if (getSubstring(currentSymbol + 1).equals("}") && isNoEscape()) {
                        currentSymbol++;
                        return;
                    }
                    if (getSubstring(currentSymbol + 1).equals("{") && isNoEscape()) {
                        currentSymbol++;
                        findInnerEmbeddings("{");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 1).equals("[") && isNoEscape()) {
                        currentSymbol++;
                        findInnerEmbeddings("[");
                        currentSymbol--;
                    }

                    if (getSubstring(currentSymbol + 2).equals("<(") && isNoEscape()) {
                        currentSymbol+=2;
                        findInnerEmbeddings("<(");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 3).equals("<?(") && isNoEscape()) {
                        currentSymbol+=3;
                        findInnerEmbeddings("<?(");
                        currentSymbol--;
                    }

                    if (getSubstring(currentSymbol + 1).equals("<") && isNoEscape()) {
                        int j = currentSymbol;
                        String currentSubstring = "";
                        while (j < input.get(currentLine).length() && !currentSubstring.matches("<.*\\[.*]>")) {
                            currentSubstring += getCharacter(j);
                            j++;
                        }
                        if (currentSubstring.matches("<.*\\[.*]>")) {
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
                        findInnerEmbeddings("'");
                        continue;
                    }
                    if (getSubstring(currentSymbol + 1).equals("\"") && isNoEscape()) {
                        findInnerEmbeddings("\"");
                        continue;
                    }
                    if (getSubstring(currentSymbol + 1).equals("]") && isNoEscape()) {
                        currentSymbol++;
                        return;
                    }
                    if (getSubstring(currentSymbol + 1).equals("{") && isNoEscape()) {
                        currentSymbol++;
                        findInnerEmbeddings("{");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 1).equals("[") && isNoEscape()) {
                        currentSymbol++;
                        findInnerEmbeddings("[");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 2).equals("<(") && isNoEscape()) {
                        currentSymbol+=2;
                        findInnerEmbeddings("<(");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 3).equals("<?(") && isNoEscape()) {
                        currentSymbol+=3;
                        findInnerEmbeddings("<?(");
                        currentSymbol--;
                    }
                    if (getSubstring(currentSymbol + 1).equals("<") && isNoEscape()) {
                        int j = currentSymbol;
                        String currentSubstring = "";
                        while (j < input.get(currentLine).length() && !currentSubstring.matches("<.*\\[.*]>")) {
                            currentSubstring += getCharacter(j);
                            j++;
                        }
                        if (currentSubstring.matches("<.*\\[.*]>")) {
                            currentSymbol += currentSubstring.length();
                            return;
                        }
                    }
                    currentSymbol++;
                }
        }
    }


    /* ********************* These are some auxiliary methods used for tokenisation ***********************************/

    /**
     * ensures that there is no escape symbol before some symbol
     * @return true if there is no escape symbol before some symbol, false otherwise
     */
    private boolean isNoEscape() {
        int number_of_slashes = 0;
        int i = 0;
        while (getSubstring(currentSymbol - 1 - i, currentSymbol - i).equals("\\")) {
            number_of_slashes++;
            i++;
        }
        return number_of_slashes % 2 == 0;
    }

    /**
     * Checks if there are empty lines or spaces, and if yes, skips them.
     */
    private void checkForEmptyLinesAndSpaces(){
        checkEndOfString();
        if (input.get(currentLine).isEmpty()) {
            currentLine++;
            currentSymbol = 0;
            return;
        }
        while (getCharacter(currentSymbol) == ' ') {
            currentSymbol++;
            checkEndOfString();
        }
    }

    /**
     * @return true if the current part of code is a multiline comment, false otherwise
     */
    private boolean isMultilineComment(){
        if ((currentSymbol == 0) && PatternsRecogniser.isStartOfMultilineComment(input.get(currentLine))) {
            do {
                currentLine++;
            } while (!PatternsRecogniser.isEndOfMultilineComment(input.get(currentLine)));
            currentLine++;
            return true;
        }
        return false;
    }

    /**
     * checks if current symbol is at the end of string and adjusts the position to the next line
     */
    private void checkEndOfString() {
        if (currentSymbol >= input.get(currentLine).length()) {
            currentLine++;
            currentSymbol = 0;
            checkEndOfString();
        }
    }

    /**
     * @param endSymbol the position of the last symbol + 1
     * @return the substring of the string at current line in rage from the current symbol till endSymbol
     */
    private String getSubstring(int endSymbol) {
        try {
            return input.get(currentLine).substring(currentSymbol, endSymbol);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @param startSymbol the position of the first symbol
     * @param endSymbol the position of the last symbol + 1
     * @return the substring of the string at current line in rage from startSymbol till endSymbol
     */
    private String getSubstring(int startSymbol, int endSymbol) {
        try {
            return input.get(currentLine).substring(startSymbol, endSymbol);
        } catch (Exception e) {
            return "";
        }
    }

    private Character getCharacter(int index){
        return input.get(currentLine).charAt(index);
    }

    /**
     * check if there are no more literals for tokenisation
     * @return true if there are no more literals for tokenisation, false otherwise
     */
    private boolean endOfInput() {
        return currentLine > input.size() - 1 
                || currentLine >= input.size() - 1 && currentSymbol >= input.get(currentLine).length();
    }
}