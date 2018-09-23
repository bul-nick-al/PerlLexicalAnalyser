/**
 * This class recognizes particular types of language units in Perl6
 */
class PatternsRecogniser {

    private static final String STRING_REGEX = "'(.*([^\\\\]|\\\\\\\\))??'|\"(.*([^\\\\]|\\\\\\\\))??\"";
    private static final String NUMBER_REGEX = "[+\\-]??\\d+|0b[01]+|0x\\d+|[+\\-]??\\d+(.\\d+)??([eE][+\\-]??\\d+)??";
    private static final String REGEX_REGEX = "/([^/]|\\\\/)*/|m/([^/]|\\\\/)*/|rx/([^/]|\\\\/)*/|s/([^/]|\\\\/)*/([^/]|\\\\/)*/";
    private static final String NAMED_REGEX_REGEX = ".*regex.*|.*token.*|.*rule.*";
    private static final String IDENTIFIER_REGEX = "([_a-zA-Z]|[a-zA-Z]'|[a-zA-Z]-)(([a-zA-Z\\d_]|[a-zA-Z]'|[a-zA-Z]-)*[_a-zA-Z\\d])??";
    private static final String EMBEDDED_COMMENT_REGEX = "#`\\([^)]*\\)";
    private static final String START_OF_MULTILINE_COMMENT = "\\s*=begin comment(\\s.*)*";
    private static final String END_OF_MULTILINE_COMMENT = "\\s*=end comment\\s*";
    /**
     * Method which checks whether given string is string in Perl6
     * @param string input string
     * @return true if input is string, false otherwise
     */
    static boolean isString(String string){
        return string.matches(STRING_REGEX);
    }

    /**
     * Method which checks whether given string is the start of a multiline comment in Perl6
     * @param string input string
     * @return true if input is string, false otherwise
     */
    static boolean isStartOfMultilineComment(String string){
        return string.matches(START_OF_MULTILINE_COMMENT);
    }

    /**
     * Method which checks whether given string is the end of a multiline cooment in Perl6
     * @param string input string
     * @return true if input is string, false otherwise
     */
    static boolean isEndOfMultilineComment(String string){
        return string.matches(END_OF_MULTILINE_COMMENT);
    }

    /**
     * Method which checks whether given string is number in Perl6
     * @param string input string
     * @return true if input is number, false otherwise
     */
    static boolean isNumber(String string){
        return string.matches(NUMBER_REGEX);
    }

    /**
     * Method which checks whether given string is regex in Perl6
     * @param string input string
     * @return true if input is regex, false otherwise
     */
    static boolean isRegex(String string){
        return string.matches(REGEX_REGEX);
    }

    /**
     * Method which checks whether given string is embedded comment in Perl6
     * @param string input string
     * @return true if input is embedded comment, false otherwise
     */
    static boolean isEmbeddedComment(String string){
        return string.matches(EMBEDDED_COMMENT_REGEX);
    }

    /**
     * Method which checks whether given string is identifier in Perl6
     * @param string input string
     * @return true if input is identified, false otherwise
     */
    static boolean isIdentifier(String string){
        return string.matches(IDENTIFIER_REGEX);
    }

    /**
     * Method which checks whether given string is named regex comment in Perl6
     * @param string input string
     * @return true if input is named regex, false otherwise
     */
    static boolean isNamedRegex(String string){
        return string.matches(NAMED_REGEX_REGEX);
    }
}
