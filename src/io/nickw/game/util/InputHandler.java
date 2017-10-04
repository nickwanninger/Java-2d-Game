package io.nickw.game.util;
import io.nickw.game.Game;
import io.nickw.game.GameFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class InputHandler {









	public class KeyAction extends AbstractAction {

		private boolean state;

		public KeyAction(boolean state) {
			this.state = state;
			putValue(NAME, state);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int keyCode = KeyEvent.getExtendedKeyCodeForChar(e.getActionCommand().charAt(0));
			toggle(keyCode, state);
		}

	}

	InputMap im;
	ActionMap am;

	public InputHandler(JPanel p) {
		im = p.getInputMap(2);
		am = p.getActionMap();

		String[] keyStrings = new String[] {
				"W", "A", "S", "D", "E", "ESCAPE", "P"
		};
		for (int i = 0; i < keyStrings.length; i++) {
			String s = keyStrings[i];
			im.put(KeyStroke.getKeyStroke("pressed " + s), "pressed");
			im.put(KeyStroke.getKeyStroke("released " + s), "released");
		}
		KeyAction pressAction = new KeyAction(true);
		KeyAction releaseAction = new KeyAction(false);
		am.put("pressed", pressAction);
		am.put("released", releaseAction);
	}






	public class Key {
		public int presses, absorbs;
		public boolean down, clicked;

		public Key() {
			keys.add(this);
		}

		public void toggle(boolean pressed) {
			if (pressed != down) {
				down = pressed;
			}
			if (pressed) {
				presses++;
			}
		}

		public void tick() {
			if (absorbs < presses) {
				absorbs++;
				clicked = true;
			} else {
				clicked = false;
			}
		}
	}

	public ArrayList<Key> keys = new ArrayList<Key>();

	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key attack = new Key();
	public Key menu = new Key();
	public Key debug = new Key();
	public Key inventory = new Key();

	public void releaseAll() {
		for (int i = 0; i < keys.size(); i++) {
			keys.get(i).down = false;
		}
	}

	public void tick() {
		for (int i = 0; i < keys.size(); i++) {
			keys.get(i).tick();
		}
	}



	private void toggle(int ke, boolean pressed) {
		if (ke == KeyEvent.VK_W) up.toggle(pressed);
		if (ke == KeyEvent.VK_S) down.toggle(pressed);
		if (ke == KeyEvent.VK_A) left.toggle(pressed);
		if (ke == KeyEvent.VK_D) right.toggle(pressed);

		if (ke == KeyEvent.VK_P) debug.toggle(pressed);

		if (ke == KeyEvent.VK_ESCAPE) inventory.toggle(pressed);
		if (ke == KeyEvent.VK_E) inventory.toggle(pressed);
	}

}