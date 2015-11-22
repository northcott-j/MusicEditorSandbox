package cs3500.music.controller;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.IOException;

/**
 * Interface to connect all Controllers Created by Jonathan on 11/18/2015.
 */
// TODO: Need to fix Javadoc for all controllers

public interface Controller {
  /**
   * Runs the Controller
   *
   * @throws java.io.IOException if there's an error
   */
  void run() throws IOException;

  /* TODO:
  * Remove existing notes
  * Add new notes of various lengths
  * Move existing notes
  * Scroll with arrow keys
  * Jump to beginning and end with Home or End keys
   */

  /**
   * When dealing with mouse clicks, store the position of the click.
   *
   * @param x  x component of the click
   * @param y  y component of the click
   */
  void setCurrent(int x, int y);

  /**
   * Assigns the current view the proper keyhandler.
   */
  void setKeyHandler(KeyListener kh);

  /**
   * Assigns the current view the proper mousehandler.
   */
  void setMouseHandler(MouseListener mh);

  /**
   * Checks if the given key is being pressed down.
   *
   * @param key unicode for key of interest
   */
  boolean isPressed(int key);

  /**
   * Adds a new note to the model.
   */
  void addNote();

  /**
   * Removes a note from the model.
   */
  void removeNote();
}
