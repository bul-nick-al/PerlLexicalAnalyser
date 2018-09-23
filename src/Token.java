import java.util.HashMap;

/**
 * Class which describes Token
 */
class Token {

    private int line;
    private int position;
    private PerlTokens type;
    private String value;
    private static HashMap<String, PerlTokens> tokenMap;

    /**
     * Constructor of the class
     * @param type of the token from PerlTokens enum
     * @param value of the token in String
     * @param line is a line where is token from the beginning of input file
     * @param position is a position of the token from the beginning of current line
     */
    Token(PerlTokens type, String value, int line, int position) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.position = position;
    }

    /**
     * Getter of the private type attribute
     * @return type of the token
     */
    PerlTokens getType() {
        return type;
    }

    /**
     * Getter of the private value attribute
     * @return value of the token
     */
    String getValue() {
        return value;
    }

    /**
     * Getter of the private line attribute
     * @return line where is token from the beginning of input file
     */
    int getLine() {
        return line;
    }

    /**
     * Getter of the private position attribute
     * @return position of the token from the beginning of current line
     */
    int getPosition() {
        return position;
    }

    /**
     * Enum describes all types of tokens in the Perl6 language
     */
    enum PerlTokens {
        //these are the standard Perl6 operators that can be unambiguously identified
        ADDITION_OPERATOR, SUBTRACTION_OPERATOR, MULTIPLICATION_OPERATOR, POWER_OPERATOR, DIVISION_OPERATOR,
        INTEGER_DIVISION_OPERATOR, DIVISIBILITY_OPERATOR, GCD_OPERATOR, LCM_OPERATOR, NUMERIC_EQUAL_OPERATOR,
        NUMERIC_NOT_EQUAL_OPERATOR, NUMERIC_LESS_THAN_OR_EQUAL_OPERATOR, NUMERIC_GREATER_THAN_OR_EQUAL_OPERATOR,
        NUMERIC_THREE_WAY_COMPARATOR_OPERATOR, STRING_EQUAL_OPERATOR, STRING_NOT_EQUAL_OPERATOR,
        STRING_LESS_THAN_OPERATOR, STRING_GREATER_THAN_OPERATOR, STRING_LESS_THAN_OR_EQUAL_OPERATOR,
        STRING_GREATER_THAN_OR_EQUAL_OPERATOR, STRING_THREE_WAY_COMPARATOR_OPERATOR, SMART_THREE_WAY_COMPARATOR_OPERATOR,
        ASSIGNMENT_OPERATOR, BINDING_OPERATOR, STRING_CONCATENATION_OPERATOR, STRING_REPLICATION_OPERATOR,
        SMART_MATCH_OPERATOR, INCREMENT_OPERATOR, DECREMENT_OPERATOR, RANGE_CONSTRUCTOR, LAZY_LIST_CONSTRUCTOR,
        RETURN_OPERATOR, // can be return, returns or -->
        REVERSED_OPERATOR, // R + any operator
        IMPLICATION_OPERATOR,// =>
        FORWARD_FEED_OPERATOR, // ==>
        BACKWARD_FEED_OPERATOR, // <==
        HYPER_OPERATOR_OPERATOR, // >>
        CARET_OPERATOR, // ^
        DOUBLE_COLUMN_OPERATOR, // ::
        ARROW_OPERATOR, //->


        IDENTIFIER,
        STRING, NUMBER, BOOLEAN, REGEX,

        //these are the symbols or combination of symbols that can play different roles depending on semantics, thus
        //their tokens are generic and it is up to the following stages to define the meaning of those.
        VERTICAL_BAR, BACKSLASH, DOLLAR_SLASH,  PERCENT_SYMBOL, AT_SYMBOL, DOLLAR_SYMBOL, AMPERSAND_SYMBOL,
        QUESTION_MARK, EXCLAMATION_MARK,

        //predefined keywords
        MY_KEYWORD, DEFAULT_KEYWORD, ELSE_KEYWORD, ELSEIF_KEYWORD, FOR_KEYWORD, GET_KEYWORD,
        GIVEN_KEYWORD, IF_KEYWORD, LOOP_KEYWORD, MULTI_KEYWORD, ORWITH_KEYWORD,
        REPEAT_KEYWORD, SUB_KEYWORD, UNLESS_KEYWORD, UNTIL_KEYWORD,
        WHEN_KEYWORD, WHILE_KEYWORD, WITH_KEYWORD, WITHOUT_KEYWORD, NOT_KEYWORD, CLASS_KEYWORD, HAS_KEYWORD, IS_KEYWORD,
        METHOD_KEYWORD, SELF_KEYWORD, SUBMETHOD_KEYWORD, ROLE_KEYWORD, DOES_KEYWORD, CATCH_KEYWORD, TRY_KEYWORD,
        USE_KEYWORD, DIE_KEYWORD, INIT_KEYWORD, CONSTANT_KEYWORD,

        LEFT_SQ_BRACKET, RIGHT_SQ_BRACKET, LEFT_PARENTH, RIGHT_PARENTH, LEFT_ANG_BRACKET, RIGHT_AND_BRACKET,
        LEFT_CURLY_BRACKET, RIGHT_CURLY_BRACKET,

        DOT, COMMA, COLON, SEMICOLON,

        //These are not in language, for Lexer only
        END_OF_INPUT, ERROR
    }

    /**
     * Method prepares hash map for easy mapping token types to their string representation
     * @return hash map which maps tokenTupes from PerlTokens enum to their string representation
     */
    static HashMap<String, PerlTokens> getTokenMapSingleton(){
        if (tokenMap == null) {
            tokenMap = new HashMap<>(){
                {
                    put("+", PerlTokens.ADDITION_OPERATOR); //done
                    put("-", PerlTokens.SUBTRACTION_OPERATOR); //done
                    put("*", PerlTokens.MULTIPLICATION_OPERATOR); //done
                    put("**", PerlTokens.POWER_OPERATOR);
                    put("/", PerlTokens.DIVISION_OPERATOR); //done
                    put("div", PerlTokens.INTEGER_DIVISION_OPERATOR); //done
                    put("%%", PerlTokens.DIVISIBILITY_OPERATOR);
                    put("gcd", PerlTokens.GCD_OPERATOR); //done
                    put("lcm", PerlTokens.LCM_OPERATOR); //done
                    put("==", PerlTokens.NUMERIC_EQUAL_OPERATOR); //done
                    put("!=", PerlTokens.NUMERIC_NOT_EQUAL_OPERATOR);
                    put("<=", PerlTokens.NUMERIC_LESS_THAN_OR_EQUAL_OPERATOR); //done
                    put(">=", PerlTokens.NUMERIC_GREATER_THAN_OR_EQUAL_OPERATOR); //done
                    put("<=>", PerlTokens.NUMERIC_THREE_WAY_COMPARATOR_OPERATOR); //done
                    put("eq", PerlTokens.STRING_EQUAL_OPERATOR); //done
                    put("ne", PerlTokens.STRING_NOT_EQUAL_OPERATOR);//done
                    put("lt", PerlTokens.STRING_LESS_THAN_OPERATOR);//done
                    put("gt", PerlTokens.STRING_GREATER_THAN_OPERATOR);//done
                    put("le", PerlTokens.STRING_LESS_THAN_OR_EQUAL_OPERATOR);//done
                    put("ge", PerlTokens.STRING_GREATER_THAN_OR_EQUAL_OPERATOR);//done
                    put("leg", PerlTokens.STRING_THREE_WAY_COMPARATOR_OPERATOR);//done
                    put("cmp", PerlTokens.SMART_THREE_WAY_COMPARATOR_OPERATOR);//done
                    put("=", PerlTokens.ASSIGNMENT_OPERATOR); //done
                    put("~", PerlTokens.STRING_CONCATENATION_OPERATOR);
                    put(":=", PerlTokens.BINDING_OPERATOR);
                    put("x", PerlTokens.STRING_REPLICATION_OPERATOR);
                    put("~~", PerlTokens.SMART_MATCH_OPERATOR);
                    put("++", PerlTokens.INCREMENT_OPERATOR);//done
                    put("--", PerlTokens.DECREMENT_OPERATOR);//done
                    put("<", PerlTokens.LEFT_ANG_BRACKET); //done
                    put(">", PerlTokens.RIGHT_AND_BRACKET); //done
                    put("%", PerlTokens.PERCENT_SYMBOL);
                    put("..", PerlTokens.RANGE_CONSTRUCTOR);
                    put("..^", PerlTokens.RANGE_CONSTRUCTOR);
                    put("^..", PerlTokens.RANGE_CONSTRUCTOR);
                    put("^..^", PerlTokens.RANGE_CONSTRUCTOR);
                    put("^", PerlTokens.CARET_OPERATOR);
                    put("...", PerlTokens.LAZY_LIST_CONSTRUCTOR);
                    put("…", PerlTokens.LAZY_LIST_CONSTRUCTOR);
                    put("|", PerlTokens.VERTICAL_BAR);
                    put("@", PerlTokens.AT_SYMBOL);
                    put("$", PerlTokens.DOLLAR_SYMBOL);
                    put("&", PerlTokens.AMPERSAND_SYMBOL);
                    put("my", PerlTokens.MY_KEYWORD);//done
                    put("default", PerlTokens.DEFAULT_KEYWORD);//done
                    put("else", PerlTokens.ELSE_KEYWORD);//done
                    put("elseif", PerlTokens.ELSEIF_KEYWORD);//done
                    put("for", PerlTokens.FOR_KEYWORD);//done
                    put("get", PerlTokens.GET_KEYWORD); //?//done
                    put("given", PerlTokens.GIVEN_KEYWORD); //? //done
                    put("if", PerlTokens.IF_KEYWORD);//done
                    put("loop", PerlTokens.LOOP_KEYWORD);//done
                    put("multi", PerlTokens.MULTI_KEYWORD);//done
                    put("orwith", PerlTokens.ORWITH_KEYWORD);//done
                    put("repeat", PerlTokens.REPEAT_KEYWORD);//done
                    put("return", PerlTokens.RETURN_OPERATOR);//done
                    put("returns", PerlTokens.RETURN_OPERATOR);//done
                    put("-->", PerlTokens.RETURN_OPERATOR);//done
                    put("sub", PerlTokens.SUB_KEYWORD);//done
                    put("unless", PerlTokens.UNLESS_KEYWORD);//done
                    put("until", PerlTokens.UNTIL_KEYWORD);//done
                    put("when", PerlTokens.WHEN_KEYWORD);//done
                    put("while", PerlTokens.WHILE_KEYWORD);//done
                    put("with", PerlTokens.WITH_KEYWORD);//done
                    put("without", PerlTokens.WITHOUT_KEYWORD);//done
                    put("not", PerlTokens.NOT_KEYWORD);//done
                    put("class", PerlTokens.CLASS_KEYWORD);//done
                    put("has", PerlTokens.HAS_KEYWORD);//done
                    put("is", PerlTokens.IS_KEYWORD);//done
                    put("method", PerlTokens.METHOD_KEYWORD);//done
                    put("self", PerlTokens.SELF_KEYWORD);//done
                    put("submethod", PerlTokens.SUBMETHOD_KEYWORD);//done
                    put("role", PerlTokens.ROLE_KEYWORD);//done
                    put("does", PerlTokens.DOES_KEYWORD);//done
                    put("CATCH", PerlTokens.CATCH_KEYWORD);//done
                    put("try", PerlTokens.TRY_KEYWORD);//done
                    put("use", PerlTokens.USE_KEYWORD);//done
                    put("die", PerlTokens.DIE_KEYWORD);//done
                    put("INIT", PerlTokens.INIT_KEYWORD);//done
                    put("constant", PerlTokens.CONSTANT_KEYWORD);//done
                    put("[", PerlTokens.LEFT_SQ_BRACKET);
                    put("]", PerlTokens.RIGHT_SQ_BRACKET);
                    put("(", PerlTokens.LEFT_PARENTH);
                    put(")", PerlTokens.RIGHT_PARENTH);
                    put("{", PerlTokens.LEFT_CURLY_BRACKET);
                    put("}", PerlTokens.RIGHT_CURLY_BRACKET);
                    put(".", PerlTokens.DOT);
                    put(",", PerlTokens.COMMA);
                    put(":", PerlTokens.COLON);
                    put(";", PerlTokens.SEMICOLON);
                    put("=>", PerlTokens.IMPLICATION_OPERATOR);
                    put("==>", PerlTokens.FORWARD_FEED_OPERATOR);//done
                    put("<==", PerlTokens.BACKWARD_FEED_OPERATOR);//done
                    put(">>", PerlTokens.HYPER_OPERATOR_OPERATOR);
                    put("::", PerlTokens.DOUBLE_COLUMN_OPERATOR);
                    put("->", PerlTokens.ARROW_OPERATOR);
                    put("True", PerlTokens.BOOLEAN);//done
                    put("False", PerlTokens.BOOLEAN);//done
                    put("?", PerlTokens.QUESTION_MARK);
                    put("!", PerlTokens.EXCLAMATION_MARK);
                    put("\\", PerlTokens.BACKSLASH);
                    put("$/", PerlTokens.DOLLAR_SLASH);
                    put("R", PerlTokens.REVERSED_OPERATOR);
                }
            };

        }
        return tokenMap;
    }
}
