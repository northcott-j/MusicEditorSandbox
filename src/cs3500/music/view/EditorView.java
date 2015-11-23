package cs3500.music.view;

import cs3500.music.model.AbstractNote;
import cs3500.music.model.NoteTypes;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
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
   * musical score CELL_SIZE is the size of one beat notesInRange is a list of all of the note names
   * in the musical score
   */

  private int scoreLength;
  private int scoreHeight;
  public static final int CELL_SIZE = 30;
  private int curBeat = 0;
  private int boardCellWidth;
  private JFrame builtBoard;
  private JScrollPane internalScrollPane;
  private List<String> notesInRange;
  private MouseListener mouseHandler;
  private KeyListener keyHandler;
  private List<Collection<AbstractNote>> notes = null;


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
    notes = vm.returnScore();
    JScrollPane board = createBoard(vm);
    internalScrollPane = board;

    JFrame output = new JFrame("Editor");
    output.addKeyListener(keyHandler);

    output.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    output.add(internalScrollPane);
    output.pack();
    builtBoard = output;
    boardCellWidth = board.getViewport().getWidth() / CELL_SIZE;
    builtBoard.setLocationRelativeTo(null);
    builtBoard.setVisible(true);
    internalScrollPane.getHorizontalScrollBar().setValue(0);
  }

  @Override
  public void tickCurBeat(ViewModel vm, int beatNum) {
    this.curBeat = beatNum;
    if (curBeat % boardCellWidth == 0) {
      internalScrollPane.getHorizontalScrollBar()
              .setValue(curBeat * CELL_SIZE);
    }
    repaint();
  }

  @Override
  public boolean drawn() {
    return false;
  }

  @Override
  public void repaint() {
    builtBoard.repaint();
  }

  @Override
  public void setKeyHandler(KeyListener kh) {
    this.keyHandler = kh;
  }

  @Override
  public void setMouseHandler(MouseListener mh) {
    this.mouseHandler = mh;
  }

  @Override
  public void goToStart() {
    internalScrollPane.getHorizontalScrollBar().setValue(0);
  }

  @Override
  public void goToEnd() {
    int max = internalScrollPane.getHorizontalScrollBar().getMaximum();
    internalScrollPane.getHorizontalScrollBar().setValue(max);
  }

  @Override
  public void scrollUp() {
    int curValue = internalScrollPane.getVerticalScrollBar().getValue();
    int nxtValue = Math.min(internalScrollPane.getVerticalScrollBar().getMaximum(),
            curValue - CELL_SIZE);
    internalScrollPane.getVerticalScrollBar().setValue(nxtValue);
  }

  @Override
  public void scrollDown() {
    int curValue = internalScrollPane.getVerticalScrollBar().getValue();
    int nxtValue = Math.max(0,
            curValue + CELL_SIZE);
    internalScrollPane.getVerticalScrollBar().setValue(nxtValue);
  }

  @Override
  public void scrollLeft() {
    int curValue = internalScrollPane.getHorizontalScrollBar().getValue();
    int nxtValue = Math.min(internalScrollPane.getHorizontalScrollBar().getMaximum(),
            curValue - CELL_SIZE);
    internalScrollPane.getHorizontalScrollBar().setValue(nxtValue);
  }

  @Override
  public void scrollRight() {
    int curValue = internalScrollPane.getHorizontalScrollBar().getValue();
    int nxtValue = Math.max(0,
            curValue + CELL_SIZE);
    internalScrollPane.getHorizontalScrollBar().setValue(nxtValue);
  }

  private JScrollPane createBoard(ViewModel vm) {
    JPanel noteLabels = new JPanel();
    noteLabels.setLayout(new GridLayout(0, 1));
    addNoteLabels(noteLabels);
    noteLabels.setPreferredSize(new Dimension(CELL_SIZE * 2, CELL_SIZE));

    JPanel editorGrid = buildEditorGrids(vm);
    editorGrid.addMouseListener(mouseHandler);


    JPanel numberWrapper = new JPanel();
    numberWrapper.setLayout(new BorderLayout());
    numberWrapper.add(editorGrid);
    numberWrapper.add(noteLabels, BorderLayout.WEST);

    JPanel sizeLocker = new JPanel(new BorderLayout());
    sizeLocker.add(numberWrapper, BorderLayout.NORTH);

    JScrollPane scrollPane = new JScrollPane(sizeLocker);
    return scrollPane;
  }


  /**
   * Adds Note Labels down the edge of the frame
   *
   * @param frame the frame that they are added in
   */
  private void addNoteLabels(JPanel frame) {
    JLabel tempLabel = new JLabel();
    Font font = tempLabel.getFont();
    GridSquare buffer = new GridSquare();
    buffer.setSize(new Dimension(CELL_SIZE, CELL_SIZE));
    frame.add(buffer);
    for (String s : notesInRange) {
      JLabel noteLabel = new JLabel();
      GridSquare noteName = new GridSquare();
      noteName.setSize(CELL_SIZE, CELL_SIZE);
      noteLabel.setFont(new Font(font.getName(), Font.BOLD, 16));
      noteLabel.setText(s);
      noteName.add(noteLabel);
      frame.add(noteName);
    }
    frame.setBorder(BorderFactory.createEmptyBorder(0, 0, CELL_SIZE, 0));
  }

  /**
   * Builds grid of Notes
   *
   * @param vm this ViewModel
   */
  private JPanel buildEditorGrids(ViewModel vm) {
    List<Collection<AbstractNote>> notes = vm.returnScore();
    JPanel frame = new JPanel(new GridLayout(1, 0)) {
      @Override
      public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.red);
        g.fillRect(CELL_SIZE * curBeat, 0, 10, (scoreHeight + 4) * CELL_SIZE);
      }
    };

    for (int col = 0; col < scoreLength; col += 1) {
      JPanel colPanel = new JPanel(new GridLayout(0, 1));
      Collection<String> newNotes = new ArrayList<>();
      Collection<String> sustainedNotes = new ArrayList<>();

      JLabel beatNumber = new JLabel(Integer.toString(col + 1), SwingConstants.CENTER);
      beatNumber.setBorder(new MatteBorder(1, 1, 1, 1, Color.gray));
      beatNumber.setSize(CELL_SIZE / 2, CELL_SIZE);
      colPanel.add(beatNumber);

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
        String noteForThisRow = notesInRange.get(row);
        GridSquare thisBeat = new GridSquare(noteForThisRow, col);
        thisBeat.setSize(CELL_SIZE, CELL_SIZE);
        Border border;
        if (col % 4 != 0 && col % 4 != 3) {
          border = new MatteBorder(1, 0, 1, 0, Color.black);
        } else if (col % 4 == 3) {
          border = new MatteBorder(1, 0, 1, 1, Color.black);
        } else {
          border = new MatteBorder(1, 1, 1, 0, Color.black);
        }
        thisBeat.setBorder(border);
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
    frame.setBorder(BorderFactory.createEmptyBorder(0, 0, CELL_SIZE, 0));
    return frame;
  }


  /**
   * Class for a grid square whose size won't change and can house other components
   */
  private class GridSquare extends JPanel {
    private Dimension size;
    NoteTypes note;
    int octave;
    private int atBeat;

    public GridSquare() {
    }

    public GridSquare(String note, int atBeat) {
      this.atBeat = atBeat;
      NoteTypes pitch;
      int octave;
      if (note.contains("-1")) {
        // If its a sharp
        if (note.length() == 4) {
          octave = -1;
          String pitchString = note.substring(0, 2);
          pitch = NoteTypes.nameLookup(pitchString);
        } else {
          octave = -1;
          String pitchString = note.substring(0, 1);
          pitch = NoteTypes.nameLookup(pitchString);
        }
      } else {
        // If its a sharp
        if (note.length() == 3) {
          octave = Integer.parseInt(note.substring(2));
          String pitchString = note.substring(0, 2);
          pitch = NoteTypes.nameLookup(pitchString);
        } else {
          octave = Integer.parseInt(note.substring(1));
          String pitchString = note.substring(0, 1);
          pitch = NoteTypes.nameLookup(pitchString);
        }
      }
      this.note = pitch;
      this.octave = octave;
    }

    @Override
    public void paint(Graphics g) {
      try {
        boolean isNoteThere = false;
        boolean isNoteHead = false;
        for (AbstractNote n : notes.get(atBeat)) {
          if (n.getOctave() == octave && n.getType().equals(note)) {
            isNoteThere = true;
            if (n.getStartBeat() == atBeat) {
              isNoteHead = true;
            }
            break;
          }
        }
        if (!isNoteThere) {
          this.setBackground(Color.white);
        } else if (!isNoteHead) {
          this.setBackground(Color.green);
        } else if (isNoteHead) {
          this.setBackground(Color.black);
        }
      } catch (IndexOutOfBoundsException e) {
        this.setBackground(Color.white);
      }
      super.paint(g);
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



