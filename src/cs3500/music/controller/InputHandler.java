package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

/**
 * Created by alexmelagrano on 11/19/15.
 */
public class InputHandler implements KeyListener, MouseListener {
    private Controller controller;
    private HashMap<Integer, Runnable> typed;
    private HashMap<Integer, Runnable> pressed;
    private HashMap<Integer, Runnable> released;

    public InputHandler(Controller controller) {
        this.controller = controller;
        this.typed = new HashMap<Integer, Runnable>();
        this.pressed = new HashMap<Integer, Runnable>();
        this.released = new HashMap<Integer, Runnable>();
    }

    /** Handle the key typed event from the text field. */
    public void keyTyped(KeyEvent e) {
        if (this.typed.containsKey(e.getExtendedKeyCode())) {
            this.typed.get(e.getExtendedKeyCode()).run();
        }
        displayInfo(e, "KEY TYPED: ");
    }

    /** Handle the key-pressed event from the text field. */
    public void keyPressed(KeyEvent e) {
        if (this.pressed.containsKey(e.getExtendedKeyCode())) {
            this.pressed.get(e.getExtendedKeyCode()).run();
        }
        displayInfo(e, "KEY PRESSED: ");
    }

    /** Handle the key-released event from the text field. */
    public void keyReleased(KeyEvent e) {
        if (this.released.containsKey(e.getExtendedKeyCode())) {
            this.released.get(e.getExtendedKeyCode()).run();
        }
        displayInfo(e, "KEY RELEASED: ");
    }

    /**
     * Adds a new key event and its corresponding action into their respective
     * map field.
     *
     * @param e   key id
     * @param r   runnable action corresponding to the key
     */
    public void addTypedEvent(int e, Runnable r) {
        this.typed.put(e, r);
    }
    public void addPressedEvent(int e, Runnable r) {
        this.pressed.put(e, r);
    }
    public void addReleasedEvent(int e, Runnable r) {
        this.released.put(e, r);
    }


    private void displayInfo(KeyEvent e, String keyStatus){

        //You should only rely on the key char if the event
        //is a key typed event.
        int id = e.getID();
        String keyString;
        if (id == KeyEvent.KEY_TYPED) {
            char c = e.getKeyChar();
            keyString = "key character = '" + c + "'";
        } else {
            int keyCode = e.getKeyCode();
            keyString = "key code = " + keyCode
                    + " ("
                    + KeyEvent.getKeyText(keyCode)
                    + ")";
        }

        int modifiersEx = e.getModifiersEx();
        String modString = "extended modifiers = " + modifiersEx;
        String tmpString = KeyEvent.getModifiersExText(modifiersEx);
        if (tmpString.length() > 0) {
            modString += " (" + tmpString + ")";
        } else {
            modString += " (no extended modifiers)";
        }

        String actionString = "action key? ";
        if (e.isActionKey()) {
            actionString += "YES";
        } else {
            actionString += "NO";
        }

        String locationString = "key location: ";
        int location = e.getKeyLocation();
        if (location == KeyEvent.KEY_LOCATION_STANDARD) {
            locationString += "standard";
        } else if (location == KeyEvent.KEY_LOCATION_LEFT) {
            locationString += "left";
        } else if (location == KeyEvent.KEY_LOCATION_RIGHT) {
            locationString += "right";
        } else if (location == KeyEvent.KEY_LOCATION_NUMPAD) {
            locationString += "numpad";
        } else { // (location == KeyEvent.KEY_LOCATION_UNKNOWN)
            locationString += "unknown";
        }
    }

    // TODO ;; THIS SHIIIIT

    /**
     * When the mouse is clicked, the method will check if any of the modifier keys
     * are being pressed. If a proper combination exists, it will run the corresponding
     * method to perform the desired activity. Otherwise, it will ignore the click.
     *
     * @param e  mouse event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        this.controller.setCurrent(e.getX(), e.getY());
        // If the left mouse button was clicked:
        if (e.getButton() == MouseEvent.BUTTON1) {
            // If the "a" key is being pressed, for adding notes
            if (this.controller.isPressed(65)) {
                this.controller.addNote();
            }
            // If the "s" key is being pressed, for removing notes
            if (this.controller.isPressed(83)) {
                this.controller.removeNote();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
