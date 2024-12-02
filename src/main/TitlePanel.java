package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.RoundedButton;

public class TitlePanel extends JPanel implements Runnable {

	Main main;
	Graphics2D g2;
	GamePanel gp;
	BufferedImage image;
	Font font;
	public int screenWidth = 960;
	public int screenHeight = 576;
	
	public RoundedButton startBtn = new RoundedButton();
	public RoundedButton continueBtn = new RoundedButton();
	public RoundedButton exitBtn = new RoundedButton();
	File file = new File("save.dat");

	//TitleUI ui = new TitleUI(this);
	KeyHandler keyH = new KeyHandler(this);

	Thread titleThread;

	public TitlePanel(Main main, GamePanel gp) {
		this.main = main;
		this.gp = gp;
		main.panelState = main.title;
		setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setLayout(null);
		
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
		drawTitlePage(g2);
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
	public void drawTitlePage(Graphics2D g2) {
		int btnWidth = 200;
		int btnHeight = 50;
		int btnX = screenWidth/2 - btnWidth/2;
		int btnY = screenHeight/2;
		
		startBtn.setText("처음부터");
		//startBtn.setFont(font);
		startBtn.setSize(btnWidth,btnHeight);
		startBtn.setLocation(btnX,btnY);
		startBtn.setBackground(Color.LIGHT_GRAY);
		addEventBtn(startBtn);
			
        continueBtn.setText("이어서");
        continueBtn.setSize(btnWidth,btnHeight);
        continueBtn.setLocation(btnX,btnY+btnHeight);
        continueBtn.setBackground(Color.LIGHT_GRAY);
        
        if (!file.exists()) { 
        	continueBtn.setEnabled(false); 
        }
		addEventBtn(continueBtn);
        
        exitBtn.setText("종료");
        exitBtn.setSize(btnWidth,btnHeight);
        exitBtn.setLocation(btnX,btnY+btnHeight*2);
        exitBtn.setBackground(Color.LIGHT_GRAY);
		addEventBtn(exitBtn);

        add(startBtn);
        add(continueBtn);
        add(exitBtn);	        
        
	}

	public void addEventBtn(JButton btn) {
		btn.addActionListener(new MyActionListener());
		//btn.addMouseListener(new MyMouseListener());       
	}
	
    class MyActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {      
            JButton b = (JButton)e.getSource();

           
            if(b.getText().equals(startBtn.getText())) {

            	main.panelState = main.game;
            	gp.gameState = gp.playState;
            	Main.layout.show(Main.mainPanel,"gp");
            	Main.mainPanel.getComponent(1).setFocusable(true);
            	Main.mainPanel.getComponent(1).requestFocusInWindow();
            }
            else if(b.getText().equals(continueBtn.getText())) {
            	
            	main.panelState = main.game;
            	gp.gameState = gp.playState;
            	Main.layout.show(Main.mainPanel,"gp");
            	Main.mainPanel.getComponent(1).setFocusable(true);
            	Main.mainPanel.getComponent(1).requestFocusInWindow();
            	gp.saveLoad.load();

            }
        	Main.window.revalidate(); // 컴포넌트 갱신
        	Main.window.repaint();    // 화면 갱신
        	
        	if(b.getText().equals(exitBtn.getText())) {
            	System.exit(0);
            }    
        }
    }
}
