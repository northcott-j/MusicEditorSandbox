package cs3500.music.model;

import java.util.Collections;
import java.util.List;

/**
 * Class to represent AltEnding
 * Created by Jonathan on 12/14/2015.
 */
final class AltEnding extends ARepetition {
  List<ARepetition> alternateEndings;
  AltEnding(int start, int end, List<ARepetition> alternateEndings) {
    super(start, end);
    this.alternateEndings = alternateEndings;
  }

  @Override
  public int length() {
    return 1 + alternateEndings.size();
  }

  @Override
  public List<ARepetition> listofRepeats() {
    return Collections.unmodifiableList(alternateEndings);
  }
}
