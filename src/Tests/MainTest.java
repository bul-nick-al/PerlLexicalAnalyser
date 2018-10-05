import org.junit.Test;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

import static org.junit.Assert.*;

/**
 * Created by rozaliaamirova on 23.09.2018.
 */
public class MainTest {
    @Test
    public void main() throws Exception {
        String[] args = new String[0];
        Main.main(args);
    }

    @Test
    public void writeFile() throws Exception {
        Main.writeFile("Hello!");
        LinkedList<String> result = new LinkedList<>();
        try {
            Scanner in = new Scanner(new File("out.txt"));
            while (in.hasNext())
                result.add(in.nextLine());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        assertEquals("Hello!", result.get(0));
    }

    @Test
    public void readFile() throws Exception {
        LinkedList<String> a = Main.readFile();
        Boolean myAssert = a != null;
        assertEquals(true, myAssert);
    }

}