package cs3500.music.view;

import java.awt.*;

import javax.sound.midi.InvalidMidiDataException;


/**
 * To represent a {@code View} of Music Editor of a Composition
 */
public interface View {
  /**
   * To initialize the view of the composition
   */
  void initialize() throws InvalidMidiDataException;

  /**
   * Update the time of the given view based on the current time
   */
  void updateTime();

  /**
   * What is the preferred dimension size of this view
   *
   * @return the dimension of the preferred size of the composition view
   */
  Dimension getPreferredSize();


}

