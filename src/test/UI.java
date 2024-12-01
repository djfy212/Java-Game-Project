package game;

import javax.swing.*;

import game.UI.BackgroundPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class UI extends JFrame {
	 private CardLayout cardLayout;
	    private JPanel mainPanel;

	    // 하트 개수를 나타내는 변수
	    private int heartCount = 5; // 기본 하트 개수 설정
	    private int damageCount = 0; // 데미지 카운트

	    private JPanel inventoryPanel; // 인벤토리 패널
	    private boolean isInventoryOpen = false; // 인벤토리 상태

	    private JLayeredPane layeredPane;
	    
	    public UI() {
	        setTitle("Game");
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setResizable(false);
	        setPreferredSize(new Dimension(768, 576));
	        pack();
	        setLocationRelativeTo(null);

	        // CardLayout을 사용하여 여러 화면을 전환할 수 있도록 설정
	        cardLayout = new CardLayout();
	        mainPanel = new JPanel(cardLayout);

	        // 시작 화면과 게임 화면을 각각의 패널로 생성
	        JPanel startScreen = createStartScreen();
	        JPanel gameScreen = createGameScreen();

	        // 각 화면을 mainPanel에 추가
	        mainPanel.add(startScreen, "StartScreen");
	        mainPanel.add(gameScreen, "GameScreen");

	        // mainPanel을 JFrame에 직접 추가
	        add(mainPanel);

	        // JLayeredPane 초기화 및 크기 설정 (게임 화면 전용)
	        layeredPane = new JLayeredPane();
	        layeredPane.setPreferredSize(new Dimension(768, 576));
	        gameScreen.add(layeredPane);

	        // 인벤토리 패널을 layeredPane에 추가
	        inventoryPanel = createInventoryPanel();
	        layeredPane.add(inventoryPanel, JLayeredPane.POPUP_LAYER);

	        addKeyListener(new KeyAdapter() {
	            @Override
	            public void keyPressed(KeyEvent e) {
	                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	                    cardLayout.show(mainPanel, "StartScreen");
	                }
	                if (e.getKeyCode() == KeyEvent.VK_I) {
	                    toggleInventory();
	                }
	                if (e.getKeyCode() == KeyEvent.VK_2) {
	                    receiveDamage();
	                }
	            }
	        });

	        setFocusable(true);
	        setVisible(true);
	    }

	    
	    // 인벤토리를 열고 닫는 메서드
	    private void toggleInventory() {
	        if (inventoryPanel.isVisible()) {
	            inventoryPanel.setVisible(false); // 인벤토리 숨기기
	        } else {
	            inventoryPanel.setSize(200, 300); // 인벤토리 패널 크기 설정
	            inventoryPanel.setLocation(getWidth() - inventoryPanel.getWidth() - 20, (getHeight() - inventoryPanel.getHeight()) / 5); // 화면 오른쪽 중앙에 위치 설정
	            inventoryPanel.setVisible(true); // 인벤토리 보이기
	        }
	    }
	    
	    // 인벤토리 패널 생성
	    private JPanel createInventoryPanel() {
	        JPanel inventoryPanel = new JPanel();
	        inventoryPanel.setLayout(new GridLayout(4, 2));
	        inventoryPanel.setBackground(new Color(255, 255, 255, 200));

	        for (int i = 1; i <= 8; i++) {
	            JLabel itemLabel = new JLabel("아이템 " + i);
	            inventoryPanel.add(itemLabel);
	        }

	        inventoryPanel.setSize(200, 300);
	        inventoryPanel.setVisible(false); // 초기 상태는 숨김
	        return inventoryPanel;
	    }


	    // 시작 화면 생성
	    private JPanel createStartScreen() {
	    	// BackgroundPanel을 사용하여 배경 설정
	        BackgroundPanel panel = new BackgroundPanel("D://java//image/start.png"); // 시작 화면 배경 이미지 경로
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // 세로 정렬

	        // 제목 라벨 생성
	        JLabel titleLabel = new JLabel("Java Game", SwingConstants.CENTER);
	        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // 폰트 설정
	        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 중앙 정렬

	        // 버튼 패널 생성
	        JPanel buttonPanel = new JPanel();
	        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS)); // 가로 정렬

	        // 버튼 생성
	        JButton startButton = new JButton("처음부터");
	        JButton continueButton = new JButton("이어서");
	        JButton exitButton = new JButton("종료");

	        // 버튼 이벤트 리스너 추가
	        startButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                // "처음부터" 선택 시 게임 화면으로 전환
	                cardLayout.show(mainPanel, "GameScreen");
	            }
	        });

	        continueButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                // "이어서" 선택 시 게임 화면으로 전환
	                cardLayout.show(mainPanel, "GameScreen");
	            }
	        });

	        exitButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                System.exit(0); // 프로그램 종료
	            }
	        });

	        // 버튼 패널에 버튼 추가 및 간격 설정
	        buttonPanel.add(startButton);
	        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0))); // 버튼 간격
	        buttonPanel.add(continueButton);
	        buttonPanel.add(Box.createRigidArea(new Dimension(20, 0))); // 버튼 간격
	        buttonPanel.add(exitButton);
	        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // 버튼 패널 중앙 정렬

	        // 메인 패널에 제목과 버튼 패널 추가
	        panel.add(Box.createVerticalStrut(100)); // 상단 여백
	        panel.add(titleLabel);
	        panel.add(Box.createVerticalStrut(250)); // 제목과 버튼 간의 여백
	        panel.add(buttonPanel);
	        return panel;
	    }

	    class BackgroundPanel extends JPanel {
	        private Image backgroundImage;

	        public BackgroundPanel(String imagePath) {
	            // 이미지 불러오기
	            ImageIcon icon = new ImageIcon(imagePath);
	            backgroundImage = icon.getImage();
	        }

	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            // 이미지를 패널 크기에 맞게 그림
	            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	        }
	    }

	    // 게임 화면 생성
	    private JPanel createGameScreen() {
	        // 배경 이미지를 사용하는 패널 생성
	        BackgroundPanel panel = new BackgroundPanel("D://java//image/game.PNG"); // 이미지 경로 설정
	        panel.setLayout(new BorderLayout());

	        // 왼쪽 상단에 하트와 코인 표시를 위한 패널 생성
	        JPanel topLeftPanel = new JPanel();
	        topLeftPanel.setLayout(new BoxLayout(topLeftPanel, BoxLayout.Y_AXIS)); // 세로 정렬로 변경
	        topLeftPanel.setOpaque(false); // 배경 투명

	        // 하트와 코인 이미지 불러오기
	        ImageIcon heartIcon = new ImageIcon("D://java//image/Heart.png"); // 하트 이미지 경로
	        ImageIcon coinIcon = new ImageIcon("D://java//image/Coin.png"); // 코인 이미지 경로

	        // 하트 표시를 위한 패널 생성
	        JPanel heartPanel = new JPanel();
	        heartPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // 가로 정렬
	        heartPanel.setOpaque(false); // 배경 투명

	        // 하트 라벨을 동적으로 추가하는 메서드 호출
	        updateHearts(heartPanel, heartCount); // 하트 개수를 인자로 넘김

	        // 코인 라벨과 코인 개수 라벨 생성
	        JLabel coinLabel = new JLabel(coinIcon);
	        JLabel coinCountLabel = new JLabel("250"); // 코인 개수 텍스트

	        // 코인 개수 라벨 스타일 설정
	        coinCountLabel.setFont(new Font("Arial", Font.BOLD, 16)); // 폰트 설정
	        coinCountLabel.setForeground(Color.BLACK); // 텍스트 색상 설정

	        // 코인과 숫자를 가로로 정렬할 패널 생성
	        JPanel coinPanel = new JPanel();
	        coinPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // 가로 정렬
	        coinPanel.setOpaque(false); // 배경 투명
	        coinPanel.add(coinLabel); // 코인 이미지 추가
	        coinPanel.add(Box.createHorizontalStrut(5)); // 간격 추가
	        coinPanel.add(coinCountLabel); // 코인 개수 텍스트 추가

	        // 하트와 코인 패널에 추가
	        topLeftPanel.add(heartPanel); // 하트 패널 추가
	        topLeftPanel.add(Box.createVerticalStrut(5)); // 하트와 코인 간의 간격 추가
	        topLeftPanel.add(coinPanel); // 코인 패널 추가

	        // topLeftPanel을 북쪽에 배치
	        panel.add(topLeftPanel, BorderLayout.NORTH);

	        // JLayeredPane 초기화 및 크기 설정
	        layeredPane = new JLayeredPane();
	        layeredPane.setPreferredSize(new Dimension(768, 576));
	        panel.add(layeredPane, BorderLayout.CENTER);

	        // 인벤토리 패널 생성 및 추가
	        inventoryPanel = createInventoryPanel();
	        inventoryPanel.setVisible(false); // 초기 상태는 숨김
	        layeredPane.add(inventoryPanel, JLayeredPane.POPUP_LAYER);

	        return panel;
	    }
	    
	    
	 // 하트를 업데이트하는 메서드
	    private void updateHearts(JPanel heartPanel, int count) {
	        heartPanel.removeAll(); // 기존 하트 라벨 제거
	        for (int i = 0; i < heartCount; i++) { // heartCount를 사용하여 하트 표시
	            ImageIcon heartIcon;
	            // 데미지를 입은 하트 상태 처리
	            if (i >= heartCount - damageCount) {
	                if (damageCount == 1) {
	                    heartIcon = new ImageIcon("D://java//image/Damage1.png"); // Damage1 이미지
	                } else {
	                    heartIcon = new ImageIcon("D://java//image/Damage2.png"); // Damage2 이미지
	                }
	            } else {
	                heartIcon = new ImageIcon("D://java//image/Heart.png"); // 정상 하트 이미지
	            }
	            JLabel heartLabel = new JLabel(heartIcon); // 하트 이미지 라벨 생성
	            heartPanel.add(heartLabel); // 하트 추가
	        }
	        heartPanel.revalidate(); // 패널 업데이트
	        heartPanel.repaint(); // 다시 그리기
	    }

	    // 데미지를 받았을 때 하트의 이미지를 데미지로 변경하는 메서드
	    public void receiveDamage() {
	        if (damageCount < heartCount) { // 더 이상 데미지를 줄 수 없을 때까지 반복
	            damageCount++; // 데미지 횟수 증가
	            // 현재 게임 화면 패널에서 하트 패널을 가져오는 부분
	            JPanel gamePanel = (JPanel) mainPanel.getComponent(1); // GameScreen 패널
	            JPanel heartPanel = (JPanel) gamePanel.getComponent(0); // topLeftPanel (0번째 컴포넌트)
	            updateHearts(heartPanel, heartCount); // 하트 업데이트
	        }
	    }
	  
	    public static void main(String[] args) {
	        new UI(); // 시작 화면 실행
	    }
	}


