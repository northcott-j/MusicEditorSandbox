package cs3500.music.controller;


import cs3500.music.model.MusicEditorModel;
import cs3500.music.view.NonGuiViewAdapter;
import cs3500.music.model.ViewModel;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import static java.util.Objects.requireNonNull;

/**
 * Controller for the Console and MIDI views as they don't need any extra fields
 * to function, unlike controllers involving gui views.
 */
public final class NonGuiController implements Controller {

  // Fields for a NonGuiController
  private final MusicEditorModel model;
  private final ViewModel vm;
  private final NonGuiViewAdapter view;

  /**
   * Constructs a controller for playing the given game model, with the given input
   * and output for communicating with the user. Called by the makeController()
   * method.
   *
   * @param model0 the music to play
   * @param view   the view to draw
   */
  private NonGuiController(MusicEditorModel model0, ViewModel vm, NonGuiViewAdapter view) {
    model = requireNonNull(model0);
    this.vm = vm;
    this.view = view;
  }

  /**
   * Factory method for the creation of NonGuiControllers.
   *
   * @param model the music to play
   * @param view  the view to draw
   * @return a new instance of a NonGuiController
   */
  static Controller makeController(MusicEditorModel model, ViewModel vm, NonGuiViewAdapter view) {
    return new NonGuiController(model, vm, view);
  }

  @Override
  public void run() throws IOException, InvalidMidiDataException {
    view.draw(vm);
  }
}
