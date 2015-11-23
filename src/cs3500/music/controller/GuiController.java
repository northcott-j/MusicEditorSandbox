package cs3500.music.controller;


import cs3500.music.model.MusicEditorModel;
import cs3500.music.view.EditorView;
import cs3500.music.view.GuiView;
import cs3500.music.view.ViewModel;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * Controller for the Music Editor console UI. Mediates between the view and the model by
 * taking user input and acting on the view, and then taking information from the view and showing
 * it to the user.
 */
public final class GuiController implements Controller {

  // Data for the GuiController class
  private final MusicEditorModel model;
  private final ViewModel vm;
  private final GuiView view;
  // State trackers
  int curBeat = 0;
  Timer timer;
  // Input handling
  private InputHandler ih;
  private int pressedKey = 0;
  int curX, curY;
  // Boolean flag helping with invariants for keyhandling
  private boolean isPaused;

  /**
   * Constructs a controller for playing the given game model, with the given input and output for
   * communicating with the user.
   *
   * @param model0 the game to play
   */
  public GuiController(MusicEditorModel model0, GuiView view) {
    model = requireNonNull(model0);
    vm = adaptModelToViewModel(model);
    this.view = view;
    this.timer = new Timer(model.getTempo() / 1000, new SwingTimerActionListener());
    // Initial state: paused, and no position selected
    this.isPaused = true;
    this.curX = -1;
    this.curY = -1;
    ih = new InputHandler(this);
    // Takes you to the beginning of the piece .. "home"
    ih.addPressedEvent(36, view::goToStart);
    // Takes you to the end of the piece ........ "end"
    ih.addPressedEvent(35, view::goToEnd);
    // Scrolls upwards .......................... "up arrow"
    ih.addPressedEvent(38, view::scrollUp);
    // Scrolls downwards ........................ "down arrow"
    ih.addPressedEvent(40, view::scrollDown);
    // Scrolls left ............................. "left arrow"
    ih.addPressedEvent(37, view::scrollLeft);
    // Scrolls right ............................ "right arrow"
    ih.addPressedEvent(39, view::scrollRight);
    // Pauses/plays the music ................... "space"
    ih.addPressedEvent(32, () -> {
      if (this.isPaused) {
        this.isPaused = false;
        timer.start();
      } else {
        this.isPaused = true;
        timer.stop();
      }
    });
    // Allows for clicking to add notes ......... "a"
    ih.addPressedEvent(65, () -> {
      if (this.pressedKey == 65) {
        this.pressedKey = 0;
      }
      else {
        this.pressedKey = 65;
      }
    });
    // Allows for clicking to remove notes ...... "s"
    ih.addPressedEvent(83, () -> {
      if (this.pressedKey == 83) {
        this.pressedKey = 0;
      }
      else {
        this.pressedKey = 83;
      }
    });

    // TODO :: CHANGEPITCH, CHANGEDURATION
  }

  static Controller makeController(MusicEditorModel model, GuiView view) {
    return new GuiController(model, view);
  }

  @Override
  public void run() throws IOException {
    setKeyHandler(ih);
    setMouseHandler(ih);
    view.draw(vm);
  }

  private class SwingTimerActionListener implements ActionListener {
    public void actionPerformed(ActionEvent a) {

      if (view.drawn() && curBeat < vm.scoreLength()) {
        curBeat += 1;
        try {
          view.tickCurBeat(vm, curBeat);
        } catch (InvalidMidiDataException | IOException e) {
          throw new IllegalStateException("Something went wrong while playing");
        }
      }

    }
  }

  /**
   * Adapts a {@link MusicEditorModel} into a {@link ViewModel}. The adapted result shares state
   * with its adaptee.
   *
   * @param adaptee the {@code MusicEditorModel} to adapt
   * @return a {@code ViewModel} backed by {@code adaptee}
   */
  private static ViewModel adaptModelToViewModel(MusicEditorModel adaptee) {
    return ViewModel.makeViewModel(adaptee);
  }

  // TODO :: CHANGE FROM 30 TO CELL_SIZE
  @Override
  public void setCurrent(int x, int y) {
    int z = EditorView.CELL_SIZE;
    this.curX = x / z;
    this.curY = (y - z) / z;
  }

  @Override
  public boolean curSet() {
    return this.curX < 0;
  }

  @Override
  public boolean isPressed(int key) {
    return pressedKey == key;
  }

  @Override
  public void setKeyHandler(KeyListener kh) {
    this.view.setKeyHandler(kh);
  }

  @Override
  public void setMouseHandler(MouseListener mh) {
    this.view.setMouseHandler(mh);
  }

  @Override
  public void addNote() {
    /**
     * eventually::
     * model.addNote(
     *  - we have the x and y of the click in pixels within the window
     *  - default to a piano for now
     *  -
     */
    if (!this.isPaused) {
      // do shit
    }
    System.out.print("New note at pixels: (" + this.curX + ", " + this.curY + ")");
  }

  @Override
  public void removeNote() {
    System.out.println("Remove note at pixels: (" + this.curX + ", " + this.curY + ")");
  }

  @Override
  public void changeDuration(int newVal) {
    if (!this.isPaused) {
      int newDuration = newVal;
    }
  }
}
