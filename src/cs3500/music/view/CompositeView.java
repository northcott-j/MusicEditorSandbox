package cs3500.music.view;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.IOException;

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
   * Constructs the Composite by taking in a CompositionModel
   * @param comp is a compositionModel
   */
  public CompositeView(CompositionModel comp){
    this.comp = comp;
    guiView = new GuiViewFrame(comp);
    midiView = new MidiViewImpl(comp);
  }

  @Override
  public void updateTime(int time) {
    guiView.updateTime(time);
    midiView.updateTime(time);
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
  public void initialize() throws InvalidMidiDataException, IOException {
    guiView.initialize();
    midiView.initialize();

  }

  @Override
  public int getCurrTime() {
    return guiView.getCurrTime();
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
  public void addKeyListener(KeyListener e) throws IOException {
    guiView.addKeyListener(e);
  }

  @Override
  public void removeKeyListener(KeyListener e) {
    guiView.removeKeyListener(e);
  }

  @Override
  public void addMouseListener(MouseListener m) throws IOException {
    guiView.addMouseListener(m);
  }

  @Override
  public void removeMouseListener(MouseListener m) {
    guiView.removeMouseListener(m);
  }
}