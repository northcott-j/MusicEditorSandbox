package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;

/**
 * To represent a GuiView
 */
public interface GuiView extends View {

  /**
   * Update the time of this view to match that of the timer; incrementing by the current time
   */
  void updateTime(int time);

  /**
   * Update the view based on the current time
   */
  void updateView();

  /**
   * What is the preferred cell size of the gui?
   *
   * @return integer representation of the constant of the cell size
   */
  int getCellSIze();

  /**
   * What is the preferred X-padding of the gui?
   *
   * @return integer representation of the constant XPADDING
   */
  int getXPadding();

  /**
   * What is the preferred Y-padding of the gui?
   *
   * @return integer representation of the constant YPADDING
   */
  int getYPadding();

  /**
   * Initialize this view as a clean way to call everything
   *
   * @throws InvalidMidiDataException when the midi data is invalid
   */
  void initialize() throws InvalidMidiDataException, IOException;

  /**
   * What is the current beat of this composition? At what time is this composition being played
   *
   * @return the integer representation of this piece
   */
  int getCurrTime();

  /**
   * Add a key listener to the guiView
   *
   * @param e a KeyListener to base changes on
   */
  void addKeyListener(KeyListener e) throws IOException;

  /**
   * Remove a key listener to the guiView
   *
   * @param e a KeyListener to base changes on
   */
  void removeKeyListener(KeyListener e);

  /**
   * Add a mouse listener to the guiView
   *
   * @param m a MouseListener to base changes on
   */
  void addMouseListener(MouseListener m) throws IOException;

  /**
   * Remove a mouse listener to the guiView
   *
   * @param m a MouseListener to base changes on
   */
  void removeMouseListener(MouseListener m);

  /**
   * What is the highest pitch of the model to be represented in the gui?
   *
   * @return the highest pitch
   */
  int getHighestPitch();

  /**
   * For scrolling abilities
   *
   * @return a scrollable pane
   */
  JScrollPane getScroll();

  /**
   * Update the view so that scrolling works accordingly
   */
  void updateScroll();
}
