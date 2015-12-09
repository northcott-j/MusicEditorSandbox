package cs3500.music.view;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;

import cs3500.music.model.ViewModel;

/**
 * Adapter class for GuiViews Created by Jonathan on 12/2/2015.
 */
public class GuiViewAdapter implements GuiViewExpansion, GuiView {

  private final GuiView adaptee;

  /**
   * Constructor for a GuiViewAdapter
   *
   * @param adaptee the view to be adapted
   */
  public GuiViewAdapter(GuiView adaptee) {
    this.adaptee = adaptee;
  }

  @Override
  public void updateTime(int time) {
    adaptee.updateTime(time);
  }

  @Override
  public Dimension getPreferredSize() {
    return adaptee.getPreferredSize();
  }

  @Override
  public void updateView() {
    adaptee.updateView();
  }

  @Override
  public int getCellSIze() {
    return adaptee.getCellSIze();
  }

  @Override
  // TODO: Accesors for static variables.....
  public int getXPadding() {
    return adaptee.getXPadding();
  }

  @Override
  public int getYPadding() {
    return adaptee.getYPadding();
  }

  @Override
  public void initialize() throws InvalidMidiDataException, IOException {
    adaptee.initialize();
  }

  @Override
  public int getCurrTime() {
    return adaptee.getCurrTime();
  }

  @Override
  public void addKeyListener(KeyListener e) {
    try {
      adaptee.addKeyListener(e);
    } catch (IOException e1) {
      throw new IllegalArgumentException("Can't add this keyListener");
    }
  }

  @Override
  public void removeKeyListener(KeyListener e) {
    adaptee.removeKeyListener(e);
  }

  @Override
  public void addMouseListener(MouseListener m) {
    try {
      adaptee.addMouseListener(m);
    } catch (IOException e1) {
      throw new IllegalArgumentException("Can't add this mouseListener");
    }
  }

  @Override
  public void removeMouseListener(MouseListener m) {
    adaptee.removeMouseListener(m);
  }

  @Override
  public int getHighestPitch() {
    return adaptee.getHighestPitch();
  }

  @Override
  public JScrollPane getScroll() {
    return adaptee.getScroll();
  }

  @Override
  public void updateScroll() {
    adaptee.updateScroll();
  }

  @Override
  public void repaint() {
    getScroll().revalidate();
    getScroll().repaint();
    getScroll().validate();
  }

  @Override
  public void setKeyHandler(KeyListener kh) {
    addKeyListener(kh);
  }

  @Override
  public List<String> getNotesInRange(ViewModel vm) {
    return vm.notesInRange();
  }

  @Override
  public void setMouseHandler(MouseListener mh) {
    addMouseListener(mh);
  }

  @Override
  public void tickCurBeat(ViewModel vm, int curBeat) throws InvalidMidiDataException, IOException {
    int boardCellWidth = getScroll().getViewport().getWidth() / getCellSIze();
    if (curBeat % boardCellWidth == 0) {
      getScroll().getHorizontalScrollBar()
              .setValue(curBeat * getCellSIze());
    }
    updateTime(curBeat);
  }

  @Override
  public void goToStart() {
    getScroll().getHorizontalScrollBar().setValue(0);
  }

  @Override
  public void goToEnd() {
    int maxValue = getScroll().getHorizontalScrollBar().getMaximum();
    getScroll().getHorizontalScrollBar().setValue(maxValue);
  }

  @Override
  public void scrollUp() {
    int curValue = getScroll().getVerticalScrollBar().getValue();
    int nxtValue = Math.min(getScroll().getVerticalScrollBar().getMaximum(),
            curValue - getCellSIze());
    getScroll().getVerticalScrollBar().setValue(nxtValue);
  }

  @Override
  public void scrollDown() {
    int curValue = getScroll().getVerticalScrollBar().getValue();
    int nxtValue = Math.max(0,
            curValue + getCellSIze());
    getScroll().getVerticalScrollBar().setValue(nxtValue);
  }

  @Override
  public void scrollLeft() {
    int curValue = getScroll().getHorizontalScrollBar().getValue();
    int nxtValue = Math.min(getScroll().getHorizontalScrollBar().getMaximum(),
            curValue - getCellSIze());
    getScroll().getHorizontalScrollBar().setValue(nxtValue);
  }

  @Override
  public void scrollRight() {
    int curValue = getScroll().getHorizontalScrollBar().getValue();
    int nxtValue = Math.max(0,
            curValue + getCellSIze());
    getScroll().getHorizontalScrollBar().setValue(nxtValue);
  }

  @Override
  public void expandUp(ViewModel vm) {
    vm.increaseViewHighOctave();
    repaint();
  }

  @Override
  public void expandDown(ViewModel vm) {
    vm.increaseViewLowOctave();
    repaint();
  }

  @Override
  public void expandOut(ViewModel vm) {
    vm.increaseViewLastBeat();
    repaint();
  }

  @Override
  public void draw(ViewModel vm) throws IOException, InvalidMidiDataException {
    initialize();
  }
}
