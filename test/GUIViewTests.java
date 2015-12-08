package cs3500.test;

import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import cs3500.music.model.MusicEditorImpl;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.util.MusicReader;
import cs3500.music.view.GuiView;
import cs3500.music.view.PlaybackView;
import cs3500.music.model.ViewModel;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the GUI view graphics Created by Jonathan on 11/23/2015.
 */
public class GUIViewTests {
  @Test
  public void testTestFileBoardTicking() throws IOException, InvalidMidiDataException {
    MusicEditorModel model;
    Readable test = new FileReader("test-file.txt");
    model = MusicReader.parseFile(test, new MusicEditorImpl.Builder());
    ViewModel vm = new ViewModel(model) {
      @Override
      public int scoreLength() {
        return super.scoreLength();
      }
    };
    Appendable log = new StringBuilder();
    GuiView playback = new PlaybackView(log);
    playback.draw(vm);
    playback.tickCurBeat(vm, 1);
    playback.tickCurBeat(vm, 2);
    playback.tickCurBeat(vm, 3);
    playback.tickCurBeat(vm, 4);
    playback.tickCurBeat(vm, 5);
    assertEquals("Creating Note Labels" + "\n" +
            "Created and drew label: D#4" + "\n" +
            "Created and drew label: D4" + "\n" +
            "Created and drew label: C#4" + "\n" +
            "Creating Editor Grid" + "\n" +
            "Created and drew note @ 0,0" + "\n" +
            "Created and drew note @ 0,1" + "\n" +
            "Created and drew note @ 0,2" + "\n" +
            "Created and drew note @ 1,0" + "\n" +
            "Created and drew note @ 1,1" + "\n" +
            "Created and drew note @ 1,2" + "\n" +
            "Created and drew note @ 2,0" + "\n" +
            "Created and drew note @ 2,1" + "\n" +
            "Created and drew note @ 2,2" + "\n" +
            "Created and drew note @ 3,0" + "\n" +
            "Created and drew note @ 3,1" + "\n" +
            "Created and drew note @ 3,2" + "\n" +
            "Created and drew note @ 4,0" + "\n" +
            "Created and drew note @ 4,1" + "\n" +
            "Created and drew note @ 4,2" + "\n" +
            "Created and drew note @ 5,0" + "\n" +
            "Created and drew note @ 5,1" + "\n" +
            "Created and drew note @ 5,2" + "\n" +
            "Created and drew note @ 6,0" + "\n" +
            "Created and drew note @ 6,1" + "\n" +
            "Created and drew note @ 6,2" + "\n" +
            "Created and drew note @ 7,0" + "\n" +
            "Created and drew note @ 7,1" + "\n" +
            "Created and drew note @ 7,2" + "\n" +
            "Created and drew note @ 8,0" + "\n" +
            "Created and drew note @ 8,1" + "\n" +
            "Created and drew note @ 8,2" + "\n" +
            "Created and drew note @ 9,0" + "\n" +
            "Created and drew note @ 9,1" + "\n" +
            "Created and drew note @ 9,2" + "\n" +
            "Created and drew note @ 10,0" + "\n" +
            "Created and drew note @ 10,1" + "\n" +
            "Created and drew note @ 10,2" + "\n" +
            "Created and drew note @ 11,0" + "\n" +
            "Created and drew note @ 11,1" + "\n" +
            "Created and drew note @ 11,2" + "\n" +
            "Created and drew note @ 12,0" + "\n" +
            "Created and drew note @ 12,1" + "\n" +
            "Created and drew note @ 12,2" + "\n" +
            "Created and drew note @ 13,0" + "\n" +
            "Created and drew note @ 13,1" + "\n" +
            "Created and drew note @ 13,2" + "\n" +
            "Created and drew note @ 14,0" + "\n" +
            "Created and drew note @ 14,1" + "\n" +
            "Created and drew note @ 14,2" + "\n" +
            "Created and drew note @ 15,0" + "\n" +
            "Created and drew note @ 15,1" + "\n" +
            "Created and drew note @ 15,2" + "\n" +
            "Created and drew note @ 16,0" + "\n" +
            "Created and drew note @ 16,1" + "\n" +
            "Created and drew note @ 16,2" + "\n" +
            "Created and drew note @ 17,0" + "\n" +
            "Created and drew note @ 17,1" + "\n" +
            "Created and drew note @ 17,2" + "\n" +
            "Created and drew note @ 18,0" + "\n" +
            "Created and drew note @ 18,1" + "\n" +
            "Created and drew note @ 18,2" + "\n" +
            "Created and drew note @ 19,0" + "\n" +
            "Created and drew note @ 19,1" + "\n" +
            "Created and drew note @ 19,2" + "\n" +
            "Created and drew note @ 20,0" + "\n" +
            "Created and drew note @ 20,1" + "\n" +
            "Created and drew note @ 20,2" + "\n" +
            "Created and drew note @ 21,0" + "\n" +
            "Created and drew note @ 21,1" + "\n" +
            "Created and drew note @ 21,2" + "\n" +
            "Created and drew note @ 22,0" + "\n" +
            "Created and drew note @ 22,1" + "\n" +
            "Created and drew note @ 22,2" + "\n" +
            "Created and drew note @ 23,0" + "\n" +
            "Created and drew note @ 23,1" + "\n" +
            "Created and drew note @ 23,2" + "\n" +
            "Created and drew note @ 24,0" + "\n" +
            "Created and drew note @ 24,1" + "\n" +
            "Created and drew note @ 24,2" + "\n" +
            "Created and drew note @ 25,0" + "\n" +
            "Created and drew note @ 25,1" + "\n" +
            "Created and drew note @ 25,2" + "\n" +
            "Created and drew note @ 26,0" + "\n" +
            "Created and drew note @ 26,1" + "\n" +
            "Created and drew note @ 26,2" + "\n" +
            "Created and drew note @ 27,0" + "\n" +
            "Created and drew note @ 27,1" + "\n" +
            "Created and drew note @ 27,2" + "\n" +
            "Created and drew note @ 28,0" + "\n" +
            "Created and drew note @ 28,1" + "\n" +
            "Created and drew note @ 28,2" + "\n" +
            "Created and drew note @ 29,0" + "\n" +
            "Created and drew note @ 29,1" + "\n" +
            "Created and drew note @ 29,2" + "\n" +
            "Created and drew note @ 30,0" + "\n" +
            "Created and drew note @ 30,1" + "\n" +
            "Created and drew note @ 30,2" + "\n" +
            "Created and drew note @ 31,0" + "\n" +
            "Created and drew note @ 31,1" + "\n" +
            "Created and drew note @ 31,2" + "\n" +
            "Created and drew note @ 32,0" + "\n" +
            "Created and drew note @ 32,1" + "\n" +
            "Created and drew note @ 32,2" + "\n" +
            "Created and drew note @ 33,0" + "\n" +
            "Created and drew note @ 33,1" + "\n" +
            "Created and drew note @ 33,2" + "\n" +
            "Created and drew note @ 34,0" + "\n" +
            "Created and drew note @ 34,1" + "\n" +
            "Created and drew note @ 34,2" + "\n" +
            "Created and drew note @ 35,0" + "\n" +
            "Created and drew note @ 35,1" + "\n" +
            "Created and drew note @ 35,2" + "\n" +
            "Created and drew note @ 36,0" + "\n" +
            "Created and drew note @ 36,1" + "\n" +
            "Created and drew note @ 36,2" + "\n" +
            "Created and drew note @ 37,0" + "\n" +
            "Created and drew note @ 37,1" + "\n" +
            "Created and drew note @ 37,2" + "\n" +
            "Created and drew note @ 38,0" + "\n" +
            "Created and drew note @ 38,1" + "\n" +
            "Created and drew note @ 38,2" + "\n" +
            "Created and drew note @ 39,0" + "\n" +
            "Created and drew note @ 39,1" + "\n" +
            "Created and drew note @ 39,2" + "\n" +
            "Created and drew note @ 40,0" + "\n" +
            "Created and drew note @ 40,1" + "\n" +
            "Created and drew note @ 40,2" + "\n" +
            "Created and drew note @ 41,0" + "\n" +
            "Created and drew note @ 41,1" + "\n" +
            "Created and drew note @ 41,2" + "\n" +
            "Packing Output and setting Visible" + "\n" +
            "Setting curBeat in editor and repainting" + "\n" +
            "(Note ON@:0 CHNL: 0 KEY: 62 VELOCITY: 72)" + "\n" +
            "(Note OFF@:200000 CHNL: 0 KEY: 62 VELOCITY: 72)" + "\n" +
            "Setting curBeat in editor and repainting" + "\n" +
            "Setting curBeat in editor and repainting" + "\n" +
            "(Note ON@:0 CHNL: 0 KEY: 61 VELOCITY: 71)" + "\n" +
            "(Note OFF@:200000 CHNL: 0 KEY: 61 VELOCITY: 71)" + "\n" +
            "Setting curBeat in editor and repainting" + "\n" +
            "Setting curBeat in editor and repainting" + "\n"
            , log.toString());
  }

