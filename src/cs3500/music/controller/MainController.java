package cs3500.music.controller;

import sun.applet.Main;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.model.MusicEditorImpl;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.util.MusicReader;
import cs3500.music.view.ConsoleView;
import cs3500.music.view.EditorView;
import cs3500.music.view.MidiView;
import cs3500.music.view.PlaybackView;

/**
 * Created by Jonathan on 11/20/2015.
 */
public class MainController {

  public MainController(String[] args) throws IOException, InvalidMidiDataException {
    // Ensures that the inputs are valid
    if (args.length != 2) {
      throw new IOException("Missing an input; requires a piece name and view type.");
    }
    String arg1 = args[0];
    String arg2 = args[1];
    if (!(arg1.equals("midi") || arg1.equals("console") ||
            arg1.equals("editor") || arg1.equals("playback") ||
            arg2.equals("midi") || arg2.equals("console") ||
            arg2.equals("editor") || arg2.equals("playback"))) {
      throw new IOException("Invalid input; please enter a correct view type.");
    }
    if (!(arg1.equals("mary.txt") || arg1.equals("mystery-1.txt") ||
            arg1.equals("mystery-2.txt") ||
            arg2.equals("mary.txt") || arg2.equals("mystery-1.txt") ||
            arg2.equals("mystery-2.txt"))) {
      throw new IOException("Invalid input; please enter a correct piece name.");
    }

    // Defining the files and initial state of the model
    Readable mary = new FileReader("mary-little-lamb.txt");
    Readable mystery1 = new FileReader("mystery-1.txt");
    Readable mystery2 = new FileReader("mystery-2.txt");
    MusicEditorModel model = MusicEditorImpl.makeEditor();

    // Updates the model to the desired song
    if (arg1.equals("mary.txt") || arg2.equals("mary.txt")) {
      model = MusicReader.parseFile(mary, new MusicEditorImpl.Builder());
    } else if (arg1.equals("mystery-1.txt") || arg2.equals("mystery-1.txt")) {
      model = MusicReader.parseFile(mystery1, new MusicEditorImpl.Builder());
    } else if (arg1.equals("mystery-2.txt") || arg2.equals("mystery-2.txt")) {
      model = MusicReader.parseFile(mystery2, new MusicEditorImpl.Builder());
    }

    // Builds and runs the desired view
    if (arg1.equals("midi") || arg2.equals("midi")) {
      NonGuiController.makeController(model, new MidiView()).run();
    } else if (arg1.equals("console") || arg2.equals("console")) {
      NonGuiController.makeController(model, ConsoleView.builder()
              .input(new Scanner(System.in))
              .output(System.out)
              .build()).run();
    } else if (arg1.equals("editor") || arg2.equals("editor")) {
      GuiController.makeController(model, new EditorView()).run();
    } else if (arg1.equals("playback") || arg2.equals("playback")) {
      GuiController.makeController(model, new PlaybackView()).run();
    }
  }
}
