package cs3500.music.view;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs3500.music.model.CompositionModel;
import cs3500.music.model.Playable;

/**
 * This class represents the console view of the Musical Editor
 */
public class TextView implements View {
  CompositionModel comp;
  public Appendable output;

  /**
   * This constructs a TextView that takes in a CompositonModel and an appendable as its input
   *
   * @param comp   is the CompositionalModel that consists of a list of notes
   * @param output is an appendable
   */
  public TextView(CompositionModel comp, Appendable output) {
    this.comp = comp;
    this.output = output;
  }

  /**
   * Prints the notes at their location by first checking if there are notes to be rendered at that
   * particular beat and pitch and then checks if it is a X or a | by checking the note's start and
   * end beats NOTE: THIS IS THE RENDERED TEXT VIEW ACTUALLY USED, NOT JUST TESTING
   */
  public void renderTextView1() {
    String base = comp.printBase();
    int totalTime = comp.lastBeat();
    int low = comp.getLowestOctave();
    int lowPitch = low * 12;
    int high = comp.getHighestOctave();
    int highPitch = 11 + (high * 12);
    System.out.println(String.format("  %s", base));
    for (int i = 0; i <= totalTime; i++) {
      System.out.print(i);
      List<Playable> currNotes = comp.notesAtTime(i);
      List<Integer> currPitches = new ArrayList<>();
      for (Playable n : currNotes) {
        currPitches.add(n.getPitch());
      }
      for (int j = lowPitch; j <= highPitch; j++) {
        if (currPitches.contains(j)) {
          int size = currPitches.size();
          for (Playable currNote : currNotes) {
            int index = currPitches.indexOf(j);
            Playable indexNote = currNotes.get(index);
            if (indexNote.hasStarted(i)) {
              System.out.print(" X  ");
              break;
            } else if (indexNote.played(i)) {
              System.out.print(" |  ");
              break;
            }
          }
        } else {
          System.out.print(("    "));
        }
      }
      System.out.println();
    }
  }

  /**
   * Prints the notes at their location by first checking if there are notes to be rendered at that
   * particular beat and pitch and then checks if it is a X or a | by checking the note's start and
   * end beats
   *
   * NOTE: THIS IS ONLY FOR TESTING PURPOSES
   */
  public void renderTextView() {
    try {
      String base = comp.printBase();
      output.append("  " + base);
      int totalTime = comp.lastBeat();
      int low = comp.getLowestOctave();
      int lowPitch = low * 12;
      int high = comp.getHighestOctave();
      int highPitch = 11 + (high * 12);

      for (int i = 0; i <= totalTime; i++) {
        output.append(String.format("%1$4s", String.valueOf(i)));
        List<Playable> currNotes = comp.notesAtTime(i);
        List<Integer> currPitches = new ArrayList<>();
        for (Playable n : currNotes) {
          currPitches.add(n.getPitch());
        }
        for (int j = lowPitch; j <= highPitch; j++) {
          if (currPitches.contains(j)) {
            int size = currPitches.size();
            for (Playable currNote : currNotes) {
              int index = currPitches.indexOf(j);
              Playable indexNote = currNotes.get(index);
              if (indexNote.hasStarted(i)) {
                output.append(" X  ");
                break;
              } else if (indexNote.played(i)) {
                output.append(" |  ");
                break;
              }
            }
          } else {
            output.append("    ");
          }
        }
        output.append("\n");

      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @Override
  public void initialize() {
    renderTextView1();
  }

  @Override
  public void updateTime(int time) {
  }

  @Override
  public int getCurrTime() {
    return 0;
  }

  @Override
  public Dimension getPreferredSize() {
    return null;
  }

}