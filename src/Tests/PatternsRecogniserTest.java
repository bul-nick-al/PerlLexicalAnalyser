import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rozaliaamirova on 23.09.2018.
 */
public class PatternsRecogniserTest {

    @Test
    public void isString() throws Exception {
        String isString1 = "'Hello!'";
        assertTrue(PatternsRecogniser.isString(isString1));
        String isString2 = "\"Hello!\"";
        assertTrue(PatternsRecogniser.isString(isString2));
    }

    @Test
    public void isNumber() throws Exception {
        String isNumber1 = "456";
        assertTrue(PatternsRecogniser.isNumber(isNumber1));
        String isNumber2 = "0x456";
        assertTrue(PatternsRecogniser.isNumber(isNumber2));
    }

    @Test
    public void isRegex() throws Exception {
        String isRegex = "/a..z/";
        assertTrue(PatternsRecogniser.isRegex(isRegex));
    }

    @Test
    public void isEmbeddedComment() throws Exception {
        String isEmbeddedComment = "#`(comment)";
        assertTrue(PatternsRecogniser.isEmbeddedComment(isEmbeddedComment));
    }

    @Test
    public void isIdentifier() throws Exception {
        String isIdentifier = "hello";
        assertTrue(PatternsRecogniser.isIdentifier(isIdentifier));
    }

    @Test
    public void isNamedRegex() throws Exception {
        String isNamedRegex = "token test { regex }";
        assertTrue(PatternsRecogniser.isNamedRegex(isNamedRegex));
    }
}