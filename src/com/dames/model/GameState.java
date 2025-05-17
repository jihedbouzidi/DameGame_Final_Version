package com.dames.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;

public class GameState {
    @JsonProperty("gameId")
    private String gameId;
    
    @JsonProperty("playerId")
    private String playerId;
    
    @JsonProperty("playerPawn")
    private String playerPawn;
    
    @JsonProperty("boardState")
    private String[][] boardState;
    
    @JsonProperty("currentPlayer")
    private String currentPlayer;
    
    @JsonProperty("saveDate")
    private Date saveDate;

    public GameState() {
        this.saveDate = new Date();
    }

    public GameState(String gameId, String playerId, String playerPawn, String[][] boardState, String currentPlayer) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.playerPawn = playerPawn;
        this.boardState = boardState;
        this.currentPlayer = currentPlayer;
        this.saveDate = new Date();
    }

    // Getters
    public String getGameId() { return gameId; }
    public String getPlayerId() { return playerId; }
    public String getPlayerPawn() { return playerPawn; }
    public String[][] getBoardState() { return boardState; }
    public String getCurrentPlayer() { return currentPlayer; }
    public Date getSaveDate() { return saveDate; }

    // Setters
    public void setGameId(String gameId) { this.gameId = gameId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }
    public void setPlayerPawn(String playerPawn) { this.playerPawn = playerPawn; }
    public void setBoardState(String[][] boardState) { this.boardState = boardState; }
    public void setCurrentPlayer(String currentPlayer) { this.currentPlayer = currentPlayer; }
    public void setSaveDate(Date saveDate) { this.saveDate = saveDate; }

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static GameState fromJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, GameState.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}