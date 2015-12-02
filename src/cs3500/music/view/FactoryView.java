package cs3500.music.view;

import cs3500.music.model.CompositionModel;

/**
 * Represents a Factory of a {@code View}
 */
public class FactoryView {

  /**
   *
   * @param comp
   * @param view
   * @return
   */
  public View getView(CompositionModel comp, String view) {
    if (view.equals("txt")) {
      TextView textView = new TextView(comp, new StringBuilder());
      textView.renderTextView1();
      return textView;
    }
    if (view.equals("gui")) {
      return new GuiViewFrame(comp);
    }
    if (view.equals("midi")) {
      return new MidiViewImpl(comp);
    }
    if (view.equals("composite")){
      return new CompositeView(comp);
    }
    else {
      throw new IllegalArgumentException("Not a valid view");
    }
  }
  /**
   *
   */
  public GuiView getGuiView(CompositionModel comp) {
    return new CompositeView(comp);
  }
}



