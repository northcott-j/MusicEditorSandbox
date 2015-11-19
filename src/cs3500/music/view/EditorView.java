package cs3500.music.view;

import cs3500.music.model.AbstractNote;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * The view that allows for interaction with the board
 */
public class EditorView extends javax.swing.JFrame implements GuiView {

  public EditorView() {
  }

  /**
   * scoreLength is the length of the musical score scoreHeight is the number of notes in the
   * musical score cellSize is the size of one beat notesInRange is a list of all of the note names
   * in the musical score
   */

  /*
  Potential fix is to create a bunch of screen sized JPanels and set visibility one at a time
   */
  int scoreLength;
  int scoreHeight;
  int cellSize = 30;
  List<String> notesInRange;


  /**
   * Initializes Editor to default fields
   */
  private void initializeFields() {
    scoreLength = 64;
    scoreHeight = 16;
    String[] notes = new String[]{"G4", "F#4", "F4", "E4", "D#4", "D4", "C#4",
            "C4", "B3", "A#3", "A3", "G#3", "G3", "F#3", "F3", "E3"};
    notesInRange = Arrays.asList(notes);
  }

  /**
   * GUI Editor representation of a board
   *
   * @return the GUI Editor view of the board
   */
  public void draw(ViewModel vm) {
    // Checks to see if array is empty and builds default board
    if (vm.scoreLength() == 0) {
      initializeFields();
    }

    // Expands board beyond the default if the Model expands
    if (vm.scoreLength() != 0) {
      if (scoreHeight < vm.scoreHeight()) {
        scoreHeight = vm.scoreHeight();
        notesInRange = vm.notesInRange();
      }
      if (scoreLength < vm.scoreLength()) {
        scoreLength = vm.scoreLength();
      }
    }

    JPanel noteLabels = new JPanel();
    noteLabels.setLayout(new GridLayout(0, 1));
    addNoteLabels(noteLabels);

    JPanel beatNumbers = new JPanel();
    beatNumbers.setLayout(new GridLayout(1, 0));
    addBeatLabels(beatNumbers);

    JPanel editorGrid = buildEditorGrids(vm);

    JPanel numberWrapper = new JPanel();
    numberWrapper.setLayout(new BorderLayout());
    numberWrapper.add(editorGrid);
    numberWrapper.add(beatNumbers, BorderLayout.NORTH);
    numberWrapper.add(noteLabels, BorderLayout.WEST);

    JPanel sizeLocker = new JPanel(new BorderLayout());
    sizeLocker.add(numberWrapper, BorderLayout.NORTH);

    JScrollPane scrollPane = new JScrollPane(sizeLocker);

    JFrame output = new JFrame("Editor");
    output.add(scrollPane);
    output.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    output.pack();
    output.setLocationRelativeTo(null);
    output.setVisible(true);
  }

  /**
   * Adds beat Labels to ViewModel
   *
   * @param frame the frame that they are added too
   */
  private void addBeatLabels(JPanel frame) {
    for (int i = 0; i <= scoreLength; i += 4) {
      GridSquare beatLabel = new GridSquare();
      JLabel beatNumber = new JLabel(Integer.toString(i));
      beatLabel.setSize(cellSize * 4, cellSize);
      beatLabel.add(beatNumber);
      frame.add(beatLabel);
    }
    frame.setBorder(BorderFactory.createEmptyBorder(0, cellSize, 0, 0));
  }

  /**
   * Adds Note Labels down the edge of the frame
   *
   * @param frame the frame that they are added in
   */
  private void addNoteLabels(JPanel frame) {
    JLabel tempLabel = new JLabel();
    Font font = tempLabel.getFont();
    for (String s : notesInRange) {
      JLabel noteLabel = new JLabel();
      GridSquare noteName = new GridSquare();
      noteName.setSize(cellSize, cellSize);
      noteLabel.setFont(new Font(font.getName(), Font.BOLD, 16));
      noteLabel.setText(s);
      noteName.add(noteLabel);
      frame.add(noteName);
    }
    frame.setBorder(BorderFactory.createEmptyBorder(0, 0, cellSize, 0));
  }

  /**
   * Builds grid of Notes
   *
   * @param vm this ViewModel
   */
  private JPanel buildEditorGrids(ViewModel vm) {
    List<Collection<AbstractNote>> notes = vm.returnScore();
    JPanel frame = new JPanel(new GridLayout(1, 0));

    for (int col = 0; col < scoreLength; col += 1) {
      JPanel colPanel = new JPanel(new GridLayout(0, 1));
      Collection<String> newNotes = new ArrayList<>();
      Collection<String> sustainedNotes = new ArrayList<>();

      if (vm.scoreLength() != 0) {
        Collection<AbstractNote> beat = notes.get(col);
        for (AbstractNote n : beat) {
          if (n.getStartBeat() == col) {
            newNotes.add(n.toString());
          } else {
            sustainedNotes.add(n.toString());
          }
        }
      }
      for (int row = 0; row < scoreHeight; row += 1) {
        GridSquare thisBeat = new GridSquare();
        thisBeat.setSize(cellSize, cellSize);
        Border border;
        if (col % 4 != 0 && col % 4 != 3) {
          border = new MatteBorder(1, 0, 1, 0, Color.black);
        } else if (col % 4 == 3) {
          border = new MatteBorder(1, 0, 1, 1, Color.black);
        } else {
          border = new MatteBorder(1, 1, 1, 0, Color.black);
        }
        thisBeat.setBorder(border);
        String noteForThisRow = notesInRange.get(row);
        if (sustainedNotes.contains(noteForThisRow)) {
          thisBeat.setBackground(Color.green);
          sustainedNotes.remove(noteForThisRow);
        } else if (newNotes.contains(noteForThisRow)) {
          thisBeat.setBackground(Color.black);
          newNotes.remove(noteForThisRow);
        }
        colPanel.add(thisBeat);
      }
      frame.add(colPanel);
    }
    frame.setBorder(BorderFactory.createEmptyBorder(0, 0, cellSize, 0));
    return frame;
  }


  /**
   * Class for a grid square whose size won't change and can house other components
   */
  private class GridSquare extends JPanel {
    private Dimension size;

    public GridSquare() {
    }

    @Override
    public void setSize(Dimension d) {
      size = d;
    }

    @Override
    public void setSize(int width, int height) {
      size = new Dimension(width, height);
    }

    @Override
    public Dimension getSize() {
      if (size != null) return size;
      return this.getSize();
    }

    @Override
    public Dimension getSize(Dimension rv) {
      if (size != null) {
        if (rv == null) rv = new Dimension();
        rv.height = size.height;
        rv.width = size.width;
        return rv;
      }
      return this.getSize(rv);
    }

    @Override
    public Dimension getPreferredSize() {
      if (size != null) return size;
      return this.getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
      if (size != null) return size;
      return this.getMaximumSize();
    }

    @Override
    public Dimension getMinimumSize() {
      if (size != null) return size;
      return this.getMinimumSize();
    }
  }
}



