package cs3500.music.controller;


import cs3500.music.model.MusicEditorModel;
import cs3500.music.view.GuiView;
import cs3500.music.view.ViewModel;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.util.Objects.requireNonNull;

/**
 * Controller for the Connect <em>N</em> console UI. Mediates between the view and the model by
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
  Timer timer = new Timer();
  // Input handling
  private InputHandler ih;
  private List<Integer> pressedKeys;
  private int curX, curY;
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
    this.isPaused = true;
    this.pressedKeys = new ArrayList<>();
    ih = new InputHandler(this);
    // Takes you to the beginning of the piece .. "home"
    ih.addTypedEvent(36, view::goToStart);
    // Takes you to the end of the piece ........ "end"
    ih.addTypedEvent(35, view::goToEnd);
    // Scrolls upwards .......................... "up arrow"
    ih.addTypedEvent(224, view::scrollUp);
    // Scrolls downwards ........................ "down arrow"
    ih.addTypedEvent(224, view::scrollDown);
    // Scrolls left ............................. "left arrow"
    ih.addTypedEvent(226, view::scrollLeft);
    // Scrolls right ............................ "right arrow"
    ih.addTypedEvent(227, view::scrollRight);
    // Pauses/plays the music ................... "space"
    ih.addTypedEvent(32, () -> {
      if (this.isPaused) {
        this.isPaused = false;
        // make it play shit
        // TODO :: play/pause
      } else {
        this.isPaused = true;
        // make it stop playing shit
      }
    });
    // Allows for clicking to add notes ......... "a"
    ih.addPressedEvent(65, ()-> {
      this.pressedKeys.clear();
      this.pressedKeys.add(65);
    });
    ih.addReleasedEvent(65, () -> {
      this.pressedKeys.remove(65);
    });
    // Allows for clicking to remove notes ...... "s"
    ih.addPressedEvent(83, ()-> {
      this.pressedKeys.clear();
      this.pressedKeys.add(83);
    });
    ih.addReleasedEvent(83, ()-> {
      this.pressedKeys.remove(83);
    });

    // TODO :: CHANGEPITCH, CHANGEDURATION
  }

  static Controller makeController(MusicEditorModel model, GuiView view) {
    return new GuiController(model, view);
  }

  @Override
  public void run() throws IOException {
    view.draw(vm);
    setKeyHandler(ih);
    setMouseHandler(ih);
    startTimer();
  }

  private void startTimer() {
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        if (view.drawn() && curBeat < vm.scoreLength()) {
          curBeat += 1;
          view.tickCurBeat(vm, curBeat);
        }
      }
    }, model.getTempo() / 1000, model.getTempo() / 1000);
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

  @Override
  public void setCurrent(int x, int y) {
    this.curX = x;
    this.curY = y;
  }

  @Override
  public boolean isPressed(int key) {
    return this.pressedKeys.contains(key);
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
}
