package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TitlePanel extends JPanel implements Runnable {
	
	Main main;
	Graphics2D g2;
	BufferedImage image;
	
//	public int screenWidth = 960;
//	public int screenHeight = 576;
	public int screenWidth = 1290;
	public int screenHeight = 720;	
	
	TitleUI ui = new TitleUI(this);
	KeyHandler keyH = new KeyHandler(this);	
	
	Thread titleThread;	

	public TitlePanel(Main main) {
		this.main = main;
		main.panelState = main.title;
		setBackground(Color.blue);
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setDoubleBuffered(true);	
		this.addKeyListener(keyH);
		//this.setFocusable(true);
	}
	public void startThread() {
		
		titleThread = new Thread(this);
		titleThread.start();
	}
	public void stopThread() {
		titleThread = null;
	}

	public void run() {
		
		if(main.panelState == main.title) {
			double drawInterval = 1000000000/60;
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
				if(main.panelState == main.title) {
					System.out.println("titleFPS:" + drawCount);	
				}
			
				drawCount = 0;
				timer = 0;
			}
		}			
		}
	
		

	}
}
