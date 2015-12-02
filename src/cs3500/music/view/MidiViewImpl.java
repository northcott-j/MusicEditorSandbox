package cs3500.music.view;

import cs3500.music.model.CompositionModel;
import cs3500.music.model.Note;

import java.awt.*;
import java.awt.event.KeyListener;
import java.util.Collection;
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

/**
 * A skeleton for MIDI playback
 */
public class MidiViewImpl implements View {
  private final Synthesizer synth; // generates audio directly, owns the receiver
  private final Receiver receiver; // in order to play music, gives midimessages
  CompositionModel comp;
  int currTime = 0;

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

  /**
   * Relevant classes and methods from the javax.sound.midi library: <ul> <li>{@link
   * MidiSystem#getSynthesizer()}</li> <li>{@link Synthesizer} <ul> <li>{@link
   * Synthesizer#open()}</li> <li>{@link Synthesizer#getReceiver()}</li> <li>{@link
   * Synthesizer#getChannels()}</li> </ul> </li> <li>{@link Receiver} <ul> <li>{@link
   * Receiver#send(MidiMessage, long)}</li> <li>{@link Receiver#close()}</li> </ul> </li>
   * <li>{@link
   * MidiMessage}</li> <li>{@link ShortMessage}</li> <li>{@link MidiChannel} <ul> <li>{@link
   * MidiChannel#getProgram()}</li> <li>{@link MidiChannel#programChange(int)}</li> </ul> </li>
   * </ul>
   *
   * @see <a href="https://en.wikipedia.org/wiki/General_MIDI"> https://en
   * .wikipedia.org/wiki/General_MIDI </a>
   */

  public void playNote() throws InvalidMidiDataException {
    long finalInt = -1; // whenever you send, time stamp? base
    int length = comp.lastBeat();
    List<Note> currNotes = comp.notesAtTime(currTime);
    List<Integer> currPitches = currNotes.stream().
            map(n -> n.pitch).collect(Collectors.toList());
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
      if (currNotes.get(k).stop() <= currTime) {
        this.receiver.send(stop, 200000000);
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
  public void updateTime() {
    currTime++;
    try {
      playNote();
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Dimension getPreferredSize() {
    return null;
  }
}
