
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author harry
 */
public class LangManager {
  private final ResourceBundle strings;
  private static final String RESOURCE_PATH = "resources/MessageBundle";
  


  public LangManager(){
    strings = ResourceBundle.getBundle(RESOURCE_PATH, new Locale("en", "GB"));  
    
  }

  public String formatSplash(String user) {
    return print(strings.getString("formatSplash"), user); 
  }
  public String formatMainMenuPrompt() {
    return strings.getString("formatMainMenuPrompt");
  }

  public String formatDraftingMenuPrompt(String tag, List<String> lines, CLFormatter helper) {
    return print(strings.getString("formatDraftingMenuPrompt"), helper.formatDrafting(tag, lines)); 
  }

  public String noUser(){
    return strings.getString("NoUser");
  }
  
  public String inputClosed(){
    return strings.getString("InputClosed");
  }
  
  public String parseArgsMsg(){
    return strings.getString("ParseArgsMsg");
  }

  static String print(String message, Object... args) {
    return MessageFormat.format(message, args);
  }


}
