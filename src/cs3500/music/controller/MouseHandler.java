package cs3500.music.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by alexmelagrano on 11/19/15.
 */
public class MouseHandler implements MouseListener {

    Controller controller;

    public MouseHandler(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Sends the position data of the mouse click to the GuiView
        controller.setCurrent(e.getX(), e.getY());
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
