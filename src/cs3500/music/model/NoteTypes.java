package cs3500.music.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The types of notes in the Music Editor Created by Jonathan on 11/1/2015.
 */
public enum NoteTypes {
  C("C", 0), CSharp("C#", 1), D("D", 2), DSharp("D#", 3), E("E", 4), F("F", 5),
  FSharp("F#", 6), G("G", 7), GSharp("G#", 8), A("A", 9), ASharp("A#", 10), B("B", 11);

  private final String noteAsString;
  private final int order;

  // Reverse-lookup map for getting a NoteType from a String value
  private static final Map<String, NoteTypes> nameLookup = new HashMap<String, NoteTypes>();

  static {
    for (NoteTypes t : NoteTypes.values()) {
      nameLookup.put(t.toString(), t);
    }
  }

  // Reverse-lookup map for getting a NoteType from a int value
  private static final Map<Integer, NoteTypes> valueLookup = new HashMap<Integer, NoteTypes>();

  static {
    for (NoteTypes t : NoteTypes.values()) {
      valueLookup.put(t.noteOrder(), t);
    }
  }

  NoteTypes(String noteAsString, int order) {
    this.noteAsString = noteAsString;
    this.order = order;
  }

  /**
   * Represents the enum as a string
   *
   * @return the string note
   */
  @Override
  public String toString() {
    return noteAsString;
  }

  /**
   * Returns the position of the note in the scale
   *
   * @return the int representing position in scale
   */
  public int noteOrder() {
    return order;
  }

  /**
   * Returns the NoteType based on name
   *
   * @param name name of note such as C#
   * @return the NoteType
   */
  public static NoteTypes nameLookup(String name) {
    return nameLookup.get(name);
  }

  /**
   * Returns the NoteType based on value
   *
   * @param val the value of the NoteType in the order
   * @return the NoteType
   */
  public static NoteTypes valueLookup(int val) {
    return valueLookup.get(val);
  }

}
