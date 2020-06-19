package io.github.oliviercailloux.y2018.apartments.gui;

import io.github.oliviercailloux.y2018.apartments.iconDisplay.DisplayIcon;
import io.github.oliviercailloux.y2018.apartments.valuefunction.profile.Profile;
import io.github.oliviercailloux.y2018.apartments.valuefunction.profile.ProfileManager;
import io.github.oliviercailloux.y2018.apartments.valuefunction.profile.ProfileType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;


/**
 * This class is a class that will allow us to demonstrate. We ask a few questions to the user at
 * the beginning, we adapt his utility then we show him a list of apartments according to his
 * preferences
 */
public class ProfileGUI {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProfileGUI.class);
  private static final int marginHorizontal = 20;
  private static final int marginVertical = 30;
  private static final int marginImages = 10;
  private static final int sizeImages = 100;


  private static final List<ProfileType> profileTypesAvailable = ProfileManager.getInstance().getAvailableTypes();

  Display display;
  Shell shell;

  public ProfileGUI() {
    this.display = new Display();
    this.shell = new Shell(display);
    shell.setText("Profile selection - Questions");
    shell.setLayout(new GridLayout());
    shell.setSize(2*marginHorizontal+(profileTypesAvailable.size()-1)*marginImages+profileTypesAvailable.size()*sizeImages,
            2*marginVertical+sizeImages);
    int x = (display.getClientArea().width - shell.getSize().x) / 2;
    int y = (display.getClientArea().height - shell.getSize().y) / 2;
    shell.setLocation(x,y);

  }

  /**
   * This is the main function, it asks Questions , AdaptAnswers and then displays the list of
   * Apartments
   *
   * @param args
   */
  public static void main(String[] args) throws IOException {
    ProfileGUI profileSelector = new ProfileGUI();
    profileSelector.askForProfile();

    LOGGER.info("Begining the Layout.");

     /*ProfileGUI lay = new ProfileGUI(avf);
     lay.displayAppart();*/
  }

  /**
   * This function will create and display the window (interface) to ask the user questions and to
   * determine later the weight of his choices
   */
  public void askForProfile() throws IOException {
    Profile selected;
    Map<ProfileType, Image> logos = new HashMap<>();
    Map<ProfileType,Button> buttons = new HashMap<>();
    for(int i = 0; i<profileTypesAvailable.size();i++){
      try (InputStream f = DisplayIcon.class.getResourceAsStream(profileTypesAvailable.get(i)+".png")) {
        Image image = new Image(display, f);
        Button b = new Button(shell,SWT.PUSH);
        int x = marginHorizontal+i*(sizeImages+marginImages);
        int y = marginVertical;
        b.setBounds(x,y,sizeImages,sizeImages);
        b.setImage(image);
        Listener selectionlistener =
                event -> {
                  shell.close();

                  //OUVRIR La NOUVELLE FENETRE

                };
        b.addListener(SWT.Selection,selectionlistener);
        buttons.put(profileTypesAvailable.get(i),b);
        logos.put(profileTypesAvailable.get(i), image);
      }

    }
    shell.setBackground(new Color(display, new RGB(180,180,180),0));
    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();
    LOGGER.info("The screen was closed with success.");
  }



  private void centerDisplay(Display d, Shell s){
    int x = (display.getClientArea().width - shell.getSize().x) / 2;
    int y = (display.getClientArea().height - shell.getSize().y) / 2;
    shell.setLocation(x,y);
  }
}
