import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by rozaliaamirova on 23.09.2018.
 */
public class TokenTest {
    private Token test1 = new Token(Token.PerlTokens.USE_KEYWORD, "use", 0, 1);
    @Test
    public void getType() throws Exception {
        assertEquals(Token.PerlTokens.USE_KEYWORD, test1.getType());
    }

    @Test
    public void getValue() throws Exception {
        assertEquals("use", test1.getValue());
    }

    @Test
    public void getLine() throws Exception {
        assertEquals(0, test1.getLine());
    }

    @Test
    public void getPosition() throws Exception {
        assertEquals(1, test1.getPosition());
    }

    @Test
    public void getTokenMapSingleton() throws Exception {
        HashMap<String, Token.PerlTokens> tokenMap = Token.getTokenMapSingleton();
    }
}