package cs3500.music.view;

import java.awt.event.KeyListener;

import cs3500.music.controller.MouseHandler;

/**
 * Sub-interface specifically for GUI's that listen to inputs
 * Created by Jonathan on 11/18/2015.
 */
public interface GuiView extends View {
  // TODO :: CHECK IF THIS IS RIGHT
  /**
   * Assigns the keyhandler to the View
   */
  void setKeyHandler(KeyListener kh);

  /**
   * Assigns the mousehandler to the View
   */
  void setMouseHandler(MouseHandler mh);
}
