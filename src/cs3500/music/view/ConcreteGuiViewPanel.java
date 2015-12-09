package cs3500.music.view;

import cs3500.music.model.CompositionModel;
import cs3500.music.model.Playable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A drawn view of the gui
 */
public class ConcreteGuiViewPanel extends JPanel {
  private CompositionModel model;
  final static int CELL_SIZE = 15;
  final static int X_PADDING = 20;
  final static int Y_PADDING = 20;
  private int currTime = 0;
  private int currStart = 0;
  private int highestPitch = 0;


  /**
   * Constructs a ConcreteGuiViewPanel based on the given composition
   *
   * @param model the composition that will be drawn
   */
  public ConcreteGuiViewPanel(CompositionModel model) {
    this.model = model;
    this.setFocusable(true);
    this.requestFocus();
  }

  /**
   * What is the current beat of this composition? At what time is this composition being played
   *
   * @return the integer representation of this piece
   */
  public int getCurrTime(){
    return currTime;
  }



  @Override
  public Dimension getPreferredSize() {
    int yRange = (model.getHighestOctave() - model.getLowestOctave()) * 12 * CELL_SIZE + Y_PADDING;
    int xRange = model.lastBeat() * CELL_SIZE + X_PADDING * 2;
    return new Dimension(xRange, yRange);
  }

  /**
   * Updates the time of a composition
   */
  public void updateTime(int time) {
    currTime = time;
    if (currTime < model.lastBeat()) {
      currTime++;
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    int high = model.getHighestOctave();
    highestPitch = (high * 12) + 11;
    int low = model.getLowestOctave();
    int lowestPitch = low * 12;
    int numPitches = (1 + (high - low)) * 12;
    int pitchLength = model.getHighestOctave() - model.getLowestOctave() * 12;

    int finalBeatNumber = model.lastBeat();
    int maxNotes = finalBeatNumber - 1;


    // to render the beat numbers - uses - 1 as a better padding, will want to fix later probably
    for (int i = 0; i <= model.lastBeat(); i = i + 4) {
      g.drawString(Integer.toString(i), CELL_SIZE * i + X_PADDING, X_PADDING - 1);
    }

    //to render the pitches
    for (int i = high; i >= low; i--) {
      g.drawString("B" + i, 0, X_PADDING + (CELL_SIZE) +
              ((high - i) % high) * 12 * CELL_SIZE);
      g.drawString("A#" + i, 0, X_PADDING + (CELL_SIZE * 2) +
              ((high - i) % high) * 12 * CELL_SIZE);
      g.drawString("A" + i, 0, X_PADDING + (CELL_SIZE * 3) +
              ((high - i) % high) * 12 * CELL_SIZE);
      g.drawString("G#" + i, 0, X_PADDING + (CELL_SIZE * 4) +
              ((high - i) % high) * 12 * CELL_SIZE);
      g.drawString("G" + i, 0, X_PADDING + (CELL_SIZE * 5) +
              ((high - i) % high) * 12 * CELL_SIZE);
      g.drawString("F#" + i, 0, X_PADDING + (CELL_SIZE * 6) +
              ((high - i) % high) * 12 * CELL_SIZE);
      g.drawString("F" + i, 0, X_PADDING + (CELL_SIZE * 7) +
              ((high - i) % high) * 12 * CELL_SIZE);
      g.drawString("E" + i, 0, X_PADDING + (CELL_SIZE * 8) +
              ((high - i) % high) * 12 * CELL_SIZE);
      g.drawString("D#" + i, 0, X_PADDING + (CELL_SIZE * 9) +
              ((high - i) % high) * 12 * CELL_SIZE);
      g.drawString("D" + i, 0, X_PADDING + (CELL_SIZE * 10) +
              ((high - i) % high) * 12 * CELL_SIZE);
      g.drawString("C#" + i, 0, X_PADDING + (CELL_SIZE * 11) +
              ((high - i) % high) * 12 * CELL_SIZE);
      g.drawString("C" + i, 0, X_PADDING + (CELL_SIZE * 12) +
              ((high - i) % high) * 12 * CELL_SIZE);
    }

    // to draw the pitches at the beat
    for (int i = currStart; i <= getWidth() / CELL_SIZE; i++) { // keeps check of the current beat
      List<Playable> currNotes = model.notesAtTime(i);
      List<Integer> pitchNums = new ArrayList<>();
      // init: list of current pitches, a list of pitches @ith beat
      for (Playable n : currNotes) {
        pitchNums.add(n.getPitch());
      }
        for (Integer pitchNum : pitchNums) {
          int index = pitchNums.indexOf(pitchNum);
          Playable indexNote = currNotes.get(index);
          int pitchRow = (high * 12 + 11) - pitchNum;
          if (indexNote.hasStarted(i)) {
            g.setColor(Color.BLACK);
            g.fillRect(i * CELL_SIZE + X_PADDING,
                    pitchRow * CELL_SIZE + Y_PADDING, CELL_SIZE, CELL_SIZE);
          } else {
            g.setColor(Color.green);
            g.fillRect(i * CELL_SIZE + X_PADDING,
                    pitchRow * CELL_SIZE + Y_PADDING, CELL_SIZE, CELL_SIZE);
          }
      }
    }
    g.setColor(Color.BLACK);
    // to render the vertical beats
    for (int i = 0; i <= model.lastBeat(); i = i + 4) {
      g.setColor(Color.BLACK);
      g.drawLine(X_PADDING + i * CELL_SIZE, Y_PADDING,
              X_PADDING + i * CELL_SIZE, numPitches * CELL_SIZE + Y_PADDING);
    }

    // to render the horizontal pitches
    for (int i = 0; i <= numPitches; ++i) {
      g.drawLine(X_PADDING, Y_PADDING + i * CELL_SIZE,
              finalBeatNumber * CELL_SIZE + X_PADDING, Y_PADDING + i * CELL_SIZE);
    }

    // to draw the red line that keeps track of what is currently being played
    g.setColor(Color.red);
    g.drawLine(X_PADDING + currTime * CELL_SIZE, Y_PADDING,
            X_PADDING + currTime * CELL_SIZE, numPitches * CELL_SIZE + Y_PADDING);
  }

  /**
   * What is the highest pitch of this composition?
   *
   * @return integer representation of the highest pitch
   */
  public int getHighestPitch() {
    return highestPitch;
  }

  /**
   * What is the preferred cell size constant
   *
   * @return the integer representation ofa constant of the cell size
   */
  public int getCellSize() {
    return CELL_SIZE;
  }

  /**
   * What is the preferred X_Padding size
   *
   * @return the integer representation of a X_PADDING
   */
  public int getXPadding() {
    return X_PADDING;
  }

  /**
   * What is the preferred Y_Padding size
   *
   * @return the integer representation of Y_PADDING
   */
  public int getYPadding() {
    return Y_PADDING;
  }
}