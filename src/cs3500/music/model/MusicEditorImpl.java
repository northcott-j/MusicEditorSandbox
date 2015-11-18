package cs3500.music.model;

import cs3500.music.util.CompositionBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Implementing the Music Editor Interface Created by Jonathan on 11/1/2015.
 */
public final class MusicEditorImpl implements MusicEditorModel {
  // The current beat of this MusicEditorImpl
  private int curBeat;
  // Tempo for the music
  private int tempo;
  // The Array of notes for each beat
  private final List<Collection<AbstractNote>> musicalArray;
  // The deepest note
  private NoteTypes lowNote;
  // The highest note
  private NoteTypes highNote;
  // The lowest octave
  private int lowOctave;
  // The highest octave
  private int highOctave;

  /**
   * musicalArray starts empty and can be changed either by adding a printedscore or by
   * individually
   * adding notes
   */
  private MusicEditorImpl() {
    curBeat = 0;
    musicalArray = new ArrayList<>();
    // The below assignments are defaults
    tempo = 200000;
    lowNote = null;
    highNote = null;
    highOctave = 10;
    lowOctave = -2;
  }

  /**
   * Creates a MusicEditorImpl
   */
  public static MusicEditorImpl makeEditor() {
    return new MusicEditorImpl();
  }

  @Override
  public AbstractNote makeNote(NoteTypes type, int octave, int start, int end,
                               int volume) {
    return Note.makeNote(type, octave, start, end, 1, volume);
  }

  public static final class Builder implements CompositionBuilder<MusicEditorModel> {
    private MusicEditorImpl accEditor = new MusicEditorImpl();

    @Override
    public MusicEditorModel build() {
      return accEditor;
    }

    @Override
    public CompositionBuilder<MusicEditorModel> setTempo(int tempo) {
      accEditor.setTempo(tempo);
      return this;
    }

    @Override
    public CompositionBuilder<MusicEditorModel> addNote(int start, int end,
                                                        int instrument, int pitch, int volume) {
      NoteTypes type = NoteTypes.valueLookup(pitch % 12);
      int octave = (pitch - (pitch % 12)) / 12 - 1;
      AbstractNote note = Note.makeNote(type, octave, start, end - 1, instrument, volume);
      accEditor.addNote(note);
      return this;
    }
  }

  /**
   * Weaves together two notes - one that is being changed and one already in that spot.
   *
   * @param modified the note being changed into the shared position
   * @param current  the note currently in the position
   */
  private void overlappedNotes(AbstractNote modified, AbstractNote current) {
    // If the starts are the same, add whichever is longer
    if (modified.getStartBeat() == current.getStartBeat()) {
      if (modified.getEndBeat() > current.getEndBeat()) {
        this.deleteNote(current);
        this.addNote(modified);
      }
    }
    // If the modified start falls somewhere inside the current
    else if (modified.getStartBeat() > current.getStartBeat()) {
      // If modified is longer, shorten the current one
      if (modified.getEndBeat() >= current.getEndBeat()) {
        this.changeNoteEnd(current, modified.getStartBeat() - 1);
        this.addNote(modified);
      }
      // If the modified is shorter, shorten the current and make the modified longer
      else if (modified.getEndBeat() < current.getEndBeat()) {
        modified.changeEnd(current.getEndBeat());
        this.changeNoteEnd(current, modified.getStartBeat() - 1);
        this.addNote(modified);
      }
    }
    // If the tail of the modified lands on the current note, but not the head of the modified note
    else if (modified.getEndBeat() >= current.getStartBeat()) {
      // If modified is longer, shorten the modified one and lengthen the current one
      if (modified.getEndBeat() >= current.getEndBeat()) {
        this.changeNoteEnd(current, modified.getEndBeat());
        modified.changeEnd(current.getStartBeat() - 1);
        this.addNote(modified);
      }
      // If the modified is shorter, shorten the modified
      else if (modified.getEndBeat() < current.getEndBeat()) {
        modified.changeEnd(current.getStartBeat() - 1);
        this.addNote(modified);
      }
    }
  }

  /**
   * Adds space if needed for a note
   *
   * @param note the note that needs space allocation
   */
  private void addEmptyBeats(AbstractNote note) {
    while (this.scoreLength() <= note.getEndBeat()) {
      this.musicalArray.add(new ArrayList<>());
    }
  }

