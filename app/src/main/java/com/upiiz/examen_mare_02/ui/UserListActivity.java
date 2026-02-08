package com.upiiz.examen_mare_02.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.upiiz.examen_mare_02.R;
import com.upiiz.examen_mare_02.data.AppDatabase;
import com.upiiz.examen_mare_02.data.entities.User;
import com.upiiz.examen_mare_02.ui.adapters.UserAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class UserListActivity extends AppCompatActivity {
    private AppDatabase db;
    private long currentUserId;
    private final List<User> users = new ArrayList<>();
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        db = AppDatabase.get(this);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setOnMenuItemClickListener(item -> MenuUtils.handle(this, item));

        SharedPreferences sp = getSharedPreferences("SESSION", MODE_PRIVATE);
        currentUserId = sp.getLong("currentUserId", -1);

        RecyclerView rv = findViewById(R.id.rvUsers);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(users, currentUserId, u -> {
            if (u.id == currentUserId) {
                Toast.makeText(this, "Selecciona otro jugador, o registra uno nuevo.", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent i = new Intent(this, MatchListActivity.class);
            i.putExtra("opponentId", u.id);
            startActivity(i);
        });
        rv.setAdapter(adapter);

        Button btnRegisterPlayer = findViewById(R.id.btnRegisterPlayer);
        btnRegisterPlayer.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        loadUsers();
    }

    private void loadUsers() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<User> all = db.userDao().getAll();
            users.clear();
            users.addAll(all);
            runOnUiThread(adapter::notifyDataSetChanged);
        });
    }
}
