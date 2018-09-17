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
                    put("+", PerlTokens.ADDITION); //done
                    put("-", PerlTokens.SUBTRACTION); //done
                    put("*", PerlTokens.MULTIPLICATION); //done
                    put("**", PerlTokens.POWER);
                    put("/", PerlTokens.DIVISION); //done
                    put("div", PerlTokens.INTEGER_DIVISION); //done
                    put("%", PerlTokens.PERCENT_SYMBOL);
                    put("%%", PerlTokens.DIVISIBILITY);
                    put("gcd", PerlTokens.GCD); //done
                    put("lcm", PerlTokens.LCM); //done
                    put("==", PerlTokens.NUMERIC_EQUAL); //done
                    put("!=", PerlTokens.NUMERIC_NOT_EQUAL);
                    put("<", PerlTokens.LEFT_ANG_BRACKET); //done
                    put(">", PerlTokens.RIGHT_AND_BRACKET); //done
                    put("<=", PerlTokens.NUMERIC_LESS_THAN_OR_EQUAL); //done
                    put(">=", PerlTokens.NUMERIC_GREATER_THAN_OR_EQUAL); //done
                    put("<=>", PerlTokens.NUMERIC_THREE_WAY_COMPARATOR); //done
                    put("eq", PerlTokens.STRING_EQUAL); //done
                    put("ne", PerlTokens.STRING_NOT_EQUAL);//done
                    put("lt", PerlTokens.STRING_LESS_THAN);//done
                    put("gt", PerlTokens.STRING_GREATER_THAN);//done
                    put("le", PerlTokens.STRING_LESS_THAN_OR_EQUAL);//done
                    put("ge", PerlTokens.STRING_GREATER_THAN_OR_EQUAL);//done
                    put("leg", PerlTokens.STRING_THREE_WAY_COMPARATOR);//done
                    put("cmp", PerlTokens.SMART_THREE_WAY_COMPARATOR);//done
                    put("=", PerlTokens.ASSIGNMENT); //done
                    put("~", PerlTokens.STRING_CONCATENATION);
                    put(":=", PerlTokens.BINDING);
                    put("x", PerlTokens.STRING_REPLICATION);
                    put("~~", PerlTokens.SMART_MATCH);
                    put("++", PerlTokens.INCREMENT);//done
                    put("--", PerlTokens.DECREMENT);//done
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
                    put("return", PerlTokens.RETURN);//done
                    put("returns", PerlTokens.RETURN);//done
                    put("-->", PerlTokens.RETURN);
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
                    put("=>", PerlTokens.IMPLICATION);
                    put("==>", PerlTokens.FORWARD_FEED);
                    put("<==", PerlTokens.BACKWARD_FEED);
                    put(">>", PerlTokens.HYPER_OPERATOR);
                    put("::", PerlTokens.DOUBLE_COLUM);
                    put("->", PerlTokens.ARROW);
                    put("True", PerlTokens.BOOLEAN);//done
                    put("False", PerlTokens.BOOLEAN);//done
                    put("?", PerlTokens.QUESTION_MARK);
                    put("!", PerlTokens.EXCLAMATION_MARK);
                }
            };

        }
        return tokenMap;
    }
}
