package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import data.Ranking;

public class RankingPanel extends JPanel implements Runnable {
    private Main main; // Main 클래스와의 연동
    private Font font; // 커스텀 폰트
    private Graphics2D g2; // 그래픽 컨텍스트
    private Thread rankingThread; // 랭킹 패널을 위한 스레드
    private Ranking rankings; // 랭킹 데이터를 관리하는 객체

    public static final int SCREEN_WIDTH = 960;
    public static final int SCREEN_HEIGHT = 576;

    // 생성자
    public RankingPanel(Main main) {
        this.main = main;
        this.rankings = new Ranking(); // 랭킹 데이터 초기화
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setDoubleBuffered(true); // 화면 깜박임 방지
        
        // 폰트 로드
        try {
            InputStream is = getClass().getResourceAsStream("/font/CookieRun Regular.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 20);
        } catch (FontFormatException | IOException e) {
            System.out.println("Font Load Error: " + e.getMessage());
            font = new Font("Arial", Font.PLAIN, 20); // 기본 폰트로 대체
        }

        // "뒤로가기" 버튼 추가
        JButton backButton = new JButton("뒤로가기");
        backButton.setBounds(SCREEN_WIDTH / 2 - 75, SCREEN_HEIGHT - 100, 150, 50);
        backButton.setBackground(Color.GRAY);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(font);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.showTitlePanel(); // 버튼 클릭 시 타이틀 화면으로 돌아가기
            }
        });
        this.setLayout(null);
        this.add(backButton); // 버튼을 패널에 추가
    }

    // 스레드 시작
    public void startThread() {
        rankingThread = new Thread(this);
        rankingThread.start();
    }

    // 스레드 종료
    public void stopThread() {
        rankingThread = null;
    }

    // 화면 그리기
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = (Graphics2D) g;
        g2.setFont(font);
        g2.setColor(Color.WHITE);

        drawRanking(); // 랭킹 데이터 표시
    }

    // 랭킹 데이터 출력
    private void drawRanking() {
        List<Ranking.PlayerScore> scoreRanking = rankings.getScoreRanking(); // 점수 랭킹 데이터 가져오기
        List<Ranking.PlayerTime> timeRanking = rankings.getTimeRanking(); // 시간 랭킹 데이터 가져오기
        
        int x = 100;
        int y = 100;
        int xOffset = SCREEN_WIDTH / 2 + 50; // 좌측과 우측을 나누기 위한 오프셋

        // 제목 출력
        g2.drawString("Score Ranking", x, 50);
        g2.drawLine(x, 60, SCREEN_WIDTH / 2 - 50, 60); // 점수 랭킹 선

        // 점수 랭킹 항목 출력 (좌측)
        for (int i = 0; i < scoreRanking.size(); i++) {
            String rank = (i + 1) + ". " + scoreRanking.get(i).getPlayerName() + " - " + scoreRanking.get(i).getScore();
            g2.drawString(rank, x, y);
            y += 30; // 항목 간 간격
        }

        // 좌측과 우측 구분선
        g2.setColor(Color.GRAY);
        g2.drawLine(SCREEN_WIDTH / 2, 50, SCREEN_WIDTH / 2, SCREEN_HEIGHT - 150);
        g2.setColor(Color.WHITE); // 색상 복원

        // 제목 출력
        y = 100; // y 값을 다시 초기화
        g2.drawString("Time Ranking", xOffset, 50);
        g2.drawLine(xOffset, 60, SCREEN_WIDTH - 100, 60); // 시간 랭킹 선

        // 시간 랭킹 항목 출력 (우측)
        for (int i = 0; i < timeRanking.size(); i++) {
            String rank = (i + 1) + ". " + timeRanking.get(i).getPlayerName() + " - " + timeRanking.get(i).getTime() + " sec";
            g2.drawString(rank, xOffset, y);
            y += 30; // 항목 간 간격
        }
    }

    // 반복 작업을 위한 스레드 실행
    @Override
    public void run() {
        double drawInterval = 1000000000 / 60.0; // 60 FPS
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (rankingThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                repaint(); // 화면 갱신
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("Ranking Panel FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }
}
