package cs3500.music.model;

/**
 * Represents a concrete Note and its information Created by Jonathan on 11/1/2015.
 */
public final class Note extends AbstractNote {

  private Note(NoteTypes pitch, int octave, int startBeat, int endBeat,
               int instrument, int volume) {
    super(pitch, octave, startBeat, endBeat, instrument, volume);
  }

  /**
   * Creates a note and enforces invariants
   *
   * @param note       is the type of note
   * @param octave     is the octave or pitch of the note
   * @param startBeat  is the start beat of the note
   * @param endBeat    is the end beat of the note
   * @param instrument the number for the instrument
   * @param volume     the number for the volume
   * @return the new note
   * @throws IllegalArgumentException if arguments don't make a proper note
   */
  static Note makeNote(NoteTypes note, int octave, int startBeat, int endBeat,
                       int instrument, int volume) {
    if (octave < -1 || octave > 9 || startBeat < 0 || endBeat < 0 || endBeat < startBeat ||
            startBeat > endBeat || note == null || volume < 0 || instrument < 0) {
      throw new IllegalArgumentException("Invalid Note");
    }
    // Volume has been set to a default of 1 for now
    return new Note(note, octave, startBeat, endBeat, instrument, volume);
  }

  // Defines super abstract method
  public int midiValue() {
    return (this.octave + 1) * 12 + this.pitch;
  }

  /**
   * This note as a string
   *
   * @return Combines note type and octave
   */
  @Override
  public String toString() {
    return NoteTypes.valueLookup(this.pitch).toString() + Integer.toString(this.octave);
  }

  /**
   * Overrides the equals method
   *
   * @param other object (hopefully a note to be checked)
   * @return true or false if it matches
   */
  @Override
  public boolean equals(Object other) {
    if (other instanceof AbstractNote) {
      AbstractNote otherNote = (AbstractNote) other;
      return this.octave == otherNote.octave &&
              this.startBeat == otherNote.startBeat &&
              this.endBeat == otherNote.endBeat &&
              this.pitch == otherNote.pitch &&
              this.instrument == otherNote.instrument &&
              this.volume == otherNote.volume;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return 31 * (octave + startBeat + endBeat + pitch + volume
            + instrument);
  }
}
