package cs3500.test;

import cs3500.music.controller.GuiController;
import cs3500.music.controller.GuiSpecificController;
import cs3500.music.model.MusicEditorImpl;
import cs3500.music.view.PlaybackView;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

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
          (GuiSpecificController)GuiController.makeController(model, new PlaybackView());
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

  // TODO :: FIGURE OUT HOW TO EMULATE KEY AND MOUSE EVENTS (POTENTIALLY USE A ROBOT; BELOW)
  // --> compiler errors are coming from the mouse event constructors

  /** Testing the usage of the "a" key used to enter/exit the addNote mode. */
  @Test
  public void testKeyA() {
    KeyEvent k = new KeyEvent(new Button(), 401, 0, 0, KeyEvent.VK_A);

    // Test initial state
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(65), false);
    // Adding the key event, and adding the corresponding message to the expected log
    controller.mockEvent("Key", k);
    expected.append("Key pressed: a, 65\n");
    // Testing for effect
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(83), false);
  }

  /** Testing the delegation of a mouse click to add a note when in that mode. */
//  @Test
//  public void testClickA() {
//    MouseEvent m = new MouseEvent();
//
//    // Test initial state
//    assertEquals(expected.toString(), this.controller.printLog());
//    // Adding the key event, and adding the corresponding message to the expected log
//    controller.mockEvent("Mouse", m);
//    expected.append("Mouse pressed: X, Y\n" + "   --> Tried to add a note.\n");
//    // Testing for effect
//    assertEquals(expected.toString(), this.controller.printLog());
//  }

  /** Testing the usage of the "s" key used to enter/exit the removeNote mode. */
  @Test
  public void testKeyS() {
    KeyEvent k = new KeyEvent(new Button(), 401, 0, 0, KeyEvent.VK_S);

    // Test initial state
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(83), false);
    // Adding the key event, and adding the corresponding message to the expected log
    controller.mockEvent("Key", k);
    expected.append("Key pressed: s, 83\n");
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
    expected.append("Key pressed: s, 68\n");
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
    expected.append("Key pressed: s, 70\n");
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
    expected.append("Key pressed: s, 87\n");
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
    expected.append("Key pressed: s, 69\n");
    // Testing for effect
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(69), true);
  }

  // TODO :: TEST MOUSE + E

  /** Testing the usage of the "r" key used to enter/exit the moveNote mode. */
  @Test
  public void testKeyR() {
    KeyEvent k = new KeyEvent(new Button(), 401, 0, 0, KeyEvent.VK_R);

    // Test initial state
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(82), false);
    // Adding the key event, and adding the corresponding message to the expected log
    controller.mockEvent("Key", k);
    expected.append("Key pressed: s, 82\n");
    // Testing for effect
    assertEquals(expected.toString(), this.controller.printLog());
    assertEquals(this.controller.isPressed(82), true);
  }

  // TODO :: TEST MOUSE + R

}
