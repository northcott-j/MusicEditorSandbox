package cs3500.music.view;

import cs3500.music.controller.MouseHandler;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 * Sub-interface specifically for GUI's that listen to inputs Created by Jonathan on 11/18/2015.
 */
public interface GuiView extends View {
  /**
   * Checks to see if the board is rendered
   * @return true if board is drawn
   */
  boolean drawn();

  /**
   * Assigns the keyhandler to the View
   */
  void setKeyHandler(KeyListener kh);

  /**
   * Assigns the mousehandler to the View
   */
  void setMouseHandler(MouseListener mh);

  /**
   * Sets the current beat number
   * @param curBeat the new beat to be set at
   */
  void tickCurBeat(ViewModel vm, int curBeat);

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

}
