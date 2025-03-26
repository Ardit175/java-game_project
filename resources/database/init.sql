-- Create the database
CREATE DATABASE IF NOT EXISTS maze_game;
USE maze_game;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,  -- Will store hashed passwords
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create saved_games table
CREATE TABLE IF NOT EXISTS saved_games (
    game_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    maze_data TEXT NOT NULL,  -- Will store serialized maze state
    player_position_x INT NOT NULL,
    player_position_y INT NOT NULL,
    collected_treasures INT DEFAULT 0,
    score INT DEFAULT 0,
    game_status VARCHAR(20) DEFAULT 'IN_PROGRESS',
    saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Create treasures table
CREATE TABLE IF NOT EXISTS treasures (
    treasure_id INT PRIMARY KEY AUTO_INCREMENT,
    treasure_type VARCHAR(50) NOT NULL,
    points INT NOT NULL,
    description VARCHAR(255)
);

-- Insert some sample treasures
INSERT INTO treasures (treasure_type, points, description) VALUES
    ('COIN', 10, 'A golden coin'),
    ('GEM', 50, 'A precious gem'),
    ('CHEST', 100, 'A treasure chest'),
    ('CROWN', 200, 'A royal crown');
