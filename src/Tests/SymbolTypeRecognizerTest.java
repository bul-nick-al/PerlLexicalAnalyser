import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rozaliaamirova on 23.09.2018.
 */
public class SymbolTypeRecognizerTest {
    @Test
    public void recognize() throws Exception {
        char test1 = '"';
        char test2 = '1';
        char test3 = 'a';
        char test4 = '#';
        char test5 = '/';
        char test6 = ';';

        assertEquals(SymbolTypeRecognizer.SymbolType.STRING_CANDIDATE, SymbolTypeRecognizer.recognize(test1));
        assertEquals(SymbolTypeRecognizer.SymbolType.NUMBER_CANDIDATE, SymbolTypeRecognizer.recognize(test2));
        assertEquals(SymbolTypeRecognizer.SymbolType.WORD_CANDIDATE, SymbolTypeRecognizer.recognize(test3));
        assertEquals(SymbolTypeRecognizer.SymbolType.COMMENT_CANDIDATE, SymbolTypeRecognizer.recognize(test4));
        assertEquals(SymbolTypeRecognizer.SymbolType.REGEX_CANDIDATE, SymbolTypeRecognizer.recognize(test5));
        assertEquals(SymbolTypeRecognizer.SymbolType.UNDEFINED, SymbolTypeRecognizer.recognize(test6));
    }
}