package cs3500.music.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent a Repeat
 * Created by Jonathan on 12/12/2015.
 */
final class Repeat extends ARepetition {
 Repeat(int start, int end) {
   super(start, end);
 }

  @Override
  public int length() {
    return 1;
  }

  @Override
  public List<ARepetition> listofRepeats() {
    List<ARepetition> acc = new ArrayList<>();
    acc.add(this);
    return acc;
  }
}
