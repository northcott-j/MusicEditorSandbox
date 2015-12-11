package cs3500.music.view;

import cs3500.music.model.AbstractNote;
import cs3500.music.model.NoteTypes;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.*;
import java.util.List;


/**
 * The view that allows for interaction with the board
 */
public class EditorView extends javax.swing.JFrame implements GuiView {

 /**
  * scoreLength is the length of the musical score
  * scoreHeight is the number of notes in the musical score
  * CELL_SIZE is the size of one beat
  * curBeat is the current beat number
  * boardCellWidth is the number of cells across a board is
  * builtBoard is the entire JFrame
  * internalScrollPane is the alias to the internal scroll JPanel
  * noteRange is the note labels JPanel
  * noteGrid is the grid of notes JPanel
  * notesInRange is the list of the range of notes as strings
  * mouseHandler is the mouseListener
  * keyHandler is the keyListener
  * notes are representations of the notes from the ViewModel
  * notesInRange is a list of all of the note names in the musical score
  * testMode is a boolean saying whether or not a log is being kept
  * testLog is the output of all relevant methods
 */

  private int scoreLength;
  private int scoreHeight;
  public static final int CELL_SIZE = 30;
  private int curBeat = 0;
  private int boardCellWidth;
  private JFrame builtBoard;
  private JScrollPane internalScrollPane;
  private JPanel noteRange;
  private JPanel noteGrid;
  private List<String> notesInRange;
  private MouseListener mouseHandler;
  private KeyListener keyHandler;
  private List<Collection<AbstractNote>> notes = null;
  private boolean testMode = false;
  Appendable testLog;

  public EditorView() {
    this.initializeFields();
  }

  // Test Editor constructor
  public EditorView(Appendable log) {
    this.initializeFields();
    this.testLog = log;
    testMode = true;
  }


  /**
   * Initializes Editor to default fields
   */
  private void initializeFields() {
    // Default length
    scoreLength = 64;
    // Default height
    scoreHeight = 16;
    // Default note types
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
    // Expands board beyond the default if the Model expands
    if (vm.scoreLength() != 0) {
      if (scoreHeight <= vm.scoreHeight()) {
        scoreHeight = vm.scoreHeight();
        notesInRange = vm.notesInRange();
      }
      if (scoreLength < vm.scoreLength()) {
        scoreLength = vm.scoreLength();
      }
    }
    notes = vm.returnScore();
    // The ScrollPanel
    JScrollPane board = createBoard(vm);
    internalScrollPane = board;

    // The upper level JFrame
    JFrame output = new JFrame("Editor");
    // Adds the keyHandler to the entire JFrame
    output.addKeyListener(keyHandler);

    output.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // Add the ScrollPanel to the JFrame
    output.add(internalScrollPane);
    if (testMode) {
      try {
        testLog.append("Packing Output and setting Visible" + "\n");
      } catch (IOException io) {
        throw new IllegalStateException("Test log broke");
      }
    } else {
      output.pack();
      output.setVisible(true);
    }
    // Set the fields for faster access to aliases
    builtBoard = output;
    boardCellWidth = board.getViewport().getWidth() / CELL_SIZE;
    builtBoard.setLocationRelativeTo(null);
    internalScrollPane.getHorizontalScrollBar().setValue(0);
  }

