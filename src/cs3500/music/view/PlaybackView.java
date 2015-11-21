package cs3500.music.view;

import cs3500.music.controller.MouseHandler;
import cs3500.music.model.AbstractNote;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.*;
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

  int curBeat = 0;
  /**
   * GUI Editor representation of a board
   *
   * @return the GUI Editor view of the board
   */
  public void draw(ViewModel vm) throws IOException {
    EditorView board = new EditorView();
    board.draw(vm);
    PlaybackMidiView midi = new PlaybackMidiView();
    // TODO: Move this up
    while (curBeat < vm.scoreLength()) {
      midi.play(vm);
      curBeat += 1;
      board.setCurBeat(curBeat);
    }
  }

  // TODO do these
  @Override
  public void setKeyHandler(KeyListener kh) {

  }

  @Override
  public void setMouseHandler(MouseHandler mh) {

  }

  @Override
  public void goToStart() {

  }

  @Override
  public void goToEnd() {

  }

  @Override
  public void scrollUp() {

  }

  @Override
  public void scrollDown() {

  }

  @Override
  public void scrollLeft() {

  }

  @Override
  public void scrollRight() {

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
      // TODO: Move this up
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






