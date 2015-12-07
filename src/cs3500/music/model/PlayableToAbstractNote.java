package cs3500.music.model;

/**
 * A class to adapt a Playable to an AbstractNote
 * Created by Jonathan on 12/7/2015.
 */
public class PlayableToAbstractNote extends AbstractNote {
  Playable adaptee;

  PlayableToAbstractNote(Playable adaptee) {
    super(NoteTypes.valueLookup(adaptee.getPitch()), adaptee.octave(), adaptee.getStart(),
            adaptee.getEnd(), adaptee.getInstrument(), adaptee.getVolume());
    this.adaptee = adaptee;
  }

  @Override
  public int midiValue() {
    return (this.octave + 1) * 12 + this.pitch;
  }
}

