package cs3500.music.view;

import cs3500.music.model.AbstractNote;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * The view that allows for interaction with the board
 */
public class EditorView extends javax.swing.JFrame implements GuiView {

  /*
  Runtime for mystery-1.txt is very slow as each beat is a clickable JPanel
  this will be fixed next time when we know better how we are going to interact
  with an individual beat
  * Multi thread rendering - start with window
  * and multithread background
  *
  * WEBSITE IDEA TO SPEED UP PROCESSING
  * http://stackoverflow.com/questions/15177367/
  * jpanel-with-grid-painted-on-it-causing-high-cpu-usage-when-scrolled
  *
  * */

  public EditorView() {
  }

  /**
   * scoreLength is the length of the musical score scoreHeight is the number of notes in the
   * musical score cellSize is the size of one beat notesInRange is a list of all of the note names
   * in the musical score
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
    if (scoreHeight < vm.scoreHeight()) {
      scoreHeight = vm.scoreHeight();
      notesInRange = vm.notesInRange();
    }
    if (scoreLength < vm.scoreLength()) {
      scoreLength = vm.scoreLength();
    }

    JPanel noteLabels = new JPanel();
    noteLabels.setLayout(new GridLayout(0, 1));
    addNoteLabels(vm, noteLabels);

    JPanel beatNumbers = new JPanel();
    beatNumbers.setLayout(new GridLayout(1, 0));
    addBeatLabels(vm, beatNumbers);

    JPanel editorGrid = new JPanel();
    editorGrid.setLayout(new GridBagLayout());
    buildEditorGrid(vm, editorGrid);

    JPanel numberWrapper = new JPanel();
    numberWrapper.setLayout(new BorderLayout());
    numberWrapper.add(beatNumbers, BorderLayout.NORTH);
    numberWrapper.add(noteLabels, BorderLayout.WEST);
    numberWrapper.add(editorGrid);

    JScrollPane scrollPane = new JScrollPane(numberWrapper);

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
   * @param vm    this view Model
   * @param frame the frame that they are added too
   */
  private void addBeatLabels(ViewModel vm, JPanel frame) {
    for (int i = 0; i <= scoreLength; i += 4) {
      GridSquare beatLabel = new GridSquare();
      JLabel beatNumber = new JLabel(Integer.toString(i));
      beatLabel.setSize(cellSize * 4, cellSize);
      beatLabel.add(beatNumber, CENTER_ALIGNMENT);
      frame.add(beatLabel);
    }
    frame.setBorder(BorderFactory.createEmptyBorder(0, cellSize, 0, 0));
  }

  /**
   * Adds Note Labels down the edge of the frame
   *
   * @param vm    this viewModel
   * @param frame the frame that they are added in
   */
  private void addNoteLabels(ViewModel vm, JPanel frame) {
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
   * @param vm    this ViewModel
   * @param frame the frame that they are added in
   */
  private void buildEditorGrid(ViewModel vm, JPanel frame) {
    List<Collection<AbstractNote>> notes = vm.returnScore();
    GridBagConstraints gbc = new GridBagConstraints();
    for (int col = 0; col < scoreLength; col += 1) {
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
      for (int row = 0; row < scoreHeight; row++) {

        gbc.gridx = col;
        gbc.gridy = row;

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
        frame.add(thisBeat, gbc);
      }
    }
    frame.setBorder(BorderFactory.createEmptyBorder(0, 0, cellSize, 0));
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



