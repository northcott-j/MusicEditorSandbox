package cs3500.music.view;

import cs3500.music.model.AbstractNote;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

import static java.util.Objects.requireNonNull;

/**
 * Class to construct the Console view
 */
public final class ConsoleView implements View {

  /**
   * Constructs a new {@code ConsoleView.Builder}, which is used to configure and instantiate new
   * {@code ConsoleView}s.
   *
   * @return the new builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Creates a new console view using the given input and output.
   *
   * @param input  where to read user input from
   * @param output where to send output for the user to see
   */
  private ConsoleView(Scanner input, Appendable output) {
    this.input = input;
    this.output = output;
  }

  private final Scanner input;
  private final Appendable output;

  /**
   * String representation of a board
   *
   * @return the text view of the board
   * @throws IllegalStateException if board is empty
   */
  public void draw(ViewModel vm) throws IOException {
    if (vm.scoreLength() == 0) {
      throw new IllegalStateException("No board to draw");
    }
    output.append(printAsText(vm));
  }

  /**
   * Builds the upper note range in the printout
   *
   * @return the String of notes in the range
   */
  private StringBuilder noteRange(ViewModel vm) {
    // Leading space to push it over to fit numbers
    StringBuilder acc = new StringBuilder();
    // The string count of the number of beats in the song
    int length = Integer.toString(vm.scoreLength()).length();
    for (String s : vm.notesInRange()) {
      if (s.length() == 2) {
        acc.insert(0, " " + s);
      } else {
        acc.insert(0, s);
      }
    }
    while (length >= 0) {
      acc.insert(0, " ");
      length -= 1;
    }
    // adds a new line character before returning it
    return acc.append(" \n");
  }

  /**
   * Creates a HashMap mapping Note names to their index in the range StringBuilder.
   * To be used to properly draw the notes in the console view.
   *
   * @param noteRange StringBuilder range of notes
   * @return HashMap of Note names to index
   */
  private HashMap<String, Integer> createRangeHMap(StringBuilder noteRange, ViewModel vm) {
    HashMap<String, Integer> map = new HashMap<>();
    for (String s : vm.notesInRange()) {
      if (s.length() == 3) {
        map.put(s, noteRange.indexOf(s) + 1);
      } else {
        map.put(s, noteRange.indexOf(s));
      }
    }
    return map;
  }

  /**
   * Outputs the text printout
   *
   * @return String of the board
   */
  private String printAsText(ViewModel vm) {
    // Gets the note range
    StringBuilder acc = this.noteRange(vm);
    //note Index Range
    int indxRange = acc.length();
    // Creates the hashmap for note indices in range
    HashMap<String, Integer> rangeMap = this.createRangeHMap(acc, vm);
    // Current beat
    int beatNumber = 0;
    for (Collection<AbstractNote> a : vm.returnScore()) {
      // Has to create a placeholder line with spaces in order to insert by index
      StringBuilder newLine = new StringBuilder();
      for (int i = indxRange + 10; i > 1; i -= 1) {
        newLine.append(" ");
      }
      // Starts the line by adding beat number
      newLine.replace(
              Integer.toString(vm.scoreLength()).length() -
                      Integer.toString(beatNumber).length(),
              Integer.toString(vm.scoreLength()).length(), Integer.toString(beatNumber));
      // adds each node to the new line represented by an X
      for (AbstractNote n : a) {
        String nodeLoc = n.toString();
        if (beatNumber == n.getStartBeat()) {
          newLine.replace(rangeMap.get(nodeLoc), rangeMap.get(nodeLoc) + 1, "X");
        } else {
          newLine.replace(rangeMap.get(nodeLoc), rangeMap.get(nodeLoc) + 1, "|");
        }
      }
      // Trims extra white space, if necessary
      newLine.setLength(indxRange - 1);
      // Add line break
      newLine.append("\n");
      // Adds the completed line to the acc
      acc.append(newLine);
      beatNumber += 1;
    }
    return acc.toString();
  }

  /**
   * Configures and constructs {@link ConsoleView}s. This isn't really necessary, since the only
   * parameters are the input and output, though it does help with the overloading on {@code
   * Scanner} versus {@code Readable}.
   */
  public static final class Builder {
    /**
     * Configures this {@code Builder} to use the given {@link Scanner} for input.
     *
     * @param input user input source for the view
     * @return {@code this}, for method chaining
     */
    public Builder input(Scanner input) {
      this.input = requireNonNull(input);
      return this;
    }

    private Scanner input = new Scanner(System.in);

    /**
     * Configures this {@code Builder} to use the given {@link Readable} for input.
     *
     * @param input user input source for the view
     * @return {@code this}, for method chaining
     */
    public Builder input(Readable input) {
      return input(new Scanner(requireNonNull(input)));
    }

    /**
     * Configures this {@code Builder} to use the given {@link Appendable} for output.
     *
     * @param output user output sink for the view
     * @return {@code this}, for method chaining
     */
    public Builder output(Appendable output) {
      this.output = requireNonNull(output);
      return this;
    }

    private Appendable output = System.out;

    /**
     * Constructs and returns a new view as configured by this {@code Builder}.
     *
     * @return the new view
     */
    public ConsoleView build() {
      return new ConsoleView(input, output);
    }
  }

}
