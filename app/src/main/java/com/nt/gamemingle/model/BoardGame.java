package com.nt.gamemingle.model;
import android.os.Parcel;
import android.os.Parcelable;
public class BoardGame implements Parcelable {

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

    protected BoardGame(Parcel in) {
        boardGameId = in.readString();
        gameName = in.readString();
        gameDescription = in.readString();
        gameImageUrl = in.readString();
        gameMinPlayers = in.readString();
        gameMaxPlayers = in.readString();
        gameCategory = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(boardGameId);
        dest.writeString(gameName);
        dest.writeString(gameDescription);
        dest.writeString(gameImageUrl);
        dest.writeString(gameMinPlayers);
        dest.writeString(gameMaxPlayers);
        dest.writeString(gameCategory);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BoardGame> CREATOR = new Creator<BoardGame>() {
        @Override
        public BoardGame createFromParcel(Parcel in) {
            return new BoardGame(in);
        }

        @Override
        public BoardGame[] newArray(int size) {
            return new BoardGame[size];
        }
    };

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

    public Integer getGameImageSource() {
        return Integer.parseInt(gameImageUrl);
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
