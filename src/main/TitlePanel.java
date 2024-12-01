package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TitlePanel extends JPanel implements Runnable {

	Main main;
	Graphics2D g2;
	BufferedImage image;
	Font font;
	public int screenWidth = 960;
	public int screenHeight = 576;

	TitleUI ui = new TitleUI(this);
	KeyHandler keyH = new KeyHandler(this);

	Thread titleThread;

	public TitlePanel(Main main) {
		this.main = main;
		main.panelState = main.title;
		setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		
		
		try {
			InputStream is = getClass().getResourceAsStream("/font/CookieRun Regular.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	public void startThread() {

		titleThread = new Thread(this);
		titleThread.start();
	}

	public void stopThread() {
		titleThread = null;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setFont(font);
		g2.setColor(Color.WHITE);
		
	}

	public void run() {

		double drawInterval = 1000000000 / 60;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		ui.drawTitlePage(g2);
		while (titleThread != null) {

			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;

			if (delta >= 1) {
				delta--;
				drawCount++;
				

			}
			if (timer >= 1000000000) {
				if (main.panelState == main.title) {
					System.out.println("titleFPS:" + drawCount);
				}

				drawCount = 0;
				timer = 0;
			}
		}
	}

}
