package cs3500.music.view;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;

/**
 * Adapter class for GuiViews
 * Created by Jonathan on 12/2/2015.
 */
public class GuiViewAdapter implements GuiViewExpansion, GuiView {

  private final View adaptee;

  /**
   * Constructor for a GuiViewAdapter
   *
   * @param adaptee the view to be adapted
   */
  public GuiViewAdapter(View adaptee) {
    this.adaptee = adaptee;
  }

  // TODO: Need to implement all of these methods
  @Override
  public void updateTime() {

  }

  @Override
  public Dimension getPreferredSize() {
    return null;
  }

  @Override
  public void updateView() {

  }

  @Override
  public int getCellSIze() {
    return 0;
  }

  @Override
  public int getXPadding() {
    return 0;
  }

  @Override
  public int getYPadding() {
    return 0;
  }

  @Override
  public void initialize() throws InvalidMidiDataException {

  }

  @Override
  public void addKeyListener(KeyListener e) {

  }

  @Override
  public void removeKeyListener(KeyListener e) {

  }

  @Override
  public void addMouseListener(MouseListener m) {

  }

  @Override
  public void removeMouseListener(MouseListener m) {

  }

  @Override
  public int getHighestPitch() {
    return 0;
  }

  @Override
  public JScrollPane getScroll() {
    return null;
  }

  @Override
  public void updateScroll() {

  }

  @Override
  public boolean drawn() {
    return false;
  }

  @Override
  public void repaint() {

  }

  @Override
  public void setKeyHandler(KeyListener kh) {

  }

  @Override
  public List<String> getNotesInRange() {
    return null;
  }

  @Override
  public void setMouseHandler(MouseListener mh) {

  }

  @Override
  public void tickCurBeat(ViewModel vm, int curBeat) throws InvalidMidiDataException, IOException {

  }

  @Override
  public void goToStart() {

  }

  @Override
  public void goToEnd() {

  }

  @Override
  public void scrollUp() {

  }

  @Override
  public void scrollDown() {

  }

  @Override
  public void scrollLeft() {

  }

  @Override
  public void scrollRight() {

  }

  @Override
  public void expandUp(ViewModel vm) {

  }

  @Override
  public void expandDown(ViewModel vm) {

  }

  @Override
  public void expandOut(ViewModel vm) {

  }

  @Override
  public void draw(ViewModel vm) throws IOException {

  }
}
