package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import data.RoundedButton;

import data.Ranking;


public class TitleUI {
    
    TitlePanel tp;
    GamePanel gp;        
    Graphics2D g2;
    
    Ranking rankings;
    public RoundedButton startBtn = new RoundedButton();
    public RoundedButton continueBtn = new RoundedButton();
    public RoundedButton rankingBtn = new RoundedButton();  // 랭킹 버튼 추가
    public RoundedButton exitBtn = new RoundedButton();
    File file = new File("Save.dat");
    
    public TitleUI(TitlePanel tp) {
        this.tp = tp;
        this.rankings = new Ranking();  // DB 연결
        tp.setLayout(null);    
    }
    
    public void drawTitlePage(Graphics2D g2) {
        int btnWidth = 200;
        int btnHeight = 50;
        int btnX = tp.screenWidth / 2 - btnWidth / 2;
        int btnY = tp.screenHeight / 2;

        // "처음부터" 버튼 설정
        startBtn.setText("처음부터");
        startBtn.setSize(btnWidth, btnHeight);
        startBtn.setLocation(btnX, btnY);
        startBtn.setBackground(Color.LIGHT_GRAY);
        addEventBtn(startBtn);

        // "이어서" 버튼 설정
        continueBtn.setText("이어서");
        continueBtn.setSize(btnWidth, btnHeight);
        continueBtn.setLocation(btnX, btnY + btnHeight);
        continueBtn.setBackground(Color.LIGHT_GRAY);

        if (file.exists()) { 
            continueBtn.setEnabled(false); 
        }
        addEventBtn(continueBtn);

        // "랭킹 보기" 버튼 설정
        rankingBtn.setText("랭킹 보기");
        rankingBtn.setSize(btnWidth, btnHeight);
        rankingBtn.setLocation(btnX, btnY + btnHeight * 2);
        rankingBtn.setBackground(Color.LIGHT_GRAY);
        addEventBtn(rankingBtn);

        // "종료" 버튼 설정
        exitBtn.setText("종료");
        exitBtn.setSize(btnWidth, btnHeight);
        exitBtn.setLocation(btnX, btnY + btnHeight * 3);
        exitBtn.setBackground(Color.LIGHT_GRAY);
        addEventBtn(exitBtn);

        // 버튼들을 패널에 추가
        tp.add(startBtn);
        tp.add(continueBtn);
        tp.add(rankingBtn);
        tp.add(exitBtn);    
    }

    public void addEventBtn(JButton btn) {
        btn.addActionListener(new MyActionListener());
    }

    class MyActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {      
            JButton b = (JButton)e.getSource();

            if(b.getText().equals(startBtn.getText())) {
                tp.main.panelState = tp.main.game;
                Main.layout.show(Main.mainPanel, "gp");
                Main.mainPanel.getComponent(1).setFocusable(true);
                Main.mainPanel.getComponent(1).requestFocusInWindow();
            }

            else if (b.getText().equals(rankingBtn.getText())) {
                // 랭킹 보기 클릭 시 RankingPanel로 전환
                tp.main.layout.show(Main.mainPanel, "ranking");
                Main.mainPanel.getComponent(2).requestFocusInWindow();
            }

            else if(b.getText().equals(continueBtn.getText())) {
                gp.saveLoad.load();
                tp.main.panelState = tp.main.game;
                gp.gameState = gp.playState;
                Main.layout.show(Main.mainPanel, "gp");
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

//	class MyMouseListener extends MouseAdapter {

//		public void mouseEntered(MouseEvent e) {
//			JButton btn = (JButton) e.getSource();
//			btn.setBackground(Color.DARK_GRAY);
//		}
//		public void mouseExited(MouseEvent e) {
//			Component c = (Component) e.getSource();
//			c.setBackground(Color.LIGHT_GRAY);
//		}
//		public void mouseClicked(MouseEvent e) {
//			Component c = (Component) e.getSource();
//			c.setBackground(Color.LIGHT_GRAY);
//		}
//	}
	

}

