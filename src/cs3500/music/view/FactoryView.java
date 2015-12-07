package cs3500.music.view;

import cs3500.music.model.CompositionModel;

/**
 * Represents a Factory of a {@code View}
 */
public class FactoryView {

  /**
   * Gets the according view to use in the factory
   *
   * @param comp the model that will be drawn
   * @param view the desired view that will be drawn
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
    if (view.equals("composite")) {
      return new CompositeView(comp);
    } else {
      throw new IllegalArgumentException("Not a valid view");
    }
  }

  /**
   * Get the composite view of this composition
   *
   * @param comp the composition that will be drawn
   */
  public GuiView getGuiView(CompositionModel comp) {
    return new CompositeView(comp);
  }
}



