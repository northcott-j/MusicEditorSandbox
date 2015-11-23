package cs3500.music.controller;

import cs3500.music.view.EditorView;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.midi.InvalidMidiDataException;

/**
 * Created by alexmelagrano on 11/19/15.
 */
public class InputHandler implements KeyListener, MouseListener {
  private GuiSpecificController controller;
  private HashMap<Integer, Runnable> typed;
  private HashMap<Integer, Runnable> pressed;
  private HashMap<Integer, Runnable> released;

  public InputHandler(GuiSpecificController controller) {
    this.controller = controller;
    this.typed = new HashMap<>();
    this.pressed = new HashMap<>();
    this.released = new HashMap<>();
  }

  /**
   * Handle the key typed event from the text field.
   */
  public void keyTyped(KeyEvent e) {
    if (this.typed.containsKey(e.getExtendedKeyCode())) {
      this.typed.get(e.getExtendedKeyCode()).run();
      System.out.println("Key typed: " + e.getKeyChar() + ", " + e.getExtendedKeyCode());
    }
  }

  /**
   * Handle the key-pressed event from the text field.
   */
  public void keyPressed(KeyEvent e) {
    if (this.pressed.containsKey(e.getExtendedKeyCode())) {
      this.pressed.get(e.getExtendedKeyCode()).run();
      System.out.println("Key pressed: " + e.getKeyChar() + ", " + e.getExtendedKeyCode());
    }
  }

  /**
   * Handle the key-released event from the text field.
   */
  public void keyReleased(KeyEvent e) {
    if (this.released.containsKey(e.getExtendedKeyCode())) {
      this.released.get(e.getExtendedKeyCode()).run();
      System.out.println("Key released: " + e.getKeyChar() + ", " + e.getExtendedKeyCode());
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
   * When the mouse is clicked, the method will check if any of the modifier keys are being pressed.
   * If a proper combination exists, it will run the corresponding method to perform the desired
   * activity. Otherwise, it will ignore the click.
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
        this.controller.addNote();
        // Prints out relevant data, then returns it to a default value
        System.out.println("Mouse pressed: " + (this.controller.getX() + 1) +
                ", " + this.controller.getY());
        System.out.println("   --> Tried to add a note.");
        this.controller.setCurrent(-1, -1);
      }
      // If the "s" key is being pressed, for removing notes
      if (this.controller.isPressed(83)) {
        this.controller.setCurrent(e.getX(), e.getY());
        this.controller.removeNote();
        // Prints out relevant data, then returns it to a default value
        System.out.println("Mouse pressed: " + (this.controller.getX() + 1) +
                ", " + this.controller.getY());
        System.out.println("   --> Tried to remove a note.");
        this.controller.setCurrent(-1, -1);
      }
      // If the "d" key is being pressed, for changing the start of notes
      if (this.controller.isPressed(68)) {
        // If the note hasn't been selected yet:
        if (!this.controller.curSet()) {
          this.controller.setCurrent(e.getX(), e.getY());
          System.out.println("Mouse pressed: " + (this.controller.getX() + 1) +
                  ", " + this.controller.getY());
          System.out.println("   --> Tried to select a note.");
        } else {
          // If it was selected, change the note
          this.controller.changeNoteStart(e.getX() / EditorView.CELL_SIZE);
          // Prints out relevant data, then returns it to a default value
          System.out.println("Mouse pressed: " + (this.controller.getX() + 1) +
                  ", " + this.controller.getY());
          System.out.println("   --> Tried to change a note's start beat to here.");
          this.controller.setCurrent(-1, -1);
        }
      }
      // If the "f" key is being pressed, for changing the end of notes
      if (this.controller.isPressed(70)) {
        // If the note hasn't been selected yet:
        if (!this.controller.curSet()) {
          this.controller.setCurrent(e.getX(), e.getY());
          System.out.println("Mouse pressed: " + (this.controller.getX() + 1) +
                  ", " + this.controller.getY());
          System.out.println("   --> Tried to select a note.");
        } else {
          // If it was selected, change the note
          this.controller.changeNoteEnd(e.getX() / EditorView.CELL_SIZE);
          // Prints out relevant data, then returns it to a default value
          System.out.println("Mouse pressed: " + (this.controller.getX() + 1) +
                  ", " + this.controller.getY());
          System.out.println("   --> Tried to change a note's end beat to here.");
          this.controller.setCurrent(-1, -1);
        }
      }
      // If the "r" key is being pressed, for moving a note
      if (this.controller.isPressed(82)) {
        // If the note hasn't been selected yet:
        if (!this.controller.curSet()) {
          this.controller.setCurrent(e.getX(), e.getY());
          System.out.println("Mouse pressed: " + (this.controller.getX() + 1) +
                  ", " + this.controller.getY());
          System.out.println("   --> Tried to select a note.");
        } else {
          // If it was selected, change the note
          this.controller.moveNote((e.getY() - EditorView.CELL_SIZE) / 30);
          // Prints out relevant data, then returns it to a default value
          System.out.println("Mouse pressed: " + (this.controller.getX() + 1) +
                  ", " + this.controller.getY());
          System.out.println("   --> Tried to move a note to here.");
          this.controller.setCurrent(-1, -1);
        }
      }
      // If the "c" key is being pressed, for changing the curBeat
      if (this.controller.isPressed(67)) {
        this.controller.setCurrent(e.getX(), e.getY());
        // If it was selected, change the note
        try {
          this.controller.changeCurBeat(this.controller.getX());
        } catch (InvalidMidiDataException | IOException a) {
          System.out.println("Couldn't change current beat");
        }
        // Prints out relevant data, then returns it to a default value
        System.out.println("Mouse pressed: " + (this.controller.getX() + 1) +
                ", " + this.controller.getY());
        System.out.println("   --> Tried to change Current Beat to here.");
        this.controller.setCurrent(-1, -1);
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
}
