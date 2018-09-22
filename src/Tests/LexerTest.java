
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import static org.junit.Assert.*;

/**
 * Created by rozaliaamirova on 22.09.2018.
 */
public class LexerTest {
    private LinkedList<String> singlelineTest;
    private LinkedList<String> multilineTest;
    private Lexer lexerMultiline;
    private Lexer lexerSingleline;
    HashMap<String, Token.PerlTokens> tokenTypes;


    private void initTests() {
        singlelineTest = new LinkedList<String>();
        ArrayList<String> examples = new ArrayList<String>();
        singlelineTest.add("try to use magic for writing perl lexer! <8");

        multilineTest = new LinkedList<String>();

        multilineTest.add("lexer");
        multilineTest.add("!");
        multilineTest.add("<");
        multilineTest.add("8");

        lexerMultiline = new Lexer(multilineTest);
        lexerSingleline = new Lexer(singlelineTest);
        tokenTypes = Token.getTokenMapSingleton();
    }

    @Test
    public void getNextToken() throws Exception {
        initTests();

        //SINGLELINE INPUT TESTS
        Token testToken1 = lexerSingleline.getNextToken();
        assertEquals(Token.PerlTokens.TRY_KEYWORD, testToken1.getType());
        assertEquals("try", testToken1.getValue());
        assertEquals(0, testToken1.getLine());
        assertEquals(0, testToken1.getPosition());

        Token testToken2 = lexerSingleline.getNextToken();
        assertEquals(Token.PerlTokens.IDENTIFIER, testToken2.getType());
        assertEquals("to", testToken2.getValue());
        assertEquals(0, testToken2.getLine());
        assertEquals(4, testToken2.getPosition());

        Token testToken3 = lexerSingleline.getNextToken();
        assertEquals(Token.PerlTokens.USE_KEYWORD, testToken3.getType());
        assertEquals("use", testToken3.getValue());
        assertEquals(0, testToken3.getLine());
        assertEquals(7, testToken3.getPosition());

        Token testToken4 = lexerSingleline.getNextToken();
        assertEquals(Token.PerlTokens.IDENTIFIER, testToken4.getType());
        assertEquals("magic", testToken4.getValue());
        assertEquals(0, testToken4.getLine());
        assertEquals(11, testToken4.getPosition());

        //MULTILINE INPUT TESTS

        Token testToken5 = lexerMultiline.getNextToken();
        assertEquals(Token.PerlTokens.IDENTIFIER, testToken5.getType());
        assertEquals("lexer", testToken5.getValue());
        assertEquals(0, testToken5.getLine());
        assertEquals(0, testToken5.getPosition());

        Token testToken6 = lexerMultiline.getNextToken();
        assertEquals(Token.PerlTokens.EXCLAMATION_MARK, testToken6.getType());
        assertEquals("!", testToken6.getValue());
        assertEquals(1, testToken6.getLine());
        assertEquals(0, testToken6.getPosition());

        Token testToken7 = lexerMultiline.getNextToken();
        assertEquals(Token.PerlTokens.LEFT_ANG_BRACKET, testToken7.getType());
        assertEquals("<", testToken7.getValue());
        assertEquals(2, testToken7.getLine());
        assertEquals(0, testToken7.getPosition());

        Token testToken8 = lexerMultiline.getNextToken();
        assertEquals(Token.PerlTokens.NUMBER, testToken8.getType());
        assertEquals("8", testToken8.getValue());
        assertEquals(3, testToken8.getLine());
        assertEquals(0, testToken8.getPosition());
    }
}