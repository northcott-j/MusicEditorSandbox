package cs3500.music.view;

import cs3500.music.model.AbstractNote;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

// TODO: FIx the JAvadoc specifically with MIDI

/**
 * The view that allows for interaction with the board
 */
public class PlaybackView extends javax.swing.JFrame implements GuiView {

  public PlaybackView() {
  }

  /**
   * scoreLength is the length of the musical score scoreHeight is the number of notes in the
   * musical score cellSize is the size of one beat notesInRange is a list of all of the note names
   * in the musical score
   */

  int scoreLength;
  int scoreHeight;
  int cellSize = 30;
  int curBeat = 0;
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
    JScrollPane board = createBoard(vm);

    JFrame output = new JFrame("Editor");

    output.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    output.add(board);
    output.pack();
    output.setLocationRelativeTo(null);
    output.setVisible(true);
    board.getHorizontalScrollBar().setValue(0);
    PlaybackMidiView midi = new PlaybackMidiView();
    while (curBeat < vm.scoreLength()) {
      board.getHorizontalScrollBar().setValue(cellSize * curBeat + 90);
      midi.play(vm);
      curBeat += 1;
    }
  }

  private JScrollPane createBoard(ViewModel vm) {
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
    return scrollPane;
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

  /**
   * The view responsible for the playback of audio based on the musical composition.
   */
  private class PlaybackMidiView {
    // The synthesizer that deals with interpreting the MIDI data
    private final Synthesizer synth;
    private Receiver receiver;

    /**
     * The constructor for a new MidiView. This will receive a ViewModel (which contains the data),
     * and assigns the model, tempo and musical data to the fields of the MidiView. The constructor
     * also deals with initializing the synthesizer, and opening the synthesizer afterwards. If
     * necessary, it will handle any exceptions thrown by this process, and prints the stack trace
     * to help the debugging process.
     */
    private PlaybackMidiView() {
      // Uses a temporary field to ensure the initialization of the final field
      Synthesizer synthTemp;
      Receiver receiverTemp;
      // Initializes the synth and the receiver fields while accounting for errors
      try {
        synthTemp = MidiSystem.getSynthesizer();
        receiverTemp = synthTemp.getReceiver();
      } catch (MidiUnavailableException e) {
        e.printStackTrace();
        synthTemp = null;      // Will never make it to this line; allows code to compile
        receiverTemp = null;
      }
      this.synth = synthTemp;
      this.receiver = receiverTemp;
      // Opens the synthesizer
      try {
        this.synth.open();
      } catch (MidiUnavailableException | NullPointerException e) {
        e.printStackTrace();
      }
    }

    /**
     * This method handles the creation and playback of the musical data. It will go through the
     * process of creating the track and filling it with the note data, assigning the track to a
     * sequence, loading this sequence into the sequencer, then playing the sequencer at the tempo
     * designated by the ViewModel's data. If necessary, it will handle any exceptions thrown by
     * this process, and prints the stack trace to help the debugging process.
     *
     * Furthermore, this method can be used to determine the output of the MIDI view. When given
     *
     * @throws IllegalArgumentException invalid mode input
     */
    public void play(ViewModel vm) {
      Collection<AbstractNote> beat = vm.returnScore().get(curBeat);
      for (AbstractNote n : beat) {
        if (n.getStartBeat() == curBeat) {
          try {
            playBeat(receiver, n, vm.getTempo());
          } catch (InvalidMidiDataException | IOException e) {
            throw new IllegalStateException("Something went wrong with MIDI");
          }
        }
      }
      try {
        TimeUnit.MICROSECONDS.sleep(vm.getTempo());
      } catch (InterruptedException e) {
        throw new IllegalStateException("Something went wrong with MIDI timing");
      }
      return;
    }

    /**
     * This will add all the notes in a given beat into the track.
     *
     * @param note the note of interest
     * @throws InvalidMidiDataException caught by the run() method
     */
    private void playBeat(Receiver r, AbstractNote note, int tempo)
            throws InvalidMidiDataException, IOException {

      int startTick = note.getStartBeat() * tempo;
      int endTick = note.getEndBeat() * tempo;
      sendMessage(r, startTick, endTick, note.midiValue(), note.getInstrument(),
              note.getVolume());
    }
  }

  /**
   * A convenience method that takes care of adding notes to the given track. Inputs determine the
   * type of note to be added, including the start and end points.
   *
   * @param startTick the start point of the note (in ticks)
   * @param endTick   the duration of the note (in ticks)
   * @param key       the midi value of the pitch and octave of the note
   * @param velocity  the volume of the note
   * @throws InvalidMidiDataException caught by the run() method
   */
  private void sendMessage(Receiver r, int startTick, int endTick, int key,
                           int instrument, int velocity)
          throws InvalidMidiDataException, IOException {
    // Creates the initial messages:
    ShortMessage on = new ShortMessage();
    // The "on" message for the current note
    on.setMessage(ShortMessage.NOTE_ON, instrument - 1, key, velocity);
    // The "off" message for the current note
    ShortMessage off = new ShortMessage();
    off.setMessage(ShortMessage.NOTE_OFF, instrument - 1, key, 0);
    // Uses the previously defined messages to add the note data into the track or out
    r.send(on, startTick);
    r.send(off, endTick);
  }
}






