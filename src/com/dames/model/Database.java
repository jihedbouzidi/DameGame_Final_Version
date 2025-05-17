package com.dames.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Database {
    private static final String DB_URL = "jdbc:h2:./gamedames";
    private static final String USER = "sa";
    private static final String PASS = "";

    private Connection connection;
    private final ObjectMapper mapper;

    public Database() {
        this.mapper = new ObjectMapper();
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTables() {
        try {
            Statement stmt = connection.createStatement();

            // Table des joueurs
            stmt.execute("CREATE TABLE IF NOT EXISTS players (" +
                    "id VARCHAR(50) PRIMARY KEY," +
                    "username VARCHAR(50) UNIQUE NOT NULL," +
                    "first_name VARCHAR(50)," +
                    "last_name VARCHAR(50)," +
                    "avatar_path VARCHAR(255)" +
                    ")");

            // Table des parties
            stmt.execute("CREATE TABLE IF NOT EXISTS games (" +
                    "id VARCHAR(50) PRIMARY KEY," +
                    "player_id VARCHAR(50)," +
                    "player_pawn VARCHAR(10)," +
                    "board_state CLOB," +
                    "current_player VARCHAR(10)," +
                    "save_date TIMESTAMP," +
                    "FOREIGN KEY (player_id) REFERENCES players(id)" +
                    ")");

            // Table des statistiques
            stmt.execute("CREATE TABLE IF NOT EXISTS stats (" +
                    "player_id VARCHAR(50) PRIMARY KEY," +
                    "games_played INT DEFAULT 0," +
                    "wins INT DEFAULT 0," +
                    "losses INT DEFAULT 0," +
                    "draws INT DEFAULT 0," +
                    "win_rate DOUBLE DEFAULT 0.0," +
                    "avg_moves DOUBLE DEFAULT 0.0," +
                    "captures INT DEFAULT 0," +
                    "promotions INT DEFAULT 0," +
                    "avg_duration DOUBLE DEFAULT 0.0," +
                    "FOREIGN KEY (player_id) REFERENCES players(id)" +
                    ")");

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePlayer(Player player) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO players (id, username, first_name, last_name, avatar_path) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "first_name = VALUES(first_name), " +
                    "last_name = VALUES(last_name), " +
                    "avatar_path = VALUES(avatar_path)");

            pstmt.setString(1, player.getId());
            pstmt.setString(2, player.getUsername());
            pstmt.setString(3, player.getFirstName());
            pstmt.setString(4, player.getLastName());
            pstmt.setString(5, player.getAvatarPath());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Player getPlayer(String username) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * FROM players WHERE username = ?");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Player player = new Player();
                player.setId(rs.getString("id"));
                player.setUsername(rs.getString("username"));
                player.setFirstName(rs.getString("first_name"));
                player.setLastName(rs.getString("last_name"));
                player.setAvatarPath(rs.getString("avatar_path"));
                return player;
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveGame(GameState gameState) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO games (id, player_id, player_pawn, board_state, current_player, save_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?)");

            pstmt.setString(1, gameState.getGameId());
            pstmt.setString(2, gameState.getPlayerId());
            pstmt.setString(3, gameState.getPlayerPawn());
            pstmt.setString(4, gameState.toJson());
            pstmt.setString(5, gameState.getCurrentPlayer());
            pstmt.setTimestamp(6, new Timestamp(gameState.getSaveDate().getTime()));
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<GameState> getSavedGames(String playerId) {
        List<GameState> games = new ArrayList<>();
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * FROM games WHERE player_id = ? ORDER BY save_date DESC");
            pstmt.setString(1, playerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                GameState gameState = GameState.fromJson(rs.getString("board_state"));
                if (gameState != null) {
                    games.add(gameState);
                }
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return games;
    }

    public void updateStats(String playerId, boolean isWin, int moves, int captures, int promotions, long duration) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO stats (player_id, games_played, wins, losses, draws, win_rate, avg_moves, captures, promotions, avg_duration) " +
                    "VALUES (?, 1, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "games_played = games_played + 1, " +
                    "wins = wins + ?, " +
                    "losses = losses + ?, " +
                    "draws = draws + ?, " +
                    "win_rate = (wins * 100.0) / games_played, " +
                    "avg_moves = ((avg_moves * (games_played - 1)) + ?) / games_played, " +
                    "captures = captures + ?, " +
                    "promotions = promotions + ?, " +
                    "avg_duration = ((avg_duration * (games_played - 1)) + ?) / games_played");

            int wins = isWin ? 1 : 0;
            int losses = isWin ? 0 : 1;
            int draws = 0;

            pstmt.setString(1, playerId);
            pstmt.setInt(2, wins);
            pstmt.setInt(3, losses);
            pstmt.setInt(4, draws);
            pstmt.setDouble(5, wins * 100.0);
            pstmt.setDouble(6, moves);
            pstmt.setInt(7, captures);
            pstmt.setInt(8, promotions);
            pstmt.setDouble(9, duration);
            pstmt.setInt(10, wins);
            pstmt.setInt(11, losses);
            pstmt.setInt(12, draws);
            pstmt.setDouble(13, moves);
            pstmt.setInt(14, captures);
            pstmt.setInt(15, promotions);
            pstmt.setDouble(16, duration);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Player> getRankings() {
        List<Player> rankings = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT p.*, s.win_rate, s.games_played " +
                    "FROM players p " +
                    "JOIN stats s ON p.id = s.player_id " +
                    "ORDER BY s.win_rate DESC, s.games_played DESC");

            while (rs.next()) {
                Player player = new Player();
                player.setId(rs.getString("id"));
                player.setUsername(rs.getString("username"));
                player.setFirstName(rs.getString("first_name"));
                player.setLastName(rs.getString("last_name"));
                player.setAvatarPath(rs.getString("avatar_path"));
                rankings.add(player);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rankings;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}