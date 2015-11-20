package cs3500.music.controller;

import cs3500.music.model.MusicEditorModel;
import cs3500.music.view.EditorView;
import cs3500.music.view.ViewModel;

import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * Created by alexmelagrano on 11/20/15.
 */
public class TempClass {
    package cs3500.music.controller;


    import cs3500.music.model.MusicEditorModel;
    import cs3500.music.view.EditorView;
    import cs3500.music.view.ViewModel;

    import java.awt.event.KeyListener;
    import java.io.IOException;
    import java.io.InputStream;
    import java.util.function.Consumer;

    import static java.util.Objects.requireNonNull;

    /**
     * Controller for the Connect <em>N</em> console UI. Mediates between the view and the model by
     * taking user input and acting on the view, and then taking information from the view and showing
     * it to the user.
     */
    public final class EditorController implements Controller {

        private final MusicEditorModel model;
        private final ViewModel vm;
        private final EditorView view;
        private MouseHandler mh;
        private KeyboardHandler kh;
        private int curX, curY;


        /**
         * Constructs a controller for playing the given game model. Uses stdin and stdout as the user's
         * console.
         *
         * @param model the game to play
         */
        public EditorController(MusicEditorModel model) {
            this(model, System.in, System.out);
        }

        /**
         * Constructs a controller for playing the given game model, with the given input and output for
         * communicating with the user.
         *
         * @param model0 the game to play
         * @param in     where to read user input from
         * @param out    where to send output to for the user to see
         */
        public EditorController(MusicEditorModel model0, InputStream in, Appendable out) {
            model = requireNonNull(model0);
            vm = adaptModelToViewModel(model);
            view = new EditorView();
            mh = new MouseHandler(this);
            kh = new KeyboardHandler();
            // Takes you to the desired part of the piece
            Consumer<Integer> start = view.toStart(curX);          // TODO ;; IMPLEMENT THIS SHIT - MAYBE USE CONSUMERS INSTEAD
            Runnable end = view.toEnd();              // TODO ;; OR LAMBDA'S... WOULD BE LIKE THE BOTTOM OF THIS LIST
            // Traverses the view
            Runnable scrollLeft = view.scrollLeft();
            Runnable scrollRight = view.scrollRight();
            Runnable play = view.play();
            Runnable pause = view.pause();
            kh.addTypedEvent(36, start);           // "home"
            kh.addTypedEvent(35, end);             // "end"
            kh.addTypedEvent(226, scrollLeft);     // "left arrow"
            kh.addTypedEvent(227, scrollRight);    // "right arrow"
            kh.addTypedEvent(32, play);            // "space"
            kh.addTypedEvent(80, pause);           // "p"

            // TODO ;; HERE
            kh.addTypedEvent(1, ()-> {
                // write method shit here; has access to the model and whatnot through the view
                // only problem is that this still can't take arguments, because it wouldn't be a runnable
            });
        }

        @Override
        public void setCurrent(int x, int y) {
            this.curX = x;
            this.curY = y;
        }

        // TODO :: CHECK IF THIS IS RIGHT
        @Override
        public void setKeyHandler(KeyListener kh) {
            this.view.setKeyHanlder(kh);
        }

        @Override
        public void setMouseHandler(MouseHandler mh) {
            this.view.setMouseHanlder(mh);
        }


        @Override
        public void run() throws IOException {
            // TODO: Uncomment While later on
            //while (true) {
            view.draw(vm);
            listen();
            //}
        }

        @Override
        public void listen() throws IOException {

            // This loop tries repeatedly to read user input and make a move until it
            // succeeds. If there is a problem either reading or executing, the
            // exception is caught, an informative message displayed, and then the
            // loop repeats to try again.
            for (; ; ) {
                try {

                    return;
                } catch () {

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
    }

}

