package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Input Handler class to delegate keys and mouse events
 * Created by alexmelagrano on 11/19/15.
 */
public class InputHandler implements KeyListener, MouseListener {

  // Fields for an InputHandler
  private GuiSpecificController controller;
  private HashMap<Integer, Consumer<Integer>> typed;
  private HashMap<Integer, Consumer<Integer>> pressed;
  private HashMap<Integer, Consumer<Integer>> released;
  private HashMap<Integer, Consumer<MouseEvent>> clicked;
  private Appendable log;

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
    this.typed = new HashMap<>();
    this.pressed = new HashMap<>();
    this.released = new HashMap<>();
    this.clicked = new HashMap<>();
    this.log = log;
  }

  /**
   * Handle the key typed event from the text field.
   */
  public void keyTyped(KeyEvent e) {
    int k = e.getKeyCode();
    if (this.typed.containsKey(k)) {
      this.typed.get(k).accept(k);
    }
  }

  /**
   * Handle the key-pressed event from the text field.
   */
  public void keyPressed(KeyEvent e) {
    int k = e.getKeyCode();
    if (this.pressed.containsKey(k)) {
      this.pressed.get(k).accept(k);
    } else {
      this.print("Not a supported key.");
    }
  }

  /**
   * Handle the key-released event from the text field.
   */
  public void keyReleased(KeyEvent e) {
    int k = e.getKeyCode();
    if (this.released.containsKey(k)) {
      this.released.get(k).accept(k);
    }
  }

  /**
   * Adds a new key event and its corresponding action into their respective map field.
   *
   * @param e key id
   * @param c runnable action corresponding to the key (a Consumer<Integer> that
   *          takes the key code)
   */
  public void addTypedEvent(int e, Consumer<Integer> c) {
    this.typed.put(e, c);
  }

  public void addPressedEvent(int e, Consumer<Integer> c) {
    this.pressed.put(e, c);
  }

  public void addReleasedEvent(int e, Consumer<Integer> c) {
    this.released.put(e, c);
  }

  public void addClickedEvent(int e, Consumer<MouseEvent> c) {
    this.clicked.put(e, c);
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

      // If the current pressed key has an action associated with mouse clicks, run it
      int key = this.controller.getPressed();
      if (this.clicked.containsKey(key)) {
        this.clicked.get(key).accept(e);
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

  /**
   * Appends the given message and a line break into the log.
   *
   * @param message message to be appended
   */
  void print(String message) {
    try {
      this.log = this.log.append(message).append("\n");
    } catch (IOException e) {
      // There is nothing to append, ignore
    }
  }

  /**
   * Prints the data stored in the log.
   *
   * @return log data
   */
  protected String printData() {
    return this.log.toString();
  }
}
