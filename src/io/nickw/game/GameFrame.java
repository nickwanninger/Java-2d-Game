package io.nickw.game;

import javax.imageio.ImageIO;
import javax.swing.*;

import io.nickw.game.gfx.Sprite;
import kuusisto.tinysound.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
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