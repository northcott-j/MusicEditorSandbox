package cs3500.music.model;

/**
 * Class that adapts our model to theirs and is a pseudo ViewModel
 */


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * This abstract view class serves as the connecting point for all other views, as it is where the
 * data is stored. It is designed to remove the direct dependency of the view on the model;
 * instead,
 * the controller should implement this interface with information from the view and then passes
 * the
 * {@code ViewModel} object to an extended class for the view to render concretely.
 */
public abstract class ViewModel implements CompositionModel {
  private final MusicEditorModel musicModel;
  // The view high octave
  private int viewHighOctave = -2;
  // The view low octave
  private int viewLowOctave = -2;
  // The view last beat
  private int viewLastBeat = -1;

  public ViewModel(MusicEditorModel model) {
    this.musicModel = model;
  }

  /**
   * Returns the highest octave the view should use
   *
   * @return highest octave shown
   */
  public int getViewHighOctave() {
    if (viewHighOctave == -2) {
      if (scoreLength() == 0) {
        // Default high octave
        viewHighOctave = 4;
      } else {
        viewHighOctave = musicModel.getHighOctave();
      }
    }
    return viewHighOctave;
  }


  /**
   * Returns the highest octave the view should use
   *
   * @return highest octave shown
   */
  public int getViewLowOctave() {
    if (viewLowOctave == -2) {
      if (scoreLength() == 0) {
        // Default low octave
        viewLowOctave = 3;
      } else {
        viewLowOctave = musicModel.getLowOctave();
      }
    }
    return viewLowOctave;
  }

  /**
   * Increases the highest octave to be drawn
   */
  public void increaseViewHighOctave() {
    if (viewHighOctave < 9) {
      viewHighOctave += 1;
    }
  }

  /**
   * Decreases the lowest octave to be drawn
   */
  public void increaseViewLowOctave() {
    if (viewLowOctave > -1) {
      viewLowOctave -= 1;
    }
  }

  /**
   * Increases the number of beats drawn
   */
  public void increaseViewLastBeat() {
    if (viewLastBeat == -1) {
      viewLastBeat = scoreLength() - 1;
    }
    viewLastBeat += 8;
  }

  @Override
  public void addNote(Playable note) {
    PlayableToAbstractNote abstractNote = new PlayableToAbstractNote(note);
    addNote(abstractNote);
  }

  @Override
  public void removeNote(Playable note) {
    PlayableToAbstractNote abstractNote = new PlayableToAbstractNote(note);
    musicModel.deleteNote(abstractNote);
  }

  @Override
  public void editNote(Playable oldNote, Playable newNote) {
    PlayableToAbstractNote abstractNoteOld = new PlayableToAbstractNote(oldNote);
    PlayableToAbstractNote abstractNoteNew = new PlayableToAbstractNote(newNote);
    musicModel.deleteNote(abstractNoteOld);
    musicModel.addNote(abstractNoteNew);
  }

  @Override
  public int length() {
    return scoreLength();
  }

  @Override
  public String printBase() {
    return null;
  }

  @Override
  public List<Playable> notesAtTime(int time) {
    List<Playable> acc = new ArrayList<>();
    if (time < musicModel.scoreLength()) {
      for (AbstractNote n : returnScore().get(time)) {
        Note abstractAsNote = Note.makeNote(n.getType(), n.getOctave(), n.getStart(),
                n.getEnd(), n.getInstrument(), n.getVolume());
        acc.add(abstractAsNote);
      }
    }
    return acc;
  }

  // TODO :: The plus one for the bottom two methods is because our octave scales were different
  //         and their scale was hard coded
  @Override
  public int getHighestOctave() {
    return getViewHighOctave() + 1;
  }

  @Override
  public int getLowestOctave() {
    return getViewLowOctave() + 1;
  }

  @Override
  public int lastBeat() {
    if (viewLastBeat == -1) {
      if (scoreLength() == 0) {
        // Default score Length
        viewLastBeat = 64;
      } else {
        viewLastBeat = scoreLength();
      }
    }
    return Math.max(viewLastBeat, scoreLength());
  }

  @Override
  public Set<Playable> getNotes() {
    return null;
  }

  @Override
  public int getCompTempo() {
    return musicModel.getTempo();
  }

  @Override
  public void setTempo(int tempo) {
    musicModel.setTempo(tempo);
  }

  @Override
  /**
   * This is the logic behind implementing this method if it were to be used
   * This method is never used so the cast won't be an issue
   */
  public Playable getNoteAtBeat(int index, int time) {
    Object[] transform = returnScore().get(time).toArray();
    return (AbstractNote) transform[index];
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
    ArrayList<String> acc = new ArrayList<>();
    // String representation of the highest note
    String highestNote = NoteTypes.valueLookup(11).toString() +
            Integer.toString(getViewHighOctave());
    String curNote = "";
    int curNoteVal = 0;
    int curNoteOct = getViewLowOctave();
    while (!curNote.equals(highestNote)) {
      if (curNoteVal > 11) {
        // 11 is the highest note order value
        curNoteVal = 0;
        curNoteOct += 1;
      }
      // Gets the NoteType string name and adds it to the current octave plus a space
      curNote = NoteTypes.valueLookup(curNoteVal).toString()
              + Integer.toString(curNoteOct);
      acc.add(0, curNote);
      curNoteVal += 1;
    }
    // adds a new line character before returning it
    return acc;
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
