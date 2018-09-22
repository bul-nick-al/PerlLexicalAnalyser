
import org.junit.Test;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by rozaliaamirova on 22.09.2018.
 */
public class LexerTest {
    private Lexer lexerMultiline;
    private Lexer lexerSingleline;

    private void initTests() {
        LinkedList<String> singlelineTest = new LinkedList<>();
        singlelineTest.add("try to use magic for writing perl lexer! <8");

        LinkedList<String> multilineTest = new LinkedList<>();

        multilineTest.add("lexer");
        multilineTest.add("!");
        multilineTest.add("<");
        multilineTest.add("8");
        multilineTest.add("# comment test");
        multilineTest.add("=begin comment");
        multilineTest.add("test comment");
        multilineTest.add("=end comment");
        multilineTest.add("token { <[ abc ]> }");
        multilineTest.add("token test { [ abc ] }");
        multilineTest.add("/a..z/");
        multilineTest.add("token testToken { [< <( #test [ [ ] ] [ <?( )> ] 'abc' \"abc\" )> >] }");
        multilineTest.add("token testToken { <( )> }");
        multilineTest.add("token testToken { <?( )> }");
        multilineTest.add("token testToken { [ \" abc \" ] }");
        multilineTest.add("token testToken { [ ' abc ' ] }");
        multilineTest.add("token testToken { [ { abc } ] }");
        lexerMultiline = new Lexer(multilineTest);
        lexerSingleline = new Lexer(singlelineTest);
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

        Token testToken9 = lexerMultiline.getNextToken();
        assertEquals(Token.PerlTokens.REGEX, testToken9.getType());
        assertEquals("token { <[ abc ]> }", testToken9.getValue());
        assertEquals(9, testToken9.getLine());
        assertEquals(0, testToken9.getPosition());

        Token testToken10 = lexerMultiline.getNextToken();
        assertEquals(Token.PerlTokens.REGEX, testToken10.getType());
        assertEquals("token test { [ abc ] }", testToken10.getValue());
        assertEquals(10, testToken10.getLine());
        assertEquals(0, testToken10.getPosition());

        Token testToken11 = lexerMultiline.getNextToken();
        assertEquals(Token.PerlTokens.REGEX, testToken11.getType());
        assertEquals("/a..z/", testToken11.getValue());
        assertEquals(10, testToken11.getLine());
        assertEquals(0, testToken11.getPosition());

        Token testToken12 = lexerMultiline.getNextToken();
        assertEquals(Token.PerlTokens.REGEX, testToken12.getType());
        assertEquals("token testToken { [< <( #test [ [ ] ] [ <?( )> ] 'abc' \"abc\" )> >] }", testToken12.getValue());
        assertEquals(12, testToken12.getLine());
        assertEquals(0, testToken12.getPosition());

        Token testToken13 = lexerMultiline.getNextToken();
        assertEquals(Token.PerlTokens.REGEX, testToken13.getType());
        assertEquals("token testToken { <( )> }", testToken13.getValue());
        assertEquals(13, testToken13.getLine());
        assertEquals(0, testToken13.getPosition());

        Token testToken14 = lexerMultiline.getNextToken();
        assertEquals(Token.PerlTokens.REGEX, testToken14.getType());
        assertEquals("token testToken { <?( )> }", testToken14.getValue());


        Token testToken15 = lexerMultiline.getNextToken();
        assertEquals(Token.PerlTokens.REGEX, testToken15.getType());
        assertEquals("token testToken { [ \" abc \" ] }", testToken15.getValue());

        Token testToken16 = lexerMultiline.getNextToken();
        assertEquals(Token.PerlTokens.REGEX, testToken16.getType());
        assertEquals("token testToken { [ ' abc ' ] }", testToken16.getValue());

        Token testToken17 = lexerMultiline.getNextToken();
        assertEquals(Token.PerlTokens.REGEX, testToken17.getType());
        assertEquals("token testToken { [ { abc } ] }", testToken17.getValue());

    }


}