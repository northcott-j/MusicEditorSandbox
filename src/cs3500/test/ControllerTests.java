package cs3500.test;

import cs3500.music.controller.GuiController;
import cs3500.music.controller.GuiSpecificController;
import cs3500.music.model.MusicEditorImpl;
import cs3500.music.view.PlaybackView;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import static org.junit.Assert.assertEquals;


/**
 * Created by alexmelagrano on 11/23/15.
 *
 * This test class deals with the GuiController and its interfaces, as well as how
 * the InputHandler interacts with that type of controller.
 */
public class ControllerTests {

  // Creates the initial state of the data to be interacted with and tested
  MusicEditorImpl model = MusicEditorImpl.makeEditor();
  GuiSpecificController controller =
          (GuiSpecificController)GuiController.makeController(model, new PlaybackView(), "test");
  // This will serve as the mock log to be tested against the one within the controller
  StringBuilder expected = new StringBuilder();

  /**
   * The testing of the InputHandler methods follow this routine:
   * - send a fake input into the controller
   * - calls the methods/Runnables within the InputHandler, updating the log
   * - add the expected log addition to the StringBuilder field {@code expected}
   * - compare their string versions to see if it tried to perform the correct action
   *
   * After the test verifies that the keys work for selecting the modes, a test will
   * follow which verifies the actuation of a mouse click within that mode.
   */

  /** Testing the usage of the "a" key used to enter/exit the addNote mode, as well
   * as the mouse's ability to add a note under this mode. */
  @Test
  public void testAddNote() throws IOException {
    KeyEvent k = new KeyEvent(new Button(), 401, 0, 0, KeyEvent.VK_A);
    MouseEvent m = new MouseEvent(new Button(), MouseEvent.BUTTON1, 0, 0, 40, 20, 1, false);

    // Test initial state
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(65), false);
    // Adding the key event, and adding the corresponding message to the expected log
    this.controller.mockEvent("Key", k);
    this.expected.append("Key pressed: A, 65\n");
    // Testing for effect
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(83), false);

    // Test initial state
    assertEquals(expected.toString(), this.controller.printLog());
    // Adding the key event, and adding the corresponding message to the expected log
    controller.mockEvent("Mouse", m);
    expected.append("Mouse pressed: 2, 0\n" + "   --> Tried to add a note.\n");
    // Testing for effect
    assertEquals(expected.toString(), this.controller.printLog());
  }

  /** Testing the usage of the "s" key used to enter/exit the removeNote mode, as well
   * the mouse's ability to remove a note under this mode. */
  @Test
  public void testRemoveNote() {
    KeyEvent k = new KeyEvent(new Button(), 401, 0, 0, KeyEvent.VK_S);

    // Test initial state
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(83), false);
    // Adding the key event, and adding the corresponding message to the expected log
    controller.mockEvent("Key", k);
    expected.append("Key pressed: S, 83\n");
    // Testing for effect
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(83), true);
  }

  /** Testing the delegation of a mouse click to remove a note when in that mode. */
//  @Test
//  public void testClickS() {
//    MouseEvent m = new MouseEvent();
//
//    // Test initial state
//    assertEquals(expected.toString(), this.controller.printLog());
//    // Adding the key event, and adding the corresponding message to the expected log
//    controller.mockEvent("Mouse", m);
//    expected.append("Mouse pressed: X, Y\n" + "   --> Tried to remove a note.\n");
//    // Testing for effect
//    assertEquals(expected.toString(), this.controller.printLog());
//  }

  /** Testing the usage of the "d" key used to enter/exit the changeNoteStart mode. */
  @Test
  public void testKeyD() {
    KeyEvent k = new KeyEvent(new Button(), 401, 0, 0, KeyEvent.VK_D);

    // Test initial state
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(68), false);
    // Adding the key event, and adding the corresponding message to the expected log
    controller.mockEvent("Key", k);
    expected.append("Key pressed: D, 68\n");
    // Testing for effect
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(68), true);
  }

  /** Testing the delegation of a mouse click to edit the start of a note when in the
   * changeNoteStart mode. */
//  @Test
//  public void testClickD() {
//    MouseEvent m = new MouseEvent();
//
//    // Test initial state
//    assertEquals(expected.toString(), this.controller.printLog());
//    // Adding the key event, and adding the corresponding message to the expected log
//    // Stage one: selecting the note to edit
//    controller.mockEvent("Mouse", m);
//    expected.append("Mouse pressed: X, Y\n" + "   --> Tried to select a note.\n");
//    // Testing for effect
//    assertEquals(expected.toString(), this.controller.printLog());
//    // Adding the key event, and adding the corresponding message to the expected log
//    // Stage two: editing the selected note
//    controller.mockEvent("Mouse", m);
//    expected.append("Mouse pressed: X, Y\n" + "   --> Tried to change a note's start beat to here.\n");
//    // Testing for effect
//    assertEquals(expected.toString(), this.controller.printLog());
//  }

  /** Testing the usage of the "f" key used to enter/exit the changeNoteEnd mode. */
  @Test
  public void testKeyF() {
    KeyEvent k = new KeyEvent(new Button(), 401, 0, 0, KeyEvent.VK_F);

    // Test initial state
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(70), false);
    // Adding the key event, and adding the corresponding message to the expected log
    controller.mockEvent("Key", k);
    expected.append("Key pressed: F, 70\n");
    // Testing for effect
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(70), true);
  }

  // TODO :: TEST MOUSE + F

  /** Testing the usage of the "w" key used to enter/exit the addNote mode, for
   * percussion notes. */
  @Test
  public void testKeyW() {
    KeyEvent k = new KeyEvent(new Button(), 401, 0, 0, KeyEvent.VK_W);

    // Test initial state
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(87), false);
    // Adding the key event, and adding the corresponding message to the expected log
    controller.mockEvent("Key", k);
    expected.append("Key pressed: W, 87\n");
    // Testing for effect
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(87), true);
  }

  // TODO :: TEST MOUSE + W

  /** Testing the usage of the "e" key used to enter/exit the changeCurBeat mode. */
  @Test
  public void testKeyE() {
    KeyEvent k = new KeyEvent(new Button(), 401, 0, 0, KeyEvent.VK_E);

    // Test initial state
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(69), false);
    // Adding the key event, and adding the corresponding message to the expected log
    controller.mockEvent("Key", k);
    expected.append("Key pressed: E, 69\n");
    // Testing for effect
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(69), true);
  }

  // TODO :: TEST MOUSE + E

  /** Testing the usage of the "q" key used to enter/exit the moveNote mode. */
  @Test
  public void testKeyQ() {
    KeyEvent k = new KeyEvent(new Button(), 401, 0, 0, KeyEvent.VK_Q);

    // Test initial state
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(81), false);
    // Adding the key event, and adding the corresponding message to the expected log
    controller.mockEvent("Key", k);
    expected.append("Key pressed: Q, 81\n");
    // Testing for effect
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(81), true);
  }

  // TODO :: TEST MOUSE + Q

}
