package cs3500.music.view;

import java.awt.*;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;


/**
 * To represent a {@code View} of Music Editor Composition
 */
public interface View {
  /**
   * To initialize the view of the composition
   */
  void initialize() throws InvalidMidiDataException, IOException;

  /**
   * To update the time which inturn updates the beat by taking an int from which point it
   * starts updating
   * @param time int which is the the starting time from which time starts incrementing
   */
  void updateTime(int time);

  /**
   * What is the current beat of this composition? At what time is this composition being played
   *
   * @return the integer representation of this piece
   */
  int getCurrTime();

  /**
   * What is the preferred dimension size of this view
   */
  Dimension getPreferredSize();


}

