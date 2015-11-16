package cs3500.test;

import cs3500.music.model.MusicEditorImpl;
import cs3500.music.model.MusicEditorModel;
import cs3500.music.util.MusicReader;
import cs3500.music.view.MidiView;
import cs3500.music.view.ViewModel;

import org.junit.Test;

import sun.security.krb5.internal.APOptions;

import java.io.FileReader;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;

import static org.junit.Assert.assertEquals;

/**
 * Class to test midi outputs Created by alexmelagrano on 11/12/15.
 */
public class MidiViewTests {

  @Test
  // Tests midi output from test file
  public void testMidiView1() throws IOException, InvalidMidiDataException {
    Appendable out = new StringBuilder();
    Readable test = new FileReader("test-file.txt");
    MusicEditorModel model = MusicEditorImpl.makeEditor();
    model = MusicReader.parseFile(test, new MusicEditorImpl.Builder());
    ViewModel vm = ViewModel.makeViewModel(model);

    MidiView midi = new MidiView(vm, out);
    midi.run();
    String shouldBe =
            "(Note ON@:0 CHNL: 0 KEY: 63 VELOCITY: 72)\n" +
                    "(Note OFF@:16 CHNL: 0 KEY: 63 VELOCITY: 72)\n" +
                    "(Note ON@:32 CHNL: 0 KEY: 62 VELOCITY: 72)\n" +
                    "(Note OFF@:48 CHNL: 0 KEY: 62 VELOCITY: 72)\n" +
                    "(Note ON@:64 CHNL: 0 KEY: 61 VELOCITY: 71)\n" +
                    "(Note OFF@:80 CHNL: 0 KEY: 61 VELOCITY: 71)\n" +
                    "(Note ON@:96 CHNL: 0 KEY: 62 VELOCITY: 79)\n" +
                    "(Note OFF@:112 CHNL: 0 KEY: 62 VELOCITY: 79)\n" +
                    "(Note ON@:128 CHNL: 0 KEY: 63 VELOCITY: 85)\n" +
                    "(Note OFF@:144 CHNL: 0 KEY: 63 VELOCITY: 85)\n" +
                    "(Note ON@:160 CHNL: 0 KEY: 63 VELOCITY: 78)\n" +
                    "(Note OFF@:176 CHNL: 0 KEY: 63 VELOCITY: 78)\n" +
                    "(Note ON@:192 CHNL: 0 KEY: 63 VELOCITY: 74)\n" +
                    "(Note OFF@:224 CHNL: 0 KEY: 63 VELOCITY: 74)\n";
    assertEquals(shouldBe, out.toString());

  }

