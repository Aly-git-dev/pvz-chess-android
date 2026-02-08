package com.upiiz.examen_mare_02.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.upiiz.examen_mare_02.data.dao.MatchDao;
import com.upiiz.examen_mare_02.data.dao.UserDao;
import com.upiiz.examen_mare_02.data.entities.Match;
import com.upiiz.examen_mare_02.data.entities.User;

@Database(
        entities = {User.class, Match.class},
        version = 1,
        exportSchema = true
)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract MatchDao matchDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase get(Context ctx) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(ctx.getApplicationContext(),
                                    AppDatabase.class, "pvs_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
