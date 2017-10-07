package io.nickw.game;

import javax.swing.*;
import kuusisto.tinysound.*;
import java.awt.event.WindowAdapter;
import java.io.IOException;

public class GameFrame extends JFrame {



	public GameFrame(String title) {

		super(title);
		if (Game.OS.contains("mac")) {
			try {
				Runtime.getRuntime().exec("defaults write NSGlobalDomain ApplePressAndHoldEnabled -bool false");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		TinySound.init();
		setFocusable(true);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				TinySound.shutdown();
				if (Game.OS.contains("mac")) {
					try {
						Runtime.getRuntime().exec("defaults write NSGlobalDomain ApplePressAndHoldEnabled -bool true");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				System.exit(0);

			}
		});



	}




}