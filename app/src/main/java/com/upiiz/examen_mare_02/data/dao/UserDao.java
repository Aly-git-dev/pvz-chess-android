package com.upiiz.examen_mare_02.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.upiiz.examen_mare_02.data.entities.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(User u);

    @Query("SELECT * FROM users ORDER BY username ASC")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    User getById(long id);

    @Query("SELECT * FROM users WHERE username = :user AND password = :pass LIMIT 1")
    User login(String user, String pass);
}
