package cs3500.music.view;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

import cs3500.music.model.CompositionModel;
import cs3500.music.model.Playable;

/**
 * MIDI View Implementation
 */
public class MidiViewImpl implements View {
  private final Synthesizer synth; // generates audio directly, owns the receiver
  private final Receiver receiver; // in order to play music, gives midimessages
  CompositionModel comp;
  int currTime = 0;

  /**
   * Constructs a MIDI view of a composition
   *
   * @param comp a given composition that will be constructed via the MIDI view
   */
  public MidiViewImpl(CompositionModel comp) {
    Synthesizer s = null;
    Receiver r = null; // recording music, sent to a receiver
    try {
      s = MidiSystem.getSynthesizer();
      r = s.getReceiver();
      s.open();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
    this.comp = comp;
    this.synth = s;
    this.receiver = r;
  }

/*  *//**
   * Testing constructor for Mocking
   *
   * @param synth represents a synthesizer to use as a mock representation
   * @param comp  as a composition that will be read
   *//*
  public MidiViewImpl(Synthesizer synth, CompositionModel comp) {
    Synthesizer s = null;
    Receiver r = null; // recording music, sent to a receiver
    try {
      s = new MockSynthesizer(new StringBuilder());
      r = s.getReceiver();
      s.open();
    } catch (MidiUnavailableException e) {
      e.printStackTrace();
    }
    this.comp = comp;
    this.synth = s;
    this.receiver = r;
  }*/

  /**
   * Relevant classes and methods from the javax.sound.midi library: <ul> <li>{@link
   * MidiSystem#getSynthesizer()}</li> <li>{@link Synthesizer} <ul> <li>{@link
   * Synthesizer#open()}</li> <li>{@link Synthesizer#getReceiver()}</li> <li>{@link
   * Synthesizer#getChannels()}</li> </ul> </li> <li>{@link Receiver} <ul> <li>{@link
   * Receiver#send(MidiMessage, long)}</li> <li>{@link Receiver#close()}</li> </ul> </li><li>{@link
   * MidiMessage}</li> <li>{@link ShortMessage}</li> <li>{@link MidiChannel} <ul> <li>{@link
   * MidiChannel#getProgram()}</li> <li>{@link MidiChannel#programChange(int)}</li> </ul> </li>
   * </ul>
   *
   * @see <a href="https://en.wikipedia.org/wiki/General_MIDI"> https://en
   * .wikipedia.org/wiki/General_MIDI </a>
   */

  /**
   * Where we actually play music based on the MIDI short messages
   *
   * @throws InvalidMidiDataException when midi data is invalid
   */
  public void playNote() throws InvalidMidiDataException {
    long finalInt = -1; // whenever you send, time stamp base. Irrelevant
    List<Playable> currNotes = comp.notesAtTime(currTime); // list of playable notes at current time
    List<Integer> currPitches = currNotes.stream().
            map(n -> n.getPitch()).collect(Collectors.toList());
    for (int j = 0; j < currNotes.size(); j++) {
      MidiMessage start = new ShortMessage(ShortMessage.NOTE_ON,
              currNotes.get(j).getInstrument() - 1,
              currPitches.get(j), currNotes.get(j).getVolume());
      if (currNotes.get(j).getStart() == currTime) {
        this.receiver.send(start, finalInt);
      }
    }
    for (int k = 0; k < currNotes.size(); k++) {
      MidiMessage stop = new ShortMessage(ShortMessage.NOTE_OFF,
              currNotes.get(k).getInstrument() - 1,
              currPitches.get(k), currNotes.get(k).getVolume());
      // TODO :: CHANGE TIMESTAMP FOR THE STOP MESSAGE
      /** Changed to -1; not sure what the 2mil microseconds/200 seconds was for, but
       * the way you've implemented the method you'll want to have it end the note as
       * soon as this method reaches it. */
      if (currNotes.get(k).getEnd() <= currTime) {
        this.receiver.send(stop, -1);
        //this.receiver.send(stop, 200000000);
      }
    }
  }

  @Override
  public void initialize() throws InvalidMidiDataException {
    if (this.comp == null) {
      throw new IllegalArgumentException("No Model!");
    }
    try {
      playNote();
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void updateTime(int time) {
    currTime = time;
    currTime++;
    try {
      playNote();
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getCurrTime() {
    return currTime;
  }

  @Override
  public Dimension getPreferredSize() {
    return null;
  }


  /**
   * In order to test our mock objects
   *
   * @return a String representation of our mock receiver
  public String logToString() {
  MockReceiver mock = (MockReceiver) this.receiver;
  return mock.logToString();
  }*/
}
