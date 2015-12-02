package cs3500.music.view;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;

import cs3500.music.model.CompositionModel;

/**
 * Class that represents both the midi and Gui classes and implements the GUiView interface
 */
public class CompositeView implements GuiView {
  CompositionModel comp;
  GuiView guiView;
  MidiViewImpl midiView;


  /**
   * Co
   * @param comp
   */
  public CompositeView(CompositionModel comp){
    this.comp = comp;
    guiView = new GuiViewFrame(comp);
    midiView = new MidiViewImpl(comp);
  }

  @Override
  public void updateTime() {
    guiView.updateTime();
    midiView.updateTime();
  }

  @Override
  public void updateView() {
    guiView.updateView();
  }

  @Override
  public int getHighestPitch() {
    return guiView.getHighestPitch();
  }

  @Override
  public JScrollPane getScroll() {
    return guiView.getScroll();
  }

  @Override
  public int getCellSIze() {
    return guiView.getCellSIze();
  }

  @Override
  public int getXPadding() {
    return guiView.getXPadding();
  }

  @Override
  public int getYPadding() {
    return guiView.getYPadding();
  }

  @Override
  public void initialize() throws InvalidMidiDataException {
    guiView.initialize();
    midiView.initialize();

  }

  @Override
  public void updateScroll() {
    guiView.updateScroll();
  }

  /**
   * What is the preferred dimension size of this view
   */
  @Override
  public Dimension getPreferredSize() {
    return null;
  }

  @Override
  public void addKeyListener(KeyListener e) {
    guiView.addKeyListener(e);
  }

  @Override
  public void removeKeyListener(KeyListener e) {
    guiView.removeKeyListener(e);
  }

  @Override
  public void addMouseListener(MouseListener m) {
    guiView.addMouseListener(m);
  }

  @Override
  public void removeMouseListener(MouseListener m) {
    guiView.removeMouseListener(m);
  }
}