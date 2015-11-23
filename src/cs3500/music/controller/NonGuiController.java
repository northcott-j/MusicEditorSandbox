package cs3500.music.controller;


import cs3500.music.model.MusicEditorModel;
import cs3500.music.view.View;
import cs3500.music.view.ViewModel;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * Controller for the Console and MIDI views as they don't need any extra fields to function
 */
public final class NonGuiController implements Controller {

  /**
   * Constructs a controller for playing the given game model, with the given input and output for
   * communicating with the user.
   *
   * @param model0 the game to play
   */
  private NonGuiController(MusicEditorModel model0, View view) {
    model = requireNonNull(model0);
    vm = adaptModelToViewModel(model);
    this.view = view;
  }

  private final MusicEditorModel model;
  private final ViewModel vm;
  private final View view;

  static Controller makeController(MusicEditorModel model, View view) {
    return new NonGuiController(model, view);
  }

  @Override
  public void run() throws IOException {
    view.draw(vm);
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
