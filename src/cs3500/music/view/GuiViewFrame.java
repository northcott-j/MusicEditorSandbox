package cs3500.music.view;

import cs3500.music.model.AbstractNote;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * To represent the piece in a graphical state.
 */
public class GuiViewFrame extends javax.swing.JFrame {

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

  public GuiViewFrame() {
  }

  /**
   * GUI representation of a board
   *
   * @return the GUI view of the board
   * @throws IllegalStateException if board is empty
   */
  public void draw(ViewModel vm) {
    if (vm.scoreLength() == 0) {
      throw new IllegalStateException("No board to draw");
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
   * @param vm this view Model
   * @param frame the frame that they are added too
   */
  private void addBeatLabels(ViewModel vm, JPanel frame) {
    for (int i = 0; i <= vm.scoreLength(); i += 4) {
      GridSquare beatLabel = new GridSquare();
      JLabel beatNumber = new JLabel(Integer.toString(i));
      beatLabel.setSize(120, 30);
      beatLabel.add(beatNumber, CENTER_ALIGNMENT);
      frame.add(beatLabel);
    }
    frame.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
  }

  /**
   * Adds Note Labels down the edge of the frame
   * @param vm this viewModel
   * @param frame the frame that they are added in
   */
  private void addNoteLabels(ViewModel vm, JPanel frame) {
    JLabel tempLabel = new JLabel();
    Font font = tempLabel.getFont();
    for (String s : vm.notesInRange()) {
      JLabel noteLabel = new JLabel();
      GridSquare noteName = new GridSquare();
      noteName.setSize(30, 30);
      noteLabel.setFont(new Font(font.getName(), Font.BOLD, 16));
      noteLabel.setText(s);
      noteName.add(noteLabel);
      frame.add(noteName);
    }
    frame.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
  }

  /**
   * Builds grid of Notes
   * @param vm this ViewModel
   * @param frame the frame that they are added in
   */
  private void buildEditorGrid(ViewModel vm, JPanel frame) {
    List<Collection<AbstractNote>> notes = vm.returnScore();
    List<String> rangeOfNotes = vm.notesInRange();
    GridBagConstraints gbc = new GridBagConstraints();
    for (int col = 0; col < vm.scoreLength(); col += 1) {
      Collection<AbstractNote> beat = notes.get(col);
      Collection<String> newNotes = new ArrayList<>();
      Collection<String> sustainedNotes = new ArrayList<>();
      for (AbstractNote n : beat) {
        if (n.getStartBeat() == col) {
          newNotes.add(n.toString());
        } else {
          sustainedNotes.add(n.toString());
        }
      }
      for (int row = 0; row < vm.scoreHeight(); row++) {

        gbc.gridx = col;
        gbc.gridy = row;

        GridSquare thisBeat = new GridSquare();
        thisBeat.setSize(30, 30);
        Border border = null;
        if (col % 4 != 0 && col % 4 != 3) {
          border = new MatteBorder(1, 0, 1, 0, Color.black);
        } else if (col % 4 == 3) {
          border = new MatteBorder(1, 0, 1, 1, Color.black);
        } else {
          border = new MatteBorder(1, 1, 1, 0, Color.black);
        }
        thisBeat.setBorder(border);
        String noteForThisRow = rangeOfNotes.get(row);
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
    frame.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
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



