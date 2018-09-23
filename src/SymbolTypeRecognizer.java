/**
 * Class which checks whether symbol can be the beginning of some types of lexical units
 */
public class SymbolTypeRecognizer {

    /**
     * Defines symbol types as beginnings of lexical units
     */
    enum SymbolType {
        STRING_CANDIDATE, NUMBER_CANDIDATE, WORD_CANDIDATE, COMMENT_CANDIDATE, REGEX_CANDIDATE, UNDEFINED
    }

    /**
     * Method which recognize symbol as a start of lexical unit
     * @param input symbol which needs to be recognized
     * @return type of the lexical unit which can begin with given input char
     */
    static SymbolType recognize(char input) {
        if (input == '\'' || input == '"')
            return SymbolType.STRING_CANDIDATE;
        if (String.valueOf(input).matches("\\d"))
            return SymbolType.NUMBER_CANDIDATE;
        if (String.valueOf(input).matches("[A-Za-z]|_"))
            return SymbolType.WORD_CANDIDATE;
        if (input == '#')
            return SymbolType.COMMENT_CANDIDATE;
        if (input == '/')
            return SymbolType.REGEX_CANDIDATE;
        return SymbolType.UNDEFINED;
    }
}
