package cs3500.music.model;

import java.util.List;
import java.util.Set;

/**
 * Represents a musical Composition model
 */
public interface CompositionModel {

  /**
   * Adds an individual Playable to the playables (ie notes) of a musical composition
   *
   * @param note - a given musical playable that will be added
   * @throws IllegalArgumentException when the playable is already there
   */
  void addNote(Playable note);

  /**
   * Removes an individual playable from the playables of a musical composition
   *
   * @param note - a given musical playable that will be added
   * @throws IllegalArgumentException when there is no playable
   */
  void removeNote(Playable note);

  /**
   * Edit this given note from the notes of a musical composition
   *
   * @param oldNote - a given musical playable that will be edited
   * @param newNote - a given musical playable that will replace the old edited note
   * @throws IllegalArgumentException when this given playable is not part of the musical
   *                                  composition
   */
  void editNote(Playable oldNote, Playable newNote);


  /**
   * What is the full length of the piece?
   *
   * @return integer representation of the size of the piece
   */
  int length();

  /**
   * Returns the string representation of a note's pitch particularly for the view's String
   * representations of the pitches of an octave
   *
   * @return String representation of the musical composition
   * @throws IllegalArgumentException when an inapplicable String is not valid for a musical piece
   */
  String printBase();

  /**
   * Returns an array list of notes at a time 0 -> time of the last beat played.
   *
   * @param time row you want to find an array list of notes
   * @return array list of notes at that time
   */
  List<Playable> notesAtTime(int time);

  /**
   * Returns lowest octave in this list of notes
   *
   * @return integer representation of lowest octave in composition
   */
  int getHighestOctave();

  /**
   * Returns highest octave in this list of playables
   *
   * @return integer representation of lowest octave in composition
   */
  int getLowestOctave();

  /**
   * Returns the length of a composition by calculating the lastbeat played in the composition
   *
   * @return the last beat played in a composition
   */
  int lastBeat();

  /**
   * Set the tempo based on the given tempo
   *
   * @param tempo the given tempo in microseconds
   */
  void setTempo(int tempo);

  /**
   * Get a set of notes from this composition
   *
   * @return the set of notes
   */
  Set<Playable> getNotes();

  /**
   * What is the integer value of the Composition's tempo?
   *
   * @return integer representation of a composition's tempo
   */

  int getCompTempo();

  /**
   * What Playable is at the given time
   *
   * @param pitch to compare the pitches at the given beat, does this given pitch match the pitches
   *              of the notes at this given time?
   * @param time  the integer value of the time you want to see where the notes are at
   * @return the note that is at this pitch at this given time
   */
  Playable getNoteAtBeat(int pitch, int time);
}

