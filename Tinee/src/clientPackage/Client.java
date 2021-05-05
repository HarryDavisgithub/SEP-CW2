package clientPackage;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import sep.tinee.net.message.Bye;
import sep.tinee.net.message.Push;
import sep.tinee.net.message.ReadReply;
import sep.tinee.net.message.ReadRequest;

/**
 * This class is an initial work-in-progress prototype for a command line
 * Tinee client. It has been hastily hacked together, as often the case
 * in early exploratory coding, and is incomplete and buggy. However, it
 * does compile and run, and <i>some</i> basic functionality, such as pushing
 * and reading tines to and from an instance of
 * {@link sep.tinee.server.Server}, is working. Try it out!
 * <p>
 * The arguments required to run a client correspond to the
 * { #set(String, String, int)} method: a user name, and the host name
 * and port number of a Tinee server.
 * <p>
 * You can compile and run this client using <b>NetBeans</b>; e.g., right-click
 * this file in the NetBeans editor and select "Run File".  Note, to provide
 * the above arguments, you should set up a <b>run configuration</b> for this
 * class: {@literal ->} "Set Project Configuration" {@literal ->} "Customize..."
 * {@literal ->} "New...".
 * <p>
 * Assuming compilation using NetBeans (etc.), you can also run {@code Client}
 * from the command line; e.g., on Windows, run:
 * <ul>
 * <li style="list-style-type: none;">
 * {@code C:\...\tinee>  java -cp build\classes Client userid localhost 8888}
 * </ul>
 * <p>
 * You will be significantly reworking and extending this client: your aim is
 * to meet the specification, and you have mostly free rein to do so.
 * (That is as opposed to the base framework, which you are <b>not</b> permitted
 * to modify, i.e., the packages {@link sep.tinee.server},
 * {@link sep.tinee.server}, {@link sep.tinee.server} and]
 * {@link sep.tinee.server}.) The constraints on your client are:
 * <ul>
 * <li>
 * You must retain a class named {@code Client}) as the frontend class for
 * running your client, i.e., via its static {@linkplain #main(String[]) main}
 * method.
 * <li>
 * The {@linkplain Client#main(String[]) main} method must accept the <i>same
 * arguments</i> as currently, i.e., user name, host name and port number.
 * <li>
 * Your client must continue to accept user commands on the <i>standard input
 * stream</i> ({@code System.in}), and output on the <i>standard output
 * stream</i> ({@code System.out}).
 * <li>
 * Any other conditions specified by the assignment tasks.
 * </ul>
 * <p>
 * <i>Tip:</i> generate and read the <b>JavaDoc</b> API documentation for the
 * provided base framework classes (if you're not already reading this there!).
 * After importing the code project into NetBeans, right-click the project in
 * the "Projects" window and select "Generate Javadoc".
 * By default, the output is written to the {@code dist/javadoc} directory.
 * You can directly read the comments in the code for the same information, but
 * the generated JavaDoc is formatted more nicely as HTML with click-able links.  
 *
 * @see CLFormatter
 */
public class Client {

  private final String user;
  private final String host;
  private final int port;
  private boolean done = true;
  private static LangManager langMan = new LangManager();
  private boolean printSplash = true;

  public Client(String user, String host, int port) {
      this.user = user;
      this.host = host;
      this.port = port; 
  }

  public static void main(String[] args) throws IOException {
    String user = args[0];
    String host = args[1];
    try{
        int port = Integer.parseInt(args[2]);
        Client client = new Client(user, host, port);
        client.run();
    }
    catch(ArrayIndexOutOfBoundsException e){
        //System.err.println(langMan.noUser());
        throw new IllegalArgumentException(langMan.noUser());
    }
  }

  // Run the client
  @SuppressFBWarnings(
      value = "DM_DEFAULT_ENCODING",
      justification = "When reading console, ignore 'default encoding' warning")
  void run() throws IOException {

    BufferedReader reader = null;
    CLFormatter helper = null;
    reader = new BufferedReader(new InputStreamReader(System.in));
    
    try {
      
      if (this.user.isEmpty() || this.host.isEmpty()) {
        System.err.println(langMan.noUser());
        done = false;
      }
      helper = new CLFormatter(this.host, this.port);
      
      if (this.printSplash)
      {
        System.out.print(langMan.formatSplash(this.user));
      }
      loop(helper, reader);
    } catch (IOException | ClassNotFoundException ex) {
        System.err.println(ex);
    } finally {
      reader.close();
      if (helper.chan.isOpen()) {
        // If the channel is open, send Bye and close
        helper.chan.send(new Bye());
        helper.chan.close();
      }
    }
  }

// Main loop: print user options, read user input and process
  void loop(CLFormatter helper, BufferedReader reader) throws IOException,
      ClassNotFoundException {

    // The app is in one of two states: "Main" or "Drafting"
    String state = "Main";  // Initial state

    // Holds the current draft data when in the "Drafting" state
    String draftTag = null;
    List<String> draftLines = new LinkedList<>();

    // The loop
    while(done) {

      // Print user options
      if ("Main".equals(state)) {
        System.out.print(langMan.formatMainMenuPrompt());
      } else {  // state = "Drafting"
        System.out.print(langMan.
            formatDraftingMenuPrompt(draftTag, draftLines, helper));
      }

      // Read a line of user input
      String raw = reader.readLine();
      if (raw == null) {
        throw new IOException(langMan.inputClosed());
      }
      // Trim leading/trailing white space, and split words according to spaces
      List<String> split = Arrays.stream(raw.trim().split(" ", 2)).map(x -> x.trim()).collect(Collectors.toList());
     
      
      String cmd = split.remove(0);  // First word is the command keyword
      String[] rawArgs = split.toArray(new String[split.size()]);
      // Remainder, if any, are arguments

      // Process user input
      if (langMan.exit().startsWith(cmd)) {
        // exit command applies in either state
        done = false;

      } // "Main" state commands
      else if ("Main".equals(state)) {
        if (langMan.manage().startsWith(cmd)) {
          // Switch to "Drafting" state and start a new "draft"
          state = "Drafting";
          draftTag = rawArgs[0];
        } else if (langMan.read().startsWith(cmd)) {
          // Read tines on server
          helper.chan.send(new ReadRequest(rawArgs[0]));
          ReadReply rep = (ReadReply) helper.chan.receive();
          System.out.print(
              helper.formatRead(rawArgs[0], rep.users, rep.lines));
        } else {
          System.out.println(langMan.parseArgsMsg());
        }
      } // "Drafting" state commands
      else if ("Drafting".equals(state)) {
        if (langMan.line().startsWith(cmd)) {
          // Add a tine message line
          String line = Arrays.stream(rawArgs).
              collect(Collectors.joining());
          draftLines.add(line);
        } else if (langMan.push().startsWith(cmd)) {
          // Send drafted tines to the server, and go back to "Main" state
          helper.chan.send(new Push(user, draftTag, draftLines));
          state = "Main";
          draftTag = null;
          draftLines.clear();
        } else {
          System.out.println(langMan.parseArgsMsg());
        }
      } else {
        System.out.println(langMan.parseArgsMsg());
      }
    }
    
  }
}

