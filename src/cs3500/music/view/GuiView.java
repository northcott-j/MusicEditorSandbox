package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

/**
 * Sub-interface specifically for GUI's that listen to inputs Created by Jonathan on 11/18/2015.
 */
public interface GuiView extends View {
  /**
   * Checks to see if the board is rendered
   *
   * @return true if board is drawn
   */
  boolean drawn();

  /**
   * Redraws the board after an update
   */
  void repaint();

  /**
   * Assigns the keyhandler to the View
   */
  void setKeyHandler(KeyListener kh);

  /**
   * Get note range
   *
   * @return the list of the string of notes in range
   */
  List<String> getNotesInRange();

  /**
   * Assigns the mousehandler to the View
   */
  void setMouseHandler(MouseListener mh);

  /**
   * Sets the current beat number
   *
   * @param curBeat the new beat to be set at
   */
  void tickCurBeat(ViewModel vm, int curBeat) throws InvalidMidiDataException, IOException;

  /**
   * Takes you to the desired part of the piece {beginning, or end}.
   */
  void goToStart();

  void goToEnd();

  /**
   * Allows you to navigate the composition.
   */
  void scrollUp();

  void scrollDown();

  void scrollLeft();

  void scrollRight();

  /**
   * Allows you to expand the board
   */
  void expandUp(ViewModel vm);

  void expandDown(ViewModel vm);

  void expandOut(ViewModel vm);

}