  @Test
  // Tests midi output from Mary
  public void testMidiView2() throws IOException {
    Appendable out = new StringBuilder();
    Readable mary = new FileReader("mary-little-lamb.txt");
    MusicEditorModel model = MusicEditorImpl.makeEditor();
    model = MusicReader.parseFile(mary, new MusicEditorImpl.Builder());
    ViewModel vm = ViewModel.makeViewModel(model);

    MidiView midi = new MidiView(vm, out);
    midi.run();
    String shouldBe =
    "(Note ON@:0 CHNL: 0 KEY: 64 VELOCITY: 72)\n" +
    "(Note OFF@:16 CHNL: 0 KEY: 64 VELOCITY: 72)\n" +
    "(Note ON@:0 CHNL: 0 KEY: 55 VELOCITY: 70)\n" +
    "(Note OFF@:96 CHNL: 0 KEY: 55 VELOCITY: 70)\n" +
    "(Note ON@:32 CHNL: 0 KEY: 62 VELOCITY: 72)\n" +
    "(Note OFF@:48 CHNL: 0 KEY: 62 VELOCITY: 72)\n" +
    "(Note ON@:64 CHNL: 0 KEY: 60 VELOCITY: 71)\n" +
    "(Note OFF@:80 CHNL: 0 KEY: 60 VELOCITY: 71)\n" +
    "(Note ON@:96 CHNL: 0 KEY: 62 VELOCITY: 79)\n" +
    "(Note OFF@:112 CHNL: 0 KEY: 62 VELOCITY: 79)\n" +
    "(Note ON@:128 CHNL: 0 KEY: 55 VELOCITY: 79)\n" +
    "(Note OFF@:224 CHNL: 0 KEY: 55 VELOCITY: 79)\n" +
    "(Note ON@:128 CHNL: 0 KEY: 64 VELOCITY: 85)\n" +
    "(Note OFF@:144 CHNL: 0 KEY: 64 VELOCITY: 85)\n" +
    "(Note ON@:160 CHNL: 0 KEY: 64 VELOCITY: 78)\n" +
    "(Note OFF@:176 CHNL: 0 KEY: 64 VELOCITY: 78)\n" +
    "(Note ON@:192 CHNL: 0 KEY: 64 VELOCITY: 74)\n" +
    "(Note OFF@:224 CHNL: 0 KEY: 64 VELOCITY: 74)\n" +
    "(Note ON@:256 CHNL: 0 KEY: 55 VELOCITY: 77)\n" +
    "(Note OFF@:368 CHNL: 0 KEY: 55 VELOCITY: 77)\n" +
    "(Note ON@:256 CHNL: 0 KEY: 62 VELOCITY: 75)\n" +
    "(Note OFF@:272 CHNL: 0 KEY: 62 VELOCITY: 75)\n" +
    "(Note ON@:288 CHNL: 0 KEY: 62 VELOCITY: 77)\n" +
    "(Note OFF@:304 CHNL: 0 KEY: 62 VELOCITY: 77)\n" +
    "(Note ON@:320 CHNL: 0 KEY: 62 VELOCITY: 75)\n" +
    "(Note OFF@:368 CHNL: 0 KEY: 62 VELOCITY: 75)\n" +
    "(Note ON@:384 CHNL: 0 KEY: 55 VELOCITY: 79)\n" +
    "(Note OFF@:400 CHNL: 0 KEY: 55 VELOCITY: 79)\n" +
    "(Note ON@:384 CHNL: 0 KEY: 64 VELOCITY: 82)\n" +
    "(Note OFF@:400 CHNL: 0 KEY: 64 VELOCITY: 82)\n" +
    "(Note ON@:416 CHNL: 0 KEY: 67 VELOCITY: 84)\n" +
    "(Note OFF@:432 CHNL: 0 KEY: 67 VELOCITY: 84)\n" +
    "(Note ON@:448 CHNL: 0 KEY: 67 VELOCITY: 75)\n" +
    "(Note OFF@:496 CHNL: 0 KEY: 67 VELOCITY: 75)\n" +
    "(Note ON@:512 CHNL: 0 KEY: 55 VELOCITY: 78)\n" +
    "(Note OFF@:624 CHNL: 0 KEY: 55 VELOCITY: 78)\n" +
    "(Note ON@:512 CHNL: 0 KEY: 64 VELOCITY: 73)\n" +
    "(Note OFF@:528 CHNL: 0 KEY: 64 VELOCITY: 73)\n" +
    "(Note ON@:544 CHNL: 0 KEY: 62 VELOCITY: 69)\n" +
    "(Note OFF@:560 CHNL: 0 KEY: 62 VELOCITY: 69)\n" +
    "(Note ON@:576 CHNL: 0 KEY: 60 VELOCITY: 71)\n" +
    "(Note OFF@:592 CHNL: 0 KEY: 60 VELOCITY: 71)\n" +
    "(Note ON@:608 CHNL: 0 KEY: 62 VELOCITY: 80)\n" +
    "(Note OFF@:624 CHNL: 0 KEY: 62 VELOCITY: 80)\n" +
    "(Note ON@:640 CHNL: 0 KEY: 55 VELOCITY: 79)\n" +
    "(Note OFF@:752 CHNL: 0 KEY: 55 VELOCITY: 79)\n" +
    "(Note ON@:640 CHNL: 0 KEY: 64 VELOCITY: 84)\n" +
    "(Note OFF@:656 CHNL: 0 KEY: 64 VELOCITY: 84)\n" +
    "(Note ON@:672 CHNL: 0 KEY: 64 VELOCITY: 76)\n" +
    "(Note OFF@:688 CHNL: 0 KEY: 64 VELOCITY: 76)\n" +
    "(Note ON@:704 CHNL: 0 KEY: 64 VELOCITY: 74)\n" +
    "(Note OFF@:720 CHNL: 0 KEY: 64 VELOCITY: 74)\n" +
    "(Note ON@:736 CHNL: 0 KEY: 64 VELOCITY: 77)\n" +
    "(Note OFF@:752 CHNL: 0 KEY: 64 VELOCITY: 77)\n" +
    "(Note ON@:768 CHNL: 0 KEY: 55 VELOCITY: 78)\n" +
    "(Note OFF@:880 CHNL: 0 KEY: 55 VELOCITY: 78)\n" +
    "(Note ON@:768 CHNL: 0 KEY: 62 VELOCITY: 75)\n" +
    "(Note OFF@:784 CHNL: 0 KEY: 62 VELOCITY: 75)\n" +
    "(Note ON@:800 CHNL: 0 KEY: 62 VELOCITY: 74)\n" +
    "(Note OFF@:816 CHNL: 0 KEY: 62 VELOCITY: 74)\n" +
    "(Note ON@:832 CHNL: 0 KEY: 64 VELOCITY: 81)\n" +
    "(Note OFF@:848 CHNL: 0 KEY: 64 VELOCITY: 81)\n" +
    "(Note ON@:864 CHNL: 0 KEY: 62 VELOCITY: 70)\n" +
    "(Note OFF@:880 CHNL: 0 KEY: 62 VELOCITY: 70)\n" +
    "(Note ON@:896 CHNL: 0 KEY: 52 VELOCITY: 72)\n" +
    "(Note OFF@:1008 CHNL: 0 KEY: 52 VELOCITY: 72)\n" +
    "(Note ON@:896 CHNL: 0 KEY: 60 VELOCITY: 73)\n" +
    "(Note OFF@:1008 CHNL: 0 KEY: 60 VELOCITY: 73)\n";
    assertEquals(shouldBe, out.toString());

  }


}
