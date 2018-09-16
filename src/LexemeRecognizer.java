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
            if (position + 1 < input.length() - 1) {
                lookahead = input.charAt(position + 1);
                lookaheadSymbolType = symbolTypeRecognizer.recognize(lookahead);
            } else {
                lookaheadSymbolType = symbolTypeRecognizer.recognize(' ');
            }

            SymbolTypeRecognizer.SymbolType currentSymbolType = symbolTypeRecognizer.recognize(character);


            if (!(currentSymbolType == SymbolTypeRecognizer.SymbolType.CHARACTER ||
                    currentSymbolType == SymbolTypeRecognizer.SymbolType.APOSTROPHE ||
                    (currentSymbolType == SymbolTypeRecognizer.SymbolType.DASH &&
                            lookaheadSymbolType == SymbolTypeRecognizer.SymbolType.CHARACTER) ||
                    (currentSymbolType == SymbolTypeRecognizer.SymbolType.DIGIT &&
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
}
