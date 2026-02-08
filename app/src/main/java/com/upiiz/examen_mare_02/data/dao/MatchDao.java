package com.upiiz.examen_mare_02.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.upiiz.examen_mare_02.data.entities.Match;

import java.util.List;

@Dao
public interface MatchDao {
    @Insert
    long insert(Match m);

    @Update
    int update(Match m);

    @Query("SELECT * FROM matches WHERE id = :id LIMIT 1")
    Match getById(long id);

    @Query("SELECT * FROM matches WHERE (player1Id = :u1 AND player2Id = :u2) OR (player1Id = :u2 AND player2Id = :u1) ORDER BY id DESC")
    List<Match> getBetween(long u1, long u2);
}
