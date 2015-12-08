package cs3500.music.view;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.model.ViewModel;

/**
 * Interface to connect all of the Views
 * Created by Jonathan on 11/18/2015.
 */
public interface ViewExpansion {
  /**
   * Displays the state of the musicEditor for the user.
   *
   * @throws IOException if writing the output throws
   * @param vm the contents of the editor
   */
  void draw(ViewModel vm) throws IOException, InvalidMidiDataException;

}