  @Override
  public void changeNoteStart(AbstractNote note, int startBeat) {
    // Remove the note
    this.deleteNote(note);
    // Adds space if needed
    this.addEmptyBeats(note);
    // Mutate the start time
    note.changeStart(startBeat);
    // Check all notes in beat range
    for (int i = startBeat; i <= note.getEndBeat(); i += 1) {
      for (AbstractNote n : this.musicalArray.get(i)) {
        // Make sure they don't overlap another note
        if (note.overlap(n)) {
          // If there is an overlap, delegate to another method and exit this one
          this.overlappedNotes(note, n);
          return;
        }
      }
    }
    // If everything is good, add back the note with new start
    this.addNote(note);
  }

  /**
   * Trims empty Arrays at the end of the piece when end is changed or notes are removed
   */

  private void trimEnd() {
    int index = this.scoreLength() - 1;
    while (this.musicalArray.get(index).size() == 0 && index > 0) {
      if (index < 0) {
        throw new IllegalStateException("This method was called when it shouldn't have been");
      }
      this.musicalArray.remove(index);
      index -= 1;
    }
  }

  @Override
  public void changeNoteEnd(AbstractNote note, int endBeat) {
    this.deleteNote(note);
    this.addEmptyBeats(note);
    note.changeEnd(endBeat);
    for (int i = note.getStartBeat(); i <= endBeat; i += 1) {
      while (i >= this.scoreLength()) {
        this.musicalArray.add(new ArrayList<>());
      }
      for (AbstractNote n : this.musicalArray.get(i)) {
        if (note.overlap(n)) {
          // If there is an overlap, delegate to another method and exit this one
          this.overlappedNotes(note, n);
          this.trimEnd();
          return;
        }
      }
    }
    this.addNote(note);
    this.trimEnd();
  }

  /**
   * Computes the note range of the piece
   */
  private void updateRange() {
    int highOctave = -2;
    int highNote = -2;
    int lowOctave = 10;
    int lowNote = 14;
    for (Collection<AbstractNote> a : this.musicalArray) {
      for (AbstractNote n : a) {
        if (n.getOctave() > highOctave) {
          highOctave = n.getOctave();
          highNote = n.getType().noteOrder();
        }
        if (n.getOctave() < lowOctave) {
          lowOctave = n.getOctave();
          lowNote = n.getType().noteOrder();
        }
        if (n.getOctave() == lowOctave && n.getType().noteOrder() < lowNote) {
          lowNote = n.getType().noteOrder();
        }
        if (n.getOctave() == highOctave && n.getType().noteOrder() > highNote) {
          highNote = n.getType().noteOrder();
        }
      }
    }

    if (highOctave != -2 && highNote != -2) {
      this.highOctave = highOctave;
      this.highNote = NoteTypes.valueLookup(highNote);
    }
    if (lowOctave != 10 && lowNote != 14) {
      this.lowOctave = lowOctave;
      this.lowNote = NoteTypes.valueLookup(lowNote);
    }
  }

  @Override
  public void changeNoteOctave(AbstractNote note, int octave) {
    this.deleteNote(note);
    this.addEmptyBeats(note);
    note.changeOctave(octave);
    for (int i = note.getStartBeat(); i <= note.getEndBeat(); i += 1) {
      for (AbstractNote n : this.musicalArray.get(i)) {
        // Checks note range to check if there is an overlap
        if (note.overlap(n)) {
          this.overlappedNotes(note, n);
          this.updateRange();
          return;
        }
      }
    }
    this.addNote(note);
    this.updateRange();
  }

  @Override
  public void changeNoteType(AbstractNote note, NoteTypes newType) {
    this.deleteNote(note);
    this.addEmptyBeats(note);
    note.changeType(newType);
    for (int i = note.getStartBeat(); i <= note.getEndBeat(); i += 1) {
      for (AbstractNote n : this.musicalArray.get(i)) {
        // Checks note range to check if there is an overlap
        if (note.overlap(n)) {
          this.overlappedNotes(note, n);
          this.updateRange();
          return;
        }
      }
    }
    this.addNote(note);
    this.updateRange();
  }

  @Override
  public void changeNoteInstrument(AbstractNote note, int instrument) {
    note.changeInstrument(instrument);
  }

  @Override
  public void changeNoteVol(AbstractNote note, int volume) {
    note.changeVolume(volume);
  }

