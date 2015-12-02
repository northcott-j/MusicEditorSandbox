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


  // TODO: Need to implement all of these methods
  @Override
  public void initialize() throws InvalidMidiDataException {

  }

  @Override
  public void updateTime() {

  }

  @Override
  public Dimension getPreferredSize() {
    return null;
  }

  @Override
  public void draw(ViewModel vm) throws IOException {

  }
}
