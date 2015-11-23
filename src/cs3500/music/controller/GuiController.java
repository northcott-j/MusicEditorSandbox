package cs3500.music.controller;


import cs3500.music.model.AbstractNote;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.NoteTypes;
import cs3500.music.view.EditorView;
import cs3500.music.view.GuiView;
import cs3500.music.view.ViewModel;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * Controller for the Music Editor console UI. Mediates between the view and the model by
 * taking user input and acting on the view, and then taking information from the view and showing
 * it to the user.
 */
public final class GuiController implements GuiSpecificController {

  // Data for the GuiController class
  private final MusicEditorModel model;
  private final ViewModel vm;
  private final GuiView view;
  // State trackers
  int curBeat = 0;
  Timer timer;
  // Input handling
  private InputHandler ih;
  private int pressedKey = 0;
  int curX, curY;
  // Boolean flag helping with invariants for keyhandling
  private boolean isPaused;

  /**
   * Constructs a controller for playing the given game model, with the given input and output for
   * communicating with the user.
   *
   * @param model0 the game to play
   */
  public GuiController(MusicEditorModel model0, GuiView view) {
    model = requireNonNull(model0);
    vm = adaptModelToViewModel(model);
    this.view = view;
    this.timer = new Timer(model.getTempo() / 1000, new SwingTimerActionListener());
    // Initial state: paused, and no position selected
    this.isPaused = true;
    this.curX = -1;
    this.curY = -1;
    ih = new InputHandler(this);
    // Takes you to the beginning of the piece .. "home"
    ih.addPressedEvent(36, view::goToStart);
    // Takes you to the end of the piece ........ "end"
    ih.addPressedEvent(35, view::goToEnd);
    // Scrolls upwards .......................... "up arrow"
    ih.addPressedEvent(38, view::scrollUp);
    // Scrolls downwards ........................ "down arrow"
    ih.addPressedEvent(40, view::scrollDown);
    // Scrolls left ............................. "left arrow"
    ih.addPressedEvent(37, view::scrollLeft);
    // Scrolls right ............................ "right arrow"
    ih.addPressedEvent(39, view::scrollRight);
    // Pauses/plays the music ................... "space"
    ih.addPressedEvent(32, () -> {
      if (this.isPaused) {
        this.isPaused = false;
        timer.start();
      } else {
        this.isPaused = true;
        timer.stop();
      }
    });
    // Allows for clicking to add notes ......... "a"
    ih.addPressedEvent(65, () -> {
      if (this.pressedKey == 65) {
        this.pressedKey = 0;
      }
      else {
        this.pressedKey = 65;
      }
    });
    // Allows for clicking to remove notes ...... "s"
    ih.addPressedEvent(83, () -> {
      if (this.pressedKey == 83) {
        this.pressedKey = 0;
      }
      else {
        this.pressedKey = 83;
      }
    });
    // Allows for clicking to change the ........ "d"
    // start of notes
    ih.addPressedEvent(68, () -> {
      if (this.pressedKey == 68) {
        this.pressedKey = 0;
      }
      else {
        this.pressedKey = 68;
      }
    });
    // Allows for clicking to change the ........ "f"
    // end of notes
    ih.addPressedEvent(70, () -> {
      if (this.pressedKey == 70) {
        this.pressedKey = 0;
      }
      else {
        this.pressedKey = 70;
      }
    });

    // TODO :: CHANGEPITCH
  }

  static Controller makeController(MusicEditorModel model, GuiView view) {
    return new GuiController(model, view);
  }

  @Override
  public void run() throws IOException {
    setKeyHandler(ih);
    setMouseHandler(ih);
    view.draw(vm);
  }

  private class SwingTimerActionListener implements ActionListener {
    public void actionPerformed(ActionEvent a) {

      if (view.drawn() && curBeat < vm.scoreLength()) {
        curBeat += 1;
        try {
          view.tickCurBeat(vm, curBeat);
        } catch (InvalidMidiDataException | IOException e) {
          throw new IllegalStateException("Something went wrong while playing");
        }
      }
    }
  }

  /**
   * Adapts a {@link MusicEditorModel} into a {@link ViewModel}. The adapted result shares state
   * with its adaptee.
   *
   * @param adaptee the {@code MusicEditorModel} to adapt
   * @return a {@code ViewModel} backed by {@code adaptee}
   */
  private static ViewModel adaptModelToViewModel(MusicEditorModel adaptee) {
    return ViewModel.makeViewModel(adaptee);
  }

  @Override
  public void setCurrent(int x, int y) {
    int z = EditorView.CELL_SIZE;
    this.curX = x / z;
    this.curY = (y - z) / z;
  }

  @Override
  public boolean curSet() {
    return this.curX < 0;
  }

  @Override
  public boolean isPressed(int key) {
    return pressedKey == key;
  }

  @Override
  public void setKeyHandler(KeyListener kh) {
    this.view.setKeyHandler(kh);
  }

  @Override
  public void setMouseHandler(MouseListener mh) {
    this.view.setMouseHandler(mh);
  }

  /**
   * Uses the curX and curY fields to return an array of the data referring to the
   * note at that position. The order is [pitch, octave].
   *
   * @return array of note data in integer form
   */
  // TODO :: FIX THIS
  private int[] getNoteData() {
    String noteAndOctave = this.model.notesInRange().get(curY);
    int pitch;
    int octave;
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
    return new int[] {pitch, octave};
  }

  @Override
  public void addNote() {
    if (this.isPaused) {
      int[] noteData = this.getNoteData();
      // Adds the note created with the new values
      AbstractNote note = this.model.makeNote(NoteTypes.valueLookup(noteData[0]), noteData[1], curX, curX, 70);
      this.model.addNote(note);
      view.repaint();
      System.out.println(note.toString());
      // TODO :: MAKE A REPAINT METHOD THAT GOES DOWN TO THE EDITORVIEW AND CALLS BUILTBOARD.REPAINT()
    }
  }

  @Override
  public void removeNote() {
    if (this.isPaused) {
      int[] noteData = this.getNoteData();
      // Adds the note created with the new values
      AbstractNote note = this.model.getNote(NoteTypes.valueLookup(noteData[0]), noteData[1], curX);
      this.model.deleteNote(note);

      // TODO :: REPAINT
    }
  }

  @Override
  public void changeNoteStart(int newStart) {
    if (!this.isPaused) {
      int[] noteData = this.getNoteData();
      AbstractNote note = this.model.getNote(NoteTypes.valueLookup(noteData[0]), noteData[1], curX);
      if (newStart > note.getEndBeat()) {
        System.out.print("This should be done using the 'end beat' mode.");
      } else {
        this.model.changeNoteStart(note, newStart);

        // TODO :: REPAINT
      }
    }
  }

  @Override
  public void changeNoteEnd(int newEnd) {
    if (!this.isPaused) {
      int[] noteData = this.getNoteData();
      AbstractNote note = this.model.getNote(NoteTypes.valueLookup(noteData[0]), noteData[1], curX);
      if (newEnd < note.getStartBeat()) {
        System.out.print("This should be done using the 'start beat' mode.");
      } else {
        this.model.changeNoteEnd(note, newEnd);

        // TODO :: REPAINT
      }
    }
  }
}
