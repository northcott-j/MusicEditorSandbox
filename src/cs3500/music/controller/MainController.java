package cs3500.music.controller;

import cs3500.music.model.MusicEditorImpl;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.ViewModel;
import cs3500.music.util.MusicReader;
import cs3500.music.view.FactoryView;
import cs3500.music.view.GuiView;
import cs3500.music.view.GuiViewAdapter;
import cs3500.music.view.NonGuiViewAdapter;

import javax.sound.midi.InvalidMidiDataException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
    // Checks for a valid file name, and sets the model data accordingly
    String[] viewList = new String[]{"playback", "midi", "console", "editor"};
    ArrayList<String> views = new ArrayList<>(Arrays.asList(viewList));
    String pieceName;
    String viewName;
    if (views.contains(arg1) && !views.contains(arg2)) {
      viewName = arg1;
      pieceName = arg2;
    } else if (views.contains(arg2) && !views.contains(arg1)) {
      viewName = arg2;
      pieceName = arg1;
    } else {
      throw new IOException("Invalid data: please enter a correct view name.");
    }
    try {
      model = this.setPiece(pieceName);
    } catch (IOException e) {
      throw new IOException("Invalid data: please enter a correct piece name.");
    }

    // Builds and runs the desired view
    FactoryView factoryView = new FactoryView();
    ViewModel vm = adaptModelToViewModel(model);
    switch (viewName) {
      case "midi":
        NonGuiController.makeController(model, vm,
                new NonGuiViewAdapter(factoryView.getView(vm, "midi"))).run();
        break;
      case "console":
        NonGuiController.makeController(model, vm,
                new NonGuiViewAdapter(factoryView.getView(vm, "txt"))).run();
        break;
      case "editor":
        GuiController.makeController(model, vm,
                // This is a controlled cast guaranteed to be a GuiView
                new GuiViewAdapter((GuiView)factoryView.getView(vm, "gui")), "run").run();
        break;
      case "playback":
        GuiController.makeController(model, vm,
                // This is a controlled cast guaranteed to be a GuiView
                new GuiViewAdapter((GuiView)factoryView.getView(vm, "composite")), "run").run();
        break;
      default:
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

  /**
   * Adapts a {@link MusicEditorModel} into a {@link ViewModel}. The adapted result
   * shares state with its adaptee.
   *
   * @param adaptee the {@code MusicEditorModel} to adapt
   * @return a {@code ViewModel} backed by {@code adaptee}
   */
  private static ViewModel adaptModelToViewModel(MusicEditorModel adaptee) {
    return new ViewModel(adaptee) {
      @Override
      public int scoreLength() {
        return super.scoreLength();
      }
    };
  }

}
