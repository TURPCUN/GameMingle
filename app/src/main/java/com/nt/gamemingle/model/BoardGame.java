package com.nt.gamemingle.model;

public class BoardGame {

    String boardGameId;
    String gameName;
    String gameDescription;
    String gameImageUrl;
    String gameMinPlayers;
    String gameMaxPlayers;
    String gameCategory;

    public BoardGame(String boardGameId, String gameName, String gameDescription, String gameImageUrl, String gameMinPlayers, String gameMaxPlayers, String gameCategory) {
        this.boardGameId = boardGameId;
        this.gameName = gameName;
        this.gameDescription = gameDescription;
        this.gameImageUrl = gameImageUrl;
        this.gameMinPlayers = gameMinPlayers;
        this.gameMaxPlayers = gameMaxPlayers;
        this.gameCategory = gameCategory;
    }

    public BoardGame() {
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameDescription() {
        return gameDescription;
    }

    public void setGameDescription(String gameDescription) {
        this.gameDescription = gameDescription;
    }

    public String getGameImageUrl() {
        return gameImageUrl;
    }

    public void setGameImageUrl(String gameImageUrl) {
        this.gameImageUrl = gameImageUrl;
    }

    public String getGameMinPlayers() {
        return gameMinPlayers;
    }

    public void setGameMinPlayers(String gameMinPlayers) {
        this.gameMinPlayers = gameMinPlayers;
    }

    public String getGameMaxPlayers() {
        return gameMaxPlayers;
    }

    public void setGameMaxPlayers(String gameMaxPlayers) {
        this.gameMaxPlayers = gameMaxPlayers;
    }

    public String getGameCategory() {
        return gameCategory;
    }

    public void setGameCategory(String gameCategory) {
        this.gameCategory = gameCategory;
    }

    public String getBoardGameId() {
        return boardGameId;
    }

    public void setBoardGameId(String boardGameId) {
        this.boardGameId = boardGameId;
    }
}
