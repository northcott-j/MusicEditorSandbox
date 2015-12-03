package cs3500.music.controller;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

/**
 * Interface to connect all Controllers Created by Jonathan on 11/18/2015.
 */
public interface Controller {
  /**
   * Runs the Controller
   *
   * @throws java.io.IOException if there's an error
   */
  void run() throws IOException, InvalidMidiDataException;
}
