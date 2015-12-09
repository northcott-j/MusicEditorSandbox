package cs3500.music.controller;


import javax.sound.midi.InvalidMidiDataException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;

/**
 * Input Handler class to delegate keys and mouse events
 * Created by alexmelagrano on 11/19/15.
 */
public class InputHandler implements KeyListener, MouseListener {

  // Fields for an InputHandler
  private GuiSpecificController controller;
  private HashMap<Integer, Runnable> typed;
  private HashMap<Integer, Runnable> pressed;
  private HashMap<Integer, Runnable> released;
  private Appendable log;
  private int CELL_SIZE;
  private int X_PADDING;
  private int Y_PADDING;

  /**
   * A default constructor for an InputHandler. Will call the the more complex
   * version, but with the standard Appendable for the log field of "System.out".
   * @param controller
   */
  public InputHandler(GuiSpecificController controller) {
    this(controller, System.out);
  }

  /**
   * A more specific constructor for an InputHandler. Is called by the default
   * constructor, which uses "System.out" as the Appendable for the log field. Here,
   * however, a custom InputHandler can be created with a specific Appendable, which
   * can be used for testing and debugging purposes.
   *
   * @param controller controller to link the event responses to
   * @param log        the Appendable to attach the input messages to
   */
  public InputHandler(GuiSpecificController controller, Appendable log) {
    this.controller = controller;
    CELL_SIZE = controller.getCellSize();
    X_PADDING = controller.getXpad();
    Y_PADDING = controller.getYpad();
    this.typed = new HashMap<>();
    this.pressed = new HashMap<>();
    this.released = new HashMap<>();
    this.log = log;
  }

  /**
   * Handle the key typed event from the text field.
   */
  public void keyTyped(KeyEvent e) {
    if (this.typed.containsKey(e.getKeyCode())) {
      this.print("Key typed: " + e.getKeyChar() + ", " + e.getKeyCode());
      this.typed.get(e.getKeyCode()).run();
    }
  }

  /**
   * Handle the key-pressed event from the text field.
   */
  public void keyPressed(KeyEvent e) {
    if (this.pressed.containsKey(e.getKeyCode())) {
      this.print("Key pressed: " + e.getKeyChar() + ", " + e.getKeyCode());
      this.pressed.get(e.getKeyCode()).run();
    } else {
      this.print("Not a supported key.");
    }
  }

  /**
   * Handle the key-released event from the text field.
   */
  public void keyReleased(KeyEvent e) {
    if (this.released.containsKey(e.getKeyCode())) {
      this.print("Key released: " + e.getKeyChar() + ", " + e.getKeyCode());
      this.released.get(e.getKeyCode()).run();
    }
  }

  /**
   * Adds a new key event and its corresponding action into their respective map field.
   *
   * @param e key id
   * @param r runnable action corresponding to the key
   */
  public void addTypedEvent(int e, Runnable r) {
    this.typed.put(e, r);
  }

  public void addPressedEvent(int e, Runnable r) {
    this.pressed.put(e, r);
  }

  public void addReleasedEvent(int e, Runnable r) {
    this.released.put(e, r);
  }

