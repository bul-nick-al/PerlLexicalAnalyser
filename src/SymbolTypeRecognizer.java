/**
 * Created by rozaliaamirova on 16.09.2018.
 */
public class SymbolTypeRecognizer {

    public enum SymbolType {
        DIGIT, CHARACTER, ARITHMETIC_OPERATION, COMPARISON_OPERATION, APOSTROPHE, DASH, UNDEFINED
    }

    public SymbolType recognize(char input) {
        if (input == '+' || input == '-' || input == '*' || input == '/') {
            return SymbolType.ARITHMETIC_OPERATION;
        }

        if (input == '>' || input == '<' || input == '=') {
            return SymbolType.COMPARISON_OPERATION;
        }

        if ((input >= 'a' && input <= 'z') || (input >= 'A' && input <= 'Z') || input == '_') {
            return SymbolType.CHARACTER;
        }

        if (input >= '1' && input <= '9') {
            return SymbolType.DIGIT;
        }

        if (input == '\'') {
            return SymbolType.APOSTROPHE;
        }

        if (input == '-') {
            return SymbolType.DASH;
        }

        return SymbolType.UNDEFINED;
    }
}
