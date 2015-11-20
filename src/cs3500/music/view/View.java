package cs3500.music.view;

import cs3500.music.controller.MouseHandler;

import java.awt.event.KeyListener;
import java.io.IOException;

/**
 * Interface to connect all of the Views
 * Created by Jonathan on 11/18/2015.
 */
public interface View {
// TODO: Views shouldn't know about Controllers (See assignment online)
  /**
   * Displays the state of the musicEditor for the user.
   *
   * @throws IOException if writing the output throws
   * @param vm the contents of the editor
   */
  void draw(ViewModel vm) throws IOException;

  // TODO :: CHECK IF THIS IS RIGHT
  /**
   * Assigns the keyhandler to the View
   */
  public void setKeyHandler(KeyListener kh);

  /**
   * Assigns the mousehandler to the View
   */
  public void setMouseHandler(MouseHandler mh);
}
