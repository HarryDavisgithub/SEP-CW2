/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import clientPackage.Client;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;

/**
 *
 * @author harry
 */
public class ClientTest {

    private PrintStream standardOut;
    private InputStream standardIn;
    
    public ClientTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        standardOut = System.out;
        standardIn = System.in;
    }
    
    @After
    public void tearDown() {
        System.setOut(standardOut);
        System.setIn(standardIn);
    }

    
    @Test
    public void testLineSpaces() throws Exception { //tests if adding new lines keeps spaces
        String[] args = {"userid", "localhost", "8888"}; 
        String input = "manage test\nline this is a message\nexit\n"; 
        
        ByteArrayOutputStream out = new ByteArrayOutputStream(); //creates a new output stream that stores all of the data sent to it in a buffer in memory instead of console output
        System.setOut(new PrintStream(out, true, "UTF8")); //Sets System.out to the created stream
        
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes("UTF8")); //creates a new input stream so allow a buffer in memory to be used for input instead of console input
        System.setIn(in);
        
        Client.main(args); 
        
        String output = out.toString("UTF8"); //converts to a string
       
        assertTrue("Strings does not match, spaces have been removed", output.contains("this is a message")); //tests to see if the output contains spaces
    }
    
    @Test
    public void testDrafting() throws Exception {
        String[] args = {"userid", "localhost", "8888"};
        String input = "manage test\nline testline\npush\nmanage test2\nexit\n";
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out, true, "UTF8"));
        
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes("UTF8")); 
        System.setIn(in);
        
        Client.main(args); 

        String output = out.toString("UTF8"); 
      
        assertFalse("Lines are carried over in drafting, new tags will have all previous lines from other tags pushed", output.contains("test2\n" +
"           1  testline")); //tests to see if the console output contains the correct data (it should be empty))
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNoUser() throws Exception {
       String[] args = {" ", "localhost", "8888"};
       Client.main(args); 
    }
}
