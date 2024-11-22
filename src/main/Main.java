package main;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

//-------------------------------메인 클래스---------------------------
// 이곳은 메인 클래스로 JFrame틀을 설정해줍니다. 각 패널들은 이 프레임 위에 생성됩니다.
public class Main{
	// 패널 상태를 나타냅니다.
	public int panelState;
	public final int title=0;
	public final int game=1;
	public final int menu=2;

	
	// JFrame 생성
    public static JFrame window;
    public static CardLayout layout;
    public static JPanel mainPanel;
    
    // 패녈 생성
    TitlePanel titlePanel;
    GamePanel gamePanel;
    MenuPanel menuPanel;
    // 메인 생성자
    public Main() {
    	
        // JFrame 설정 	
        window = new JFrame("TEST Project");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        
        // CardLayout과 메인 JPanel 설정
        layout = new CardLayout();
        mainPanel = new JPanel(layout);
        
        // 각 패널 생성 및 추가
        titlePanel = new TitlePanel(this);
        gamePanel = new GamePanel(this);
        menuPanel = new MenuPanel(this, gamePanel);
        
        // CardLayout에 패널들 추가
        mainPanel.add(titlePanel, "tp");
        mainPanel.add(gamePanel, "gp");
        mainPanel.add(menuPanel, "mp");
        
        // JFrame에 메인 패널 추가
        window.add(mainPanel);
        window.pack();
        
        window.setLocationRelativeTo(null); // 화면 중앙에 창을 위치시킴
        window.setVisible(true);
        
        // 초기화 완료 메시지 및 스레드 시작
        titlePanel.startThread();
        gamePanel.setupGame();
        gamePanel.startThread();
        menuPanel.startThread();
    }
		
	public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
	}

}
