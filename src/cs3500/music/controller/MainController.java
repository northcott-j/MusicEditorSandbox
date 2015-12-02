package cs3500.music.controller;

import cs3500.music.model.MusicEditorImpl;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.util.MusicReader;
import cs3500.music.view.*;

import javax.sound.midi.InvalidMidiDataException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Adapter package between the Main method and the Controllers
 * Created by Jonathan on 11/20/2015.
 */
public class MainController {

  /**
   * Constructor for a MainController; this takes care of the creation of the proper
   * Music Editor. The String[] input {@code args} is where the user can define the
   * type of output they want the program to create. The order of these inputs does
   * not matter. The constructor will also throw exceptions for invalid inputs, such
   * as unsupported view types and if the desired music piece of that title cannot
   * be found in the system. The line comments found within the constructor will
   * explain the progression of this initialization.
   *
   * @param args the name of the desired view, and the name of the desired piece
   * @throws IOException invalid view/piece name
   * @throws InvalidMidiDataException error in the playback/setup of MIDI
   */
  public MainController(String[] args) throws IOException, InvalidMidiDataException {
    // Ensures that the inputs are valid
    if (args.length != 2) {
      throw new IOException("Missing an input; requires a piece name and view type.");
    }
    String arg1 = args[0].toLowerCase();
    String arg2 = args[1].toLowerCase();
    // Defining the initial state of the model
    MusicEditorModel model;
    // Stores proper file types
    String[] pieceList = new String[]{"mary.txt", "mystery-1.txt", "mystery-2.txt",
            "test-file.txt", "martet.txt", "mystery-3.txt", "default"};
    ArrayList<String> pieces = new ArrayList<>(Arrays.asList(pieceList));
    // Checks for a valid file name, and sets the model data accordingly
    String pieceName;
    if (pieces.contains(arg1) && !pieces.contains(arg2)) {
      pieceName = arg1;
    } else if (pieces.contains(arg2) && !pieces.contains(arg1)) {
      pieceName = arg2;
    } else {
      throw new IOException("Invalid data: please enter a correct piece name.");
    }
    model = this.setPiece(pieceName);

    // Builds and runs the desired view
    if (arg1.equals("midi") || arg2.equals("midi")) {
      NonGuiController.makeController(model, new MidiView()).run();
    } else if (arg1.equals("console") || arg2.equals("console")) {
      NonGuiController.makeController(model, ConsoleView.builder()
              .input(new Scanner(System.in))
              .output(System.out)
              .build()).run();
    } else if (arg1.equals("editor") || arg2.equals("editor")) {
      GuiController.makeController(model, new EditorView(), "run").run();
    } else if (arg1.equals("playback") || arg2.equals("playback")) {
      GuiController.makeController(model, new PlaybackView(), "run").run();
    } else {
      throw new IOException("Invalid input: please enter a correct view type.");
    }
  }

  /**
   * Helper method for setting the model to the desired file.
   *
   * @param pieceName the name of the desired file
   * @return the correct model
   * @throws IOException invalid or no file name input
   */
  private MusicEditorModel setPiece(String pieceName) throws IOException {
    MusicEditorModel model;
    // If the desired file is "mary-little-lamb.txt":
    if (pieceName.equals("mary.txt")) {
      model = MusicReader.parseFile(new FileReader("mary-little-lamb.txt"),
              new MusicEditorImpl.Builder());
    } else if (pieceName.equals("default")) {
      model = MusicEditorImpl.makeEditor();
    } else {
        model = MusicReader.parseFile(new FileReader(pieceName), new MusicEditorImpl.Builder());
    }
    return model;
  }

}
