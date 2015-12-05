package cs3500.music.view;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;

/**
 * Created by SaiSujitha on 11/16/2015.
 */
public interface GuiView extends View {

  void updateTime();

  //TODO: Method to change the currTime to a given int

  void updateView();

  int getCellSIze();

  int getXPadding();

  int getYPadding();

  void initialize() throws InvalidMidiDataException;

  void addKeyListener(KeyListener e);

  void removeKeyListener(KeyListener e);

  void addMouseListener(MouseListener m);

  void removeMouseListener(MouseListener m);

  int getHighestPitch();

  JScrollPane getScroll();

  void updateScroll();
}
