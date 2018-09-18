/**
 * Created by rozaliaamirova on 16.09.2018.
 */
public class SymbolTypeRecognizer {

    public enum SymbolType {
        STRING_CANDIDATE, NUMBER_CANDIDATE, WORD_CANDIDATE, COMMENT_CANDIDATE, REGEX_CANDIDATE, UNDEFINED
    }

    public static SymbolType recognize(char input) {
        if (input == '\'' || input == '"')
            return SymbolType.STRING_CANDIDATE;
        if (String.valueOf(input).matches("\\d"))
            return SymbolType.NUMBER_CANDIDATE;
        if (String.valueOf(input).matches("[A-Za-z]"))
            return SymbolType.WORD_CANDIDATE;
        if (input == '#')
            return SymbolType.COMMENT_CANDIDATE;
        if (input == '/')
            return SymbolType.REGEX_CANDIDATE;
        return SymbolType.UNDEFINED;
    }
}
