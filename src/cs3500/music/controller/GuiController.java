package cs3500.music.controller;


import cs3500.music.model.MusicEditorModel;
import cs3500.music.view.GuiView;
import cs3500.music.view.ViewModel;

import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Timer;

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
  Timer 
  // Input handling
  private MouseHandler mh;
  private KeyboardHandler kh;
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
    mh = new MouseHandler(this);
    kh = new KeyboardHandler();
    // Takes you to the beginning of the piece .. "home"
    kh.addTypedEvent(36, ()-> {
      view.goToStart();
    });
    // Takes you to the end of the piece ........ "end"
    kh.addTypedEvent(35, ()-> {
      view.goToEnd();
    });
    // Scrolls upwards .......................... "up arrow"
    kh.addTypedEvent(224, ()-> {
      view.scrollUp();
    });
    // Scrolls downwards ........................ "down arrow"
    kh.addTypedEvent(224, ()-> {
      view.scrollDown();
    });
    // Scrolls left ............................. "left arrow"
    kh.addTypedEvent(226, ()-> {
      view.scrollLeft();
    });
    // Scrolls right ............................ "right arrow"
    kh.addTypedEvent(227, ()-> {
      view.scrollRight();
    });
    // Pauses/plays the music ................... "space"
    kh.addTypedEvent(32, ()-> {
      if (this.isPaused) {
        this.isPaused = false;
        // make it play shit
      } else {
        this.isPaused = true;
        // make it stop playing shit
      }
    });

    // TODO :: ADDNOTE, REMOVENOTE, CHANGEPITCH, CHANGEDURATION
  }

  static Controller makeController(MusicEditorModel model, GuiView view) {
    return new GuiController(model, view);
  }


  @Override
  public void setCurrent(int x, int y) {
    this.curX = x;
    this.curY = y;
  }

  // TODO :: CHECK IF THIS IS RIGHT
  @Override
  public void setKeyHandler(KeyListener kh) {
    this.view.setKeyHandler(kh);
  }

  @Override
  public void setMouseHandler(MouseHandler mh) {
    this.view.setMouseHandler(mh);
  }

  @Override
  public void run() throws IOException {
    // TODO: Uncomment While later on
    // TODO: Figure out how to limit while
    //while (true) {
    view.draw(vm);
    //}
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
}
