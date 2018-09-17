import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by rozaliaamirova on 16.09.2018.
 */
public class LexemeRecognizer {
    Lexer lexer;
    HashMap<String, Token.PerlTokens> tokenMap;
    SymbolTypeRecognizer symbolTypeRecognizer;

    LexemeRecognizer() {
        tokenMap = Token.getTokenMapSingleton();
        symbolTypeRecognizer = new SymbolTypeRecognizer();
    }

    public Token recognizeIdentifier(int currentPosition, String input) {
        String identifier = "";
        int position = currentPosition;
        SymbolTypeRecognizer.SymbolType lookaheadSymbolType;
        char character;
        char lookahead;

        while (position < input.length()) {
            character = input.charAt(position);
            if (position + 1 < input.length()) {
                lookahead = input.charAt(position + 1);
                lookaheadSymbolType = symbolTypeRecognizer.recognize(lookahead);
            } else {
                lookaheadSymbolType = symbolTypeRecognizer.recognize(' ');
            }
            SymbolTypeRecognizer.SymbolType currentSymbolType = symbolTypeRecognizer.recognize(character);
            if (!(currentSymbolType == SymbolTypeRecognizer.SymbolType.CHARACTER ||
                    currentSymbolType == SymbolTypeRecognizer.SymbolType.DIGIT ||
                    (character == '-' &&
                            lookaheadSymbolType == SymbolTypeRecognizer.SymbolType.CHARACTER) ||
                    (character == '\'' &&
                            lookaheadSymbolType == SymbolTypeRecognizer.SymbolType.CHARACTER))) {
                break;
            }
            identifier += String.valueOf(character);
            position += 1;
        }
        if (tokenMap.containsKey(identifier)) {
            Token.PerlTokens type = tokenMap.get(identifier);
            return new Token(type, identifier);
        }
        return new Token(Token.PerlTokens.VARIABLE, identifier);
    }

    public Token recognizeArithmeticOperator(int currentPosition, String input) {
        return new Token(tokenMap.get(String.valueOf(input.charAt(currentPosition))), String.valueOf(input.charAt(currentPosition)));
    }

    public Token recognizeComparisonOperator(int currentPosition, String input) {
        String identifier = "";
        Token.PerlTokens lookaheadSymbolType;
        Token.PerlTokens secondLookaheadSymbolType;
        char character;
        char lookahead;
        char secondLookeahead;

        character = input.charAt(currentPosition);
        if (currentPosition + 1 < input.length()) {
            lookahead = input.charAt(currentPosition + 1);
            lookaheadSymbolType = tokenMap.get(String.valueOf(lookahead));
            if (currentPosition + 2 < input.length()) {
                secondLookeahead = input.charAt(currentPosition + 2);
                secondLookaheadSymbolType = tokenMap.get(String.valueOf(lookahead));
            } else {
                secondLookaheadSymbolType = Token.PerlTokens.ERROR;
            }
        } else {
            lookaheadSymbolType = Token.PerlTokens.ERROR;
            secondLookaheadSymbolType = Token.PerlTokens.ERROR;
        }


        if (lookaheadSymbolType == Token.PerlTokens.ASSIGNMENT) {
            if (secondLookaheadSymbolType == Token.PerlTokens.RIGHT_AND_BRACKET) {
                return new Token(Token.PerlTokens.NUMERIC_THREE_WAY_COMPARATOR, "<=>");
            }
            if (tokenMap.get(String.valueOf(character)) == Token.PerlTokens.RIGHT_AND_BRACKET) {
                return new Token(Token.PerlTokens.NUMERIC_GREATER_THAN_OR_EQUAL, ">=");
            }
            if (tokenMap.get(String.valueOf(character)) == Token.PerlTokens.LEFT_ANG_BRACKET) {
                return new Token(Token.PerlTokens.NUMERIC_LESS_THAN_OR_EQUAL, "<=");
            }
            if (tokenMap.get(String.valueOf(character)) == Token.PerlTokens.ASSIGNMENT) {
                return new Token(Token.PerlTokens.NUMERIC_EQUAL, "==");
            }
        }

        return new Token(tokenMap.get(String.valueOf(input.charAt(currentPosition))), String.valueOf(input.charAt(currentPosition)));
    }

}
