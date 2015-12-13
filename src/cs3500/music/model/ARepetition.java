package cs3500.music.model;

/**
 * Represents a Repetition of a portion of a MusicEditorModel
 * Created by Jonathan on 12/12/2015.
 */
public abstract class ARepetition {
  private final int start;
  private final int end;

  /**
   * Makes an instance of a Repeat
   * @param start the start beat (inclusive)
   * @param end the end beat (exclusive)
   */
  ARepetition(int start, int end) {
    if (!validRepeat(start, end)) {
      throw new IllegalArgumentException("This isn't a valid Repeat");
    } else {
      this.start = start;
      this.end = end;
    }
  }

  /**
   * Checks to see if a Repeat is valid
   * @param start the start of the repeat
   * @param end the end of the repeat
   * @return is it valid?
   */
  boolean validRepeat(int start, int end) {
    return (start < end) && (start >= 0) && (end >= 0);
  }

  /**
   * Gets the start of the Repeat
   * @return the startBeat number
   */
  public int getStart() {
    return this.start;
  }

  /**
   * Gets the end of the Repeat
   * @return the endBeat number
   */
  public int getEnd() {
    return this.end;
  }
}