  @Test
  public void testTestFileBoardRender() throws IOException, InvalidMidiDataException {
    MusicEditorModel model;
    Readable test = new FileReader("test-file.txt");
    model = MusicReader.parseFile(test, new MusicEditorImpl.Builder());
    ViewModel vm = new ViewModel(model) {
      @Override
      public int scoreLength() {
        return super.scoreLength();
      }
    };    Appendable log = new StringBuilder();
    GuiView playback = new PlaybackView(log);
    playback.draw(vm);
    assertEquals(
            "Creating Note Labels" + "\n" +
                    "Created and drew label: D#4" + "\n" +
                    "Created and drew label: D4" + "\n" +
                    "Created and drew label: C#4" + "\n" +
                    "Creating Editor Grid" + "\n" +
                    "Created and drew note @ 0,0" + "\n" +
                    "Created and drew note @ 0,1" + "\n" +
                    "Created and drew note @ 0,2" + "\n" +
                    "Created and drew note @ 1,0" + "\n" +
                    "Created and drew note @ 1,1" + "\n" +
                    "Created and drew note @ 1,2" + "\n" +
                    "Created and drew note @ 2,0" + "\n" +
                    "Created and drew note @ 2,1" + "\n" +
                    "Created and drew note @ 2,2" + "\n" +
                    "Created and drew note @ 3,0" + "\n" +
                    "Created and drew note @ 3,1" + "\n" +
                    "Created and drew note @ 3,2" + "\n" +
                    "Created and drew note @ 4,0" + "\n" +
                    "Created and drew note @ 4,1" + "\n" +
                    "Created and drew note @ 4,2" + "\n" +
                    "Created and drew note @ 5,0" + "\n" +
                    "Created and drew note @ 5,1" + "\n" +
                    "Created and drew note @ 5,2" + "\n" +
                    "Created and drew note @ 6,0" + "\n" +
                    "Created and drew note @ 6,1" + "\n" +
                    "Created and drew note @ 6,2" + "\n" +
                    "Created and drew note @ 7,0" + "\n" +
                    "Created and drew note @ 7,1" + "\n" +
                    "Created and drew note @ 7,2" + "\n" +
                    "Created and drew note @ 8,0" + "\n" +
                    "Created and drew note @ 8,1" + "\n" +
                    "Created and drew note @ 8,2" + "\n" +
                    "Created and drew note @ 9,0" + "\n" +
                    "Created and drew note @ 9,1" + "\n" +
                    "Created and drew note @ 9,2" + "\n" +
                    "Created and drew note @ 10,0" + "\n" +
                    "Created and drew note @ 10,1" + "\n" +
                    "Created and drew note @ 10,2" + "\n" +
                    "Created and drew note @ 11,0" + "\n" +
                    "Created and drew note @ 11,1" + "\n" +
                    "Created and drew note @ 11,2" + "\n" +
                    "Created and drew note @ 12,0" + "\n" +
                    "Created and drew note @ 12,1" + "\n" +
                    "Created and drew note @ 12,2" + "\n" +
                    "Created and drew note @ 13,0" + "\n" +
                    "Created and drew note @ 13,1" + "\n" +
                    "Created and drew note @ 13,2" + "\n" +
                    "Created and drew note @ 14,0" + "\n" +
                    "Created and drew note @ 14,1" + "\n" +
                    "Created and drew note @ 14,2" + "\n" +
                    "Created and drew note @ 15,0" + "\n" +
                    "Created and drew note @ 15,1" + "\n" +
                    "Created and drew note @ 15,2" + "\n" +
                    "Created and drew note @ 16,0" + "\n" +
                    "Created and drew note @ 16,1" + "\n" +
                    "Created and drew note @ 16,2" + "\n" +
                    "Created and drew note @ 17,0" + "\n" +
                    "Created and drew note @ 17,1" + "\n" +
                    "Created and drew note @ 17,2" + "\n" +
                    "Created and drew note @ 18,0" + "\n" +
                    "Created and drew note @ 18,1" + "\n" +
                    "Created and drew note @ 18,2" + "\n" +
                    "Created and drew note @ 19,0" + "\n" +
                    "Created and drew note @ 19,1" + "\n" +
                    "Created and drew note @ 19,2" + "\n" +
                    "Created and drew note @ 20,0" + "\n" +
                    "Created and drew note @ 20,1" + "\n" +
                    "Created and drew note @ 20,2" + "\n" +
                    "Created and drew note @ 21,0" + "\n" +
                    "Created and drew note @ 21,1" + "\n" +
                    "Created and drew note @ 21,2" + "\n" +
                    "Created and drew note @ 22,0" + "\n" +
                    "Created and drew note @ 22,1" + "\n" +
                    "Created and drew note @ 22,2" + "\n" +
                    "Created and drew note @ 23,0" + "\n" +
                    "Created and drew note @ 23,1" + "\n" +
                    "Created and drew note @ 23,2" + "\n" +
                    "Created and drew note @ 24,0" + "\n" +
                    "Created and drew note @ 24,1" + "\n" +
                    "Created and drew note @ 24,2" + "\n" +
                    "Created and drew note @ 25,0" + "\n" +
                    "Created and drew note @ 25,1" + "\n" +
                    "Created and drew note @ 25,2" + "\n" +
                    "Created and drew note @ 26,0" + "\n" +
                    "Created and drew note @ 26,1" + "\n" +
                    "Created and drew note @ 26,2" + "\n" +
                    "Created and drew note @ 27,0" + "\n" +
                    "Created and drew note @ 27,1" + "\n" +
                    "Created and drew note @ 27,2" + "\n" +
                    "Created and drew note @ 28,0" + "\n" +
                    "Created and drew note @ 28,1" + "\n" +
                    "Created and drew note @ 28,2" + "\n" +
                    "Created and drew note @ 29,0" + "\n" +
                    "Created and drew note @ 29,1" + "\n" +
                    "Created and drew note @ 29,2" + "\n" +
                    "Created and drew note @ 30,0" + "\n" +
                    "Created and drew note @ 30,1" + "\n" +
                    "Created and drew note @ 30,2" + "\n" +
                    "Created and drew note @ 31,0" + "\n" +
                    "Created and drew note @ 31,1" + "\n" +
                    "Created and drew note @ 31,2" + "\n" +
                    "Created and drew note @ 32,0" + "\n" +
                    "Created and drew note @ 32,1" + "\n" +
                    "Created and drew note @ 32,2" + "\n" +
                    "Created and drew note @ 33,0" + "\n" +
                    "Created and drew note @ 33,1" + "\n" +
                    "Created and drew note @ 33,2" + "\n" +
                    "Created and drew note @ 34,0" + "\n" +
                    "Created and drew note @ 34,1" + "\n" +
                    "Created and drew note @ 34,2" + "\n" +
                    "Created and drew note @ 35,0" + "\n" +
                    "Created and drew note @ 35,1" + "\n" +
                    "Created and drew note @ 35,2" + "\n" +
                    "Created and drew note @ 36,0" + "\n" +
                    "Created and drew note @ 36,1" + "\n" +
                    "Created and drew note @ 36,2" + "\n" +
                    "Created and drew note @ 37,0" + "\n" +
                    "Created and drew note @ 37,1" + "\n" +
                    "Created and drew note @ 37,2" + "\n" +
                    "Created and drew note @ 38,0" + "\n" +
                    "Created and drew note @ 38,1" + "\n" +
                    "Created and drew note @ 38,2" + "\n" +
                    "Created and drew note @ 39,0" + "\n" +
                    "Created and drew note @ 39,1" + "\n" +
                    "Created and drew note @ 39,2" + "\n" +
                    "Created and drew note @ 40,0" + "\n" +
                    "Created and drew note @ 40,1" + "\n" +
                    "Created and drew note @ 40,2" + "\n" +
                    "Created and drew note @ 41,0" + "\n" +
                    "Created and drew note @ 41,1" + "\n" +
                    "Created and drew note @ 41,2" + "\n" +
                    "Packing Output and setting Visible" + "\n"
            , log.toString());
  }
}
