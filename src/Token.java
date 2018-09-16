import java.util.HashMap;

public class Token {

    int line;
    int position;
    PerlTokens type;
    String value;


    public Token(PerlTokens type, String value, int line, int position) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.position = position;
    }

    public Token(PerlTokens type, String value) {
        this.type = type;
        this.value = value;
    }

    public static enum PerlTokens {
        ADDITION, SUBTRACTION, MULTIPLICATION, POWER, DIVISION, INTEGER_DIVISION, DIVISIBILITY,
        GCD, LCM, NUMERIC_EQUAL, NUMERIC_NOT_EQUAL,
        NUMERIC_LESS_THAN_OR_EQUAL, NUMERIC_GREATER_THAN_OR_EQUAL, NUMERIC_THREE_WAY_COMPARATOR,
        STRING_EQUAL, STRING_NOT_EQUAL, STRING_LESS_THAN, STRING_GREATER_THAN,
        STRING_LESS_THAN_OR_EQUAL, STRING_GREATER_THAN_OR_EQUAL, STRING_THREE_WAY_COMPARATOR,
        SMART_THREE_WAY_COMPARATOR, ASSIGNMENT, BINDING, STRING_CONCATENATION, STRING_REPLICATION, SMART_MATCH,
        INCREMENT, DECREMENT,

        RANGE_CONSTRUCTOR, LAZY_LIST_CONSTRUCTOR,

        VERTICAL_BAR,

        PERCENT_SYMBOL, AT_SYMBOL, DOLLAR_SYMBOL, AMPERSAND_SYMBOL, IDENTIFIER,

        REVERSED_OPERATOR, // R + any operator

        MY_KEYWORD, DEFAULT_KEYWORD, ELSE_KEYWORD, ELSEIF_KEYWORD, FOR_KEYWORD, GET_KEYWORD,
        GIVEN_KEYWORD, IF_KEYWORD, LOOP_KEYWORD, MULTI_KEYWORD, ORWITH_KEYWORD,
        REPEAT_KEYWORD, SUB_KEYWORD, UNLESS_KEYWORD, UNTIL_KEYWORD,
        WHEN_KEYWORD, WHILE_KEYWORD, WITH_KEYWORD, WITHOUT_KEYWORD, NOT_KEYWORD, CLASS_KEYWORD, HAS_KEYWORD, IS_KEYWORD,
        METHOD_KEYWORD, SELF_KEYWORD, SUBMETHOD_KEYWORD, ROLE_KEYWORD, DOES_KEYWORD, CATCH_KEYWORD, TRY_KEYWORD,
        USE_KEYWORD, DIE_KEYWORD, INIT_KEYWORD, CONSTANT_KEYWORD,


        LEFT_SQ_BRACKET, RIGHT_SQ_BRACKET, LEFT_PARENTH, RIGHT_PARENTH, LEFT_ANG_BRACKET, RIGHT_AND_BRACKET,
        LEFT_CURLY_BRACKET, RIGHT_CURLY_BRACKET, DOT, COMMA, COLON, SEMICOLON,

        // can be return, returns or -->
        RETURN,

        IMPLICATION,// =>
        FORWARD_FEED, // ==>
        BACKWARD_FEED, // <==
        HYPER_OPERATOR, // >>
        CARET, // ^
        DOUBLE_COLUM, // ::
        ARROW, //->

        BOOLEAN,

        REGEX,

        QUESTION_MARK, EXCLAMATION_MARK,

        VARIABLE,
        END_OF_INPUT, ERROR // not in language, for Lexer only
    }

    private static HashMap<String, PerlTokens> tokenMap;

    public static HashMap<String, PerlTokens> getTokenMapSingleton(){
        if (tokenMap == null) {
            tokenMap = new HashMap<String, PerlTokens>(){
                {
                    put("+", PerlTokens.ADDITION);
                    put("-", PerlTokens.SUBTRACTION);
                    put("*", PerlTokens.MULTIPLICATION);
                    put("**", PerlTokens.POWER);
                    put("/", PerlTokens.DIVISION);
                    put("div", PerlTokens.INTEGER_DIVISION);
                    put("%", PerlTokens.PERCENT_SYMBOL);
                    put("%%", PerlTokens.DIVISIBILITY);
                    put("gcd", PerlTokens.GCD);
                    put("lcm", PerlTokens.LCM);
                    put("==", PerlTokens.NUMERIC_EQUAL);
                    put("!=", PerlTokens.NUMERIC_NOT_EQUAL);
                    put("<", PerlTokens.LEFT_ANG_BRACKET);
                    put(">", PerlTokens.RIGHT_AND_BRACKET);
                    put("<=", PerlTokens.NUMERIC_LESS_THAN_OR_EQUAL);
                    put(">=", PerlTokens.NUMERIC_GREATER_THAN_OR_EQUAL);
                    put("<=>", PerlTokens.NUMERIC_THREE_WAY_COMPARATOR);
                    put("eq", PerlTokens.STRING_EQUAL);
                    put("ne", PerlTokens.STRING_NOT_EQUAL);
                    put("lt", PerlTokens.STRING_LESS_THAN);
                    put("gt", PerlTokens.STRING_GREATER_THAN);
                    put("le", PerlTokens.STRING_LESS_THAN_OR_EQUAL);
                    put("ge", PerlTokens.STRING_GREATER_THAN_OR_EQUAL);
                    put("leg", PerlTokens.STRING_THREE_WAY_COMPARATOR);
                    put("cmp", PerlTokens.SMART_THREE_WAY_COMPARATOR);
                    put("=", PerlTokens.ASSIGNMENT);
                    put("~", PerlTokens.STRING_CONCATENATION);
                    put(":=", PerlTokens.BINDING);
                    put("x", PerlTokens.STRING_REPLICATION);
                    put("~~", PerlTokens.SMART_MATCH);
                    put("++", PerlTokens.INCREMENT);
                    put("--", PerlTokens.DECREMENT);
                    put("..", PerlTokens.RANGE_CONSTRUCTOR);
                    put("..^", PerlTokens.RANGE_CONSTRUCTOR);
                    put("^..", PerlTokens.RANGE_CONSTRUCTOR);
                    put("^..^", PerlTokens.RANGE_CONSTRUCTOR);
                    put("^", PerlTokens.CARET);
                    put("...", PerlTokens.LAZY_LIST_CONSTRUCTOR);
                    put("…", PerlTokens.LAZY_LIST_CONSTRUCTOR);
                    put("|", PerlTokens.VERTICAL_BAR);
                    put("@", PerlTokens.AT_SYMBOL);
                    put("$", PerlTokens.DOLLAR_SYMBOL);
                    put("&", PerlTokens.AMPERSAND_SYMBOL);
                    put("my", PerlTokens.MY_KEYWORD);
                    put("default", PerlTokens.DEFAULT_KEYWORD);
                    put("else", PerlTokens.ELSE_KEYWORD);
                    put("elseif", PerlTokens.ELSEIF_KEYWORD);
                    put("for", PerlTokens.FOR_KEYWORD);
                    put("get", PerlTokens.GET_KEYWORD); //?
                    put("given", PerlTokens.GIVEN_KEYWORD); //?
                    put("if", PerlTokens.IF_KEYWORD);
                    put("loop", PerlTokens.LOOP_KEYWORD);
                    put("multi", PerlTokens.MULTI_KEYWORD);
                    put("orwith", PerlTokens.ORWITH_KEYWORD);
                    put("repeat", PerlTokens.REPEAT_KEYWORD);
                    put("return", PerlTokens.RETURN);
                    put("returns", PerlTokens.RETURN);
                    put("-->", PerlTokens.RETURN);
                    put("sub", PerlTokens.SUB_KEYWORD);
                    put("unless", PerlTokens.UNLESS_KEYWORD);
                    put("until", PerlTokens.UNTIL_KEYWORD);
                    put("when", PerlTokens.WHEN_KEYWORD);
                    put("while", PerlTokens.WHILE_KEYWORD);
                    put("with", PerlTokens.WITH_KEYWORD);
                    put("without", PerlTokens.WITHOUT_KEYWORD);
                    put("not", PerlTokens.NOT_KEYWORD);
                    put("class", PerlTokens.CLASS_KEYWORD);
                    put("has", PerlTokens.HAS_KEYWORD);
                    put("is", PerlTokens.IS_KEYWORD);
                    put("method", PerlTokens.METHOD_KEYWORD);
                    put("self", PerlTokens.SELF_KEYWORD);
                    put("submethod", PerlTokens.SUBMETHOD_KEYWORD);
                    put("role", PerlTokens.ROLE_KEYWORD);
                    put("does", PerlTokens.DOES_KEYWORD);
                    put("CATCH", PerlTokens.CATCH_KEYWORD);
                    put("try", PerlTokens.TRY_KEYWORD);
                    put("use", PerlTokens.USE_KEYWORD);
                    put("die", PerlTokens.DIE_KEYWORD);
                    put("INIT", PerlTokens.INIT_KEYWORD);
                    put("constant", PerlTokens.CONSTANT_KEYWORD);
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
                    put("=>", PerlTokens.IMPLICATION);
                    put("==>", PerlTokens.FORWARD_FEED);
                    put("<==", PerlTokens.BACKWARD_FEED);
                    put(">>", PerlTokens.HYPER_OPERATOR);
                    put("::", PerlTokens.DOUBLE_COLUM);
                    put("->", PerlTokens.ARROW);
                    put("True", PerlTokens.BOOLEAN);
                    put("False", PerlTokens.BOOLEAN);
                    put("?", PerlTokens.QUESTION_MARK);
                    put("!", PerlTokens.EXCLAMATION_MARK);
                }
            };

        }
        return tokenMap;
    }
}
