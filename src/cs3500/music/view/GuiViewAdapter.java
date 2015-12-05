package cs3500.music.view;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;

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
  public void updateTime() {
    adaptee.updateTime();
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
  public void initialize() throws InvalidMidiDataException {
    adaptee.initialize();
  }

  @Override
  public void addKeyListener(KeyListener e) {
    adaptee.addKeyListener(e);
  }

  @Override
  public void removeKeyListener(KeyListener e) {
    adaptee.removeKeyListener(e);
  }

  @Override
  public void addMouseListener(MouseListener m) {
    adaptee.addMouseListener(m);
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
    updateView();
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
    updateTime();
  }

  @Override
  public void goToStart() {
    adaptee.getScroll().getHorizontalScrollBar().setValue(0);
  }

  @Override
  public void goToEnd() {
    int maxValue = adaptee.getScroll().getHorizontalScrollBar().getMaximum();
    adaptee.getScroll().getHorizontalScrollBar().setValue(maxValue);
  }

  @Override
  public void scrollUp() {
    int curValue = adaptee.getScroll().getVerticalScrollBar().getValue();
    int nxtValue = Math.min(adaptee.getScroll().getVerticalScrollBar().getMaximum(),
            curValue - getCellSIze());
    adaptee.getScroll().getVerticalScrollBar().setValue(nxtValue);
  }

  @Override
  public void scrollDown() {
    int curValue = adaptee.getScroll().getVerticalScrollBar().getValue();
    int nxtValue = Math.max(0,
            curValue + getCellSIze());
    adaptee.getScroll().getVerticalScrollBar().setValue(nxtValue);
  }

  @Override
  public void scrollLeft() {
    int curValue = adaptee.getScroll().getHorizontalScrollBar().getValue();
    int nxtValue = Math.min(adaptee.getScroll().getHorizontalScrollBar().getMaximum(),
            curValue - getCellSIze());
    adaptee.getScroll().getHorizontalScrollBar().setValue(nxtValue);
  }

  @Override
  public void scrollRight() {
    int curValue = adaptee.getScroll().getHorizontalScrollBar().getValue();
    int nxtValue = Math.max(0,
            curValue + getCellSIze());
    adaptee.getScroll().getHorizontalScrollBar().setValue(nxtValue);
  }

  @Override
  public void expandUp(ViewModel vm) {
    vm.increaseViewHighOctave();
    adaptee.updateView();
  }

  @Override
  public void expandDown(ViewModel vm) {
    vm.increaseViewLowOctave();
    adaptee.updateView();
  }

  @Override
  public void expandOut(ViewModel vm) {
    // TODO: This is gonna get gross
  }

  @Override
  public void draw(ViewModel vm) throws IOException, InvalidMidiDataException {
    initialize();
  }
}
