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
        program.main(args);
    }


    Main program = new Main();
    @Test
    public void writeFile() throws Exception {
        program.writeFile("Hello!");
        String a = program.readFile().get(0);

        LinkedList<String> result = new LinkedList<>();
        try {
            Scanner in = new Scanner(new File("output.txt"));
            StringBuffer data = new StringBuffer();
            while (in.hasNext())
                result.add(in.nextLine());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        assertEquals("Hello!", result.get(0));
    }

    @Test
    public void readFile() throws Exception {
        LinkedList<String> a = program.readFile();
        Boolean myAssert = a != null;
        assertEquals(true, myAssert);
    }

}