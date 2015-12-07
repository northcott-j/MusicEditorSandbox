package cs3500.music.model;

/**
 * Represents the length of a note
 */
public class Duration implements Comparable {
  int length;

  /**
   * Represents the length of a musical note
   *
   * @param length - the integer representation of how long a note is
   */
  public Duration(int length) {
    this.length = length;
  }

  /**
   * Compares duration with object o
   * @param o and object that is compared with duration
   * @return -1 is the duration is lesser than o.duration, 0 if it is equal and 1 if it is greater
   * @throws ClassCastException is o is not an instance of duration
   */
  public int compareTo(Object o) {
    if (!(o instanceof Duration)) {
      throw new ClassCastException("");
    } else {
      Duration result;
      result = (Duration) o;
      if (this.length < result.length) {
        return -1;
      } else if (this.length == result.length) {
        return 0;
      } else {
        return 1;
      }
    }
  }

  @Override
  public String toString() {
    return "Duration{" +
            "length=" + length +
            '}';
  }
}