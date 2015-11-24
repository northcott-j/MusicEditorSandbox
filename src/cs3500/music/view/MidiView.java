
package cs3500.music.view;
import cs3500.music.model.AbstractNote;

import javax.sound.midi.*;
import java.io.IOException;
import java.util.Collection;


/**
 * The view responsible for the playback of audio based on the musical composition.
 */
public final class MidiView implements View {
  // The synthesizer that deals with interpreting the MIDI data
  private final Synthesizer synth;
  // Stores the System on and off messages rather than plays sound
  private Appendable out;

  /**
   * The constructor for a new MidiView. This will receive a ViewModel (which contains the data),
   * and assigns the model, tempo and musical data to the fields of the MidiView. The constructor
   * also deals with initializing the synthesizer, and opening the synthesizer afterwards. If
   * necessary, it will handle any exceptions thrown by this process, and prints the stack trace to
   * help the debugging process.
   *
   */
  public MidiView() {
    this(null);
  }

  public MidiView(Appendable out) {
    this.out = out;
    // Uses a temporary field to ensure the initialization of the final field
    Synthesizer synthTemp;
    // Initializes the synth and the receiver fields while accounting for errors
    try {
      synthTemp = MidiSystem.getSynthesizer();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
      synthTemp = null;      // Will never make it to this line; allows code to compile
    }
    this.synth = synthTemp;
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
   * designated by the ViewModel's data. If necessary, it will handle any exceptions thrown by this
   * process, and prints the stack trace to help the debugging process.
   *
   * Furthermore, this method can be used to determine the output of the MIDI view. When given
   *
   * @throws IllegalArgumentException invalid mode input
   */
  public void draw(ViewModel vm) {
    try {
      Sequencer sequencer = MidiSystem.getSequencer();
      sequencer.open();
      // Creating the sequence and adding the notes
      Sequence sequence = new Sequence(Sequence.PPQ, 16);
      this.buildTrack(sequence, vm);
      // Loading the sequence into the sequencer, and setting the tempo
      // in microseconds per quarter note
      sequencer.setSequence(sequence);
      sequencer.setTempoInMPQ(vm.getTempo());
      //sequencer.startRecording();
      //sequencer.stopRecording();
      if (out == null) {
        // Plays the music
        sequencer.start();
      }
    } catch (MidiUnavailableException | IOException | InvalidMidiDataException e) {
      e.printStackTrace();
    }
  }

  /**
   * This will add all notes in the {@code score} into the sequence by creating a track, and
   * assigning it to the given sequence.
   *
   * @throws InvalidMidiDataException caught by the run() method
   */
  private void buildTrack(Sequence sequence, ViewModel vm)
          throws InvalidMidiDataException, IOException {
    // Creates the track to add the notes to
    Track track = sequence.createTrack();

    for (int i = 0; i < vm.scoreLength(); i += 1) {
      this.buildBeat(i, track, vm);
    }
  }

  /**
   * This will add all the notes in a given beat into the track.
   *
   * @param beatNum the beat of interest
   * @param track   the track to load data to
   * @throws InvalidMidiDataException caught by the run() method
   */
  private void buildBeat(int beatNum, Track track, ViewModel vm)
          throws InvalidMidiDataException, IOException {
    Collection<AbstractNote> notes = vm.returnScore().get(beatNum);
    for (AbstractNote n : notes) {
      if (n.getStartBeat() == beatNum) {

        // 16 is the microseconds/quarter note (MPQ) value assigned to the sequence
        int startTick = beatNum * 16;
        int tickLength = (n.getEndBeat() - n.getStartBeat()) * 16;
        this.addNote(track, startTick, tickLength, n.midiValue(), n.getInstrument(),
                n.getVolume());
      }
    }
  }

  /**
   * A convenience method that takes care of adding notes to the given track. Inputs determine the
   * type of note to be added, including the start and end points.
   *
   * @param track      the track to which the notes are added
   * @param startTick  the start point of the note (in ticks)
   * @param tickLength the duration of the note (in ticks)
   * @param key        the midi value of the pitch and octave of the note
   * @param velocity   the volume of the note
   * @throws InvalidMidiDataException caught by the run() method
   */
  private void addNote(Track track, int startTick, int tickLength, int key,
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
    if (out == null) {
      track.add(new MidiEvent(on, startTick));
      track.add(new MidiEvent(off, startTick + tickLength));
    }
    else {
      outputShortMessage("ON", instrument - 1, key, velocity, startTick, startTick + tickLength);
      outputShortMessage("OFF", instrument - 1, key, velocity, startTick, startTick + tickLength);
    }
  }

  private void outputShortMessage(String type, int instrument, int key, int velocity, int start,
                                  int end) throws IOException {
    String beatTime;
    if (type.equals("OFF")) {
      beatTime =  Integer.toString(end);
    }
    else {
      beatTime = Integer.toString(start);
    }

    out.append("(" + "Note " + type + "@:" + beatTime + " CHNL: " + Integer.toString(instrument) +
            " KEY: " + Integer.toString(key)
            + " VELOCITY: " + Integer.toString(velocity) + ")" + "\n");
  }
}

