package cs3500.music.view;

import java.awt.*;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;


/**
 * Adapter for non Gui views Created by Jonathan on 12/2/2015.
 */
public class NonGuiViewAdapter implements ViewExpansion, View {

  private final View adaptee;

  /**
   * Constructor for a NonGuiViewAdapter
   *
   * @param adaptee the view to be adapted
   */

  public NonGuiViewAdapter(View adaptee) {
    this.adaptee = adaptee;
  }

  @Override
  public void initialize() throws InvalidMidiDataException {
    adaptee.initialize();
  }

  @Override
  public void updateTime(int time) {

  }

  @Override
  public Dimension getPreferredSize() {
    return adaptee.getPreferredSize();
  }

  @Override
  public void draw(ViewModel vm) throws IOException, InvalidMidiDataException {
    initialize();
  }
}
