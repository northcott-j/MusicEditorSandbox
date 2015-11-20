package cs3500.music.controller;


import cs3500.music.model.MusicEditorModel;
import cs3500.music.view.View;
import cs3500.music.view.ViewModel;

import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * Controller for the Connect <em>N</em> console UI. Mediates between the view and the model by
 * taking user input and acting on the view, and then taking information from the view and showing
 * it to the user.
 */
public final class GuiController implements Controller {

  // Fields for the GuiController class
  private final MusicEditorModel model;
  private final ViewModel vm;
  private final View view;
  private MouseHandler mh;
  private KeyboardHandler kh;
  private int curX, curY;

  /**
   * Constructs a controller for playing the given game model, with the given input and output for
   * communicating with the user.
   *
   * @param model0 the game to play
   */
  public GuiController(MusicEditorModel model0, View view) {
    model = requireNonNull(model0);
    vm = adaptModelToViewModel(model);
    this.view = view;
    mh = new MouseHandler(this);
    kh = new KeyboardHandler();
   /* // Takes you to the desired part of the piece
    Consumer<Integer> start = view.toStart(curX);          // TODO ;; IMPLEMENT THIS SHIT - MAYBE USE CONSUMERS INSTEAD
    Runnable end = view.toEnd();              // TODO ;; OR LAMBDA'S... WOULD BE LIKE THE BOTTOM OF THIS LIST
    // Traverses the view
    Runnable scrollLeft = view.scrollLeft();
    Runnable scrollRight = view.scrollRight();
    Runnable play = view.play();
    Runnable pause = view.pause();
    kh.addTypedEvent(36, start);           // "home"
    kh.addTypedEvent(35, end);             // "end"
    kh.addTypedEvent(226, scrollLeft);     // "left arrow"
    kh.addTypedEvent(227, scrollRight);    // "right arrow"
    kh.addTypedEvent(32, play);            // "space"
    kh.addTypedEvent(80, pause);           // "p"*/

    // TODO ;; HERE
    kh.addTypedEvent(1, ()-> {
      // write method shit here; has access to the model and whatnot through the view
      // only problem is that this still can't take arguments, because it wouldn't be a runnable
    });
  }

  static Controller makeController(MusicEditorModel model, View view) {
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
