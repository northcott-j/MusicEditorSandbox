package cs3500.music.view;

import cs3500.music.model.AbstractNote;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Collection;

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

  int curBeat = 0;
  int tempo;
  boolean drawn = false;
  PlaybackMidiView midi = new PlaybackMidiView();
  EditorView board = new EditorView();

  /**
   * GUI Editor representation of a board
   *
   * @return the GUI Editor view of the board
   */
  public void draw(ViewModel vm) throws IOException {
    tempo = vm.getTempo();
    board.draw(vm);
    drawn = true;
  }

  @Override
  public void tickCurBeat(ViewModel vm, int curBeat) throws InvalidMidiDataException, IOException{
    midi.play(vm);
    board.tickCurBeat(vm, curBeat);
    this.curBeat = curBeat;
  }

  @Override
  public boolean drawn() {
    return drawn;
  }

  @Override
  public void setKeyHandler(KeyListener kh) {
    board.setKeyHandler(kh);
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

  /**
   * The view responsible for the playback of audio based on the musical composition.
   */
  private class PlaybackMidiView {
    // The synthesizer that deals with interpreting the MIDI data
    private final Synthesizer synth;
    private Receiver receiver;

    /**
     * The constructor for a new PlaybackMidi. This will receive a ViewModel (which contains the
     * data), and assigns the model, tempo and musical data to the fields of the MidiView. The
     * constructor also deals with initializing the synthesizer, and opening the synthesizer
     * afterwards. If necessary, it will handle any exceptions thrown by this process, and prints
     * the stack trace to help the debugging process.
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

      long startTick = 0;
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
      r.send(on, startTick);
      r.send(off, endTick);
    }
  }
}






