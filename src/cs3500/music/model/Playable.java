package cs3500.music.model;

/**
 * Represents a Playable item of a composition. (ie: notes, grace notes, trills, etc)
 */
public interface Playable {
  /**
   * Is this played at this time?
   *
   * @param duration of when this is played
   * @return true if this is played at the given time
   */
  boolean played(int duration);

  /**
   * What is this note's octave based off of the note's pitch?
   *
   * @return a calculated integer value of the octave of this note based on its pitch
   */
  int octave();

  /**
   * Has this playable started based on the given time?
   *
   * @param duration to represent the beat representation of the time
   * @return true if this playable has started
   */
  boolean hasStarted(int duration);

  /**
   * What is the integer representation of this playable?
   *
   * @return the integer representation of this playable
   */
  int getPitch();

  /**
   * Does this playable equal that object?
   *
   * @param o to represent an arbitrary object
   * @return true if the two objects are equal
   */
  boolean equals(Object o);

  /**
   * What is the hashcode of this playable?
   *
   * @return an integer representation of the hashcode for this {@code Playable}
   */
  int hashCode();

  /**
   * What instrument is playing at this playable?
   *
   * @return the integer representation of the playing instrument
   */
  int getInstrument();

  /**
   * What is the volume of the current playable?
   *
   * @return the integer representation of the volume of the piece
   */
  int getVolume();

  /**
   * What is the duration of this playable based on the start and end positions?
   *
   * @return the integer representation of how long a note is
   */
  int duration();

  /**
   * Gets the duration of a note
   *
   * @return Duration which states the start time of the note
   */
  Duration getDuration();

  /**
   * Gets the start time of the note
   *
   * @return int which states the start time of the note
   */
  int getStart();

  /**
   * When does the note end playing?
   *
   * @return int which states the end time of the note
   */
  int getEnd();

  /**
   * Compare this note the given note in terms of pitch
   *
   * @param note the given note that you are going to compare
   * @return an integer representation of how the note compares. Returns(-) if this notes pitch
   * comes before the given note's pitch. Returns 0 if this pitch is the same as the given note's
   * pitch. Returns (+) if this note's pitch is greater than the given note's pitch.
   */
  int compareTo(Playable note);
}


