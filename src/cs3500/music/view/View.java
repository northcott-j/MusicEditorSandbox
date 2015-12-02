package cs3500.music.view;

import java.awt.*;
import javax.sound.midi.InvalidMidiDataException;


/**
 * To represent a {@code View} of Music Editor Composition
 */
public interface View {
  /**
   * To initialize the view of the composition
   */
  void initialize() throws InvalidMidiDataException;

  void updateTime();

  /**
   * What is the preferred dimension size of this view
   */
  Dimension getPreferredSize();


}

