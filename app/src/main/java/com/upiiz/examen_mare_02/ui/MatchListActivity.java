package com.upiiz.examen_mare_02.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.upiiz.examen_mare_02.R;
import com.upiiz.examen_mare_02.data.AppDatabase;
import com.upiiz.examen_mare_02.data.entities.Match;
import com.upiiz.examen_mare_02.data.game.Board;
import com.upiiz.examen_mare_02.data.game.BoardSerializer;
import com.upiiz.examen_mare_02.ui.adapters.MatchAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MatchListActivity extends AppCompatActivity {
    private AppDatabase db;
    private long meId, otherId;
    private final List<Match> matches = new ArrayList<>();
    private MatchAdapter adapter;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_match_list);
        db = AppDatabase.get(this);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setOnMenuItemClickListener(item -> MenuUtils.handle(this, item));

        SharedPreferences sp = getSharedPreferences("SESSION", MODE_PRIVATE);
        meId = sp.getLong("currentUserId", -1);
        otherId = getIntent().getLongExtra("opponentId", -1);

        RecyclerView rv = findViewById(R.id.rvMatches);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MatchAdapter(matches, m -> {
            Intent i = new Intent(this, GameActivity.class);
            i.putExtra("matchId", m.id);
            startActivity(i);
        });
        rv.setAdapter(adapter);

        Button btnNew = findViewById(R.id.btnNewMatch);
        btnNew.setOnClickListener(v -> Executors.newSingleThreadExecutor().execute(() -> {
            String state = BoardSerializer.serialize(Board.initial());
            Match m = new Match(meId, otherId, meId, state);
            db.matchDao().insert(m);
            load();
        }));

        load();
    }

    private void load() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Match> list = db.matchDao().getBetween(meId, otherId);
            matches.clear();
            matches.addAll(list);
            runOnUiThread(adapter::notifyDataSetChanged);
        });
    }
}
