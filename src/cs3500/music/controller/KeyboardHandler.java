package cs3500.music.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

/**
 * Created by alexmelagrano on 11/19/15.
 */
public class KeyboardHandler implements KeyListener {
    private HashMap<Integer, Runnable> typed;
    private HashMap<Integer, Runnable> pressed;
    private HashMap<Integer, Runnable> released;

    public KeyboardHandler() {
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
// TODO ; WHATS THIS
        ...//Display information about the KeyEvent...
    }
}
