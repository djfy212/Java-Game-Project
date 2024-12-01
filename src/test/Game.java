package game;


import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DB 연결 및 데이터 처리 클래스
class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/rankingdb";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

// 랭킹 데이터 클래스
class RankingEntry {
    private final String playerName;
    private final int score;

    public RankingEntry(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }
}

// 랭킹 관리 클래스
class RankingService {
    // 점수 저장
    public static void saveScore(String playerName, int score) {
        String sql = "INSERT INTO Rankings (player_name, score) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, playerName);
            stmt.setInt(2, score);
            stmt.executeUpdate();

            System.out.println("Your score has been saved!");
        } catch (SQLException e) {
            System.err.println("Failed to save score: " + e.getMessage());
        }
    }

    // 랭킹 가져오기
    public static List<RankingEntry> getRankings() {
        List<RankingEntry> rankings = new ArrayList<>();
        String sql = "SELECT player_name, score FROM Rankings ORDER BY score DESC LIMIT 10";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                rankings.add(new RankingEntry(rs.getString("player_name"), rs.getInt("score")));
            }
        } catch (SQLException e) {
            System.err.println("Failed to retrieve rankings: " + e.getMessage());
        }

        return rankings;
    }
}

// 메인 클래스 (화면 전환 포함)
public class Game {
    private static String playerName;
    private static int finalScore;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Game::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new CardLayout());

        // 메인 패널
        JPanel mainPanel = new JPanel(new GridLayout(3, 1));
        JButton playButton = new JButton("Play Game");
        JButton rankingButton = new JButton("View Rankings");
        JButton exitButton = new JButton("Exit");

        mainPanel.add(playButton);
        mainPanel.add(rankingButton);
        mainPanel.add(exitButton);

        // 게임 패널
        JPanel gamePanel = new JPanel(new BorderLayout());
        JLabel gameLabel = new JLabel("Playing the game...", SwingConstants.CENTER);
        JButton backToMenuButton = new JButton("Back to Menu");

        gamePanel.add(gameLabel, BorderLayout.CENTER);
        gamePanel.add(backToMenuButton, BorderLayout.SOUTH);

        // 랭킹 패널
        JPanel rankingPanel = new JPanel(new BorderLayout());
        JLabel rankingTitle = new JLabel("Top 10 Rankings", SwingConstants.CENTER);
        JTable rankingTable = new JTable(new String[0][3], new String[]{"Rank", "Player Name", "Score"});
        JButton backFromRankingButton = new JButton("Back to Menu");

        rankingPanel.add(rankingTitle, BorderLayout.NORTH);
        rankingPanel.add(new JScrollPane(rankingTable), BorderLayout.CENTER);
        rankingPanel.add(backFromRankingButton, BorderLayout.SOUTH);

        // CardLayout에 패널 추가
        CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
        frame.add(mainPanel, "Main");
        frame.add(gamePanel, "Game");
        frame.add(rankingPanel, "Rankings");

        // 버튼 액션
        playButton.addActionListener(e -> {
            cardLayout.show(frame.getContentPane(), "Game");
            playGame(frame, gameLabel);
        });

        rankingButton.addActionListener(e -> {
            cardLayout.show(frame.getContentPane(), "Rankings");
            updateRankingsTable(rankingTable);
        });

        backToMenuButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Main"));
        backFromRankingButton.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Main"));

        exitButton.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    private static void playGame(JFrame frame, JLabel gameLabel) {
        playerName = JOptionPane.showInputDialog(frame, "Enter your name:");
        if (playerName == null || playerName.isEmpty()) {
            playerName = "Anonymous";
        }
        finalScore = (int) (Math.random() * 4000) + 1000; // 임의 점수 생성
        gameLabel.setText("Game Over! Your score is: " + finalScore);

        RankingService.saveScore(playerName, finalScore);
    }

    private static void updateRankingsTable(JTable table) {
        List<RankingEntry> rankings = RankingService.getRankings();
        String[][] data = new String[rankings.size()][3];
        for (int i = 0; i < rankings.size(); i++) {
            RankingEntry entry = rankings.get(i);
            data[i][0] = String.valueOf(i + 1);
            data[i][1] = entry.getPlayerName();
            data[i][2] = String.valueOf(entry.getScore());
        }
        table.setModel(new javax.swing.table.DefaultTableModel(data, new String[]{"Rank", "Player Name", "Score"}));
    }
}
