package cs3500.music.controller;

import cs3500.music.model.AbstractNote;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.model.NoteTypes;
import cs3500.music.view.EditorView;
import cs3500.music.view.GuiView;
import cs3500.music.view.ViewModel;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;

import java.awt.event.*;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * Controller for the Music Editor console UI. Mediates between the view and the model by taking
 * user input and acting on the view, and then taking information from the view and showing it to
 * the user.
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
  private int curX, curY;
  // Boolean flag helping with invariants for keyhandling
  private boolean isPaused;
  private boolean initializedDefault;
  // Stores input data for testing and debugging purposes

  /**
   * Constructs a controller for playing the given game model, with the given input and output for
   * communicating with the user.
   *
   * @param model0 the music to play
   * @param view   the view to draw
   */
  public GuiController(MusicEditorModel model0, GuiView view, String mode) {
    model = requireNonNull(model0);
    vm = adaptModelToViewModel(model);
    this.view = view;
    this.timer = new Timer(model.getTempo() / 1000, new SwingTimerActionListener());
    this.initializedDefault = model.scoreLength() == 0;
    // Initial state: paused, and no valid position selected
    this.isPaused = true;
    this.curX = -1;
    this.curY = -1;
    if (mode.equals("run")) {
      ih = new InputHandler(this);
    } else {
      ih = new InputHandler(this, new StringBuilder());
    }
    /** Loading the actions dealing with the view */
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
    /** Loading the actions dealing with the model */
    // Allows for clicking to add notes ......... "a"
    ih.addPressedEvent(65, () -> {
      if (this.pressedKey == 65) {
        this.pressedKey = 0;
      } else {
        this.pressedKey = 65;
      }
    });
    // Allows for clicking to add percussion .... "w"
    // notes
    ih.addPressedEvent(87, () -> {
      if (this.pressedKey == 87) {
        this.pressedKey = 0;
      } else {
        this.pressedKey = 87;
      }
    });
    // Allows for clicking to remove notes ...... "s"
    ih.addPressedEvent(83, () -> {
      if (this.pressedKey == 83) {
        this.pressedKey = 0;
      } else {
        this.pressedKey = 83;
      }
    });
    // Allows for clicking to change the ........ "d"
    // start of notes
    ih.addPressedEvent(68, () -> {
      if (this.pressedKey == 68) {
        this.pressedKey = 0;
      } else {
        this.pressedKey = 68;
      }
    });
    // Allows for clicking to change the ........ "f"
    // end of notes
    ih.addPressedEvent(70, () -> {
      if (this.pressedKey == 70) {
        this.pressedKey = 0;
      } else {
        this.pressedKey = 70;
      }
    });
    // Allows for clicking to change the ........ "e"
    // to reset curBeat to selected point
    ih.addPressedEvent(69, () -> {
      if (this.pressedKey == 69) {
        this.pressedKey = 0;
      } else {
        this.pressedKey = 69;
      }
    });
    // Allows for clicking to change the ........ "q"
    // note's entire position
    ih.addPressedEvent(81, () -> {
      if (this.pressedKey == 81) {
        this.pressedKey = 0;
      } else {
        this.pressedKey = 81;
      }
    });
    // Allows for the user to increase the ...... "v"
    // size of the board
    ih.addPressedEvent(86, () -> {
      if (this.pressedKey == 86) {
        this.pressedKey = 0;
      } else {
        this.pressedKey = 86;
      }
    });
    // Expands the board to include the ......... "t"
    // next highest octave
    ih.addPressedEvent(84, ()-> {
      if (this.pressedKey == 86) {
        view.expandUp(vm);
      }
    });
    // Expands the board to include 8 ........... "g"
    // more beats
    ih.addPressedEvent(71, ()-> {
      if (this.pressedKey == 86) {
        view.expandOut(vm);
      }
    });
    // Expands the board to include the ......... "b"
    // next lowest octave
    ih.addPressedEvent(66, ()-> {
      if (this.pressedKey == 86) {
        view.expandDown(vm);
      }
    });
  }

  /**
   * Creates a new controller based on the given inputs.
   *
   * @param model the desired model to be played
   * @param view  the desired view to be drawn
   * @param mode  the desired mode; running or testing
   * @return new instance of a controller
   */
  public static Controller makeController(MusicEditorModel model, GuiView view, String mode) {
    return new GuiController(model, view, mode);
  }

  @Override
  public void run() throws IOException {
    setKeyHandler(ih);
    setMouseHandler(ih);
    view.draw(vm);
  }

  /**
   * This method is called by the timer, and helps drive the playback of music properly.
   */
  private class SwingTimerActionListener implements ActionListener {
    public void actionPerformed(ActionEvent a) {

      if (view.drawn() && curBeat < vm.scoreLength()) {
        try {
          view.tickCurBeat(vm, curBeat);
        } catch (InvalidMidiDataException | IOException e) {
          throw new IllegalStateException("Something went wrong while playing");
        }
        curBeat += 1;
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
    return new ViewModel(adaptee) {
      @Override
      public int scoreLength() {
        return super.scoreLength();
      }
    };
  }

  @Override
  public void setCurrent(int x, int y) {
    int z = EditorView.CELL_SIZE;
    this.curX = x / z;
    this.curY = (y - z) / z;
  }

  @Override
  public boolean curSet() {
    return this.curX > 0;
  }

  @Override
  public int getX() {
    return this.curX;
  }

  @Override
  public int getY() {
    return this.curY;
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

  @Override
  public void mockEvent(String type, InputEvent e) {
    if (type.equals("Key")) {
      this.ih.keyPressed((KeyEvent) e);
    } else {
      this.ih.mouseClicked((MouseEvent) e);
    }
  }

  @Override
  public String printLog() {
    return this.ih.printData();
  }

  /**
   * Uses the curX and curY fields to return an array of the data referring to the note at that
   * position. The order is [pitch, octave].
   *
   * @return array of note data in integer form
   */

  private int[] getNoteData(int yPos) {
    String noteAndOctave = view.getNotesInRange().get(yPos);
    int pitch;
    int octave;
    // Special case if octave is -1
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

  @Override
  public void addNote() {
    if (this.isPaused) {
      int[] noteData = this.getNoteData(curY);
      int instrument = 1;
      if (this.pressedKey == 87) {
        instrument = 10;
      }
      // Adds the note created with the new values
      AbstractNote note = this.model.makeNote(NoteTypes.valueLookup(noteData[0]),
              noteData[1], curX, curX, 70);
      this.model.changeNoteInstrument(note, instrument);
      this.model.addNote(note);
      view.repaint();
    }
  }

  @Override
  public void removeNote() {
    if (this.isPaused) {
      int[] noteData = this.getNoteData(curY);
      try {
        // Adds the note created with the new values
        AbstractNote note = this.model.getNote(NoteTypes.valueLookup(noteData[0]),
                noteData[1], curX);
        this.model.deleteNote(note);
      } catch (IllegalArgumentException e) {
        System.out.println("There isn't a note here");
      }
      view.repaint();
    }
  }

  @Override
  public void changeNoteStart(int newStart) {
    if (this.isPaused) {
      try {
        int[] noteData = this.getNoteData(curY);
        AbstractNote note = this.model.getNote(NoteTypes.valueLookup(noteData[0]),
                noteData[1], curX);
        if (newStart > note.getEndBeat()) {
          System.out.println("This should be done using the 'end beat' mode.");
        } else {
          this.model.changeNoteStart(note, newStart);
        }
      } catch (IllegalArgumentException e) {
        System.out.println("There isn't a note here");
      }
    }
    view.repaint();
  }

  @Override
  public void changeNoteEnd(int newEnd) {
    if (this.isPaused) {
      try {
        int[] noteData = this.getNoteData(curY);
        AbstractNote note = this.model.getNote(NoteTypes.valueLookup(noteData[0]),
                noteData[1], curX);
        if (newEnd < note.getStartBeat()) {
          System.out.println("This should be done using the 'start beat' mode.");
        } else {
          this.model.changeNoteEnd(note, newEnd);
        }
      } catch (IllegalArgumentException e) {
        System.out.println("There isn't a note here");
      }
    }
    view.repaint();
  }

  @Override
  public void moveNote(int newX, int newY) {
    if (this.isPaused) {
      try {
        int[] noteData = this.getNoteData(curY);
        int[] newNoteData = this.getNoteData(newY);
        AbstractNote note = this.model.getNote(NoteTypes.valueLookup(noteData[0]),
                noteData[1], curX);
        int noteLength = note.getEndBeat() - note.getStartBeat();
        AbstractNote newNote = this.model.makeNote(NoteTypes.valueLookup(newNoteData[0]),
                newNoteData[1], newX, noteLength + newX, note.getVolume());
        this.model.deleteNote(note);
        this.model.addNote(newNote);
      }
      catch (IllegalArgumentException e) {
        System.out.println("There isn't a note here");
      }
    }
    view.repaint();
  }

  @Override
  public void changeCurBeat(int newBeat) throws InvalidMidiDataException, IOException {
    curBeat = newBeat;
    view.tickCurBeat(vm, newBeat);
  }
}
