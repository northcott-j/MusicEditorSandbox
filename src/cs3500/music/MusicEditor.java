package cs3500.music;
import cs3500.music.model.MusicEditorImpl;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.util.MusicReader;
import cs3500.music.view.ConsoleView;
import cs3500.music.view.Controller;
import cs3500.music.view.EditorView;
import cs3500.music.view.MidiView;
import cs3500.music.view.ViewModel;

import javax.sound.midi.InvalidMidiDataException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * MONDAY* - MusicEditor.java main method - Do we need the interface? - ViewModel - ALEX ON MIDI -
 * JON ON TEXT TUESDAY* - Do one of the views WEDNESDAY* - Do the other view THURSDAY* - TESTS
 */

public class MusicEditor {
  /**
   * The main method for displaying a desired piece through the desired view type. View types
   * include: - console : outputs the music file in text format - midi : outputs the music file in
   * audio format Will throw appropriate exceptions for invalid inputs.
   *
   * @param args the name of a piece, and the desired view
   * @throws IOException              invalid inputs
   * @throws InvalidMidiDataException invalid data within the MIDI view
   */

  // TODO: Add in hybrid view to if Statements
  // TODO: Make sure everything still works
  public static void main(String[] args) throws IOException, InvalidMidiDataException {
    // Ensures that the inputs are valid
    if (args.length != 2) {
      throw new IOException("Missing an input; requires a piece name and view type.");
    }
    String arg1 = args[0];
    String arg2 = args[1];
    if (!(arg1.equals("midi") || arg1.equals("console") ||
            arg1.equals("editor") ||
            arg2.equals("midi") || arg2.equals("console") ||
            arg2.equals("editor"))) {
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
      new Controller(model, new MidiView()).run();
    } else if (arg1.equals("console") || arg2.equals("console")) {
      new Controller(model, ConsoleView.builder()
              .input(new Scanner(System.in))
              .output(System.out)
              .build()).run();
    } else if (arg1.equals("editor") || arg2.equals("editor")) {
      new Controller(model, new EditorView()).run();
    }
  }

}
