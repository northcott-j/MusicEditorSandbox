package cs3500.test;

import cs3500.music.model.MusicEditorImpl;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.util.MusicReader;
import cs3500.music.view.ConsoleView;
import cs3500.music.view.ViewModel;

import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertEquals;


/**
 * Created by Jonathan on 11/11/2015.
 */
public class ConsoleViewTests {
  Appendable out = new StringBuilder();
  ConsoleView view = ConsoleView.builder().output(out).build();

  /**
   * Tests the console output of "mary-little-lamb.txt".
   */
  @Test
  public void consoleTest1() throws IOException {
    MusicEditorModel model;
    Readable mary = new FileReader("mary-little-lamb.txt");
    model = MusicReader.parseFile(mary, new MusicEditorImpl.Builder());
    ViewModel vm = new ViewModel(model) {
      @Override
      public int scoreLength() {
        return super.scoreLength();
      }
    };
    view.draw(vm);

    assertEquals("    E3 F3F#3 G3G#3 A3A#3 B3 C4C#4 D4D#4 E4 F4F#4 G4 \n" +
                    " 0           X                          X           \n" +
                    " 1           |                          |           \n" +
                    " 2           |                    X                 \n" +
                    " 3           |                    |                 \n" +
                    " 4           |              X                       \n" +
                    " 5           |              |                       \n" +
                    " 6           |                    X                 \n" +
                    " 7                                |                 \n" +
                    " 8           X                          X           \n" +
                    " 9           |                          |           \n" +
                    "10           |                          X           \n" +
                    "11           |                          |           \n" +
                    "12           |                          X           \n" +
                    "13           |                          |           \n" +
                    "14           |                          |           \n" +
                    "15                                                  \n" +
                    "16           X                    X                 \n" +
                    "17           |                    |                 \n" +
                    "18           |                    X                 \n" +
                    "19           |                    |                 \n" +
                    "20           |                    X                 \n" +
                    "21           |                    |                 \n" +
                    "22           |                    |                 \n" +
                    "23           |                    |                 \n" +
                    "24           X                          X           \n" +
                    "25           |                          |           \n" +
                    "26                                               X  \n" +
                    "27                                               |  \n" +
                    "28                                               X  \n" +
                    "29                                               |  \n" +
                    "30                                               |  \n" +
                    "31                                               |  \n" +
                    "32           X                          X           \n" +
                    "33           |                          |           \n" +
                    "34           |                    X                 \n" +
                    "35           |                    |                 \n" +
                    "36           |              X                       \n" +
                    "37           |              |                       \n" +
                    "38           |                    X                 \n" +
                    "39           |                    |                 \n" +
                    "40           X                          X           \n" +
                    "41           |                          |           \n" +
                    "42           |                          X           \n" +
                    "43           |                          |           \n" +
                    "44           |                          X           \n" +
                    "45           |                          |           \n" +
                    "46           |                          X           \n" +
                    "47           |                          |           \n" +
                    "48           X                    X                 \n" +
                    "49           |                    |                 \n" +
                    "50           |                    X                 \n" +
                    "51           |                    |                 \n" +
                    "52           |                          X           \n" +
                    "53           |                          |           \n" +
                    "54           |                    X                 \n" +
                    "55           |                    |                 \n" +
                    "56  X                       X                       \n" +
                    "57  |                       |                       \n" +
                    "58  |                       |                       \n" +
                    "59  |                       |                       \n" +
                    "60  |                       |                       \n" +
                    "61  |                       |                       \n" +
                    "62  |                       |                       \n" +
                    "63  |                       |                       \n",
            out.toString());
  }

  /**
   * Tests the console output of "test.txt".
   */
  @Test
  public void consoleTest2() throws IOException {
    MusicEditorModel model;
    Readable test = new FileReader("test-file.txt");
    model = MusicReader.parseFile(test, new MusicEditorImpl.Builder());
    ViewModel vm = new ViewModel(model) {
      @Override
      public int scoreLength() {
        return super.scoreLength();
      }
    };
    view.draw(vm);
    assertEquals("   C#4 D4D#4 \n" +
                    " 0        X  \n" +
                    " 1        |  \n" +
                    " 2     X     \n" +
                    " 3     |     \n" +
                    " 4  X        \n" +
                    " 5  |        \n" +
                    " 6     X     \n" +
                    " 7     |     \n" +
                    " 8        X  \n" +
                    " 9        |  \n" +
                    "10        X  \n" +
                    "11        |  \n" +
                    "12        X  \n" +
                    "13        |  \n" +
                    "14        |  \n",
            out.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void consoleTest3() throws IOException {
    MusicEditorModel model = new MusicEditorImpl.Builder().build();
    ViewModel vm = new ViewModel(model) {
      @Override
      public int scoreLength() {
        return super.scoreLength();
      }
    };
    view.draw(vm);
  }
}

