-- Création de la base de données
CREATE DATABASE IF NOT EXISTS dames_db;
USE dames_db;

-- Table des joueurs
CREATE TABLE IF NOT EXISTS players (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    avatar_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des parties sauvegardées
CREATE TABLE IF NOT EXISTS saved_games (
    game_id INT AUTO_INCREMENT PRIMARY KEY,
    player_id VARCHAR(50),
    player_pawn VARCHAR(10) NOT NULL,
    board_state JSON NOT NULL,
    current_player VARCHAR(10) NOT NULL,
    save_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (player_id) REFERENCES players(username) ON DELETE CASCADE
);

-- Table des statistiques
CREATE TABLE IF NOT EXISTS stats (
    player_id VARCHAR(50) PRIMARY KEY,
    games_played INT DEFAULT 0,
    wins INT DEFAULT 0,
    losses INT DEFAULT 0,
    draws INT DEFAULT 0,
    total_moves INT DEFAULT 0,
    total_captures INT DEFAULT 0,
    total_promotions INT DEFAULT 0,
    total_duration BIGINT DEFAULT 0,
    win_rate DOUBLE DEFAULT 0.0,
    last_played TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (player_id) REFERENCES players(username) ON DELETE CASCADE
);

-- Table des parties terminées
CREATE TABLE IF NOT EXISTS completed_games (
    game_id INT AUTO_INCREMENT PRIMARY KEY,
    player_id VARCHAR(50),
    player_pawn VARCHAR(10) NOT NULL,
    winner VARCHAR(10) NOT NULL,
    moves_count INT NOT NULL,
    captures_count INT NOT NULL,
    promotions_count INT NOT NULL,
    game_duration BIGINT NOT NULL,
    end_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (player_id) REFERENCES players(username) ON DELETE CASCADE
); 