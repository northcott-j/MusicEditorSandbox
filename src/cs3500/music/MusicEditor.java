package cs3500.music;

import cs3500.music.controller.MainController;
import javax.sound.midi.InvalidMidiDataException;
import java.io.IOException;

/**
 * This is the main method to run the music editor.
 */
public class MusicEditor {
  /**
   * The main method for displaying a desired piece through the desired view type.
   * View types include:
   * - console : outputs the music file in text format
   * - midi : outputs the music file in
   * audio format
   * - gui : outputs the music file in a visual format
   * - playback : outputs the music file as an interactive program, which displays
   * the music visually and can play it audibly upon request
   *
   * Will throw appropriate exceptions for invalid inputs. The order of the inputs
   * does not matter.
   *
   * @param args the name of a piece, and the desired view
   * @throws IOException              invalid inputs
   * @throws InvalidMidiDataException invalid data within the MIDI view
   */
  public static void main(String[] args) throws IOException, InvalidMidiDataException {
    new MainController(args);
  }
}
