package cs3500.music.controller;


import java.io.IOException;
import java.io.InputStream;

import cs3500.music.model.MusicEditorModel;
import cs3500.music.view.EditorView;
import cs3500.music.view.View;
import cs3500.music.view.ViewModel;

import static java.util.Objects.requireNonNull;

/**
 * Controller for the Connect <em>N</em> console UI. Mediates between the view and the model by
 * taking user input and acting on the view, and then taking information from the view and showing
 * it to the user.
 */
public final class EditorController implements Controller {
  /**
   * Constructs a controller for playing the given game model. Uses stdin and stdout as the user's
   * console.
   *
   * @param model the game to play
   */
  public EditorController(MusicEditorModel model) {
    this(model, System.in, System.out);
  }

  /**
   * Constructs a controller for playing the given game model, with the given input and output for
   * communicating with the user.
   *
   * @param model0 the game to play
   * @param in     where to read user input from
   * @param out    where to send output to for the user to see
   */
  public EditorController(MusicEditorModel model0, InputStream in, Appendable out) {
    model = requireNonNull(model0);
    vm = adaptModelToViewModel(model);
    view = new EditorView();
  }

  private final MusicEditorModel model;
  private final ViewModel vm;
  private final View view;

  @Override
  public void run() throws IOException {
    // TODO: Uncomment While later on
    //while (true) {
    view.draw(vm);
    listen();
    //}
  }

  @Override
  public void listen() throws IOException {

    // This loop tries repeatedly to read user input and make a move until it
    // succeeds. If there is a problem either reading or executing, the
    // exception is caught, an informative message displayed, and then the
    // loop repeats to try again.
    for (; ; ) {
      return;
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
}
