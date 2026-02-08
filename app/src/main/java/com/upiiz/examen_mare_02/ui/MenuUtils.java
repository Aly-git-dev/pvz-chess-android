package com.upiiz.examen_mare_02.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.upiiz.examen_mare_02.R;

public class MenuUtils {

    public static void inflate(AppCompatActivity act, Menu menu) {
        MenuInflater inflater = act.getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
    }

    public static boolean handle(AppCompatActivity act, MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_home) {
            Intent i = new Intent(act, UserListActivity.class);
            act.startActivity(i);
            return true;
        } else if (id == R.id.action_register) {
            Intent i = new Intent(act, RegisterActivity.class);
            act.startActivity(i);
            return true;
        } else if (id == R.id.action_logout) {
            SharedPreferences sp = act.getSharedPreferences("SESSION", AppCompatActivity.MODE_PRIVATE);
            sp.edit().clear().apply(); // limpia currentUserId y remember
            Intent i = new Intent(act, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            act.startActivity(i);
            return true;
        } else if (id == R.id.action_rules) {
            Intent i = new Intent(act, RulesActivity.class);
            act.startActivity(i);
            return true;
        }
        return false;
    }

    private MenuUtils() {}
}
