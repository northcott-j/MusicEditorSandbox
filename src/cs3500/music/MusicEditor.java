package cs3500.music;

import cs3500.music.controller.MainController;

import javax.sound.midi.InvalidMidiDataException;
import java.io.IOException;

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

  // TODO: Delete unused import statements
  public static void main(String[] args) throws IOException, InvalidMidiDataException {
    new MainController(args);
  }
}
