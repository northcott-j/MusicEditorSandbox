package cs3500.music.model;

import java.util.Collection;
import java.util.List;

/**
 * The model interface for the Music Editor Created by Jonathan on 11/1/2015.
 */
public interface MusicEditorModel {
  /**
   * Creates a note where instrument is set to default of 1
   *
   * @param type   is the type of note
   * @param octave is the octave or pitch of the note
   * @param start  is the start beat of the note
   * @param end    is the end beat of the note
   * @param volume the number for the volume
   * @return the new note
   * @throws IllegalArgumentException if arguments don't make a proper note
   */
  AbstractNote makeNote(NoteTypes type, int octave, int start, int end,
                        int volume);

  /**
   * Changes the Start beat of a note
   *
   * @param note      the note to be changed
   * @param startBeat the new start beat
   * @throws IllegalArgumentException if startBeat is negative or greater than end
   */
  void changeNoteStart(AbstractNote note, int startBeat);

  /**
   * Changes the End beat of a note
   *
   * @param note    the note to be changed
   * @param endBeat the new end beat
   * @throws IllegalArgumentException if endBeat is negative or less than start
   */
  void changeNoteEnd(AbstractNote note, int endBeat);

  /**
   * Changes the Octave of a note
   *
   * @param note   the note to be changed
   * @param octave the new octave
   * @throws IllegalArgumentException if octave < -1 or octave > 9
   */
  void changeNoteOctave(AbstractNote note, int octave);

  /**
   * Changes the type of a note
   *
   * @param note    the note to be changed
   * @param newType the new type for the note
   */
  void changeNoteType(AbstractNote note, NoteTypes newType);

  /**
   * Changes the note instrument
   *
   * @param note       the note to be changed
   * @param instrument the new instrument for the note
   * @throws IllegalArgumentException if instrument is negative
   */
  void changeNoteInstrument(AbstractNote note, int instrument);

  /**
   * Changes the note volume
   *
   * @param note   the note to be changed
   * @param volume the new volume for the note
   * @throws IllegalArgumentException if volume is negative
   */
  void changeNoteVol(AbstractNote note, int volume);

  /**
   * Gets the note based on type and octave during a certain beat
   *
   * @param type   the type of the note
   * @param octave the octave of the note
   * @param beat   the beat the note is on
   * @return the Note wanted
   * @throws IllegalArgumentException if no note matching those arguments
   */
  AbstractNote getNote(NoteTypes type, int octave, int beat);

  /**
   * Adds a Note into the board
   *
   * @param note the note to be added
   */
  void addNote(AbstractNote note);

  /**
   * The amount of beats in the array
   *
   * @return the length of the score in beats
   */
  int scoreLength();

  /**
   * The length of the range of notes
   *
   * @return the number of notes in the range
   */
  int scoreHeight();

  /**
   * All of the notes in the range
   *
   * @return List of the notes in the range
   */
  List<String> notesInRange();

  /**
   * Returns the current beat
   *
   * @return the current beat
   */
  int getCurBeat();

  /**
   * Returns the tempo of the score
   *
   * @return the tempo
   */
  int getTempo();

  /**
   * Sets the tempo
   *
   * @throws IllegalArgumentException if tempo is negative or 0
   */
  void setTempo(int newTempo);

  /**
   * Outputs the musical array
   *
   * @return the musical array
   */
  List<Collection<AbstractNote>> returnScore();

  /**
   * Updates the current beat of the editor
   *
   * @param newBeat is the new beat to start on
   * @throws IllegalArgumentException if the beat is negative
   */
  void changeCurBeat(int newBeat);

  /**
   * Deletes a certain note from the board
   *
   * @param note is the note to be deleted
   * @throws IllegalArgumentException if no such note
   */
  void deleteNote(AbstractNote note);

  /**
   * Outputs information needed to play music and ticks the beat up
   *
   * @return the Collection of notes to be played at this beat
   * @throws IllegalStateException no more music
   */
  Collection<AbstractNote> playMusic();

  /**
   * Adds the two musical arrays together ignoring repeated notes
   *
   * @param secondScore is the music to be combined with current score
   */

  void simultaneousScore(List<Collection<AbstractNote>> secondScore);

  /**
   * Adds this score to the end of the current
   *
   * @param secondScore is the music to be added at the end
   */
  void consecutiveScore(List<Collection<AbstractNote>> secondScore);
}
