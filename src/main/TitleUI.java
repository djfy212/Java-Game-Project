package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;

public class TitleUI {
	//Main mainW;
	TitlePanel tp;
	GamePanel gp;	
	MenuPanel mp;
	Graphics2D g2;
	Font font;
	
	public JButton startBtn = new JButton();
	public JButton continueBtn = new JButton();
	public JButton exitBtn = new JButton();
	
	
	public TitleUI(TitlePanel tp) {
		this.tp = tp;
		tp.setLayout(null);		
	}
	
	public Font settFont() {
		//폰트 파일을 통해 폰트 생성
		try {
			InputStream is = getClass().getResourceAsStream("/font/CookieRun Bold.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, is);
		}catch(FontFormatException e) {
			System.out.println(e);
			//e.printStackTrace();
		}catch(IOException e) {
			System.out.println(e);
			//e.printStackTrace();
		}
		return font;
	}
	
	
	public void drawTitlePage(Graphics2D g2) {
		
		
		int btnWidth = 200;
		int btnHeight = 50;
		int btnX = tp.screenWidth/2 - btnWidth/2;
		int btnY = tp.screenHeight/2;
		
		startBtn.setText("처음부터");
		startBtn.setFont(font);
		startBtn.setSize(btnWidth,btnHeight);
		startBtn.setLocation(btnX,btnY);
		startBtn.setBackground(Color.LIGHT_GRAY);
		addEventBtn(startBtn);
		

        continueBtn.setText("이어서");
        continueBtn.setSize(btnWidth,btnHeight);
        continueBtn.setLocation(btnX,btnY+btnHeight);
        continueBtn.setBackground(Color.LIGHT_GRAY);
		addEventBtn(continueBtn);
        
        exitBtn.setText("종료");
        exitBtn.setSize(btnWidth,btnHeight);
        exitBtn.setLocation(btnX,btnY+btnHeight*2);
        exitBtn.setBackground(Color.LIGHT_GRAY);
		addEventBtn(exitBtn);

        tp.add(startBtn);
        tp.add(continueBtn);
        tp.add(exitBtn);	
	}
	public void addEventBtn(JButton btn) {
		btn.addActionListener(new MyActionListener());
		btn.addMouseListener(new MyMouseListener());       
	}
    class MyActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {      
            JButton b = (JButton)e.getSource();
            //Main.window.getContentPane().removeAll(); // 현재 패널 제거
           
            if(b.getText().equals(startBtn.getText())) {

            	tp.main.panelState = tp.main.game;
            	Main.layout.show(Main.mainPanel,"gp");
            	Main.mainPanel.getComponent(1).setFocusable(true);
            	Main.mainPanel.getComponent(1).requestFocusInWindow();
            }
            else if(b.getText().equals(continueBtn.getText())) {
            	tp.main.panelState = tp.main.game;
            	Main.layout.show(Main.mainPanel,"gp");
            	Main.mainPanel.getComponent(1).setFocusable(true);
            	Main.mainPanel.getComponent(1).requestFocusInWindow();

            }
        	Main.window.revalidate(); // 컴포넌트 갱신
        	Main.window.repaint();    // 화면 갱신
        	
        	if(b.getText().equals(exitBtn.getText())) {
            	System.exit(0);
            }
            
        }
    }

	class MyMouseListener extends MouseAdapter {

		public void mouseEntered(MouseEvent e) {
			JButton btn = (JButton) e.getSource();
			btn.setBackground(Color.DARK_GRAY);
		}
		public void mouseExited(MouseEvent e) {
			Component c = (Component) e.getSource();
			c.setBackground(Color.LIGHT_GRAY);
		}
		public void mouseClicked(MouseEvent e) {
			Component c = (Component) e.getSource();
			c.setBackground(Color.LIGHT_GRAY);
		}
	}

}

