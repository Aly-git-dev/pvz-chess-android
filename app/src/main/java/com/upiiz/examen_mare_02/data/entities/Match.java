package com.upiiz.examen_mare_02.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "matches")
public class Match {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public long player1Id;
    public long player2Id;

    public long currentTurnPlayerId;
    @NonNull public String status;         // "EN_CURSO" | "FINALIZADA"
    @NonNull public String boardState;     // estado serializado
    public long lastTurnStartTime;         // millis

    public Match(long player1Id, long player2Id, long currentTurnPlayerId, @NonNull String boardState) {
        this.player1Id = player1Id;
        this.player2Id = player2Id;
        this.currentTurnPlayerId = currentTurnPlayerId;
        this.status = "EN_CURSO";
        this.boardState = boardState;
        this.lastTurnStartTime = System.currentTimeMillis();
    }
}
