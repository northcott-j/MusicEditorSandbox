package cs3500.music.controller;

import cs3500.music.model.ARepetition;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

// TODO :: IMPROVE LAYOUT AND READABILITY OF THIS CLASS AND ITS METHODS

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
  // Repetition handling
  // Map of endBeats to List of Repetitions to be played
  private final Map<Integer, List<ARepetition>> repetitionsToBePlayed;
  private List<ARepetition> repetitionsBeingPlayed;
  // Input handling
  private InputHandler ih;
  private int pressedKey = 0;
  private int curX, curY;
  // Boolean flag helping with invariants for keyhandling
  private boolean isPaused;

  /**
   * Constructs a controller for playing the given game model, with the given input
   * and output for communicating with the user. Includes loading the key and mouse
   * actions and field initialization.
   *
   * @param model0 the music to play
   * @param view   the view to draw
   */
  public GuiController(MusicEditorModel model0, GuiView view, String mode) {
    model = requireNonNull(model0);
    vm = adaptModelToViewModel(model);
    this.view = view;
    this.timer = new Timer(model.getTempo() / 1000, new SwingTimerActionListener());
    repetitionsToBePlayed = new HashMap<>();
    repetitionsBeingPlayed = new ArrayList<>();
    // Initial state: paused, and no valid position selected
    this.isPaused = true;
    this.curX = -1;
    this.curY = -1;
    // Sets the InputHandler's log appendable to the proper value.
    if (mode.equals("run")) {
      ih = new InputHandler(this);
    } else {
      ih = new InputHandler(this, new StringBuilder());
    }
    /** Loading the actions dealing with the views */
    // Takes you to the beginning of the piece .. "home"
    ih.addPressedEvent(KeyEvent.VK_HOME, view::goToStart);
    // Takes you to the end of the piece ........ "end"
    ih.addPressedEvent(KeyEvent.VK_END, view::goToEnd);
    // Scrolls upwards .......................... "up arrow"
    ih.addPressedEvent(KeyEvent.VK_UP, view::scrollUp);
    // Scrolls downwards ........................ "down arrow"
    ih.addPressedEvent(KeyEvent.VK_DOWN, view::scrollDown);
    // Scrolls left ............................. "left arrow"
    ih.addPressedEvent(KeyEvent.VK_LEFT, view::scrollLeft);
    // Scrolls right ............................ "right arrow"
    ih.addPressedEvent(KeyEvent.VK_RIGHT, view::scrollRight);
    // Pauses/plays the music ................... "space"
    ih.addPressedEvent(KeyEvent.VK_SPACE, (Integer k) -> {
      if (this.isPaused) {
        this.isPaused = false;
        timer.start();
        ih.print("Began playing music from beat " + curBeat);
      } else {
        this.isPaused = true;
        timer.stop();
        ih.print("Stopped playing music from beat " + curBeat);
      }
    });
    /** Loading the actions dealing with the model */
    // Allows for clicking to add notes ......... "a"
    ih.addPressedEvent(KeyEvent.VK_A, this::changePressed);
    // Allows for clicking to add percussion .... "w"
    // notes
    ih.addPressedEvent(KeyEvent.VK_W, this::changePressed);
    // Allows for clicking to remove notes ...... "s"
    ih.addPressedEvent(KeyEvent.VK_S, this::changePressed);
    // Allows for clicking to change the ........ "d"
    // start of notes
    ih.addPressedEvent(KeyEvent.VK_D, this::changePressed);
    // Allows for clicking to change the ........ "f"
    // end of notes
    ih.addPressedEvent(KeyEvent.VK_F, this::changePressed);
    // Allows for clicking to change the ........ "e"
    // to reset curBeat to selected point
    ih.addPressedEvent(KeyEvent.VK_E, this::changePressed);
    // Allows for clicking to change the ........ "q"
    // note's entire position
    ih.addPressedEvent(KeyEvent.VK_Q, this::changePressed);
    // Allows for the user to increase the ...... "v"
    // size of the board
    ih.addPressedEvent(KeyEvent.VK_V, this::changePressed);
    // Expands the board to include the ......... "t"
    // next highest octave
    ih.addPressedEvent(KeyEvent.VK_T, (Integer k) -> {
      if (this.pressedKey == KeyEvent.VK_V) {
        view.expandUp(vm);
        ih.print("Tried to expand the board up one octave.");
      }
    });
    // Expands the board to include 8 ........... "g"
    // more beats
    ih.addPressedEvent(KeyEvent.VK_G, (Integer k) -> {
      if (this.pressedKey == KeyEvent.VK_V) {
        view.expandOut(vm);
        ih.print("Tried to expand the board out by 8 beats.");
      }
    });
    // Expands the board to include the ......... "b"
    // next lowest octave
    ih.addPressedEvent(KeyEvent.VK_B, (Integer k) -> {
      if (this.pressedKey == KeyEvent.VK_V) {
        view.expandDown(vm);
        ih.print("Tried to expand the board down one octave.");
      }
    });
    /** Loading the actions dealing with mouse clicks. */
    // If the "a" key is being pressed, for adding notes
    ih.addClickedEvent(KeyEvent.VK_A, this::oneStep);
    // If the "w" key is being pressed, for adding percussion notes
    ih.addClickedEvent(KeyEvent.VK_W, this::oneStep);
    // If the "s" key is being pressed, for removing notes
    ih.addClickedEvent(KeyEvent.VK_S, this::oneStep);
    // If the "e" key is being pressed, for changing current beat of the piece
    ih.addClickedEvent(KeyEvent.VK_E, this::oneStep);
    // If the "d" key is being pressed, for changing the start of a note
    ih.addClickedEvent(KeyEvent.VK_D, this::twoStep);
    // If the "f" key is being pressed, for changing the end of a note
    ih.addClickedEvent(KeyEvent.VK_F, this::twoStep);
    // If the "q" key is being pressed, for changing the location of a note
    ih.addClickedEvent(KeyEvent.VK_Q, this::twoStep);
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
    // Initializes field if a Repetition has multiple subRepeats
    ARepetition multiRepeat;
    // List of the subRepeats
    List<ARepetition> listOfSubRepetitions;

    public void actionPerformed(ActionEvent a) {
      if (view.drawn() && curBeat < vm.scoreLength()) {
        try {
          handleRepetitions();
          view.tickCurBeat(vm, curBeat);
        } catch (InvalidMidiDataException | IOException e) {
          throw new IllegalStateException("Something went wrong while playing");
        }
        curBeat += 1;
      }
    }

    /**
     * Queues and Plays Repetitions
     */
    private void handleRepetitions() {
      addRepetitionsToQueue();
      addRepetitionsToBePlayed();
      if ((multiRepeat != null) && (repetitionsBeingPlayed.isEmpty() ||
              !repetitionsBeingPlayed.get(0).equals(multiRepeat))) {
        continueRepetition();
      }
      if (!repetitionsBeingPlayed.isEmpty()) {
        playNextRepetition();
      }
    }

    /**
     * Deals with queueing Repetitions (Maps end of Repetition to a List of Repetitions ending
     * there)
     */
    private void addRepetitionsToQueue() {
      Map<Integer, List<ARepetition>> modelRepetitions = model.getRepetitions();
      // Handles adding to the to-do list or Repetitions
      // If there is a Repetition(s) starting at this beat,
      if (modelRepetitions.get(curBeat) != null) {
        // Add each one to the tobeplayed map
        for (ARepetition r : modelRepetitions.get(curBeat)) {
          int end;
          // If length isn't one, map to ending of first sub-repetition
          if (r.length() != 1) {
            end = r.listofRepeats().get(0).getEnd();
          } else {
            end = r.getEnd();
          }
          List<ARepetition> repetitionToBeQueued = repetitionsToBePlayed.get(end);
          // Either the List is Empty or it isn't currently being played
          if ((repetitionsBeingPlayed.isEmpty() || repetitionsBeingPlayed.get(0) != r) &&
                  // And the Repetition hasn't already been added to the list to be played
                  (repetitionToBeQueued == null ||
                          !repetitionToBeQueued.contains(r))) {
            // If the above statements are true, check to see if there has already been
            // an entry made
            if (repetitionToBeQueued == null) {
              List<ARepetition> newList = new ArrayList<>();
              newList.add(r);
              repetitionsToBePlayed.put(end, newList);
            } else {
              // Otherwise, add to the existing List
              repetitionToBeQueued.add(0, r);
            }
          }
        }
      }
    }

    /**
     * Deals with playing Repetitions (Adds the list of Repetitions ending at this beat to the
     * playing field)
     */
    private void addRepetitionsToBePlayed() {
      // Handles setting up the controller to play a Repetition
      List<ARepetition> repetitionAtThisBeat = repetitionsToBePlayed.get(curBeat);
      if (repetitionAtThisBeat != null) {
        // Removes empty entry
        if (repetitionAtThisBeat.isEmpty()) {
          repetitionsToBePlayed.remove(curBeat);
        } else {
          // If not empty, set the repetitionsBeingPlayed to current List
          repetitionsBeingPlayed = repetitionAtThisBeat;
        }
      }
    }

    /**
     * Plays the next in the list of Repetitions to be Played
     */
    private void playNextRepetition() {
      // If just a single, Repeat
      if (repetitionsBeingPlayed.get(0).length() == 1) {
        curBeat = repetitionsBeingPlayed.get(0).getStart();
      } else {
        if (multiRepeat == null) {
          multiRepeat = repetitionsBeingPlayed.get(0);
          listOfSubRepetitions = multiRepeat.listofRepeats();
          curBeat = multiRepeat.getStart();
        } else {
          curBeat = repetitionsBeingPlayed.get(0).getStart();
        }
      }
      repetitionsBeingPlayed.remove(0);
    }

    /**
     * Continues a Repetition if it has several subRepetitions
     */
    // Keeps track of the ending number of a Continued Repetition
    int endingNumber = 1;

    private void continueRepetition() {
      if (endingNumber < listOfSubRepetitions.size()) {
        if (curBeat == multiRepeat.getEnd()) {
          ARepetition endRepetition = listOfSubRepetitions.get(endingNumber);
          curBeat = endRepetition.getStart();
          if (endingNumber != listOfSubRepetitions.size() - 1) {
            // Sets up a repeat to go back to start of AltEnding
            List<ARepetition> repetitionToBeQueued =
                    repetitionsToBePlayed.get(endRepetition.getEnd());
            if (repetitionToBeQueued == null) {
              List<ARepetition> newList = new ArrayList<>();
              newList.add(multiRepeat);
              repetitionsToBePlayed.put(endRepetition.getEnd(), newList);
            } else {
              // Otherwise, add to the existing List
              repetitionToBeQueued.add(0, multiRepeat);
            }
          }
          endingNumber += 1;
        }
      } else {
        multiRepeat = null;
        endingNumber = 1;
        listOfSubRepetitions = null;
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
  public void setKeyHandler(KeyListener kh) {
    this.view.setKeyHandler(kh);
  }

  @Override
  public void setMouseHandler(MouseListener mh) {
    this.view.setMouseHandler(mh);
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
  public int getPressed() {
    return this.pressedKey;
  }
  /**
   * Changes the current pressedKey to the new value. If it's the same as the new value, reset the
   * pressedKey field to 0.
   *
   * @param k the keyCode of the new key
   */
  private void changePressed(Integer k) {
    if (this.pressedKey == k) {
      this.pressedKey = 0;
    } else {
      this.pressedKey = k;
    }
    // Prints a message according to the key that was pressed
    ih.print(this.printMode(k));
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
      // Prints a message according to the note added
      if (this.pressedKey == 87) {
        ih.print(String.format("Added a percussion note at: (%1$d, %2$s)",
                curX + 1, view.getNotesInRange().get(curY)));
      } else {
        ih.print(String.format("Added a note at: (%1$d, %2$s)",
                curX + 1, view.getNotesInRange().get(curY)));
      }
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
        // Prints a message according to the note removed
        ih.print(String.format("Removed a note at: (%1$d, %2$s)",
                curX + 1, view.getNotesInRange().get(curY)));
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
          // Prints a message according to the note's new start beat
          ih.print(String.format("Changed the note's start beat to: %1$d", newStart + 1));
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
          // Prints a message according to the note's new end beat
          ih.print(String.format("Changed the note's start beat to: %1$d", newEnd + 1));
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
        ih.print(String.format("Moved the note to: (%1$d, %2$s)",
                newX + 1, view.getNotesInRange().get(newY)));
      } catch (IllegalArgumentException e) {
        System.out.println("There isn't a note here");
      }
    }
    view.repaint();
  }

  @Override
  public void changeCurBeat(int newBeat) throws InvalidMidiDataException, IOException {
    curBeat = newBeat;
    view.tickCurBeat(vm, newBeat);
    // Prints a message according to the new beat location
    ih.print(String.format("Changed the current beat to: %1$d)", curX + 1));
  }

  /**
   * The action performed on mouse clicks for one-step processes, such as adding a new note (regular
   * or percussion) and removing notes based on the given mouse event.
   *
   * @param e mouse event data
   */
  private void oneStep(MouseEvent e) {
    this.setCurrent(e.getX(), e.getY());
    // Performs the proper action according to the pressed key. Prints relevant
    // data after each action.
    switch (pressedKey) {
      case KeyEvent.VK_A:   // adding notes
        this.addNote();
        break;
      case KeyEvent.VK_W:   // adding percussion notes
        this.addNote();
        break;
      case KeyEvent.VK_S:   // removing notes
        this.removeNote();
        break;
      case KeyEvent.VK_E:   // updating the current beat
        try {
          this.changeCurBeat(this.curX);
        } catch (InvalidMidiDataException | IOException e1) {
          e1.printStackTrace();
        } catch (IndexOutOfBoundsException e2) {
          throw new IllegalArgumentException("Can't change the beat to here.");
        }
      default:
        break;
    }
    // Returns the selected note data to the default value
    this.setCurrent(-1, -1);
  }

  /**
   * The action performed on mouse clicks for two-step processes, such as modifying the start and
   * end beats of a note, moving a note, and changing the current beat of the piece based on the
   * given mouse event. The first step is to set the mouse data by using setCurrent(x, y); once the
   * location is stored, the mutation can be performed.
   *
   * @param e mouse event data
   */
  private void twoStep(MouseEvent e) {
    // If the note hasn't been selected yet:
    if (!this.curSet()) {
      this.setCurrent(e.getX(), e.getY());
      ih.print(String.format("Tried to select the note at: (%1$d, %2$s)",
              curX + 1, view.getNotesInRange().get(curY)));
    } else {
      // Performs the proper action according to the pressed key, given that the
      // location was already set. Prints relevant data after each action.
      switch (pressedKey) {
        case KeyEvent.VK_D:   // changing the start of a note
          this.changeNoteStart(e.getX() / EditorView.CELL_SIZE);
          break;
        case KeyEvent.VK_F:   // changing the end of a note
          this.changeNoteEnd(e.getX() / EditorView.CELL_SIZE);
          break;
        case KeyEvent.VK_Q:   // changing the location of a note
          this.moveNote(e.getX() / EditorView.CELL_SIZE,
                  (e.getY() - EditorView.CELL_SIZE) / EditorView.CELL_SIZE);
          break;
        default:
          break;
      }
      this.setCurrent(-1, -1);
    }
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
   * Helps print the mode switches caused by key events.
   *
   * @param k the key that was pressed
   */
  private String printMode(Integer k) {
    String message;
    String mode = "";
    switch (k) {
      case KeyEvent.VK_A:
        mode = "addNote";
        break;
      case KeyEvent.VK_W:
        mode = "addNote (percussion)";
        break;
      case KeyEvent.VK_S:
        mode = "removeNote";
        break;
      case KeyEvent.VK_D:
        mode = "changeNoteStart";
        break;
      case KeyEvent.VK_F:
        mode = "changeNoteEnd";
        break;
      case KeyEvent.VK_Q:
        mode = "moveNote";
        break;
      case KeyEvent.VK_E:
        mode = "changeCurBeat";
        break;
      case KeyEvent.VK_V:
        mode = "expandBoard";
        break;
      default:
        break;
    }
    // If the mode was activated
    if (k == pressedKey) {
      message = "Entered the " + mode + " mode.";
    } else {
      // If the mode was deactivated
      message = "Exited the " + mode + " mode.";
    }
    return message;
  }
}
