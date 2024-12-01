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
    public void addScore(String playerName, int score, int timeInSeconds) {
        String query = "INSERT INTO rankings (player_name, score, time) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, playerName);
            stmt.setInt(2, score);
            stmt.setInt(3, timeInSeconds);  // 초 단위로 시간 저장
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 점수 기반 랭킹을 불러오는 메서드 (최다 스코어 랭킹)
    public List<PlayerScore> getScoreRanking() {
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

    // 시간 기반 랭킹을 불러오는 메서드 (최단 시간 랭킹)
    public List<PlayerTime> getTimeRanking() {
        List<PlayerTime> timeRanking = new ArrayList<>();  // PlayerTime 객체를 담는 리스트
        String query = "SELECT player_name, score, time FROM rankings ORDER BY time LIMIT 10"; // 시간 기준으로 정렬

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String playerName = rs.getString("player_name");
                int timeInSeconds = rs.getInt("time");

                // 초를 시간, 분, 초 형식으로 변환
                int hours = timeInSeconds / 3600;
                int minutes = (timeInSeconds % 3600) / 60;
                int seconds = timeInSeconds % 60;

                // 시간을 HH:MM:SS 형식으로 포맷팅
                String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                // PlayerTime 객체를 timeRanking 리스트에 추가 (시간을 포맷된 문자열로 저장)
                timeRanking.add(new PlayerTime(playerName, formattedTime));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return timeRanking;
    }

    // 점수 정보 저장 클래스
    public static class PlayerScore {
        private String playerName;
        private int score;
        private String time;

        public PlayerScore(String playerName, int score) {
            this.playerName = playerName;
            this.score = score;
            this.time = "";  // 시간은 포함되지 않음
        }

        public PlayerScore(String playerName, int score, String time) {
            this.playerName = playerName;
            this.score = score;
            this.time = time;  // 시간을 포함
        }

        public String getPlayerName() {
            return playerName;
        }

        public int getScore() {
            return score;
        }

        public String getTime() {
            return time;
        }
    }

    // 시간 정보 저장 클래스
    public static class PlayerTime {
        private String playerName;
        private String time; // 시간을 포맷된 문자열로 저장

        public PlayerTime(String playerName, String time) {
            this.playerName = playerName;
            this.time = time;
        }

        public String getPlayerName() {
            return playerName;
        }

        public String getTime() {
            return time;
        }
    }
}
