package cs3500.music.view;

import cs3500.music.model.AbstractNote;

import javax.sound.midi.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * The view that allows for interaction with the board
 */
public class PlaybackView extends javax.swing.JFrame implements GuiView {

  /**
   * @field curBeat is the current beat
   * @field tempo is the tempo of the piece
   * @field drawn is if the board has rendered
   * @field midi is the specialized midi view
   * @field board is an EditorView
   */

  private int curBeat = 0;
  private int tempo;
  private boolean drawn = false;
  private PlaybackMidiView midi = new PlaybackMidiView();
  private GuiView board = new EditorView();
  private boolean testMode = false;
  private Appendable testLog;


  public PlaybackView() {
  }

  // Test mode constructor
  public PlaybackView(Appendable log) {
    this.testMode = false;
    testLog = log;
    board = new EditorView(log);
    midi = new PlaybackMidiView(log);
  }

  /**
   * GUI Editor representation of a board
   *
   * @return the GUI Editor view of the board
   */
  public void draw(ViewModel vm) throws IOException {
    tempo = vm.getTempo();
    if (testMode) {
      testLog.append("Delegated to Editor View draw method" + "\n");
    }
    board.draw(vm);
    drawn = true;
  }

  @Override
  public void tickCurBeat(ViewModel vm, int curBeat) throws InvalidMidiDataException, IOException{
    this.curBeat = curBeat;
    if (testMode) {
      testLog.append("Started MIDI @ beat: " + Integer.toString(curBeat)  + "\n");
    }
    midi.play(vm);
    board.tickCurBeat(vm, curBeat);
  }

  @Override
  public boolean drawn() {
    return drawn;
  }

  @Override
  public void repaint() {
    board.repaint();
  }

  @Override
  public void setKeyHandler(KeyListener kh) {
    board.setKeyHandler(kh);
  }

  @Override
  public List<String> getNotesInRange() {
    return board.getNotesInRange();
  }

  @Override
  public void setMouseHandler(MouseListener mh) {
    board.setMouseHandler(mh);
  }

  @Override
  public void goToStart() {
    board.goToStart();
  }

  @Override
  public void goToEnd() {
    board.goToEnd();
  }

  @Override
  public void scrollUp() {
    board.scrollUp();
  }

  @Override
  public void scrollDown() {
    board.scrollDown();
  }

  @Override
  public void scrollLeft() {
    board.scrollLeft();
  }

  @Override
  public void scrollRight() {
    board.scrollRight();
  }

  @Override
  public void expandUp(ViewModel vm) {
    board.expandUp(vm);
  }

  @Override
  public void expandDown(ViewModel vm) {
    board.expandDown(vm);
  }

  @Override
  public void expandOut(ViewModel vm) {
    board.expandOut(vm);
  }

  /**
   * The view responsible for the playback of audio based on the musical composition.
   * This view was specialized for use with a Timer. I did not want to break the other MIDI
   * as we wanted to keep each view functional on its own
   */
  private class PlaybackMidiView {
    // The synthesizer that deals with interpreting the MIDI data
    private final Synthesizer synth;
    private Receiver receiver;
    private boolean testMode = false;
    private Appendable testLog;

    /**
     * The constructor for a new PlaybackMidi. This will receive a ViewModel (which contains the
     * data), and assigns the model, tempo and musical data to the fields of the MidiView. The
     * constructor also deals with initializing the synthesizer, and opening the synthesizer
     * afterwards. If necessary, it will handle any exceptions thrown by this process, and prints
     * the stack trace to help the debugging process.
     */
    private PlaybackMidiView() {
      this(null);
      testMode = false;
    }

    // Test constructor
    private PlaybackMidiView(Appendable testLog) {
      testMode = true;
      this.testLog = testLog;
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
     * Gets all the notes at a beat and plays them
     *
     * @throws IllegalArgumentException invalid mode input
     */
    public void play(ViewModel vm) throws InvalidMidiDataException, IOException {
      Collection<AbstractNote> beat = vm.returnScore().get(curBeat);
      for (AbstractNote n : beat) {
        if (n.getStartBeat() == curBeat) {
          playBeat(receiver, n, tempo);
        }
      }
    }

    /**
     * This will add all the notes in a given beat into the track.
     *
     * @param note the note of interest
     * @throws InvalidMidiDataException caught by the run() method
     */
    private void playBeat(Receiver r, AbstractNote note, int tempo)
            throws InvalidMidiDataException, IOException {

      // Time is 0 to start the note immediately
      long startTick = 0;
      // The end tick is the duration of the note in beats * the tempo and plus the current
      // position of the synth in order to time everything correctly
      long endTick = (note.getEndBeat() - note.getStartBeat()) * tempo +
              synth.getMicrosecondPosition();
      sendMessage(r, startTick, endTick, note.midiValue(), note.getInstrument(),
              note.getVolume());
    }

    /**
     * Method that sends the needed on and off shortmessages to midi
     *
     * @param startTick the start point of the note (in ticks)
     * @param endTick   the duration of the note (in ticks)
     * @param key       the midi value of the pitch and octave of the note
     * @param velocity  the volume of the note
     * @throws InvalidMidiDataException caught by the run() method
     */
    private void sendMessage(Receiver r, long startTick, long endTick, int key,
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
      if (testMode) {
        outputShortMessage("ON", instrument - 1, key, velocity, startTick, endTick);
        outputShortMessage("OFF", instrument - 1, key, velocity, startTick, endTick);
      } else {
        r.send(on, startTick);
        r.send(off, endTick);
      }
    }

    private void outputShortMessage(String type, int instrument, int key, int velocity, long start,
                                    long end) throws IOException {
      String beatTime;
      if (type.equals("OFF")) {
        // Turns off after this amount of time
        beatTime = Long.toString(end - synth.getMicrosecondPosition());
      } else {
        // Is always 0 because the note starts immediately
        beatTime = Long.toString(start);
      }

      testLog.append("(" + "Note " + type + "@:" + beatTime + " CHNL: "
              + Integer.toString(instrument) + " KEY: " + Integer.toString(key)
              + " VELOCITY: " + Integer.toString(velocity) + ")" + "\n");
    }
  }
}






