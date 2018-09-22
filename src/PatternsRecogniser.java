public class PatternsRecogniser {

    private static final String STRING_REGEX = "'(.*([^\\\\]|\\\\\\\\))??'|\"(.*([^\\\\]|\\\\\\\\))??\"";
    private static final String NUMBER_REGEX = "[+\\-]??\\d+|0b[01]+|0x\\d+|[+\\-]??\\d+(.\\d+)??([eE][+\\-]??\\d+)??";
    private static final String REGEX_REGEX = "/([^/]|\\\\/)*/|m/([^/]|\\\\/)*/|rx/([^/]|\\\\/)*/|s/([^/]|\\\\/)*/([^/]|\\\\/)*/";
    private static final String NAMED_REGEX_REGEX = ".*regex.*|.*token.*|.*rule.*";
    private static final String IDENTIFIER_REGEX = "([_a-zA-Z]|[a-zA-Z]'|[a-zA-Z]-)(([a-zA-Z\\d_]|[a-zA-Z]'|[a-zA-Z]-)*[_a-zA-Z\\d])??";
    private static final String SINGLE_COMMENT_REGEX = "#.*";
    private static final String EMBEDDED_COMMENT_REGEX = "#`\\([^)]*\\)";

    static boolean isString(String string){
        return string.matches(STRING_REGEX);
    }
    static boolean isNumber(String string){
        return string.matches(NUMBER_REGEX);
    }
    static boolean isRegex(String string){
        return string.matches(REGEX_REGEX);
    }
    public static boolean isSingleComment(String string){
        return string.matches(SINGLE_COMMENT_REGEX);
    }
    static boolean isEmbeddedComment(String string){
        return string.matches(EMBEDDED_COMMENT_REGEX);
    }
    static boolean isIdentifier(String string){
        return string.matches(IDENTIFIER_REGEX);
    }
    public static boolean isReserved(String string){
        return Token.getTokenMapSingleton().containsKey(string);
    }
    static boolean isNamedRegex(String string){
        return string.matches(NAMED_REGEX_REGEX);
    }
}
