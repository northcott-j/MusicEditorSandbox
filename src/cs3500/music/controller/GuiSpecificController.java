package cs3500.music.controller;

import javax.sound.midi.InvalidMidiDataException;
import java.awt.event.InputEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.IOException;

/**
 * Interface for Controller methods specifically for GUI views
 * Created by Jonathan on 11/23/2015.
 */
public interface GuiSpecificController extends Controller {

  /**
   * When dealing with mouse clicks, store the position of the click. Converts from
   * pixels to notes positions, where x will become the beat number and y the number
   * of pitches it is away from the highest note.
   *
   * @param x  x component of the click, in pixels
   * @param y  y component of the click, in pixels
   */
  void setCurrent(int x, int y);

  /**
   * Checks if a position has been selected.
   *
   * @return whether or not a position was selected
   */
  boolean curSet();

  /**
   * Getter methods for the current position of the mouse; for printing purposes.
   * @return x or y field
   */
  int getX();
  int getY();

  /**
   * Assigns the current view the proper keyhandler.
   */
  void setKeyHandler(KeyListener kh);

  /**
   * Assigns the current view the proper mousehandler.
   */
  void setMouseHandler(MouseListener mh);

  /**
   * This method is responsible for adding data into the log field for testing and
   * debugging purposes.
   *
   * @param input the string to be added to the input log
   * @throws IOException no input
   */
  void append(String input) throws IOException;

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

  /**
   * Changes the start beat of the currently selected note to the given startpoint.
   */
  void changeNoteStart(int newStart);

  /**
   * Changes the end beat of the currently selected note to the given endpoint.
   */
  void changeNoteEnd(int newEnd);

  /**
   * Moves a note
   * @param newLocation the new location of the note
   */
  void moveNote(int newLocation);

  /**
   * Change the Current Beat
   */
  void changeCurBeat(int newBeat) throws InvalidMidiDataException, IOException;

  /**
   * Sends a mock event to the InputHandler; for testing purposes.
   *
   * @param type  helps delegate whether it's a key or mouse input
   * @param e     the key or mouse event to be passed
   */
  void mockEvent(String type, InputEvent e);

  /**
   * Prints the input log stored within the controller.
   */
  String printLog();
}