  /**
   * When the mouse is clicked, the method will check if any of the modifier keys
   * are being pressed. If a proper combination exists, it will run the corresponding
   * method to perform the desired activity. Otherwise, it will ignore the click.
   *
   * @param e mouse event
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    // If the left mouse button was clicked:
    if (e.getButton() == MouseEvent.BUTTON1) {
      // If the "a" key is being pressed, for adding notes
      if (this.controller.isPressed(65)) {
        this.controller.setCurrent(e.getX(), e.getY());
        this.print("Mouse pressed: " + (this.controller.getX() + 1) +
                ", " + this.controller.getY() + "\n" + "   --> Tried to add a note.");
        this.controller.addNote();
        // Prints out relevant data, then returns it to a default value
        this.controller.setCurrent(-1, -1);
      }
      // If the "w" key is being pressed, for adding percussion notes
      if (this.controller.isPressed(87)) {
        this.controller.setCurrent(e.getX(), e.getY());
        this.print("Mouse pressed: " + (this.controller.getX() + 1) +
                ", " + this.controller.getY() + "\n" + "   --> Tried to add a note.");
        this.controller.addNote();
        // Prints out relevant data, then returns it to a default value
        this.controller.setCurrent(-1, -1);
      }
      // If the "s" key is being pressed, for removing notes
      if (this.controller.isPressed(83)) {
        this.controller.setCurrent(e.getX(), e.getY());
        this.print("Mouse pressed: " + (this.controller.getX() + 1) +
                ", " + this.controller.getY() + "\n" + "   --> Tried to remove a note.");
        this.controller.removeNote();
        // Prints out relevant data, then returns it to a default value
        this.controller.setCurrent(-1, -1);
      }
      // If the "d" key is being pressed, for changing the start of notes
      if (this.controller.isPressed(68)) {
        // If the note hasn't been selected yet:
        if (!this.controller.curSet()) {
          this.controller.setCurrent(e.getX(), e.getY());
          this.print("Mouse pressed: " + (this.controller.getX() + 1) +
                  ", " + this.controller.getY() + "\n" + "   --> Tried to select a note.");
        } else {
          // If it was selected, change the note
          this.print("Mouse pressed: " + (this.controller.getX() + 1) +
                  ", " + this.controller.getY() + "\n"
                  + "   --> Tried to change a note's start beat to here.");
          this.controller.changeNoteStart((e.getX() - X_PADDING) / CELL_SIZE);
          // Prints out relevant data, then returns it to a default value
          this.controller.setCurrent(-1, -1);
        }
      }
      // If the "f" key is being pressed, for changing the end of notes
      if (this.controller.isPressed(70)) {
        // If the note hasn't been selected yet:
        if (!this.controller.curSet()) {
          this.controller.setCurrent(e.getX(), e.getY());
          this.print("Mouse pressed: " + (this.controller.getX() + 1) +
                  ", " + this.controller.getY() + "\n" + "   --> Tried to select a note.");
        } else {
          // If it was selected, change the note
          this.print("Mouse pressed: " + (this.controller.getX() + 1) +
                  ", " + this.controller.getY() + "\n" +
                  "   --> Tried to change a note's end beat to here.");
          this.controller.changeNoteEnd((e.getX() - X_PADDING) / CELL_SIZE);
          // Prints out relevant data, then returns it to a default value
          this.controller.setCurrent(-1, -1);
        }
      }
      // If the "q" key is being pressed, for changing the entire position of notes
      if (this.controller.isPressed(81)) {
        // If the note hasn't been selected yet:
        if (!this.controller.curSet()) {
          this.controller.setCurrent(e.getX(), e.getY());
          this.print("Mouse pressed: " + (this.controller.getX() + 1) +
                  ", " + this.controller.getY() + "\n" + "   --> Tried to select a note.");
        } else {
          // If it was selected, move the note to the new position
          this.print("Mouse pressed: " + (this.controller.getX() + 1) +
                  ", " + this.controller.getY() + "\n"
                  + "   --> Tried to change a note's location to here.");
          this.controller.moveNote((e.getX() - X_PADDING) / CELL_SIZE,
                  (e.getY() - Y_PADDING) / CELL_SIZE);
          // Prints out relevant data, then returns it to a default value
          this.controller.setCurrent(-1, -1);
        }
      }
      // If the "e" key is being pressed, for changing the curBeat
      if (this.controller.isPressed(69)) {
        this.controller.setCurrent(e.getX(), e.getY());
        // If it was selected, change the note
        try {
          this.print("Mouse pressed: " + (this.controller.getX() + 1) +
                  ", " + this.controller.getY() + "\n" + "   --> Changed the Current " +
                  "Beat to here.");
          this.controller.changeCurBeat(this.controller.getX());
          // Prints out relevant data, then returns it to a default value
          this.controller.setCurrent(-1, -1);
        } catch (InvalidMidiDataException | IOException | IndexOutOfBoundsException a) {
          this.print("Couldn't change current beat to here.");
        }
      }
    }
  }


  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  private void print(String message) {
    try {
      this.log = this.log.append(message).append("\n");
    } catch (IOException e) {
      // There is nothing to append, ignore
    }
  }


  protected String printData() {
    return this.log.toString();
  }




//  /**
//   * Handles the printing and appending of input data.
//   */
//  private void print(String input) {
//    try {
//      this.log.append(input).append("\n");
//    } catch (IOException x) {
//      // If there is nothing to append, ignore
//    }
//  }

//  /**
//   * Prints the input log.
//   */
//  public String printLog() {
//    return this.toString();
//  }
}
