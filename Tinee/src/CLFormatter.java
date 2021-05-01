
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import sep.tinee.net.channel.ClientChannel;
import sep.tinee.net.message.Message;

/**
 * A helper class for the current prototype {@link Client client}.  <i>E.g.</i>,
 * for formatting Command Line messages.
 */
public class CLFormatter {

  static ClientChannel chan;  // Client-side channel for talking to a Tinee server

  CLFormatter(String host, int port) {
    this.chan = new ClientChannel(host, port);
  }

  /* Interact with Tinee server */

  private void send(Message msg) throws IOException {
    this.chan.send(msg);
  }

  private Message receive() throws IOException, ClassNotFoundException {
    return this.chan.receive();
  }

  /* Following are the auxiliary methods for formatting the UI text */

  static String formatSplash(String user, ResourceBundle strings) {
    return printLn(strings.getString("formatSplash"), user); 
  }
  static String formatMainMenuPrompt(ResourceBundle strings) {
    return strings.getString("formatMainMenuPrompt");
  }

  static String formatDraftingMenuPrompt(String tag, List<String> lines, ResourceBundle strings) {
    return printLn(strings.getString("formatDraftingMenuPrompt"), formatDrafting(tag, lines)); 
  }

  static String formatDrafting(String tag, List<String> lines) {
    StringBuilder b = new StringBuilder("#");
    b.append(tag);
    int i = 1;
    for (String x : lines) {
      b.append("\n");
      b.append(String.format("%12d", i++));
      b.append("  ");
      b.append(x);
    };
    return b.toString();
  }

  static String formatRead(String tag, List<String> users,
      List<String> read) {
    StringBuilder b = new StringBuilder("Read: #");
    b.append(tag);
    Iterator<String> it = read.iterator();
    for (String user : users) {
      b.append("\n");
      b.append(String.format("%12s", user));
      b.append("  ");
      b.append(it.next());
    };
    b.append("\n");
    return b.toString();
  }
  
  static String printLn(String message, Object... args) {
      return MessageFormat.format(message, args);
  }
  
  static 
  
}

