package data;


import java.sql.*;
import java.util.*;

public class Ranking {

    private static final String URL = "jdbc:mysql://localhost:3306/rankingdb"; // DB URL (데이터베이스 주소와 포트)
    private static final String USER = "root"; // DB 사용자명
    private static final String PASSWORD = "1234"; // DB 비밀번호

    private Connection connection;

    public Ranking() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 점수를 DB에 저장하는 메서드
    public void addScore(String playerName, int score) {
        String query = "INSERT INTO rankings (player_name, score) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, playerName);
            stmt.setInt(2, score);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 랭킹을 불러오는 메서드
    public List<PlayerScore> getRanking() {
        List<PlayerScore> rankings = new ArrayList<>();
        String query = "SELECT player_name, score FROM rankings ORDER BY score DESC LIMIT 10";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String playerName = rs.getString("player_name");
                int score = rs.getInt("score");
                rankings.add(new PlayerScore(playerName, score));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rankings;
    }

    public static class PlayerScore {
        private String playerName;
        private int score;

        public PlayerScore(String playerName, int score) {
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
}