  @Override
  public void tickCurBeat(ViewModel vm, int beatNum) {
    this.curBeat = beatNum;
    if (testMode) {
      try {
        testLog.append("Setting curBeat in editor and repainting" + "\n");
      } catch (IOException io) {
        throw new IllegalStateException("Test log broke");
      }
    } else {
      // If the beat is going outside the current view, shift the view
      if (curBeat % boardCellWidth == 0) {
        internalScrollPane.getHorizontalScrollBar()
                .setValue(curBeat * CELL_SIZE);
      }
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
  public List<String> getNotesInRange() {
    return Collections.unmodifiableList(notesInRange);
  }

  @Override
  public void setMouseHandler(MouseListener mh) {
    this.mouseHandler = mh;
  }

  @Override
  public void goToStart(Integer k) {
    internalScrollPane.getHorizontalScrollBar().setValue(0);
  }

  @Override
  public void goToEnd(Integer k) {
    int max = internalScrollPane.getHorizontalScrollBar().getMaximum();
    internalScrollPane.getHorizontalScrollBar().setValue(max);
  }

  @Override
  public void scrollUp(Integer k) {
    int curValue = internalScrollPane.getVerticalScrollBar().getValue();
    int nxtValue = Math.min(internalScrollPane.getVerticalScrollBar().getMaximum(),
            curValue - CELL_SIZE);
    internalScrollPane.getVerticalScrollBar().setValue(nxtValue);
  }

  @Override
  public void scrollDown(Integer k) {
    int curValue = internalScrollPane.getVerticalScrollBar().getValue();
    int nxtValue = Math.max(0,
            curValue + CELL_SIZE);
    internalScrollPane.getVerticalScrollBar().setValue(nxtValue);
  }

  @Override
  public void scrollLeft(Integer k) {
    int curValue = internalScrollPane.getHorizontalScrollBar().getValue();
    int nxtValue = Math.min(internalScrollPane.getHorizontalScrollBar().getMaximum(),
            curValue - CELL_SIZE);
    internalScrollPane.getHorizontalScrollBar().setValue(nxtValue);
  }

  @Override
  public void scrollRight(Integer k) {
    int curValue = internalScrollPane.getHorizontalScrollBar().getValue();
    int nxtValue = Math.max(0,
            curValue + CELL_SIZE);
    internalScrollPane.getHorizontalScrollBar().setValue(nxtValue);
  }

  @Override
  public void expandUp(ViewModel vm) {
    // Find the max note value
    int[] max = findMaxandMin("MAX");
    int currentHighNote = max[0];
    // Turn the range of notes into a maluable array
    ArrayList<String> notesInRange = new ArrayList<>();
    notesInRange.addAll(this.notesInRange);
    int currentHighOctave = max[1];
    // If already at the highest octave,
    if (currentHighOctave == 9) {
      // Max out the notes
      while (currentHighNote < 11) {
        currentHighNote += 1;
        String notePitch = NoteTypes.valueLookup(currentHighNote).toString();
        String newNote = notePitch + Integer.toString(currentHighNote);
        notesInRange.add(0, newNote);
      }
    } else {
      // Otherwise add an octave's worth of notes up
      String targetHighNote = NoteTypes.valueLookup(currentHighNote).toString() +
              Integer.toString(currentHighOctave + 1);
      String newNote = "";
      while (!newNote.equals(targetHighNote)) {
        currentHighNote += 1;
        if (currentHighNote % 12 == 0) {
          currentHighNote = 0;
          currentHighOctave += 1;
        }
        String notePitch = NoteTypes.valueLookup(currentHighNote).toString();
        newNote = notePitch + Integer.toString(currentHighOctave);
        notesInRange.add(0, newNote);
      }
    }
    // Update the note range
    this.notesInRange = notesInRange;
    // Update the height
    scoreHeight = this.notesInRange.size();
    // Draw the board
    JScrollPane newPane = createBoard(vm);
    builtBoard.add(newPane);
    builtBoard.remove(internalScrollPane);
    internalScrollPane = newPane;
    builtBoard.revalidate();
    builtBoard.repaint();
  }

  @Override
  public void expandDown(ViewModel vm) {
    // Find the minimum note value
    int[] min = findMaxandMin("MIN");
    int currentLowNote = min[0];
    // Make the note range into a maluable array
    ArrayList<String> notesInRange = new ArrayList<>();
    notesInRange.addAll(this.notesInRange);
    int currentLowOctave = min[1];
    // If we are already at the lowest octave,
    // TODO :: Hard coded octave range
    if (currentLowOctave == -1) {
      // Just add to the lowest note
      while (currentLowNote > 0) {
        currentLowNote -= 1;
        String notePitch = NoteTypes.valueLookup(currentLowNote).toString();
        String newNote = notePitch + Integer.toString(currentLowNote);
        notesInRange.add(newNote);
      }
    } else {
      // Otherwise, add one octave's worth of notes down
      String targetLowNote = NoteTypes.valueLookup(currentLowNote).toString() +
              Integer.toString(currentLowOctave - 1);
      String newNote = "";
      while (!newNote.equals(targetLowNote)) {
        currentLowNote -= 1;
        if (currentLowNote == -1) {
          currentLowNote = 11;
          currentLowOctave -= 1;
        }
        String notePitch = NoteTypes.valueLookup(currentLowNote).toString();
        newNote = notePitch + Integer.toString(currentLowOctave);
        notesInRange.add(newNote);
      }
    }
    // Change the note range
    this.notesInRange = notesInRange;
    // update the range height
    scoreHeight = this.notesInRange.size();
    // Recreate and draw the board
    JScrollPane newPane = createBoard(vm);
    builtBoard.add(newPane);
    builtBoard.remove(internalScrollPane);
    internalScrollPane = newPane;
    builtBoard.revalidate();
    builtBoard.repaint();
  }

  @Override
  public void expandOut(ViewModel vm) {
    // Increases the GUI drawn score length by 8
    scoreLength += 8;
    // Redraws the board
    JScrollPane newPane = createBoard(vm);
    // Adds the new board and draws everything
    builtBoard.add(newPane);
    builtBoard.remove(internalScrollPane);
    builtBoard.revalidate();
    builtBoard.repaint();
    internalScrollPane = newPane;
  }

  /**
   * Finds the current high and low notes being drawn
   */
  private int[] findMaxandMin(String maxOrMin) {
    String noteAndOctave;
    if (maxOrMin.equals("MAX")) {
      noteAndOctave = notesInRange.get(0);
    } else {
      noteAndOctave = notesInRange.get(notesInRange.size() - 1);
    }
    int pitch;
    int octave;
    // Special case if octave is -1
    // TODO :: Hard coded octave range
    if (noteAndOctave.contains("-1")) {
      // If its a sharp
      if (noteAndOctave.length() == 4) {
        octave = -1;
        String pitchString = noteAndOctave.substring(0, 2);
        pitch = NoteTypes.nameLookup(pitchString).noteOrder();
      } else {
        octave = -1;
        String pitchString = noteAndOctave.substring(0, 1);
        pitch = NoteTypes.nameLookup(pitchString).noteOrder();
      }
    } else {
      // Regular octaves 0 - 9
      // If its a sharp
      if (noteAndOctave.length() == 3) {
        octave = Integer.parseInt(noteAndOctave.substring(2));
        String pitchString = noteAndOctave.substring(0, 2);
        pitch = NoteTypes.nameLookup(pitchString).noteOrder();
      } else {
        octave = Integer.parseInt(noteAndOctave.substring(1));
        String pitchString = noteAndOctave.substring(0, 1);
        pitch = NoteTypes.nameLookup(pitchString).noteOrder();
      }
    }
    return new int[]{pitch, octave};
  }

  /**
   * Creates the scrollable pane to be put in the JFrame
   *
   * @param vm the viewModel
   * @return the JScrollPane making up the board
   */
  private JScrollPane createBoard(ViewModel vm) {
    // JPanel to house the JLabels of notes
    JPanel noteLabels = new JPanel();
    noteLabels.setLayout(new GridLayout(0, 1));
    // Void method to add labels to the above JPanel
    if (testMode) {
      try {
        testLog.append("Creating Note Labels" + "\n");
      } catch (IOException io) {
        throw new IllegalStateException("Test log broke");
      }
    }
    addNoteLabels(noteLabels);
    // Sets the size of the list of Note names
    noteLabels.setPreferredSize(new Dimension(CELL_SIZE * 2, CELL_SIZE));
    noteRange = noteLabels;
    if (testMode) {
      try {
        testLog.append("Creating Editor Grid" + "\n");
      } catch (IOException io) {
        throw new IllegalStateException("Test log broke");
      }
    }
    // Outputs a JPanel with the grid of notes
    JPanel editorGrid = buildEditorGrids(vm);
    // Adds the Mouse Listener to this JPanel
    editorGrid.addMouseListener(mouseHandler);
    noteGrid = editorGrid;

    // This locks the size and binds notes to the board
    JPanel numberWrapper = new JPanel();
    numberWrapper.setLayout(new BorderLayout());
    numberWrapper.add(editorGrid);
    numberWrapper.add(noteLabels, BorderLayout.WEST);

    // This stops resizing from stretching everything
    JPanel sizeLocker = new JPanel(new BorderLayout());
    sizeLocker.add(numberWrapper, BorderLayout.NORTH);

    // Everything is then placed in a JScrollPane and returned
    JScrollPane scrollPane = new JScrollPane(sizeLocker);
    return scrollPane;
  }


  /**
   * Adds Note Labels down the edge of the frame
   *
   * @param frame the frame that they are added in
   */
  private void addNoteLabels(JPanel frame) {
    frame.removeAll();
    JLabel tempLabel = new JLabel();
    Font font = tempLabel.getFont();
    // Adds an empty buffer cell to align things properly
    GridSquare buffer = new GridSquare();
    buffer.setSize(new Dimension(CELL_SIZE, CELL_SIZE));
    frame.add(buffer);
    // For every note in the range, create a JLabel and add it
    for (String s : notesInRange) {
      JLabel noteLabel = new JLabel();
      GridSquare noteName = new GridSquare();
      noteName.setSize(CELL_SIZE, CELL_SIZE);
      noteLabel.setFont(new Font(font.getName(), Font.BOLD, 16));
      noteLabel.setText(s);
      noteName.add(noteLabel);
      if (testMode) {
        try {
          testLog.append("Created and drew label: " + s + "\n");
        } catch (IOException io) {
          throw new IllegalStateException("Test log broke");
        }
      }
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
    // Creates a JPanel and overrides the paint method to draw a red line at the current beat
    JPanel frame = new JPanel(new GridLayout(1, 0)) {
      @Override
      public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.red);
        g.fillRect(CELL_SIZE * curBeat, 0, 10, (scoreHeight + 4) * CELL_SIZE);
      }
    };

    // For each column (beat)...
    for (int col = 0; col < scoreLength; col += 1) {
      // Creates a JPanel for this one column
      JPanel colPanel = new JPanel(new GridLayout(0, 1));
      // Adds the number of the beat to the top of the column
      JLabel beatNumber = new JLabel(Integer.toString(col + 1), SwingConstants.CENTER);
      beatNumber.setBorder(new MatteBorder(1, 1, 1, 1, Color.gray));
      beatNumber.setSize(CELL_SIZE / 2, CELL_SIZE);
      colPanel.add(beatNumber);
      // For each Row...
      for (int row = 0; row < scoreHeight; row += 1) {
        // Gets the type of note for this row
        String noteForThisRow = notesInRange.get(row);
        // Creates a grid square
        GridSquare thisBeat = new GridSquare(noteForThisRow, col);
        thisBeat.setSize(CELL_SIZE, CELL_SIZE);
        Border border;
        // Sets the proper border
        if (col % 4 != 0 && col % 4 != 3) {
          border = new MatteBorder(1, 0, 1, 0, Color.black);
        } else if (col % 4 == 3) {
          border = new MatteBorder(1, 0, 1, 1, Color.black);
        } else {
          border = new MatteBorder(1, 1, 1, 0, Color.black);
        }
        thisBeat.setBorder(border);
        // Adds the completed beat to the column
        colPanel.add(thisBeat);
        if (testMode) {
          try {
            testLog.append("Created and drew note @ " + Integer.toString(col) + "," +
                    Integer.toString(row) + "\n");
          } catch (IOException io) {
            throw new IllegalStateException("Test log broke");
          }
        }
      }
      // After all the rows are completed, the column is added to the overall JPanel
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
    // Note information
    NoteTypes note;
    int octave;
    private int atBeat;

    public GridSquare() {
    }

    public GridSquare(String note, int atBeat) {
      this.atBeat = atBeat;
      NoteTypes pitch;
      int octave;
      // Turns the Note string into a useable NoteType and Octave
      // TODO :: Hard coded Octave range
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
        // Is there a note there
        boolean isNoteThere = false;
        // Is it the head of a note
        boolean isNoteHead = false;
        // Check each note at this beat
        for (AbstractNote n : notes.get(atBeat)) {
          // If the octave and type are the same...
          if (n.getOctave() == octave && n.getType().equals(note)) {
            // A note is at this point
            isNoteThere = true;
            // If the startbeat is the same as this beat..
            if (n.getStartBeat() == atBeat) {
              // It is the ehad of a note
              isNoteHead = true;
            }
            // Break the for loop because this note has been found
            break;
          }
        }
        // If it isn't there...
        if (!isNoteThere) {
          // Make the background white
          this.setBackground(Color.white);
          // If it isn't the head...
        } else if (!isNoteHead) {
          // Make the background green
          this.setBackground(Color.green);
          // If it is the head...
        } else if (isNoteHead) {
          // Make the background black
          this.setBackground(Color.black);
        }
        // If an IndexOutOfBoundsException is thrown...
      } catch (IndexOutOfBoundsException e) {
        // Make background white
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



