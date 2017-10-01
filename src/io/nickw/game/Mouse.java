package io.nickw.game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Mouse implements MouseListener {



	public static class MouseKey {
		public long lastPressed = System.currentTimeMillis();
		public long lastReleased = System.currentTimeMillis();
		public boolean down = false;
		public MouseKey() {

		}

		public void press() {
			lastPressed = System.currentTimeMillis();
			down = true;
		}

		public void release() {
			lastReleased = System.currentTimeMillis();
			down = false;
		}

		public long getPressedLength() {
			long endTime = lastReleased > lastPressed ? lastReleased : System.currentTimeMillis();
			return endTime - lastPressed;
		}

	}

	public static MouseKey left = new MouseKey();
	public static MouseKey middle = new MouseKey();
	public static MouseKey right = new MouseKey();

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) Mouse.left.press();
		if (e.getButton() == 2) Mouse.middle.press();
		if (e.getButton() == 3) Mouse.right.press();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == 1) Mouse.left.release();
		if (e.getButton() == 2) Mouse.middle.release();
		if (e.getButton() == 3) Mouse.right.release();
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}