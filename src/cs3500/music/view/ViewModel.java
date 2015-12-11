package cs3500.music.view;

/**
 * Class that adapts the model to the view Created by alexmelagrano on 11/9/15.
 */

import cs3500.music.model.AbstractNote;
import cs3500.music.model.MusicEditorModel;

import java.util.Collection;
import java.util.List;


/**
 * This abstract view class serves as the connecting point
 * for all other views, as it is where the
 * data is stored. It is designed to remove the
 * direct dependency of the view on the model; instead,
 * the controller should implement this interface
 * with information from the view and then passes the
 * {@code ViewModel} object to an extended class for the view to render concretely.
 */
public abstract class ViewModel {
  private final MusicEditorModel musicModel;

  public ViewModel(MusicEditorModel model) {
    this.musicModel = model;
  }

  /**
   * The amount of beats in the array
   *
   * @return the length of the score in beats
   */
  public int scoreLength() {
    return musicModel.scoreLength();
  }

  /**
   * Returns the notes in the range
   *
   * @return returns a list of notes in the range
   */
  public List<String> notesInRange() {
    return musicModel.notesInRange();
  }

  /**
   * The number of notes in the range
   *
   * @return the number of notes in the range
   */
  public int scoreHeight() {
    return musicModel.scoreHeight();
  }


  /**
   * Returns the tempo of the score
   *
   * @return the tempo
   */
  int getTempo() {
    return musicModel.getTempo();
  }

  /**
   * Outputs the musical array
   *
   * @return an unmodifiable list of collection of abstract note
   */
  List<Collection<AbstractNote>> returnScore() {
    return musicModel.returnScore();
  }

}
