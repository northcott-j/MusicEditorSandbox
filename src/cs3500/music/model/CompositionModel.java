package cs3500.music.model;

import java.util.List;
import java.util.Set;

/**
 * Represents a Composition Model
 */
public interface CompositionModel {
  /**
   * Adds an individual note to the notes of a musical composition
   *
   * @param note - a given musical note that will be added
   * @throws IllegalArgumentException when the note is already there
   */
  void addNote(Note note);

  /**
   * Removes an individual note from the notes of a musical composition
   *
   * @param note - a given musical note that will be added
   * @throws IllegalArgumentException when there is no note
   */
  void removeNote(Note note);

  /**
   * Edit this given note from the notes of a musical composition
   *
   * @param oldNote - a given musical note that will be edited
   * @param newNote - a given musical note that will replace the old edited note
   * @throws IllegalArgumentException when this given note is not part of the musical composition
   */
  void editNote(Note oldNote, Note newNote);


  /**
   * What is the full length of the piece?
   *
   * @return integer representation of the size of the piece
   */
  int length();

  /**
   * Returns the string representation of a note's pitch
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
  List<Note> notesAtTime(int time);

  /**
   * Returns lowest octave in this list of notes
   *
   * @return integer representation of lowest octave in composition
   */
  int getHighestOctave();

  /**
   * Returns highest octave in this list of notes
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
  Set<Note> getNotes();

  int getCompTempo();

  int endBeat();

  boolean contains(Note note);

  Note getNoteAtBeat(int index,int time);
}

