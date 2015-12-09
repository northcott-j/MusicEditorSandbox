package cs3500.music.view;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.*;

import cs3500.music.model.CompositionModel;

/**
 * A skeleton Frame (i.e., a window) in Swing
 */
public class GuiViewFrame extends javax.swing.JFrame implements GuiView {
  CompositionModel comp;
  JScrollPane scrollable;
  int scroll;
  public final static int WINDOW_WIDTH = 1200;
  private final ConcreteGuiViewPanel displayPanel;

  /**
   * Creates new GuiView
   */
  public GuiViewFrame(CompositionModel comp) {
    this.comp = comp;
    this.displayPanel = new ConcreteGuiViewPanel(comp);
    this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    scrollable = new JScrollPane(displayPanel);
    this.getContentPane().add(scrollable);
    this.scroll = scrollable.getHorizontalScrollBar().getValue() + 1;
    this.setResizable(false);
    this.pack();
  }

  @Override
  public int getCurrTime(){
    return displayPanel.getCurrTime();
  }


  @Override
  public JScrollPane getScroll() {
    return scrollable;
  }


  @Override
  public void updateScroll() {
    if (getCurrTime() == scroll) {
      getScroll().getViewport().setViewPosition(new Point((getCurrTime() - 2) * getCellSIze(), 0));
    }
    scroll++;
  }

  @Override
  public void updateTime(int time) {
    displayPanel.updateTime(time);
    displayPanel.repaint();
  }


  @Override
  public void updateView() {
    displayPanel.repaint();
  }

  @Override
  public int getCellSIze() {
    return displayPanel.getCellSize();
  }

  @Override
  public int getXPadding() {
    return displayPanel.getXPadding();
  }

  @Override
  public int getYPadding() {
    return displayPanel.getYPadding();
  }

  @Override
  public void initialize() {
    this.setVisible(true);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(WINDOW_WIDTH, 600);
  }

  @Override
  public void addKeyListener(KeyListener e) {
    displayPanel.addKeyListener(e);
  }

  @Override
  public void removeKeyListener(KeyListener e) {
    displayPanel.removeKeyListener(e);
  }

  @Override
  public void addMouseListener(MouseListener m) {
    displayPanel.addMouseListener(m);
  }

  @Override
  public void removeMouseListener(MouseListener m) {
    displayPanel.removeMouseListener(m);
  }

  @Override
  public int getHighestPitch() {
    return displayPanel.getHighestPitch();
  }
}
