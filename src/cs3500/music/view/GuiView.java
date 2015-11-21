package cs3500.music.view;

import cs3500.music.controller.MouseHandler;

import java.awt.event.KeyListener;

/**
 * Sub-interface specifically for GUI's that listen to inputs
 * Created by Jonathan on 11/18/2015.
 */
public interface GuiView extends View {
  /**
   * Assigns the keyhandler to the View
   */
  void setKeyHandler(KeyListener kh);

  /**
   * Assigns the mousehandler to the View
   */
  void setMouseHandler(MouseHandler mh);

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
