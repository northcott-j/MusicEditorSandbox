package cs3500.music.model;

/**
 * Abstract representation of a Note Created by Jonathan on 11/8/2015.
 */
public abstract class AbstractNote {
  protected NoteTypes type;
  protected int octave;
  protected int startBeat;
  protected int endBeat;
  protected int instrument;
  protected int volume;

  /**
   * @param type       is the type of note that it is
   * @param octave     is the pitch of the note
   * @param startBeat  is the beat the note starts on
   * @param endBeat    is the beat the note ends on
   * @param instrument this is the instrument number
   * @param volume     the volume of the node (not used right now)
   */
  AbstractNote(NoteTypes type, int octave, int startBeat, int endBeat,
               int instrument, int volume) {
    if (type == null) {
      throw new IllegalArgumentException("Type can't be null");
    }
    this.type = type;
    this.octave = octave;
    this.startBeat = startBeat;
    this.endBeat = endBeat;
    this.instrument = instrument;
    this.volume = volume;
  }

  /**
   * The value of the note that MIDI takes
   *
   * @return the int value between 0 and 127 of this note
   */
  public abstract int midiValue();

  /**
   * Returns the type of the note
   *
   * @return returns the NoteType of this note
   */
  public NoteTypes getType() {
    return this.type;
  }

  /**
   * Returns the note's octave
   *
   * @return the octave of this note
   */
  public int getOctave() {
    return this.octave;
  }

  /**
   * Returns the startBeat of the note
   *
   * @return the startBeat of this note
   */
  public int getStartBeat() {
    return this.startBeat;
  }

  /**
   * Returns the end beat of the note
   *
   * @return the endBeat of this note
   */
  public int getEndBeat() {
    return this.endBeat;
  }

  /**
   * Returns the instrument of the note
   *
   * @return the instrument of this note
   */
  public int getInstrument() {
    return this.instrument;
  }

  /**
   * Returns the volume of the note
   *
   * @return the volume of this note
   */
  public int getVolume() {
    return this.volume;
  }

  /**
   * Changes the startBeat of this note
   *
   * @param newStart the new startBeat
   * @throws IllegalArgumentException if startBeat is invalid
   */
  void changeStart(int newStart) {
    if (newStart < 0 || newStart >= endBeat) {
      throw new IllegalArgumentException("Invalid startBeat");
    }
    this.startBeat = newStart;
  }

  /**
   * Changes the endBeat of this note
   *
   * @param newEnd the new endBeat
   * @throws IllegalArgumentException if invalid endBeat
   */
  void changeEnd(int newEnd) {
    if (newEnd < 0 || newEnd < startBeat) {
      throw new IllegalArgumentException("Invalid endBeat");
    }
    this.endBeat = newEnd;
  }

  /**
   * Changes the type of the note
   *
   * @param newType the new type for this note
   */
  void changeType(NoteTypes newType) {
    this.type = newType;
  }

  /**
   * Changes the octave of this note
   *
   * @param newOctave the new octave
   * @throws IllegalArgumentException if the octave is invalid
   */
  void changeOctave(int newOctave) {
    if (newOctave < -1 || newOctave > 9) {
      throw new IllegalArgumentException("Invalid octave");
    }
    this.octave = newOctave;
  }

  /**
   * Changes the instrument of this note
   *
   * @param instrument the new instrument
   * @throws IllegalArgumentException if instrument is invalid
   */
  void changeInstrument(int instrument) {
    if (instrument < 0 || instrument > 127) {
      throw new IllegalArgumentException("Invalid instrument");
    }
    this.instrument = instrument;
  }

  /**
   * Changes the volume of this note
   *
   * @param newVolume the new volume
   * @throws IllegalArgumentException if the volume is invalid
   */
  void changeVolume(int newVolume) {
    if (newVolume < 0 || newVolume > 127) {
      throw new IllegalArgumentException("Invalid volume");
    }
    this.volume = newVolume;
  }

  /**
   * Checks to see if it overlaps another note
   *
   * @param other note to be checked
   * @return true if this note doesn't land on any part of the other note
   */
  boolean overlap(AbstractNote other) {
    return this.type == other.type &&
            this.octave == other.octave &&
            this.instrument == other.instrument;
  }
}

