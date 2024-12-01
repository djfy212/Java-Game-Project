package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;

import data.RoundedButton;

public class MenuPanel extends JPanel implements Runnable {
	
	Main main;
	GamePanel gp;
	Graphics2D g2;
	
	public final int screenWidth = 960;
	public final int screenHeight = 576;

	public int menuState;
	public final int charState = 0;
	public final int skillState = 1;
	public final int equipState = 2;
	public final int invenState = 3;
	public final int optionState = 4;
	
	public boolean backGame = false;
	public String usedBtn = "";
	
	Thread menuThread;	
	public KeyHandler keyH = new KeyHandler(this);
	public MenuUI ui = new MenuUI(this);
	
	public RoundedButton charBtn = new RoundedButton();
	public RoundedButton skillBtn = new RoundedButton();
	public RoundedButton equipBtn = new RoundedButton();
	public RoundedButton invenBtn = new RoundedButton();
	public RoundedButton optionBtn = new RoundedButton();
		
    
	public MenuPanel(Main main, GamePanel gp) {
		
		this.main = main;
		this.gp = gp;
		setBackground(new Color(0x009A7B4F));
		this.setPreferredSize(new Dimension(gp.screenWidth, gp.screenHeight));
		this.addKeyListener(keyH);
		this.setLayout(null);

		menuState = charState;
		usedBtn = "charBtn";

		DisplayPanel dp = new DisplayPanel(this);
		dp.setSize(gp.screenWidth-gp.tileSize*3, gp.screenHeight);
		dp.setLocation(gp.tileSize*3,0);
		this.add(dp);
		this.setVisible(true);
	}
	public void startThread() {		
		menuThread = new Thread(this);
		menuThread.start();
	}
	public void stopThread() {
		menuThread = null;
	}

	public void run() {
		double drawInterval = 1000000000 / 60;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;
		
		drawButtons(g2,this);

		while (menuThread != null) {

			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;

			if (delta >= 1) {
				if(main.panelState == main.menu) {					
					repaint();
					if(backGame == true) {
			        	Main.mainPanel.getComponent(2).setFocusable(true);
			        	Main.mainPanel.getComponent(2).requestFocusInWindow();
			        	backGame = false;
					}
				}
				delta--;
				drawCount++;			
			}
			if (timer >= 1000000000) {
				if(main.panelState == main.menu) {
				
					System.out.println("gameFPS:" + drawCount);
					System.out.println("state:" + main.panelState);	
				}

				drawCount = 0;
				timer = 0;
			}
		}
	}
	
	public void drawButtons(Graphics2D g2, MenuPanel mp) {

		int btnWidth = gp.tileSize*3;
		int btnHeight = 70;
		int btnX = 0;
		int btnY = btnHeight;

		charBtn.setText("캐릭터 정보");
		charBtn.setSize(btnWidth, btnHeight);
		charBtn.setLocation(btnX, btnY);
		charBtn.setBackground(Color.DARK_GRAY);
		addEventBtn(charBtn);
		btnY += btnHeight+16;
		skillBtn.setText("스킬 정보");
		skillBtn.setSize(btnWidth, btnHeight);
		skillBtn.setLocation(btnX, btnY);
		skillBtn.setBackground(Color.LIGHT_GRAY);
		addEventBtn(skillBtn);
		btnY += btnHeight+16;
		equipBtn.setText("장비 정보");
		equipBtn.setSize(btnWidth, btnHeight);
		equipBtn.setLocation(btnX, btnY);
		equipBtn.setBackground(Color.LIGHT_GRAY);
		addEventBtn(equipBtn);
		btnY += btnHeight+16;
		invenBtn.setText("인벤토리");
		invenBtn.setSize(btnWidth, btnHeight);
		invenBtn.setLocation(btnX, btnY);
		invenBtn.setBackground(Color.LIGHT_GRAY);
		addEventBtn(invenBtn);
		btnY += btnHeight+16;
		optionBtn.setText("옵션");
		optionBtn.setSize(btnWidth, btnHeight);
		optionBtn.setLocation(btnX, btnY);
		optionBtn.setBackground(Color.LIGHT_GRAY);
		addEventBtn(optionBtn);

		mp.add(charBtn);
		mp.add(skillBtn);
		mp.add(equipBtn);
		mp.add(invenBtn);
		mp.add(optionBtn);

	}
	public void addEventBtn(JButton btn) {
		btn.addActionListener(new MyActionListener());
		btn.addMouseListener(new MyMouseListener());
	}
	class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton) e.getSource();
			setButtonColor();
			b.setBackground(Color.DARK_GRAY);
			usedBtn = b.getText();
			if (b.getText().equals(charBtn.getText())) {
				menuState = charState;
			}
			else if (b.getText().equals(skillBtn.getText())) {
				menuState = skillState;
          	
			}			
			else if (b.getText().equals(equipBtn.getText())) {
				menuState = equipState;
       	
			}
			else if (b.getText().equals(invenBtn.getText())) {
				menuState = invenState;
           	
			}          	
			else if (b.getText().equals(optionBtn.getText())) {
				menuState = optionState;          	
			}			
			backGame = true;
		}
	}

	class MyMouseListener extends MouseAdapter {

		public void mouseEntered(MouseEvent e) {
			JButton btn = (JButton) e.getSource();
			if(!btn.getText().equals(usedBtn))
				btn.setBackground(Color.DARK_GRAY);
		}

		public void mouseExited(MouseEvent e) {
			JButton btn = (JButton) e.getSource();
			if(!btn.getText().equals(usedBtn))
				btn.setBackground(Color.LIGHT_GRAY);
		}

		public void mouseClicked(MouseEvent e) {
			JButton btn = (JButton) e.getSource();
			if(!btn.getText().equals(usedBtn))
				btn.setBackground(Color.LIGHT_GRAY);
		}
	}
	public void setButtonColor(){
		charBtn.setBackground(Color.LIGHT_GRAY);
		skillBtn.setBackground(Color.LIGHT_GRAY);
		equipBtn.setBackground(Color.LIGHT_GRAY);
		invenBtn.setBackground(Color.LIGHT_GRAY);
		optionBtn.setBackground(Color.LIGHT_GRAY);
	}

}
class DisplayPanel extends JPanel{
	MenuPanel mp;
	MenuUI ui;
	public DisplayPanel(MenuPanel mp){
		this.mp = mp;
		ui = new MenuUI(mp);
		this.setBackground(Color.LIGHT_GRAY);
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
        ui.draw(g2);
        g2.dispose();
	}
}
