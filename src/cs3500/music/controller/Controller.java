package cs3500.music.controller;

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

}
