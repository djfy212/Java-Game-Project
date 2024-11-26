package game;

import java.sql.*;
import java.util.Scanner;


//DB 연결 및 데이터 처리 클래스
class DatabaseManager {
 private static final String URL = "jdbc:mysql://localhost:3306/rankingdb";
 private static final String USER = "root";
 private static final String PASSWORD = "1234";

 public static Connection connect() throws SQLException {
     return DriverManager.getConnection(URL, USER, PASSWORD);
 }
}

//랭킹 저장 및 조회 클래스
class RankingService {
 // 점수 저장
 public static void saveScore(String playerName, int score) {
     String sql = "INSERT INTO Rankings (player_name, score) VALUES (?, ?)";

     try (Connection conn = DatabaseManager.connect();
          PreparedStatement stmt = conn.prepareStatement(sql)) {

         stmt.setString(1, playerName);
         stmt.setInt(2, score);

         
         stmt.executeUpdate();
         System.out.println("Score saved successfully for " + playerName);
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }

 // 랭킹 조회
 public static void showRankings() {
     String sql = "SELECT player_name, score FROM Rankings ORDER BY score DESC LIMIT 10";

     try (Connection conn = DatabaseManager.connect();
          PreparedStatement stmt = conn.prepareStatement(sql);
          ResultSet rs = stmt.executeQuery()) {

         System.out.println("=== Top 10 Rankings ===");
         while (rs.next()) {
             System.out.println(rs.getString("player_name") + " - " + rs.getInt("score"));
         }
     } catch (SQLException e) {
         e.printStackTrace();
     }
 }
}

//메인 클래스
public class Ranking {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 플레이어 이름 입력
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine();

        // 게임 클리어 후 점수 (예제에서는 임의의 점수로 설정)
        int finalScore = (int) (Math.random() * 5000) + 1000; // 1000 ~ 6000 사이의 임의 점수
        System.out.println("Congratulations, " + playerName + "! Your final score is: " + finalScore);

        // 점수 저장
        RankingService.saveScore(playerName, finalScore);

        // 랭킹 출력
        RankingService.showRankings();

        scanner.close();
    }
}