  @Override
  public AbstractNote getNote(NoteTypes type, int octave, int beat) {
    try {
      this.musicalArray.get(beat);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("No such note");
    }
    // Gets the Array of notes at a certain beat
    Collection<AbstractNote> beatNotes = this.musicalArray.get(beat);
    for (AbstractNote n : beatNotes) {
      // If the octave and type are the same at this beat, it is the note we are looking for
      // because duplicate same notes are not allowed at this point
      if (type == n.getType() && octave == n.getOctave()) {
        return n;
      }
    }
    throw new IllegalArgumentException("No such note");
  }

  @Override
  public void addNote(AbstractNote note) {
    this.addEmptyBeats(note);
    for (int i = note.getStartBeat(); i <= note.getEndBeat(); i += 1) {
      for (AbstractNote n : this.musicalArray.get(i)) {
        // Checks note range to check if there is an overlap
        if (note.overlap(n)) {
          this.overlappedNotes(note, n);
          this.updateRange();
          return;
        }
      }
    }
    for (int i = note.getStartBeat(); i <= note.getEndBeat(); i += 1) {
      this.musicalArray.get(i).add(note);
    }
    this.updateRange();
  }

  @Override
  public int scoreLength() {
    return this.musicalArray.size();
  }

  @Override
  public int scoreHeight() {
    AbstractNote highNote = makeNote(this.highNote, highOctave, 0, 1, 100);
    AbstractNote lowNote = makeNote(this.lowNote, lowOctave, 0, 1, 100);
    return highNote.midiValue() - lowNote.midiValue() + 1;
  }

  @Override
  public List<String> notesInRange() {
    ArrayList<String> acc = new ArrayList<>();
    // String representation of the highest note
    String highestNote = this.highNote.toString() + Integer.toString(this.highOctave);
    String curNote = "";
    int curNoteVal = this.lowNote.noteOrder();
    int curNoteOct = this.lowOctave;
    while (!curNote.equals(highestNote)) {
      if (curNoteVal > 11) {
        // 11 is the highest note order value
        curNoteVal = 0;
        curNoteOct += 1;
      }
      // Gets the NoteType string name and adds it to the current octave plus a space
      curNote = NoteTypes.valueLookup(curNoteVal).toString()
              + Integer.toString(curNoteOct);
      acc.add(0, curNote);
      curNoteVal += 1;
    }
    // adds a new line character before returning it
    return acc;
  }

  @Override
  public int getCurBeat() {
    return this.curBeat;
  }

  @Override
  public int getTempo() {
    return this.tempo;
  }

  @Override
  public void setTempo(int newTempo) {
    if (newTempo <= 0) {
      throw new IllegalArgumentException("Not a valid tempo");
    }
    this.tempo = newTempo;
  }

  @Override
  public List<Collection<AbstractNote>> returnScore() {
    List<Collection<AbstractNote>> shield = Collections.unmodifiableList(this.musicalArray);
    return shield;
  }

  @Override
  public void changeCurBeat(int newBeat) {
    if (newBeat < 0) {
      throw new IllegalArgumentException("Invalid beat");
    }
    if (newBeat > this.scoreLength()) {
      throw new IllegalStateException("No more music at this beat");
    }
    this.curBeat = newBeat;
  }

  @Override
  public void deleteNote(AbstractNote note) {
    for (int i = note.getStartBeat(); i <= note.getEndBeat(); i += 1) {
      this.musicalArray.get(i).remove(note);
    }
    this.trimEnd();
    this.updateRange();
  }

  @Override
  public Collection<AbstractNote> playMusic() {
    this.changeCurBeat(curBeat + 1);
    try {
      this.musicalArray.get(curBeat - 1);
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalStateException("No more music");
    }
    Collection<AbstractNote> shield =
            Collections.unmodifiableCollection(this.musicalArray.get(curBeat - 1));
    return shield;
  }

  @Override
  public void simultaneousScore(List<Collection<AbstractNote>> secondScore) {
    for (int i = 0; i < secondScore.size(); i += 1) {
      for (AbstractNote n : secondScore.get(i)) {
        if (n.getStartBeat() == i) {
          this.addNote(n);
        }
      }
    }
  }

  @Override
  public void consecutiveScore(List<Collection<AbstractNote>> secondScore) {
    // Push secondScore's notes start and end beats by the length of this musicalArray
    for (int i = 0; i < secondScore.size(); i += 1) {
      for (AbstractNote n : secondScore.get(i)) {
        if (n.getStartBeat() == i) {
          n.changeEnd(n.getEndBeat() + this.scoreLength());
          n.changeStart(n.getStartBeat() + this.scoreLength());
          this.addNote(n);
        }
      }
    }
  }
}
